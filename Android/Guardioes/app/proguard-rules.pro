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

-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class io.fabric.sdk.android.** { *; }
-keep class io.fabric.sdk.android.services.concurrency.PriorityThreadPoolExecutor
-keep class com.twitter.sdk.android.core.identity.TwitterLoginButton
-keep class android.support.v7.widget.RecyclerView
-keep class com.crashlytics.android.** { *; }
-keep class com.viewpagerindicator.** { *; }

-keep interface android.support.v7.** { *; }
-keepattributes Exceptions, Signature, InnerClasses
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}