package trio.com.trionewdemo;

import android.app.Application;
import android.util.Log;

import com.trio.nnpredict.TrioWrap.InitProgressListener;
import com.trio.nnpredict.TrioWrap.Trio;
import com.trio.nnpredict.Utils.Consts;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Trio trio = new Trio.Builder()
                .context(getApplicationContext())
                .assetManager(getAssets())
                .timeOut(5000)
                .userID("demo")  // YOUR USERID
                .appkey("b8c032ece893c9ccaaea11293c210103452dfb9d") // YOUR APPKEY
                .setTest(true) // 是否使用测试环境
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
