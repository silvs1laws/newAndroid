package kakao.iitstudy.multimedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //노래 목록을 저장할 리스트
    ArrayList<String> list;
    //현재 재생 중인 노래의 인덱스를 저장할 프로퍼티
    int idx;

    //노래와 영상 재생기
    MediaPlayer player;
    //버튼
    Button playbtn, stopbtn, prevbtn, nextbtn;
    //텍스트 뷰
    TextView fileName;
    //seekbar
    SeekBar progress;

    //현재 음원 재생 여부를 저장할 프로퍼티
    boolean wasPlaying;

    //노래 제목을 출력하고 Seekbar 초기화를 위한 핸들러
    Handler messageHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //핸들러에게 전송된 메세지 가져오기
            boolean result = (Boolean)msg.obj;
            //노래를 가져오면
            if (result == true){
                //노래 제목을 출력하고 seekbar의 범위를 노래의 길이로 변경
                String resultMsg = "재생 준비 완료";
                fileName.setText(list.get(idx));
                progress.setMax(player.getDuration());
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        resultMsg, Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "재생 준비 실패", Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    //사용자 정의 메소드

    //노래를 재생할 수 있도록 준비해두는 메소드
    private boolean prepare() {
        try {
            //재생 준비
            player.prepare();
        } catch (Exception e) {
            Log.e("노래 재생 준비 실패", e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    //재생할 위치에 노래의 인덱스를 받아서 재생기에 노래를 설정메소드
    private void loadMedia(int idx) {
        Message message = new Message();
        //재생할 노래의 인덱스를 핸들러에게 전달하기 위해서 메세지에 저장
        message.what = idx;

        try {
            //web url을 이용해서 재생할 노래 설정
            player.setDataSource(MainActivity.this, Uri.parse(list.get(idx)));
        } catch (Exception e) {
            message.obj = false;
        }
        //노래 재생 준비
        if (prepare() == false) {
            message.obj = false;
        } else {
            message.obj = true;
        }
        //핸들러에게 메세제 전송
        messageHandler.sendMessage(message);
    }

    //Seekbar를 업데이트 해주는 해들러
    Handler progressHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //노래 재생 중이 아니면 리턴
            if (player == null) {
                return;
            }
            //노래가 재생 중인 경우 노래의 현재 위치를 seekbar에 표시
            if (player.isPlaying()) {
                progress.setProgress(player.getCurrentPosition());
            }
            //0.2초 후에 핸들러 다시 호출
            progressHandler.sendEmptyMessageDelayed(0, 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //텍스트 뷰 찾아오기
        fileName = (TextView) findViewById(R.id.filename);
        //노래 목록을 저장할 리스트 객체 생성
        list = new ArrayList<String>();

        //노래 목록을 가져올 스레드 생성
        new Thread() {
            @Override
            public void run() {
                try {
                    //url todtjd
                    URL url = new URL("http://cyberadam.cafe24.com/song/song.txt");
                    //연결 객체를 생성
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //옵션 설정 - 파라미터 전송 방법, 시간, 캐시 사용 여부, 파라미터 전송 처리
                    con.setConnectTimeout(30000);

                    //스트림 생성 byte 단위인지 character 단위 인지에 따라 다르게 생성
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    //문자열 전체 읽기
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line + "\n");
                    }
                    //문자열을 제대로 다운로드 받았는지 화인
                    //Snackbar.make(getWindow().getDecorView().getRootView(),
                    // sb.toString(), BaseTransientBottomBar.LENGTH_LONG).show();

                    //다운로드 받은 데이터를 사용
                    //,로 구분도니 문자열 사용
                    String[] songlist = sb.toString().split(",");
                    //배열의 내용을 리스트에 저장
                    for (String song : songlist) {
                        list.add("http://cyberadam.cafe24.com/song/" + song + ".mp3");
                    }

                    //배열을 이용해서 list를 생성
                    //list = new ArrayList<String>(Arrays.asList(songlist));

                    //재생기 생성
                    player = new MediaPlayer();
                    //노래의 인덱스 초기화
                    idx = 0;
                    //노래를 로드
                    loadMedia(idx);

                    //시크바 찾아오기
                    progress = (SeekBar) findViewById(R.id.progress);

                    //노래 재생이 끝나면 호출되는 이벤트 핸들러 - 다음 노래를 재생
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //마지막 노래면 처음으로 그렇지 않으면 +1
                            idx = (idx == list.size() - 1) ? 0 : idx + 1;
                            //재생기를 다시 시작 - 다음 노래를 재생
                            player.reset();
                            loadMedia(idx);
                            player.start();
                        }
                    });

                    //player가 노래를 재생하다가 에러가 발생한 경우 (mediaplayer의 onerrorlistener가 처리)
                    player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    "재생 중 에러 발생", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    //노래의 재생 위치를 옮겼을 때 호출되는 메소드
                    player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                        @Override
                        public void onSeekComplete(MediaPlayer mp) {
                            //재생 중이면 노래를 재생
                            if (wasPlaying){
                                player.start();
                            }
                        }
                    });

                    //seekbar의 thumb을 사용자가 움직였을 때 처리하는 이벤트
                    progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        //seekbar 의 thumb을 움직이고 난 후 호출되는 메소드
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser == true){
                                //노래의 재생 위치를 progress 값으로 변경
                                player.seekTo(progress);
                            }
                        }
                        //thumb을 눌렀을 때 호출되는 메소드
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            wasPlaying = player.isPlaying();
                            if (wasPlaying){
                                player.pause();
                            }
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    //핸들러에게 메세지를 전송
                    //메세지는 없고 .2초 후에 처리
                    progressHandler.sendEmptyMessageDelayed(0,200);
                } catch (Exception e) {
                    Log.e("음원 목록 가져오기 실패", e.getLocalizedMessage());
                }
            }
        }.start();

        //재생 버튼을 눌렀을 때 처리
        playbtn = (Button)findViewById(R.id.play);
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying() == false){
                    player.start();
                    playbtn.setText("Pause");
                }else{
                    player.pause();
                    playbtn.setText("Play");
                }
            }
        });

        //중지 버튼을 눌렀을 때 처리
        stopbtn = (Button)findViewById(R.id.stop);
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //재생을 중지
                playbtn.setText("Play");
                player.stop();
                progress.setProgress(0);
                //노래준비
                prepare();
            }
        });

        //이전 버튼의 이벤트 핸들러
        prevbtn = (Button)findViewById(R.id.prev);
        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasPlaying = player.isPlaying();
                idx = (idx == 0) ? list.size() -1 : idx -1;
                player.reset();
                loadMedia(idx);

                if (wasPlaying){
                    player.start();
                    playbtn.setText("Pause");
                }
            }
        });
        nextbtn = (Button)findViewById(R.id.prev);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasPlaying = player.isPlaying();
                idx = (idx == list.size() -1 ) ? 0 : idx +1;
                player.reset();
                loadMedia(idx);

                if (wasPlaying){
                    player.start();
                    playbtn.setText("Pause");
                }
            }
        });

    }
}