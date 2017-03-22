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
            AwtEnv.initial(this);
            AwtEnv.configPaintAntiAliasFlag(true);

            // 建立 BX6G API 運行環境。
            Bx6GEnv.initial();

            this.initial = true;
        }
        catch (Exception ex) {
            this.initial = false;
        }
    }
}