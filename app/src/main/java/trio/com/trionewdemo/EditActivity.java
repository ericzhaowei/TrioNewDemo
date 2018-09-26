package trio.com.trionewdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trio.nnpredict.TrioWrap.Trio;
import com.trio.nnpredict.Utils.HttpUtil;

import static trio.com.trionewdemo.Constant.APP_KEY;
import static trio.com.trionewdemo.Constant.SERVER_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SHOW_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.STYLE_KEY;
import static trio.com.trionewdemo.Constant.USERID_KEY;

public class EditActivity extends AppCompatActivity {
    private RadioGroup showTypeRadioGroup;
    private RadioGroup styleRadioGroup;
    private RadioGroup serverRadioGroup;

    private RadioButton testServerBtn;
    private RadioButton onlineServerBtn;

    private RadioButton fullScreenBtn;
    private RadioButton fromBottomBtn;

    private RadioButton lineBtn;
    private RadioButton circleBtn;
    private RadioButton waveBtn;

    private EditText userIdET;
    private EditText appKeyET;

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;
    private boolean isTestServer = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_activity);
        showTypeRadioGroup = findViewById(R.id.show_type_group);
        fullScreenBtn = findViewById(R.id.full_screen);
        fromBottomBtn = findViewById(R.id.from_bottom);

        styleRadioGroup = findViewById(R.id.style_group);
        lineBtn = findViewById(R.id.line_progress);
        circleBtn = findViewById(R.id.circle_progress);
        waveBtn = findViewById(R.id.wave_progress);

        serverRadioGroup = findViewById(R.id.server_group);
        testServerBtn = findViewById(R.id.test_server);
        onlineServerBtn = findViewById(R.id.online_server);

        userIdET = findViewById(R.id.user_id);
        appKeyET = findViewById(R.id.app_key);

        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        String userID = sp.getString(USERID_KEY, "");
        userIdET.setText(userID);

        String appKey = sp.getString(APP_KEY, "");
        appKeyET.setText(appKey);

        String serverType = sp.getString(SERVER_TYPE_KEY, Constant.SERVER_TYPE.TEST_SERVER);

        if (serverType.equalsIgnoreCase(Constant.SERVER_TYPE.TEST_SERVER)) {
            testServerBtn.setChecked(true);
            isTestServer = true;
        } else {
            onlineServerBtn.setChecked(true);
            isTestServer = false;
        }

        serverRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.test_server:
                        editor.putString(SERVER_TYPE_KEY, Constant.SERVER_TYPE.TEST_SERVER);
                        isTestServer = true;
                        break;
                    case R.id.online_server:
                        editor.putString(SERVER_TYPE_KEY, Constant.SERVER_TYPE.ONLINE_SERVER);
                        isTestServer = false;
                        break;
                    default:
                        break;
                }
                editor.commit();
            }
        });

        String showType = sp.getString(SHOW_TYPE_KEY, Constant.SHOW_TYPE.FULL_SCREEN);

        if (showType.equalsIgnoreCase(Constant.SHOW_TYPE.FULL_SCREEN)) {
            fullScreenBtn.setChecked(true);
        } else {
            fromBottomBtn.setChecked(true);
        }

        String style = sp.getString(STYLE_KEY, Constant.STYLE_TYPE.CIRCLE);

        if (style.equalsIgnoreCase(Constant.STYLE_TYPE.CIRCLE)) {
            circleBtn.setChecked(true);
        } else if (style.equalsIgnoreCase(Constant.STYLE_TYPE.LINE)) {
            lineBtn.setChecked(true);
        } else if (style.equalsIgnoreCase(Constant.STYLE_TYPE.WAVE)) {
            waveBtn.setChecked(true);
        }

        showTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.full_screen:
                        editor.putString(SHOW_TYPE_KEY, Constant.SHOW_TYPE.FULL_SCREEN);
                        break;
                    case R.id.from_bottom:
                        editor.putString(SHOW_TYPE_KEY, Constant.SHOW_TYPE.FROM_BOTTOM);
                        break;
                    default:
                        break;
                }
                editor.commit();
            }
        });

        styleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.line_progress:
                        editor.putString(STYLE_KEY, Constant.STYLE_TYPE.LINE);
                        break;
                    case R.id.circle_progress:
                        editor.putString(STYLE_KEY, Constant.STYLE_TYPE.CIRCLE);
                        break;
                    case R.id.wave_progress:
                        editor.putString(STYLE_KEY, Constant.STYLE_TYPE.WAVE);
                        break;
                    default:
                        break;
                }
                editor.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String userID = userIdET.getText().toString();
        String appKey = appKeyET.getText().toString();

        editor = sp.edit();
        editor.putString(USERID_KEY, TextUtils.isEmpty(userID) ? "demo" : userID);
        editor.putString(APP_KEY, TextUtils.isEmpty(appKey) ? "b8c032ece893c9ccaaea11293c210103452dfb9d" : appKey);
        editor.commit();

        Trio.with().mUserID = sp.getString(USERID_KEY, "");
        Trio.with().mAppkey = sp.getString(APP_KEY, "");
        Trio.isTestEnv = isTestServer;
    }
}
