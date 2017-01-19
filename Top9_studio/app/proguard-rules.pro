# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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

##################### 通用 start #####################
#压缩级别
-optimizationpasses 5
#类名不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#忽略警告
-ignorewarning
# 保持哪些类不被混淆
#-keep public class * extends android.app.Fragment
#-keep public class android.content.Context
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-keep class * extends java.lang.annotation.Annotation {*;}

#-libraryjars ./libs/android-support-v4.jar
#-dontwarn android.support.v4.**
#-dontwarn **CompatHoneycomb
#-dontwarn **CompatHoneycombMR2
#-dontwarn **CompatCreatorHoneycombMR2
#-keep interface android.support.v4.app.** { *; }
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
}
#保持自定义控件类不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 反射来调用构造函数 NoSuchMethod
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
##################### 通用 end #####################
#####################记录生成的日志数据,gradle build时在本项目根目录输出 start ################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出 end ################
##################### lib start #####################
#声明Lib
#-libraryjars libs/umeng-analytics-v5.5.3.jar
#-libraryjars libs/gson-2.3.1.jar
#-libraryjars libs/android-async-http-1.4.8.jar
#-libraryjars libs/universal-image-loader-1.9.4.jar
#-libraryjars libs/nineoldandroids-2.4.0.jar
#project start
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.view.View);
}
-keep public class com.zeustel.top9.result.** {*;}

#project end
#友盟统计
-keep public class com.umeng.analytics.** {*;}
# gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.**{*;}
#android-async-http
-keep class com.loopj.android.http.** { *; }
#imageloader
-keep class com.nostra13.universalimageloader.** { *; }
#nineoldandroids
-keep public class com.nineoldandroids.** {*;}
#sharSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class org.apache.**{*;}
#gotyeapi
-keep class com.gotye.**{*;}
-keep class com.google.protobuf.**{*;}
##################### lib end #####################