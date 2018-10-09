package trio.com.trionewdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trio.nnpredict.TrioWrap.Trio;

import static trio.com.trionewdemo.Constant.APP_KEY;
import static trio.com.trionewdemo.Constant.SERVER_TYPE_KEY;
import static trio.com.trionewdemo.Constant.CARD_SHOW_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.PROGRESS_STYLE_KEY;
import static trio.com.trionewdemo.Constant.Settings_TRIO_APPKEY;
import static trio.com.trionewdemo.Constant.Settings_TRIO_USERID;
import static trio.com.trionewdemo.Constant.USERID_KEY;

public class EditActivity extends AppCompatActivity {
    private RadioGroup cardShowTypeRadioGroup;
    private RadioGroup progressStyleRadioGroup;
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
        cardShowTypeRadioGroup = findViewById(R.id.card_show_type_group);
        fullScreenBtn = findViewById(R.id.full_screen);
        fromBottomBtn = findViewById(R.id.from_bottom);

        progressStyleRadioGroup = findViewById(R.id.progress_style_group);
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

        String showType = sp.getString(CARD_SHOW_TYPE_KEY, Constant.CARD_SHOW_TYPE.FROM_BOTTOM);

        if (showType.equalsIgnoreCase(Constant.CARD_SHOW_TYPE.FULL_SCREEN)) {
            fullScreenBtn.setChecked(true);
        } else {
            fromBottomBtn.setChecked(true);
        }

        cardShowTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.full_screen:
                        editor.putString(CARD_SHOW_TYPE_KEY, Constant.CARD_SHOW_TYPE.FULL_SCREEN);
                        Constant.cardStyle = Trio.CardStyle.ACTIVITY;
                        break;
                    case R.id.from_bottom:
                        editor.putString(CARD_SHOW_TYPE_KEY, Constant.CARD_SHOW_TYPE.FROM_BOTTOM);
                        Constant.cardStyle = Trio.CardStyle.DIALOG;
                        break;
                    default:
                        break;
                }
                editor.commit();
            }
        });

        String style = sp.getString(PROGRESS_STYLE_KEY, Constant.PROGRESS_STYLE_TYPE.CIRCLE);

        if (style.equalsIgnoreCase(Constant.PROGRESS_STYLE_TYPE.CIRCLE)) {
            circleBtn.setChecked(true);
        } else if (style.equalsIgnoreCase(Constant.PROGRESS_STYLE_TYPE.LINE)) {
            lineBtn.setChecked(true);
        } else if (style.equalsIgnoreCase(Constant.PROGRESS_STYLE_TYPE.WAVE)) {
            waveBtn.setChecked(true);
        }

        progressStyleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.line_progress:
                        editor.putString(PROGRESS_STYLE_KEY, Constant.PROGRESS_STYLE_TYPE.LINE);
                        Constant.progressBarMode = Trio.ProgressBarMode.TOP_LEFT_TO_RIGHT;
                        break;
                    case R.id.circle_progress:
                        editor.putString(PROGRESS_STYLE_KEY, Constant.PROGRESS_STYLE_TYPE.CIRCLE);
                        Constant.progressBarMode = Trio.ProgressBarMode.CIRCLE;
                        break;
                    case R.id.wave_progress:
                        editor.putString(PROGRESS_STYLE_KEY, Constant.PROGRESS_STYLE_TYPE.WAVE);
                        Constant.progressBarMode = Trio.ProgressBarMode.RADIATION;
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
        editor.putString(USERID_KEY, userID);
        editor.putString(APP_KEY, appKey);
        editor.commit();

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(EditActivity.this.getPackageName(),
                "com.trio.nnpredict.TrioWrap.Trio$TrioBroadcastReceiver"));
        intent.putExtra("trio_userId", sp.getString(USERID_KEY, ""));
        intent.putExtra("trio_appKey", sp.getString(APP_KEY, ""));
        sendBroadcast(intent);

        Trio.isTestEnv = isTestServer;
    }
}
