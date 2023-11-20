// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    // Add the dependency for the Google services Gradle plugin - Firebase

    // firebase plugin Client library 4.3.13보다 높으면 호환성 문제로 에러 발생
    id("com.google.gms.google-services") version "4.3.13" apply false
}