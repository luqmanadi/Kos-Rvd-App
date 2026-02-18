// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // kotlin serialization
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    // ksp
    alias(libs.plugins.google.devtools.ksp) apply false
    // hilt
    alias(libs.plugins.hilt.android) apply false
    // google services plugin
    alias(libs.plugins.google.gms.google.services) apply false
}