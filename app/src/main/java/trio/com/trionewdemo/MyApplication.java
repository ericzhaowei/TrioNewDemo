package trio.com.trionewdemo;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.trio.nnpredict.TrioWrap.InitProgressListener;
import com.trio.nnpredict.TrioWrap.Trio;
import com.trio.nnpredict.Utils.Consts;

import static trio.com.trionewdemo.Constant.APP_KEY;
import static trio.com.trionewdemo.Constant.CARD_SHOW_TYPE_KEY;
import static trio.com.trionewdemo.Constant.PROGRESS_STYLE_KEY;
import static trio.com.trionewdemo.Constant.SERVER_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.USERID_KEY;
import static trio.com.trionewdemo.Constant.cardStyle;

public class MyApplication extends Application {
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        String serverType = sp.getString(SERVER_TYPE_KEY, Constant.SERVER_TYPE.TEST_SERVER);

        boolean isTestServer;
        if (serverType.equalsIgnoreCase(Constant.SERVER_TYPE.TEST_SERVER)) {
            isTestServer = true;
        } else {
            isTestServer = false;
        }

        String userID = sp.getString(USERID_KEY, "demo");
        String appKey = sp.getString(APP_KEY, "b8c032ece893c9ccaaea11293c210103452dfb9d");

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERID_KEY, userID);
        editor.putString(APP_KEY, appKey);
        editor.commit();

        // card类型
        Constant.cardStyle = sp.getString(CARD_SHOW_TYPE_KEY, Constant.CARD_SHOW_TYPE.FROM_BOTTOM)
                .equalsIgnoreCase(Constant.CARD_SHOW_TYPE.FROM_BOTTOM) ? Trio.CardStyle.DIALOG : Trio.CardStyle.ACTIVITY;

        // 进度条样式
        switch (sp.getString(PROGRESS_STYLE_KEY, Constant.PROGRESS_STYLE_TYPE.WAVE)) {
            case Constant.PROGRESS_STYLE_TYPE.CIRCLE:
                Constant.progressBarMode = Trio.ProgressBarMode.CIRCLE;
                break;
            case Constant.PROGRESS_STYLE_TYPE.LINE:
                Constant.progressBarMode = Trio.ProgressBarMode.TOP_LEFT_TO_RIGHT;
                break;
            case Constant.PROGRESS_STYLE_TYPE.WAVE:
                Constant.progressBarMode = Trio.ProgressBarMode.RADIATION;
                break;
            default:
                Constant.progressBarMode = Trio.ProgressBarMode.RADIATION;
        }

        Trio trio = new Trio.Builder()
                .context(getApplicationContext())
                .assetManager(getAssets())
                .timeOut(5000)
                .userID(userID)  // YOUR USERID
                .appkey(appKey) // YOUR APPKEY
                .setTest(isTestServer) // 是否使用测试环境
                .build(new InitProgressListener() {
                    @Override
                    public void onCompleted() {
                        Log.d(Consts.NNPredictLogTag, "Init success");
                    }

                    @Override
                    public void onError(String errorMsg, int errorCode) {
                        Log.e(Consts.NNPredictLogTag, errorMsg);
                    }
                });

        Trio.setSingletonInstance(trio);
    }
}
