package kakao.iitstudy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoSub = (Button)findViewById(R.id.btnGoSub);
        btnGoSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*//명시적 인텐트 호출
                //하위 activity 출력을 위한 intent 생성
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //하위 activity 출력
                startActivity(intent);*/
                //암시적 인텐트 호출
                Intent intent = new Intent();

                //입력한 내용 가져오기
                EditText mainEdit = (EditText)findViewById(R.id.mainEdit);
                String msg = mainEdit.getText().toString();
                //출력할 activity로 데이터 전달
                intent.putExtra("data", msg);

                intent.setAction("com.example.ACTION_VIEW");
                //startActivity(intent);

                //subactivity가 소멸될 대 콜백 메소드를 호출하도록 작성
                //10은 구분하기 위한 코드
                startActivityForResult(intent, 10);
            }
        });
    }

    //하위 Activity가 사라질 때 호출되는 메소드
    //이름을 잘못 입력했으면 @override를 지우라고 에러
    //그런 경우가 아니면 return을 해야 하거나 상위 클래스의 메소드를 호출하라고 에러
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case 10:
                switch (resultCode){
                    case RESULT_OK:
                        String msg = intent.getStringExtra("subData");
                        EditText editMain = (EditText)findViewById(R.id.mainEdit);
                        editMain.setText(msg);
                }
        }
    }
}