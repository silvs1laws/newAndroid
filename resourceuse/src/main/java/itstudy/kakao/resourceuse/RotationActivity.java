package itstudy.kakao.resourceuse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class RotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        //처음 만들어서 값이 없으면
        if (savedInstanceState != null) {

            //key라는 이름으로 저장된 데이터 출력
            String key = savedInstanceState.getString("key");
            //처음 실행할 때는 안나오고 회전이 발생하거나 다른 앱에 의해서 앱이 종료되었다가
            //다시 실행할 경우 출력
            if (key != null) {
                Log.e("key", key);
            }
        }
    }

    //기기 환경이 변경될 때 호출되는 메소드
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //상위 클래스의 메소드를 호출합니다.
        //안드로이드가 제공해주는 기능을 수행하고 내가 원하는 기능을 추가합니다.
        super.onConfigurationChanged(newConfig);

        TextView lblRotation = (TextView) findViewById(R.id.lblRotation);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lblRotation.setText("기기 방향은 가로");
            Log.e("방향", "가로");
        } else {
            lblRotation.setText("기기의 방향은 세로");
            Log.e("방향", "세로");
        }
    }

    //actibity가 종료되기 전에 호출되어서 앱의 상태를 저장할 수 있는 메소드를 재정의
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        //상위 클래스의 메소드 호출
        super.onSaveInstanceState(bundle);

        bundle.putString("key", "현재 상태 저장");

    }
}