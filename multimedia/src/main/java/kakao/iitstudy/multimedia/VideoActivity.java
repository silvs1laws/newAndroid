package kakao.iitstudy.multimedia;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //videoview
        VideoView videoView = (VideoView)findViewById(R.id.videoView);

        //재생할 비디오 설정
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);

        //비디오 파일 경로
        String path = "android.resource://" + getPackageName() + "/" + R.raw.trailer;
        videoView.setVideoURI(Uri.parse(path));
        videoView.requestFocus();

        //재생 버튼 이벤트 처리
        Button startbtn = (Button)findViewById(R.id.startBtn);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(0);
                videoView.start();
            }
        });

        //재생 준비가 되면
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "재생 준비가 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}