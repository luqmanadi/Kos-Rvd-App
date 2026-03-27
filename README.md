# Kos RVD - Management System 🏠
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

Aplikasi manajemen operasional kos-kosan berbasis Android modern. Proyek ini dibangun menggunakan **Jetpack Compose** dengan implementasi arsitektur **MVI (Model-View-Intent)** dan pemisahan logika yang bersih melalui struktur *package* yang sistematis.

---

## 🚀 Fitur Utama

Aplikasi ini memiliki sistem hak akses yang membedakan tampilan dan fungsi antara **Admin** dan **Penghuni**:

### 🛡️ Authentication & User Management
* **Auth**: Alur *On-boarding*, Login, dan *Forgot Password*.
* **Account**: (Khusus Admin) Mengelola akun pengguna (Create, Read, & Deactivate).
* **Profile**: Pengaturan data pribadi (Nama, Email, Password, Foto, Alamat, No HP) dan fitur Logout.

### 📊 Operational & Dashboard
* **Dashboard**: 
    * **Admin**: Statistik tagihan, verifikasi tertunda, kamar kosong, dan keluhan baru.
    * **Penghuni**: Akses cepat ke pengumuman dan direktori penghuni.
* **Room**: (Khusus Admin) Manajemen unit kamar (CRUD).
* **Resident**: Daftar penghuni aktif yang dapat diakses oleh Admin & Penghuni.

### 💰 Rental & Billing System (The Core)
* **Rental (Penyewaan)**: (Khusus Admin) Mengelola kontrak sewa, update biaya tambahan (parkir/elektronik), hingga terminasi sewa.
* **Billing (Tagihan)**: 
    * **Admin**: Generate tagihan, verifikasi bukti bayar, dan hapus tagihan.
    * **Penghuni**: Lihat daftar tagihan dan unggah bukti pembayaran.
    * **Automation**: Generate tagihan otomatis dan pengingat jatuh tempo via Cloud Functions.

### 🚗 Parking & Facilities
* **Parking**: (Khusus Admin) Manajemen **Zona Parkir** (slot) dan **Parkir Harian** (transaksi & bukti bayar).
* **Complaints (Keluhan)**: Fitur pelaporan kendala fasilitas bagi penghuni dan tindak lanjut bagi admin.
* **Announcement**: Pengumuman real-time dari admin kepada seluruh penghuni.

---

## 🏗️ Tech Stack & Architecture
Aplikasi ini mengadopsi **Official Android Architecture Recommendations** dengan pemisahan *layer* yang tegas, dikombinasikan dengan pola **MVI (Model-View-Intent)** pada UI Layer untuk *state management* yang reaktif dan mudah diprediksi.

### 1. App Architecture (Layering)
Proyek ini distrukturkan ke dalam tiga *layer* utama:
* **UI Layer (Presentation)**: Menangani tampilan (Jetpack Compose) dan logika interaksi pengguna. Layer ini bereaksi terhadap perubahan *state* dan mengirimkan *action* ke *layer* di bawahnya.
* **Domain Layer (Opsional/Use Cases)**: Berisi logika bisnis inti aplikasi (misalnya: kalkulasi denda tagihan, validasi form) yang memisahkan UI dari asal usul data.
* **Data Layer**: Bertanggung jawab atas operasi pengambilan dan penyimpanan data. Terdiri dari *Repository* tunggal sebagai *single source of truth* yang berinteraksi langsung dengan Firebase (Firestore, Auth, Storage).

### 2. UI Pattern: MVI (Model-View-Intent)
Pada UI Layer, aplikasi ini menggunakan pola MVI yang diimplementasikan dengan fitur-fitur modern Kotlin:
* **State (`UiState`)**: Direpresentasikan oleh `data class` tunggal yang mencerminkan kondisi layar saat ini secara utuh.
* **Intent (`Action`)**: Direpresentasikan oleh `sealed interface` untuk mendefinisikan aksi pengguna (contoh: `OnSubmitClicked`, `OnEmailChanged`) yang dikirim ke ViewModel.
* **Side Effects (`Event`)**: Direpresentasikan oleh `sealed interface` untuk menangani aksi satu kali yang tidak mengubah *state* secara langsung (contoh: *Navigation, Show Snackbar*).

**Alur Data MVI (Unidirectional Data Flow):**
```text
[User Interaction] ──(Action/Intent)──► [ViewModel] ──(Process Business Logic)──┐
       ▲                                                                        │
       │                                                                        ▼
   [UI / Compose] ◄──(Observe State & Render)─────────────────────────────── [UiState]
       ▲
       └─────────────(Emit One-time Event/Side Effect)──────────────────────────┘
```

