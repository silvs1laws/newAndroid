package kakao.iitstudy.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class ImageActivity extends AppCompatActivity {

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            //여러 스레드에서 사용하도록 하고자 할 때는 msg 의 what 을 이용해서 분기
            switch(msg.what){
                case:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //이미지 당누로드 받아서 바로 출력하는 버튼의 클릭 이벤트 핸들러 작성
        Button btnDownload = (Button)findViewById(R.id.btnDownload);
        //anonymous class 이용해서 이벤트 처리 - java의 nested class, 람다 복습
       btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}