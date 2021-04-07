package kakao.iitstudy.adapterview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MovieLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_link);

        //데이터 가져오기
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        //webview 찾아와서 link 출력
        WebView webView = (WebView)findViewById(R.id.webview);
        //리다이렉트 되는 url 일 때 크롬을 사용하지 않도록 설정
        webView.setWebViewClient(new WebViewClient());

        //자바스키립트를 사용할 수 있도록 설정
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        //링크 출력
        webView.loadUrl(link);
    }
}