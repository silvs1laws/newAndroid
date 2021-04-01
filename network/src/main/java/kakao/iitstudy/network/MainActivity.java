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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    //전송 받은 내용을 출력하는 핸들러
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            String data = (String)msg.obj;
            //텍스트 뷰 출력하기
            TextView tfinput = (TextView)findViewById(R.id.tfInput);
            tfinput.setText(data);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tfInput = (TextView)findViewById(R.id.tfInput);
        EditText tfOutput = (EditText)findViewById(R.id.tfOutput);
        Button btnSend = (Button)findViewById(R.id.btnSend);

        //버튼 이벤트 처리
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //스레드 생성
                new Thread(){
                    public void run(){
                        try {
                            //서버와 통신할 소켓 생성
                            Socket socket = new Socket("172.30.1.8", 11001);
                            //데이터 전송
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(tfOutput.getText().toString());
                            oos.flush();

                            //전송된 데이터 읽기
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            String msg = (String)ois.readObject();

                            //데이터 출력을 스레드가 하지 않고 핸들러에게 위임
                            //tfInput.setText(msg);

                            Message message = new Message();
                            message.obj = msg;
                            handler.sendMessage(message);

                            oos.close();
                            ois.close();
                            socket.close();
                        }catch(Exception e){
                            Log.e("예외", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });
    }
}