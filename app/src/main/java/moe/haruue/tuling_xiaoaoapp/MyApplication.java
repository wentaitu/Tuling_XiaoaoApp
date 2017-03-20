package moe.haruue.tuling_xiaoaoapp;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 晴天 on 2017/3/20.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
