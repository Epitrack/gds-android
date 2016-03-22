# Add project specific ProGuard rules here.
# By default, the flags slide_out_left this file are appended to flags specified
# slide_out_left /Users/IgorMorais/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive slide_out_left build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class io.fabric.sdk.android.** { *; }
-keep class com.facebook.FacebookSdk