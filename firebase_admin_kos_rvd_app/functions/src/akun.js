const {onRequest, HttpsError} = require("firebase-functions/v2/https");
const {onDocumentUpdated} = require("firebase-functions/v2/firestore");
const {getFirestore, Timestamp} = require("firebase-admin/firestore");
const {getAuth} = require("firebase-admin/auth");
const admin = require("firebase-admin");
const utils = require("./utils");
const {info, error} = require("firebase-functions/logger");
const cors = require("cors")({origin: true});

// --- HELPER: Validasi Token ---
const validateAuth = async (req) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    throw new HttpsError("unauthenticated",
        "Missing or invalid Authorization header");
  }
  const idToken = authHeader.split("Bearer ")[1];
  try {
    return await admin.auth().verifyIdToken(idToken);
  } catch (error) {
    throw new HttpsError("unauthenticated", "Invalid token", error);
  }
};

// --- HELPER: Error Handler ---
const handleError = (res, messageError) => {
  error("Error Function:", messageError);
  const code =
      messageError instanceof HttpsError ? messageError.code : "internal";
  const message = messageError.message || "Internal Server Error";

  // Mapping code Firebase ke HTTP Status Code
  const statusMapping = {
    "unauthenticated": 401,
    "invalid-argument": 400,
    "not-found": 404,
    "already-exists": 409,
    "permission-denied": 403,
    "internal": 500};

  res.status(statusMapping[code] || 500).json({
    error: {code, message}});
};

// ==================================================================
// 1. GET DETAIL (Method: GET, Param: Query)
// ==================================================================
exports.getDetailAkunPengguna = onRequest(async (request, response) =>{
  cors(request, response, async () => {
    // Pastikan method GET
    if (request.method !== "GET") {
      return response.status(405).json({error:
            {code: "method-not-allowed", message: "Use GET"}});
    }
    try {
      // 1. Cek Login Manual
      await validateAuth(request);

      // Ambil dari Query Param (?idAkun=...)
      const idAkun = request.query.idAkun;
      if (!idAkun) throw new HttpsError("invalid-argument", "ID Akun kosong.");

      const db = getFirestore();
      const auth = getAuth();

      const [userAuth, userDoc] = await Promise.all([
        auth.getUser(idAkun).catch((err) => {
          if (err.code === "auth/user-not-found") return null;
          throw err;
        }),
        db.collection("akun").doc(idAkun).get(),
      ]);

      if (!userDoc.exists) {
        throw new HttpsError("not-found", "Data profil tidak ditemukan.");
      }

      const userData = userDoc.data();
      const emailUser = userAuth ? userAuth.email : (userData.email || "");

      // Kirim Response JSON standar
      response.status(200).json({
        idAkun: idAkun,
        name: userData.name || "",
        photo: userData.photo || "",
        role: userData.role || "",
        status: userData.status || "",
        email: emailUser,
        dateCreated: userData.dateCreated ?
            userData.dateCreated.toMillis() : Date.now(),
        dataPenghuni: userData.dataPenghuni ? {
          address: userData.dataPenghuni.address || "",
          phoneNumber: userData.dataPenghuni.phoneNumber || "",
          photoKtp: userData.dataPenghuni.photoKtp || "",
          numberRoom: userData.dataPenghuni.numberRoom || null,
        } : null,
      });
    } catch (error) {
      handleError(response, error);
    }
  });
});

// ==================================================================
// 2. CREATE ACCOUNT (Method: POST, Param: Body JSON)
// ==================================================================
exports.createAkunPengguna = onRequest(async (request, response) => {
  cors(request, response, async () => {
    if (request.method !== "POST") {
      return response.status(405).send("Method Not Allowed");
    }
    try {
      await validateAuth(request);
      // Ambil dari Body JSON
      const {idAkun, email, password, name, role,
        photoKtp, photoKtpFileName, address, phoneNumber} = request.body;

      if (!idAkun || !email || !password || !name || !role) {
        throw new HttpsError("invalid-argument", "Data wajib tidak lengkap.");
      }

      const db = getFirestore();
      const auth = getAuth();
      let isAuthCreated = false;

      try {
        await auth.createUser({
          uid: idAkun, email, displayName: name, password,
        });
        isAuthCreated = true;

        const accountData = {
          name: name, photo: "", role: role, status: "Aktif",
          dateCreated: Timestamp.now(), fcmTokens: [],
          numberOfUnreadNotification: 0,
          dataPenghuni: role === "penghuni" ? {
            address: address || "",
            phoneNumber: phoneNumber || "",
            photoKtp: photoKtp,
            numberRoom: null,
            idPenyewa: "",
            finalBill: null,
          }: null,
        };

        await db.collection("akun").doc(idAkun).set(accountData);
        response.status(200).json({success: true,
          message: "Akun berhasil dibuat"});
      } catch (innerError) {
        // Cleanup Logic
        if (photoKtpFileName) {
          try {
            await admin.storage().bucket()
                .file(`imageKtp/${photoKtpFileName}`).delete();
          } catch (e) {
            console.error("Gagal hapus foto sisa:", e);
          }
        }
        if (isAuthCreated) {
          try {
            await auth.deleteUser(idAkun);
          } catch (e) {
            console.error("Gagal hapus user auth sisa:", e);
          }
        }
        if (innerError.code === "auth/email-already-exists") {
          throw new HttpsError("already-exists", "Email sudah terdaftar.");
        }
        throw innerError;
      }
    } catch (error) {
      handleError(response, error);
    }
  });
});

