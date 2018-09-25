package trio.com.trionewdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static trio.com.trionewdemo.Constant.SHOW_TYPE_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;
import static trio.com.trionewdemo.Constant.STYLE_KEY;

public class EditActivity extends AppCompatActivity {
    private RadioGroup showTypeRadioGroup;
    private RadioGroup styleRadioGroup;

    private RadioButton fullScreenBtn;
    private RadioButton fromBottomBtn;

    private RadioButton lineBtn;
    private RadioButton circleBtn;
    private RadioButton waveBtn;

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

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

        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
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
}
