package kakao.iitstudy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        TextView resultText = (TextView)findViewById(R.id.resultText);
        Button btn_contacts = (Button)findViewById(R.id.btn_contacts);
        Button btn_camera = (Button)findViewById(R.id.btn_camera);
        Button btn_speech = (Button)findViewById(R.id.btn_speech);
        Button btn_browser = (Button)findViewById(R.id.btn_browser);
        Button btn_map = (Button)findViewById(R.id.btn_map);
        Button btn_call = (Button)findViewById(R.id.btn_call);
        ImageView resultImage = (ImageView)findViewById(R.id.resultImage);

        //지도출력
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //지도 앱 출력
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:37.5662952, 126.977945"
                ));
                startActivity(intent);
            }
        });

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://cyberadam.cafe24.com"
                ));
                startActivity(intent);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "tel:010-2081-2180"
                ));
                startActivity(intent);
            }
        });

        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //음성녹음 activity
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //데이터 설정
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "음성 녹음 테스트");
                //activity를 출력하고 activity가 종료되면 콜백 메소드를 호출해서
                //데이터를 넘겨줍니다.
                startActivityForResult(intent,10);
            }
        });
        //카메라 앱을 실행해서 사진을 촬영하고 촬영한 사진을 이미지 뷰에 출력
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //카메라 앱 activity 생성
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //실행
                startActivityForResult(intent, 20);
            }
        });
        //주소록 앱을 실행해서 데이터를 가져오기
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //디바이스의 젖아된 데이터 activity
                Intent intent = new Intent(Intent.ACTION_PICK);
                //주소록 설정
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 30);

            }
        });
    }
    //startActivityforresult로 activity를 출력한 경우
    //출력한 activity가 종료되면 호출되는 콜백 메소드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //상위 클래스의 메소드 호출
        super.onActivityResult(requestCode, resultCode, data);
        //주소록 앱을 실행하고 확인을 눌러서 돌아온 경우 처리
        if (requestCode == 30 && resultCode == RESULT_OK) {
            String result = data.getDataString();
            TextView resultText = (TextView)findViewById(R.id.resultText);
            resultText.setText(result);
        }
        //카메라를 실행하고 확인을 눌러서 돌아온 경우 처리
        if(requestCode == 20 && resultCode == RESULT_OK){
            //촬영된 데이터 가져오기
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //이미지 뷰에 출력
            ImageView resultImage = (ImageView)findViewById(R.id.resultImage);
            resultImage.setImageBitmap(bitmap);
        }
        //10번 코드와 함께 호출한 activity가 종료되고 확인을 눌렀다면
        if(requestCode == 10 && resultCode == RESULT_OK){
            //음성 데이터 파일 이름 출력하기
            ArrayList<String> list = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
            );
            String result = list.get(0);
            TextView resultText = (TextView)findViewById(R.id.resultText);
            resultText.setText(result);
        }
    }
}