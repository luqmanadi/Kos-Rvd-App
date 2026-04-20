package com.kosrvd.app.core.presentation.utils

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.kosrvd.app.R

object PatternValidation {

    private val INDONESIAN_PHONE_REGEX = Regex("^08[1-9][0-9]{7,10}$")

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 && password.isNotBlank() && password.any { it.isDigit() } && password.any { it.isLetter() }
    }

    fun isPasswordLogInValid(password: String): Boolean {
        return password.isNotBlank()
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return INDONESIAN_PHONE_REGEX.matches(phoneNumber) && phoneNumber.isNotBlank()
    }

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isAddressValid(address: String): Boolean {
        return address.isNotBlank()
    }

    fun isTitleValid(title: String): Boolean {
        return title.isNotBlank()
    }

    fun isDescriptionValid(description: String): Boolean {
        return description.isNotBlank()
    }

    fun isRepeatPasswordValid(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword && repeatPassword.isNotBlank()
    }

    fun isAlasanPenolakanValid(alasanPenolakan: String): Boolean {
        return alasanPenolakan.isNotBlank()
    }

    fun isResponseValid(response: String): Boolean {
        return response.isNotBlank()
    }

    fun isNomorKamarValid(numberRoom: String): Boolean {
        return numberRoom.isNotBlank() && numberRoom.all { it.isLetterOrDigit() } && !numberRoom.contains(" ")
    }

    fun isTarifValid(tarif: String): Boolean {
        return tarif.isNotBlank() && tarif.isDigitsOnly()
    }

    fun isBiayaBulananValid(biayaBulanan: String): Boolean {
        return biayaBulanan.isNotBlank() && biayaBulanan.isDigitsOnly()
    }

    fun isBiayaHarianValid(biayaHarian: String): Boolean {
        return biayaHarian.isNotBlank() && biayaHarian.isDigitsOnly()
    }

    fun isNamaZonaValid(namaZona: String): Boolean {
        return namaZona.isNotBlank()
    }

    fun isDescriptionDiscountValid(descriptionDiscount: String): Boolean {
        return descriptionDiscount.isNotBlank()
    }

    fun isPercentageDiscountValid(percentageDiscount: String): Boolean {
        return percentageDiscount.isNotBlank() && percentageDiscount.isDigitsOnly()
    }

    fun isCarBrandValid(carBrand: String): Boolean {
        return carBrand.isNotBlank()
    }

    fun isCarNameValid(carName: String): Boolean {
        return carName.isNotBlank()
    }

    fun isUserNameValid(userName: String): Boolean {
        return userName.isNotBlank()
    }

    fun isNumberPlateValid(numberPlate: String): Boolean {
        return numberPlate.isNotBlank()
    }

    fun isHargaSewaValid(hargaSewa: String): Boolean {
        return hargaSewa.isNotBlank() && hargaSewa.isDigitsOnly()
    }

    fun isNamaElektronikValid(namaElektronik: String): Boolean {
        return namaElektronik.isNotBlank()
    }

    fun getNamaElektronikError(namaElektronik: String): UiText {
        return when {
            namaElektronik.isBlank() -> UiText.StringResource(R.string.nama_elektronik_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getHargaSewaError(hargaSewa: String): UiText {
        return when {
            hargaSewa.isBlank() -> UiText.StringResource(R.string.harga_sewa_tidak_boleh_kosong)
            !hargaSewa.isDigitsOnly() -> UiText.StringResource(R.string.harga_sewa_harus_angka)
            else -> UiText.DynamicString("")
        }
    }

    fun getCarBrandError(carBrand: String): UiText {
        return when {
            carBrand.isBlank() -> UiText.StringResource(R.string.merk_mobil_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getCarNameError(carName: String): UiText {
        return when {
            carName.isBlank() -> UiText.StringResource(R.string.nama_mobil_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getUserNameError(userName: String): UiText {
        return when {
            userName.isBlank() -> UiText.StringResource(R.string.nama_penyewa_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getNumberPlateError(numberPlate: String): UiText {
        return when {
            numberPlate.isBlank() -> UiText.StringResource(R.string.plat_nomor_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getPercentageDiscountError(percentageDiscount: String): UiText {
        return when {
            percentageDiscount.isBlank() -> UiText.StringResource(R.string.nominal_persen_diskon_tidak_boleh_kosong)
            !percentageDiscount.isDigitsOnly() -> UiText.StringResource(R.string.nominal_persen_diskon_harus_angka)
            else -> UiText.DynamicString("")
        }
    }

    fun getDescriptionDiscountError(descriptionDiscount: String): UiText {
        return when {
            descriptionDiscount.isBlank() -> UiText.StringResource(R.string.keterangan_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getNamaZonaError(namaZona: String): UiText {
        return when {
            namaZona.isBlank() -> UiText.StringResource(R.string.nama_zona_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getBiayaBulananError(biayaBulanan: String): UiText {
        return when {
            biayaBulanan.isBlank() -> UiText.StringResource(R.string.biaya_bulanan_tidak_boleh_kosong)
            !biayaBulanan.isDigitsOnly() -> UiText.StringResource(R.string.biaya_bulanan_harus_angka)
            else -> UiText.DynamicString("")
        }
    }

    fun getBiayaHarianError(biayaHarian: String): UiText {
        return when {
            biayaHarian.isBlank() -> UiText.StringResource(R.string.biaya_harian_tidak_boleh_kosong)
            !biayaHarian.isDigitsOnly() -> UiText.StringResource(R.string.biaya_harian_harus_angka)
            else -> UiText.DynamicString("")
        }
    }

    fun getTarifError(tarif: String): UiText {
        return when {
            tarif.isBlank() -> UiText.StringResource(R.string.tarif_tidak_boleh_kosong)
            !tarif.isDigitsOnly() -> UiText.StringResource(R.string.tarif_harus_angka)
            else -> UiText.DynamicString("")
        }
    }

    fun getNomorKamarError(numberRoom: String): UiText {
        return when {
            numberRoom.isBlank() -> UiText.StringResource(R.string.nomor_kamar_tidak_boleh_kosong)
            numberRoom.contains(" ") -> UiText.StringResource(R.string.nomor_kamar_tidak_boleh_ada_spasi)
            !numberRoom.all { it.isLetterOrDigit() } -> UiText.StringResource(R.string.nomor_kamar_hanya_angka_dan_huruf)
            else -> UiText.DynamicString("")
        }
    }
    fun getResponseError(response: String): UiText {
        return when {
            response.isBlank() -> UiText.StringResource(R.string.response_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getAlasanPenolakanError(alasanPenolakan: String): UiText {
        return when {
            alasanPenolakan.isBlank() -> UiText.StringResource(R.string.alasan_penolakan_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getTitleError(title: String): UiText {
        return when{
            title.isBlank() -> UiText.StringResource(R.string.judul_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getDescriptionError(description: String): UiText {
        return when{
            description.isBlank() -> UiText.StringResource(R.string.deskripsi_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getNameError(name: String): UiText {
        return when{
            name.isBlank() -> UiText.StringResource(R.string.nama_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getPhoneNumberError(phoneNumber: String): UiText {
        return when{
            phoneNumber.isBlank() -> UiText.StringResource(R.string.no_hp_tidak_boleh_kosong)
            !INDONESIAN_PHONE_REGEX.matches(phoneNumber) -> UiText.StringResource(R.string.no_hp_tidak_valid)
            else -> UiText.DynamicString("")
        }
    }

    fun getAddressError(address: String): UiText {
        return when{
            address.isBlank() -> UiText.StringResource(R.string.alamat_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getEmailError(email: String): UiText {
        return when{
            email.isBlank() -> UiText.StringResource(R.string.email_tidak_boleh_kosong)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> UiText.StringResource(R.string.email_tidak_valid)
            else -> UiText.DynamicString("")
        }
    }

    fun getPasswordLogInError(password: String): UiText {
        return when {
            password.isBlank() -> UiText.StringResource(R.string.password_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

    fun getPasswordError(password: String): UiText {
        return when{
            password.isBlank() -> UiText.StringResource(R.string.password_tidak_boleh_kosong)
            password.length < 6 -> UiText.StringResource(R.string.password_minimal_6_karakter)
            !password.any { it.isDigit() } -> UiText.StringResource(R.string.password_harus_berisi_angka)
            !password.any { it.isLetter() } -> UiText.StringResource(R.string.password_harus_berisi_huruf)
            else -> UiText.DynamicString("")
        }
    }

    fun getRepeatPasswordError(password: String, repeatPassword: String): UiText {
        return when{
            repeatPassword != password -> UiText.StringResource(R.string.password_tidak_sama)
            repeatPassword.isBlank() -> UiText.StringResource(R.string.tulis_ulang_password_tidak_boleh_kosong)
            else -> UiText.DynamicString("")
        }
    }

}