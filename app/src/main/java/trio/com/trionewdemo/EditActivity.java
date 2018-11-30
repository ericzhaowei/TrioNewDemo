package trio.com.trionewdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trio.nnpredict.TrioWrap.Trio;

import static trio.com.trionewdemo.Constant.APP_KEY;
import static trio.com.trionewdemo.Constant.SERVER_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.UI_TYPE_KEY;
import static trio.com.trionewdemo.Constant.USERID_KEY;

public class EditActivity extends AppCompatActivity {
    private RadioGroup uiTypeGroup;
    private RadioGroup serverRadioGroup;

    private RadioButton testServerBtn;
    private RadioButton onlineServerBtn;

    private RadioButton one;
    private RadioButton two;
    private RadioButton three;

    private EditText userIdET;
    private EditText appKeyET;

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;
    private boolean isTestServer = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_activity);
        uiTypeGroup = findViewById(R.id.ui_type_group);

        one = findViewById(R.id.solution_one);
        two = findViewById(R.id.solution_two);
        three = findViewById(R.id.solution_three);

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

        String showType = sp.getString(UI_TYPE_KEY, "0");

        if (showType.equalsIgnoreCase("0")) {
            one.setChecked(true);
        } else if (showType.equalsIgnoreCase("1")) {
            two.setChecked(true);
        } else if (showType.equalsIgnoreCase("2")) {
            three.setChecked(true);
        }

        uiTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor = sp.edit();
                switch (i) {
                    case R.id.solution_one:
                        editor.putString(UI_TYPE_KEY, "0");
                        Constant.uiType = Trio.UIType.UI_ONE;
                        break;
                    case R.id.solution_two:
                        editor.putString(UI_TYPE_KEY, "1");
                        Constant.uiType = Trio.UIType.UI_TWO;
                        break;
                    case R.id.solution_three:
                        editor.putString(UI_TYPE_KEY, "2");
                        Constant.uiType = Trio.UIType.UI_THREE;
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
