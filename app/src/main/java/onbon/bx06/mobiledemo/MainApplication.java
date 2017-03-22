package onbon.bx06.mobiledemo;

/**
 * Created by gazer on 3/21/2017.
 */

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