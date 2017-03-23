package onbon.bx06.mobiledemo;

/**
 * Created by gazer on 3/21/2017.
 */

import android.app.Application;

import j2a.awt.AwtEnv;
import onbon.bx06.Bx6GEnv;

public class MainApplication extends Application {

    private boolean initial;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // java.awt for android 初始化
            AwtEnv.link(this);                          // 將 Application 與 AWT 連結
            AwtEnv.configPaintAntiAliasFlag(true);      // 設定圖案是要抗鋸齒。

            // 建立 BX6G API 運行環境。
            Bx6GEnv.initial();

            this.initial = true;
        }
        catch (Exception ex) {
            this.initial = false;
        }
    }
}