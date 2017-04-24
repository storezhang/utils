# 不要将类标记为Final类
-optimizations !class/marking/final

# 添加资源文件
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF,META-INF/spring.*

# 保证各种注解可以使用
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# 保留启动类
-keepclasseswithmembers public class * { public static void main(java.lang.String[]);}

# 不修改模型类
-keep public class com.ruijc.**.bean.** { *; }
-keep class com.ruijc.**.bean.** {
    void set*(***);
    boolean is*();
    *** get*();
}

# 保证Spring的注入能使用
-keepclassmembers class * {
    @org.springframework.beans.factory.annotation.Autowired *;
    @org.springframework.beans.factory.annotation.Value *;
}

# 不被混淆的代码配置
-keep public class * {
    public protected *;
}