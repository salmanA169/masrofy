# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.masrofy.core.backup.AccountBackupData
-keep class com.masrofy.core.backup.TransactionBackupData
-keep class com.masrofy.core.backup.BackupDataModel
-keep class com.masrofy.data.entity.AccountEntity
-keep class com.masrofy.model.CategoryAccount
-keep class com.masrofy.currency.Currency
-keep class com.masrofy.model.TransactionType
-keep class com.masrofy.data.entity.TransactionEntity
-keep class com.google.api.client.json.GenericJson
-keep class com.google.api.services.drive.**
-keep class com.google.api.client.googleapis.json.*
-keep class com.google.api.client.googleapis.services.**
-keep class com.masrofy.core.drive.GoogleSigningAuthManager
-keep class com.google.android.gms.auth.**
-keep class com.google.api.client.googleapis.extensions.android.gms.auth.**
-keep class com.google.api.client.http.javanet.**
-keep class com.masrofy.core.backup.AbstractBackupData
-keep class com.masrofy.core.backup.BackupExternalStorage
-keep class com.masrofy.core.backup.DriveBackupDataImpl
-keep class com.google.gson**
-keep class com.google.gson.internal.**
-keepclassmembers class com.masrofy.core.backup.BackupDataModel {
 !transient <fields>;
}
-keepclassmembers class com.masrofy.core.backup.AccountBackupData {
 !transient <fields>;
}
-keepclassmembers class com.masrofy.core.backup.TransactionBackupData {
 !transient <fields>;
}
-keepclassmembers class com.masrofy.currency.Currency {
 !transient <fields>;
}
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}


-keep class com.google.gson.examples.android.model.** { <fields>; }



-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}



