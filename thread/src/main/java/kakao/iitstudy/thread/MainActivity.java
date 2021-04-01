package kakao.iitstudy.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.util.logging.LoggingPermission;

public class MainActivity extends AppCompatActivity {

    //Handler 객체 생성
    Handler handler = new Handler(Looper.getMainLooper()){
        //핸들러가 메세지를 받으면 호출되는 메소드
        @Override
        public void handleMessage(Message msg){

            //데이터를 받아서 ui를 갱신
            //전송받은 메세지를 가져오기
            int i = (Integer)msg.obj;

            TextView resultView = (TextView)findViewById(R.id.resultView);
            resultView.setText(i +"");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*//스레드를 생성해서 핸들러를 호출
        new Thread(){
            @Override
            public void run(){
                try {
                    for(int i = 0; i<10; i++){
                        Thread.sleep(1000);
                        //메세지 객체를 만들어서 데이터를 저장
                        Message msg = new Message();
                        msg.obj = i;
                        //핸들러에게 메세지 보내기
                        handler.sendMessage(msg);
                    }
                }catch(Exception e){
                    Log.e("예외", e.getLocalizedMessage());
                }
            }
        }.start();*/

        new Thread(){
            int value;
            public void run(){
                  try {
                      for(int i = 0; i<10; i++){
                          Thread.sleep(1000);
                          handler.post(new Runnable() {
                              @Override
                              public void run() {
                                  TextView resultView =
                                          (TextView)findViewById(R.id.resultView);
                                  resultView.setText(value +"");
                                  value++;
                              }
                          });
                      }
                  }catch (Exception e){
                      Log.e("예외", e.getLocalizedMessage());
                  }
            }
        }.start();

        //thread.sleep을 사용하기 위한 예외 처리
        //try {
           /* //스레드 미사용으로 모아서 textview에 한번에 출력됨
            for(int i = 0; i<10; i++){
                Thread.sleep(1000);
                Log.e("내용", "메세지1");
                resultView.setText("i=" + i);
            }*/

            /*//thread를 사용하지 않기 때문에 순차적 동작
            for(int i = 0; i<10; i++){
                Thread.sleep(1000);
                Log.e("내용", "메세지1");
            }
            for(int i = 0; i<10; i++){
                Thread.sleep(1000);
                Log.e("내용", "메세지2");
            }*/

            //스레드 사용으로 동시 진행
            /*new Thread(){
                public void run(){
                    try {
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(1000);
                            Log.e("내용", "메세지1");
                        }
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(1000);
                            Log.e("내용", "메세지2");
                        }
                    }catch(Exception e){}
                }
            }.start();*/

     /*   }catch (Exception e){
            Log.e("예외", e.getLocalizedMessage());
        }*/
    }
}