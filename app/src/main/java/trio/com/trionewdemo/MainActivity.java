package trio.com.trionewdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trio.com.trionewdemo.model.ListItemData;

import static trio.com.trionewdemo.Constant.LIST_KEY;
import static trio.com.trionewdemo.Constant.SP_NAME;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<ListItemData> mData;
    private MainListViewAdapter adapter;
    private TextView sendBtn;
    private EditText inputET;
    private SharedPreferences sp;
    private Gson gson;
    private ArrayList<String> spList = new ArrayList<>();

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

                    spList.add(input);
                    String jsonStr = gson.toJson(spList, new TypeToken<ArrayList<String>>() {}.getType());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(LIST_KEY, jsonStr);
                    editor.commit();
                }
            }
        });
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

        mData.add(new ListItemData(true, "CustomInput"));

        String jsonStr = sp.getString(LIST_KEY, "");

        List<String> spDataList = gson.fromJson(jsonStr, new TypeToken<ArrayList<String>>() {}.getType());

        if (spDataList != null) {
            spList.clear();
            spList.addAll(spDataList);
            for (String data : spList) {
                mData.add(new ListItemData(false, data));
            }
        }
    }
}
