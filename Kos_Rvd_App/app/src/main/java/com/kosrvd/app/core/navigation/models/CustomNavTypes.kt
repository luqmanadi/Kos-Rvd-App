package com.kosrvd.app.core.navigation.models

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json

object CustomNavTypes {
    val ResultTagihanType = object : NavType<ResultTagihan?>(isNullableAllowed = true) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: ResultTagihan?
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): ResultTagihan? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ResultTagihan {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ResultTagihan?): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

    val ResultCreatePenyewaanType = object : NavType<ResultCreatePenyewaan?>(isNullableAllowed = true){
        override fun put(
            bundle: SavedState,
            key: String,
            value: ResultCreatePenyewaan?
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): ResultCreatePenyewaan? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ResultCreatePenyewaan? {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ResultCreatePenyewaan?): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

    val ResultLaporanKeluhanType = object : NavType<ResultLaporanKeluhan?>(isNullableAllowed = true){
        override fun put(
            bundle: SavedState,
            key: String,
            value: ResultLaporanKeluhan?
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): ResultLaporanKeluhan? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ResultLaporanKeluhan {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ResultLaporanKeluhan?): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }

    val TemporaryDataType = object : NavType<TemporaryData?>(isNullableAllowed = true){
        override fun put(
            bundle: SavedState,
            key: String,
            value: TemporaryData?
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): TemporaryData? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): TemporaryData? {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: TemporaryData?): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }

    val EditTypeKamarType = object : NavType<EditTypeKamar>(isNullableAllowed = false){
        override fun put(
            bundle: SavedState,
            key: String,
            value: EditTypeKamar
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): EditTypeKamar? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): EditTypeKamar {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: EditTypeKamar): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

    val editPindahKamarType = object : NavType<EditPindahKamarType>(isNullableAllowed = false){
        override fun put(
            bundle: SavedState,
            key: String,
            value: EditPindahKamarType
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): EditPindahKamarType? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): EditPindahKamarType {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: EditPindahKamarType): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

    val tambahPenghuniType = object : NavType<TambahPenghuniType>(isNullableAllowed = false){
        override fun put(
            bundle: SavedState,
            key: String,
            value: TambahPenghuniType
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): TambahPenghuniType? {
            return Json.Default.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): TambahPenghuniType {
            return Json.Default.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: TambahPenghuniType): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}