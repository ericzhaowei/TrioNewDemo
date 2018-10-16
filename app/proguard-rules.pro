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
-keep class com.trio.nnpredict.Requests.RequestCallback { *; }

-keep class com.trio.nnpredict.TrioWrap.Trio { *; }
-keep class com.trio.nnpredict.TrioWrap.Trio$** {   # keep enum in Trio
    **[] $VALUES;
    public *;
}

-keep class com.trio.nnpredict.TrioWrap.InitProgressListener { *; }
-keep class com.trio.nnpredict.TrioWrap.Model.** { *; }

-keep class com.trio.nnpredict.BaseModel { *; }
-keep class com.trio.nnpredict.TrioWrap.Model.TrioResult$** { *; } # keep inner class in TrioResult

-keep class com.trio.nnpredict.Http.HttpCallback { *; }
-keep class com.trio.nnpredict.Utils.** { *; }


-keep class com.trio.nnpredict.LocalPredict.NNPredict {
    public static void init(**);
}

-keep class com.trio.nnpredict.Friso.FrisoWrapper { *; }

-keep class com.trio.nnpredict.rule.ai.trio.nlu.rule.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------
-keep class com.trio.uimodule.** { *; }