package trio.com.trionewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.trio.nnpredict.TrioWrap.Trio;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview_layout);

        mWebView = findViewById(R.id.customActionWebView);

        Trio.with().registeWebView(mWebView);

        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
    }
}
