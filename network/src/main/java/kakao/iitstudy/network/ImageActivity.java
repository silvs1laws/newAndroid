package kakao.iitstudy.network;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //여러 스레드에서 사용하도록 하고자 할 때는 msg 의 what 을 이용해서 분기
            ImageView imageView = (ImageView) findViewById(R.id.ImageView);
            Bitmap bitmap = null;

            switch (msg.what) {
                case 1:
                    //데이터 가져오기
                    bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //이미지 당누로드 받아서 바로 출력하는 버튼의 클릭 이벤트 핸들러 작성
        Button btnDisplay = (Button) findViewById(R.id.btnDisplay);
        //anonymous class 이용해서 이벤트 처리 - java의 nested class, 람다 복습
        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        //이미지 가져오기
                        try {
                            //이미지를 다운로드 받을 스트림 생성
                            InputStream is = new URL("https://dimg.donga.com/wps/NEWS/IMAGE/2019/07/01/96260737.1.jpg").openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            //핸들러에게 전송할 메세지를 생성
                            Message message = new Message();
                            message.what = 1;
                            message.obj = bitmap;
                            //핸들러에게 메세지 전송
                            handler.sendMessage(message);


                        } catch (Exception e) {
                            Log.e("예외", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });
        //두번째 버튼의 클릭 이벤트 핸들러
        Button btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 앱 내에 파일이 존재하는지 확인 - web.jpg
                //파일 경로 생성

                //data가 저장되는 directory 경로를 생성
                String dirPath = Environment.getDataDirectory().getAbsolutePath();
                //파일 경로 생성
                //web.jpg는 저장할 파일 이름
                String path = dirPath + "/data/kakao.iitstudy.network/files/web.jpg";

                //파일이 있는지 확인
                //file 클래스를 다시 공부할 때 존재여부 ,마지막 수정 날짜, 생성 날짜, 크기를
                //가져오는 것은 중요
                if (new File(path).exists()){
                    //핸들러에게 전송할 메세지를 생성해서 출력을 요청
                    //이미 파일이 있는 경우이므로 데이터를 전달할 필요가 없습니다.
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }else{
                    //이미지 파일이 없는 경우 이미지 파일을 다운로드 받아서 저장하고 핸들러에게
                    //메세지를 전송
                    new Thread(){
                        @Override
                        public void run(){
                            //이미지 데이터 다운로드 받기
                            try {
                                URL url = new URL("https://www.spcmagazine.com/wp-content/uploads/2019/07/%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%B0%EC%8A%A4%ED%82%A8%EB%9D%BC%EB%B9%88%EC%8A%A4_-%EC%9D%B8%EA%B8%B0-%ED%94%8C%EB%A0%88%EC%9D%B4%EB%B2%84-2%EC%A2%85-%EC%9E%AC%EC%B6%9C%EC%8B%9C.jpg");
                            }catch (Exception e){
                                Log.e("예외 발생2", e.getLocalizedMessage());
                            }
                        }
                    }.start();

                }
            }
        });
    }
}
