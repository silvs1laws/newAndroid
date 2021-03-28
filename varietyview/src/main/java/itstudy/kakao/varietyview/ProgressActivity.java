package itstudy.kakao.varietyview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ProgressActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Button btnStart = (Button)findViewById(R.id.btnStart);
        Button btnStop = (Button)findViewById(R.id.btnStop);
        ProgressBar rect = (ProgressBar)findViewById(R.id.progressbar);
        ProgressBar circle = (ProgressBar)findViewById(R.id.progressindicator);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rect.setProgress(50);
                circle.setVisibility(View.VISIBLE);

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rect.setProgress(0);
                circle.setVisibility(View.INVISIBLE);

            }
        });

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        TextView lblSeek = (TextView)findViewById(R.id.lblSeekValue);

        //시크바 값이 변경 될 때 처리
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //값이 변경될 때
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //현재 값을 TextView에 출력
                String msg = String.format("%d", progress);
                lblSeek.setText(msg);
            }
            //값을 변경하기 위해서 thumb을 선택 햇을 때
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //값의 변경이 종료디고 thumb에서 손을 뗄 때
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Snackbar.make(seekBar, "값 변경 종료", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}