// ==================================================================
// 3. DEACTIVATE ACCOUNT (Method: POST)
// ==================================================================
exports.deactivateUserAccount = onRequest(async (request, response) => {
  cors(request, response, async () => {
    if (request.method !== "POST") {
      return response.status(405).send("Method Not Allowed");
    }

    try {
      await validateAuth(request);
      const {idAkun, role, photoUrl} = request.body; // Body

      if (!idAkun || !role) {
        throw new HttpsError("invalid-argument", "ID/Role kosong.");
      }

      const db = getFirestore();
      const auth = getAuth();
      const batch = db.batch();
      const timestampNow = Timestamp.now();

      // ==========================================
      // STEP 1: HAPUS FOTO Profile (Logic Admin & Penghuni sama)
      // ==========================================

      if (photoUrl) {
        const fileName = utils
            .getFilePathFromUrl(photoUrl); // Cek path require utils kamu!
        if (fileName) {
          admin.storage().bucket().file(fileName).delete()
              .catch((e) => error("Gagal hapus foto profil:", e));
        }
      }

      // ==========================================
      // STEP 2: LOGIC BERDASARKAN ROLE
      // ==========================================

      // --- KASUS ADMIN ---
      if (role === "admin") {
        batch.update(db.collection("akun").doc(idAkun), {
          status: "Tidak Aktif",
          photo: "",
        });
      } else if (role === "penghuni") { // --- KASUS PENGHUNI ---
        // 1. Cari Penyewaan Aktif yang memiliki penghuni ini
        const rentalSnapshot = await db.collection("penyewaan")
            .where("rentalStatus", "==", "Aktif")
            .get();

        let targetRentalDoc = null;

        // Looping cari idAkun di listResident
        for (const doc of rentalSnapshot.docs) {
          const data = doc.data();
          const residents = data.listResident || [];
          // Cek apakah ada object yang idAkun-nya sama
          if (residents.some((res) => res.idAkun === idAkun)) {
            targetRentalDoc = doc;
            break;
          }
        }

        // 2. Jika ada penyewaan terikat
        if (targetRentalDoc) {
          const rentalData = targetRentalDoc.data();
          const rentalRef = targetRentalDoc.ref;
          const currentResidents = rentalData.listResident || [];

          if (currentResidents.length > 1) {
            // KASUS: Ada lebih dari 1 penghuni
            // -> Hapus penghuni ini dari listResident saja
            // -> Penyewaan tetap aktif untuk penghuni lainnya
            const updatedResidents = currentResidents.filter(
                (res) => res.idAkun !== idAkun,
            );

            batch.update(rentalRef, {
              listResident: updatedResidents,
            });

            info(`Penghuni ${idAkun} dihapus dari penyewaan, ` +
                    `sisa ${updatedResidents.length} penghuni`);
          } else {
            // KASUS: Hanya 1 penghuni (penghuni terakhir)
            // -> Nonaktifkan penyewaan sepenuhnya
            batch.update(rentalRef, {
              rentalStatus: "Tidak Aktif",
              rentalCompletionDate: timestampNow,
            });

            info(`Penyewaan ${targetRentalDoc.id} dinonaktifkan ` +
                  `karena penghuni terakhir dinonaktifkan`);
          }
        }

        // 3. UPDATE AKUN PENGHUNI (selalu dilakukan)
        const akunRef = db.collection("akun").doc(idAkun);
        batch.update(akunRef, {
          "status": "Tidak Aktif",
          "dataPenghuni.numberRoom": null, // Set field nested jadi null
          "photo": "",
        });
      }

      // ==========================================
      // STEP 3: COMMIT BATCH & DISABLE AUTH
      // ==========================================

      // Eksekusi semua update database sekaligus
      await batch.commit();

      // Disable Auth (Login)
      await auth.updateUser(idAkun, {disabled: true});

      response.status(200).json({success: true, message: "Akun dinonaktifkan"});
    } catch (error) {
      handleError(response, error);
    }
  });
});

