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
-keep class com.trio.nnpredict.GeneralProgressBar { *; }
-keep class com.trio.nnpredict.model.CustomInfoItem { *; }
-keep class com.trio.nnpredict.model.CustomInfoItem$* { *; }
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