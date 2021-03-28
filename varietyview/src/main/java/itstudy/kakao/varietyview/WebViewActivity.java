package itstudy.kakao.varietyview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = (WebView)findViewById(R.id.webView);

        //Url이 리다이렉트 되는 경우에는 이 코드를 추가해야만 웹 뷰로 출력합니다.
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.google.com");

        Button btnLoadHtml = (Button)findViewById(R.id.btnLoadHtml);
        btnLoadHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //출력하는 Url에 자바 스크립트 코드가 있는 경우 webView의 설정을 변경
                WebSettings set = webView.getSettings();
                set.setJavaScriptEnabled(true);

                webView.loadUrl("file:///android_asset/test.html");
            }
        });
    }
}