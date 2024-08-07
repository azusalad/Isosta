// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // For Room
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}
