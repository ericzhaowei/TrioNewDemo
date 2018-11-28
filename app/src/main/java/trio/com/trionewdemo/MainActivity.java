package trio.com.trionewdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trio.nnpredict.LocalPredict.NNPredict;
import com.trio.nnpredict.TrioWrap.Trio;
import com.trio.nnpredict.Utils.MACUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trio.com.trionewdemo.model.ListItemData;

import static trio.com.trionewdemo.Constant.LIST_DATA_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;

public class MainActivity extends AppCompatActivity {
    public ListView mListView;
    private ArrayList<ListItemData> mData;
    private MainListViewAdapter adapter;
    private TextView sendBtn;
    private EditText inputET;
    private SharedPreferences sp;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        initData();

        setContentView(R.layout.main_activity);
        mListView = findViewById(R.id.list_view);

        adapter = new MainListViewAdapter(this, mData);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setAdapter(adapter);

        sendBtn = findViewById(R.id.send_btn);
        inputET = findViewById(R.id.input_et);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputET.getText().toString();

                if (!TextUtils.isEmpty(input)) {
                    mData.add(new ListItemData(false, input));

                    adapter.notifyDataSetChanged();
                    inputET.setText("");

                    saveListToSp();
                }
            }
        });

        // 获取手机唯一标志，定位，联系人权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Trio.UNIQUE_ID = MACUtil.getUniqueId(this) ;
            }
            if (grantResults.length == 3 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            NNPredict.phoneBookTrie = NNPredict.getNNPredictInstance().buildPhoneBookTrie();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);  //初始化加载菜单项
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private void initData() {
        mData = new ArrayList<>();

        String listDataStr = sp.getString(LIST_DATA_KEY, "");

        if (TextUtils.isEmpty(listDataStr)) {
            InputStream inputStream;
            BufferedReader reader;
            try {
                inputStream = getAssets().open("init_samples.txt");
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!TextUtils.isEmpty(line)) {
                        boolean isTitle = (line.startsWith("[") && line.endsWith("]")) ? true : false;
                        mData.add(new ListItemData(isTitle, isTitle ? line.substring(1, line.length() - 1) : line));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            mData.add(new ListItemData(true, "手动输入"));
        } else {
            String jsonStr = sp.getString(LIST_DATA_KEY, "");

            ArrayList<ListItemData> spDataList = gson.fromJson(jsonStr, new TypeToken<ArrayList<ListItemData>>() {}.getType());

            if (spDataList != null) {
                mData.clear();
                mData.addAll(spDataList);
            }
        }
    }

    public void deleteItem(int position) {
        mData.remove(position);
        adapter.notifyDataSetChanged();

        saveListToSp();
    }

    private void saveListToSp() {
        String jsonStr = gson.toJson(mData, new TypeToken<ArrayList<ListItemData>>() {}.getType());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LIST_DATA_KEY, jsonStr);
        editor.commit();
    }

    public void openBrowserActivity(String content) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("url", content);

        startActivity(intent);
    }
}