### 3.Frontend (Android)
* **UI Framework**: Jetpack Compose (Declarative UI).
* **Navigation**: Compose Navigation (dikelola terpusat di `presentation/navigation`).
* **Dependency Injection**: Hilt.
* **Image Loading**: Coil.
* **Asynchronous Programming**: Coroutines & Flow (StateFlow/SharedFlow).

### 4.Backend (Firebase Ecosystem)
* **Database**: Cloud Firestore (Real-time NoSQL).
* **Authentication**: Firebase Auth (Email/Password).
* **Storage**: Firebase Storage (File & Gambar).
* **Automation**: Cloud Functions (Trigger, Scheduler, & FCM Push Notification).
* **Security**: Firebase App Check.

---

## 💻 Panduan Menjalankan Project (Local Development)

### 1. Prasyarat Sistem & Instalasi
Aplikasi ini bergantung pada **Firebase Emulator Suite** untuk menjalankan layanan backend secara lokal.
* **Node.js**: Gunakan **Versi 22** (LTS direkomendasikan).
* **Firebase CLI**: Pastikan telah terinstall secara global (`npm install -g firebase-tools`).
* **Java SDK**: Diperlukan untuk menjalankan Java-based emulators.

> **Dokumentasi Resmi**: Untuk panduan instalasi mendalam, silakan merujuk ke [Firebase Emulator Suite: Install and Configure](https://firebase.google.com/docs/emulator-suite/install_and_configure#install_the_local_emulator_suite).

### 2. Setup Firebase Emulator
Lakukan inisialisasi dan instalasi dependensi backend sebelum menjalankan emulator:

1.  **Install Dependencies (Cloud Functions)**:
    Masuk ke direktori functions dan install library yang diperlukan agar trigger dan scheduler berjalan:
    ```bash
    cd firebase_admin_kos_rvd_app/functions
    npm install
    ```

2.  **Login & Project Setup**:
    Pastikan Anda sudah login dan mengaktifkan layanan yang digunakan (Firestore, Functions, Emulators, Storage, Pub/Sub, dan Hosting):
    ```bash
    firebase login
    ```
    *(Catatan: Jika Anda ingin menghubungkan ke project di Console pribadi, gunakan perintah `firebase use --add` setelah login).*

3.  **Menjalankan Emulator dengan Data Dummy**:
    Masuk ke folder backend dan jalankan emulator. Gunakan flag `--project` dengan prefix `demo-` agar emulator berjalan secara instan dalam **Demo Mode** (tanpa perlu membuat project di web Console):
    ```bash
    cd firebase_admin_kos_rvd_app
    firebase emulators:start --import=./data_dummy --project=demo-kos-rvd
    ```
    *Perintah di atas akan otomatis mengimpor data dummy (kamar, user, tagihan, dll) sehingga aplikasi tidak kosong saat pertama kali dijalankan.*

### 3. Konfigurasi Jaringan (Network Security)
Sesuaikan IP PC Anda pada `app/src/main/res/xml/network_security_config.xml` agar aplikasi dapat terhubung ke emulator:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain> 
        <domain includeSubdomains="true">192.168.1.X</domain> 
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```
**Catatan**: Pastikan **Build Variant** di Android Studio diatur ke mode **Debug**.

### 4. File Konfigurasi
* Letakkan file `google-services.json` ke folder `app/`.
* Pastikan layanan Firebase terkait (**Firestore, Auth, Storage**) sudah diaktifkan di Console.

---

## 📁 Struktur Folder Project
Aplikasi menggunakan struktur *single-module* dengan pembagian package sebagai berikut:
```Plaintext
app/src/main/java/com/rvd/kosrvdapp/
├── core/           # Reusable components, Network, & Base classes
├── feature/        # Business logic & ViewModels (MVI Pattern) per fitur
├── presentation/   # Navigation, Main Activity, & AppShell
└── KosRvdApp.kt    # Application class (Initialization)
```
**Backend logic** (Cloud Functions, Rules, & Emulator Data) tersedia di folder: `firebase_admin_kos_rvd_app/`

---

## 🗺️ Roadmap / Future Work
* [ ] Integrasi Payment Gateway API (Midtrans/Xendit) via Cloud Functions untuk otomatisasi pembayaran (QRIS & Virtual Account).
* [ ] Fitur Generate Laporan Bulanan (PDF).
* [ ] Dark Mode Support.
* [ ] Unit Testing untuk logic Billing & Rental.

## 📝 Disclaimer
Proyek ini dikembangkan untuk tujuan **Tugas Akhir**. Meskipun fitur fungsional sudah lengkap, beberapa bagian kode masih dalam tahap pengembangan lebih lanjut untuk pembersihan (*refactoring*) dan optimalisasi performa.

---

## 📄 License
Proyek ini didistribusikan di bawah lisensi **MIT**. Lihat file `LICENSE` untuk informasi lebih lanjut.
