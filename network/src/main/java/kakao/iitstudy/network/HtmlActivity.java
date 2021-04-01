package kakao.iitstudy.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlActivity extends AppCompatActivity {

    //데이터를 출력할 핸들러
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String html = (String) msg.obj;
            //텍뷰 출력
            TextView tfResult = (TextView) findViewById(R.id.tfResult);
            tfResult.setText(html);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        EditText tfUrl = (EditText) findViewById(R.id.tfUrl);

        //버튼 클릭 이벤트 처리
        Button btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        try {
                            //다운로드 받을 url 생성
                            URL url = new URL(tfUrl.getText().toString());

                            //연결 객체를 생성
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            //필요한 옵션 설정
                            con.setConnectTimeout(30000); //연결 제한 시간 설정
                            //캐시 사용 안함
                            //자주 변경되는 데이터 false 변경되지 않는 데이터 true
                            con.setUseCaches(false);
                            con.setRequestMethod("GET");//요청방식 설정 get 생략 가능

                            //응답이 제대로 오면
                            if (con.getResponseCode() == HttpURLConnection.HTTP_OK){
                                //전송된 문자열 읽기 - 무조건 utf-8로 읽어옮
                                /*
                                BufferedReader br = new BufferedReader(
                                        new InputStreamReader(con.getInputStream())); */

                                BufferedReader br;
                                //연결된 곳의 헤더 정보를 가져옵니다.
                                String headerType = con.getContentType();
                                //문자열 비교 euc - kr 이 포함되어 있는지 확인
                                if(headerType.toUpperCase().indexOf("EUC-KR") >= 0){
                                    br = new BufferedReader(new InputStreamReader(
                                            con.getInputStream(), "EUC-KR"
                                    ));
                                }else{
                                    br = new BufferedReader(new InputStreamReader(
                                            con.getInputStream(), "UTF-8"
                                    ));
                                }


                                // 문자열을 추가할 stringbuilder 생성
                                StringBuilder sb = new StringBuilder();
                                //문자열을 줄단위로 읽어서 sb에 추가
                                while(true){
                                    String line = br.readLine(); //한줄 읽기
                                    //읽은 값이 없으면
                                    if (line == null){
                                        break;
                                    }
                                    sb.append(line + "\n");
                                }
                                br.close();
                                con.disconnect();

                                //핸들러에게 메세지를 전송
                                Message msg = new Message();
                                msg.obj = sb.toString();
                                handler.sendMessage(msg);
                            }
                        }catch (Exception e){
                            Log.e("예외", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });
    }
}