// ==================================================================
// 4. REACTIVATE ACCOUNT (Method: POST)
// ==================================================================
exports.reactivateUserAccount = onRequest(async (request, response) => {
  cors(request, response, async () => {
    if (request.method !== "POST") {
      return response.status(405).send("Method Not Allowed");
    }
    try {
      await validateAuth(request);
      const {idAkun} = request.body;

      if (!idAkun) throw new HttpsError("invalid-argument", "ID kosong.");

      const db = getFirestore();
      const auth = getAuth();

      const userDocRef = db.collection("akun").doc(idAkun);
      const userDoc = await userDocRef.get();
      if (!userDoc.exists) {
        throw new HttpsError("not-found", "Akun tidak ditemukan.");
      }

      await auth.updateUser(idAkun, {disabled: false});
      await userDocRef.update({status: "Aktif"});

      response.status(200).json({
        success: true, message: "Akun diaktifkan kembali"});
    } catch (error) {
      handleError(response, error);
    }
  });
});

// ==================================================================
// 5. Trigger onUpdated collection akun
// ==================================================================

exports.onAkunUpdate = onDocumentUpdated("akun/{akunId}", async (event) => {
  try {
    if (!event.data) return null;

    const idAkun = event.params["akunId"];
    const dataBefore = event.data.before.data();
    const dataAfter = event.data.after.data();

    const oldName = dataBefore.name;
    const newName = dataAfter.name;

    // 1. Cek apakah nama berubah
    if (oldName === newName) {
      // Tidak perlu log jika tidak ada perubahan (mengurangi noise di log)
      return null;
    }

    // 2. Cek apakah role penghuni (admin tidak perlu sync ke penyewaan)
    if (dataAfter.role !== "penghuni") {
      info(`Nama akun ${idAkun} berubah, `+
          `tapi role bukan penghuni. Skip.`);
      return null;
    }

    const db = getFirestore();

    // 3. Cari Penyewaan Aktif yang memiliki penghuni ini
    const dataRental = await db.collection("penyewaan")
        .where("rentalStatus", "==", "Aktif")
        .get();

    let targetRentalDoc = null;

    for (const doc of dataRental.docs) {
      const data = doc.data();
      const residents = data.listResident || [];
      if (residents.some((res) => res.idAkun === idAkun)) {
        targetRentalDoc = doc;
        break;
      }
    }

    // 4. Jika tidak ada penyewaan terikat, skip
    if (!targetRentalDoc) {
      info(`Nama penghuni ${idAkun} berubah, `+
          `tapi tidak ada penyewaan aktif.`);
      return null;
    }

    // 5. Update listResident dengan nama baru
    const rentalData = targetRentalDoc.data();
    const currentResidents = rentalData.listResident || [];

    const updatedResidents = currentResidents.map((resident) => {
      if (resident.idAkun === idAkun) {
        return {
          idAkun: resident.idAkun,
          name: newName,
        };
      }
      return resident;
    });

    await targetRentalDoc.ref.update({
      listResident: updatedResidents,
    });

    info(`Nama penghuni ${idAkun} diupdate di penyewaan ` +
          `${targetRentalDoc.id}: "${oldName}" -> "${newName}"`);

    return null;
  } catch (e) {
    error(`Gagal update data akun dengan id `+
        `${event.params["akunId"]}`, e);
    return null;
  }
});


// ==================================================================
// 5. DELETE ACCOUNT (Method: DELETE)
// ==================================================================
exports.deleteAccount = onRequest(async (request, response) => {
  cors(request, response, async () => {
    if (request.method !== "DELETE") {
      return response.status(405).send("Method Not Allowed");
    }
    try {
      await validateAuth(request);
      const {idAkun, role, ktpUrl} = request.body;
      if (!idAkun || !role) {
        throw new HttpsError("invalid-argument", "ID/role kosong.");
      }
      const db = getFirestore();
      const auth = getAuth();

      // ==========================================
      // STEP 1: HAPUS FOTO KTP role Penghuni
      // ==========================================
      if (role === "penghuni" && ktpUrl) {
        const fileName = utils
            .getFilePathFromUrl(ktpUrl); // Cek path require utils kamu!
        if (fileName) {
          admin.storage().bucket().file(fileName).delete()
              .catch((e) => error("Gagal hapus foto KTP:", e));
        }
      }

      // ==========================================
      // STEP 2: HAPUS Data Akun di Firestore collection Akun dan Auth Firebase
      // ==========================================
      const userDocRef = db.collection("akun").doc(idAkun);
      const userDoc = await userDocRef.get();
      if (!userDoc.exists) {
        throw new HttpsError("not-found", "Akun tidak ditemukan.");
      }
      await auth.deleteUser(idAkun);
      await userDocRef.delete();
      response.status(200).json({success: true, message: "Akun Berhasil " +
            "Dihapus"});
    } catch (error) {
      handleError(response, error);
    }
  });
});
