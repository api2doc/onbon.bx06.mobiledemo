onbon bx06 api for Android
=====================

本文件說明如何在 Android Studio 開發環境下，建立 [onbon.bx06 api](https://github.com/api2doc/onbon.bx06.api) 的開發項目。

github source 提供 Android Studio 項目樣板，可下載進行簡易測試。

<img src="images/screenshot1.png" alt="Sample" style="width: 250px;"/>

## 相依檔案

#### JAR - onbon bx06 api 核心
* bx06.message-0.5.0-SNAPSHOT.jar
* bx06-0.5.0-SNAPSHOT.jar
* log4j-1.2.14.jar
* simple-xml-2.7.1.jar
* uia.comm-0.2.1.jar
* uia.message-0.5.1.jar
* uia.utils-0.1.2.jar

#### AAR - java.awt for android 核心
* java.awt4a-0.1-release.aar


## project 相關設定

#### build.gradle

* flatDir - 設定相依 lib 的儲存位置。

```
allprojects {
    repositories {
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}
```

## app 相關設定

#### build.gradle

* project.ext.set - 設定輸出檔案名稱。

* dexOptions - 重新掛載 Java AWT wrapper。

* dependencies - 加入 API 相關的 jar & aar 共八個檔案。檔案儲存在 libs 資料夾下。


```
android {
    ...
    defaultConfig {
        ...
        project.ext.set("archivesBaseName", "bx6g.mobiledemo-" + defaultConfig.versionName);
    }
    dexOptions {
        preDexLibraries = false
        additionalParameters =["--core-library"]
    }

}

dependencies {
    ...
    compile files('libs/bx06.message-0.5.0-SNAPSHOT.jar')
    compile files('libs/bx06-0.5.0-SNAPSHOT.jar')
    compile files('libs/log4j-1.2.14.jar')
    compile files('libs/simple-xml-2.7.1.jar')
    compile files('libs/uia.comm-0.2.1.jar')
    compile files('libs/uia.message-0.5.1.jar')
    compile files('libs/uia.utils-0.1.2.jar')
    compile(name:'java.awt4a-0.1-release', ext:'aar')
    ...
}

```

#### AndroidManifest.xml

* android:name - 設定啟動應用程式類別，用來建立 BX6G API 運行環境。

* android.permission.INTERNET - 設定允許 Socket 操作。

```
<application
    android:name="onbon.bx06.mobiledemo.MainApplication"
    ...>
</application>
<uses-permission android:name="android.permission.INTERNET" />
```


#### MainApplication.java

* 對應於 AndroidManifest.xml 中 android:name 的設定。

```
package onbon.bx06.mobiledemo;

import android.app.Application;
import onbon.bx06.Bx6GEnv;

public class MainApplication extends Application {

    private boolean initial;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Bx6GEnv.initial();  // 建立 BX6G API 運行環境。
            this.initial = true;
        }
        catch (Exception ex) {
            this.initial = false;
        }
    }
}
```
