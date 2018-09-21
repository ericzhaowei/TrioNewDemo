package trio.com.trionewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import trio.com.trionewdemo.model.ListItemData;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<ListItemData> mData;
    private MainListViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        setContentView(R.layout.main_activity);
        mListView = findViewById(R.id.list_view);

        adapter = new MainListViewAdapter(this, mData);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setAdapter(adapter);
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
    }
}
