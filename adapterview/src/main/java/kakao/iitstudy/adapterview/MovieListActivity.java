package kakao.iitstudy.adapterview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    private ProgressBar downloadview;

    //파싱한 결과 데이터를 저장할 프로퍼티 생성
    private int count;
    private ArrayList<Movie> movieList;

    //listview 관련 프로퍼티
    ListView listView;
    //ArrayAdapter<Movie> adapter;

    //사용자 항목 뷰를 사용하기 위한 adapter
    MovieAdapter adapter;

    //listview에 데이터를 다시 출력할 핸들러
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            //listview 재출력
            adapter.notifyDataSetChanged();
            //진행 상황을 나타내는 뷰 안보이게
            downloadview.setVisibility(View.INVISIBLE);
            //스레드 변수를 초기화
            th = null;

        }
    };

    //마지막 항목 뷰가 스크롤에 의해서 보여진 것인지를 판별한 변수
    boolean lastItemVisibleFlag = false;

    //페이지 번호를 저장할 프로퍼티
    int page = 1;

    //데이터를 다운로드 받는 스레드 클래스
    class ThreadEx extends Thread{
        public void run(){
            try {
                //url생성 - 한글이 포함되어있는지 확인 포함되어 있을 시 인코딩
                URL url = new URL("http://cyberadam.cafe24.com/movie/list?page=" + page);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                //옵션 설정 - 전송방식 그리고 파라미터 확인
                con.setRequestMethod("GET");
                con.setConnectTimeout(30000);
                con.setUseCaches(false);
                //post 방식이면 파라미터에 파일이 포함되어 있는지 여부에 따라 코드 추가

                //문자열을 다운로드 받아서 저장할 객체와 스트림을 생성
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        con.getInputStream()
                ));

                //문자열을 줄 단위로 읽어서 sb에 추가
                while (true){
                    String line = br.readLine();
                    if (line == null){
                        break;
                    }
                    sb.append(line + "\n");
                }
                //연결 해제
                br.close();
                con.disconnect();
                //문자열 jsonobject로 변환
                JSONObject root = new JSONObject(sb.toString());
                //데이터 개수 가져오기
                count = root.getInt("count");
                //영화 목록 가져오기
                JSONArray movies = root.getJSONArray("list");
                for (int i = 0; i<movies.length(); i++){
                    //하나의 데이터 가져오기
                    JSONObject item = movies.getJSONObject(i);
                    //vo에 데이터 저장
                    Movie movie = new Movie();
                    movie.setTitle(item.getString("title"));
                    movie.setSubtitle(item.getString("subtitle"));
                    movie.setRating(item.getDouble("rating"));
                    movie.setLink(item.getString("link"));
                    movie.setThumbnail(item.getString("thumbnail"));
                    //list에 저장
                    movieList.add(movie);
                }

                //핸들러에게 메세지 전송
                Message msg = new Message();
                handler.sendMessage(msg);

                //보낼 데이터가 없을 시 아래처럼 작성 가능
                //handler.sendEmptyMessage(1);

            }catch (Exception e){
                Log.e("다운로드 파싱 예외", e.getLocalizedMessage());
            }
        }
    }
    //스레드 클래스의 객체를 참조할 프로퍼티
    //여기에 만든 이유는 스레드가 동작 중이면 다른 스레드를 생성하지 못하도록 하기 위해서
    ThreadEx th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //영화목록을 저장할 list생성
        movieList = new ArrayList<>();

        //ListView 출력을 위한 설정
        listView = (ListView)findViewById(R.id.listview);
        /*adapter = new ArrayAdapter<>(MovieListActivity.this, android.R.layout.simple_list_item_1,
                movieList);*/
        adapter = new MovieAdapter(MovieListActivity.this, movieList);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.CYAN));
        listView.setDividerHeight(3);

        //진행상황을 표시할 뷰
        downloadview = (ProgressBar)findViewById(R.id.downloadview);
        //화면출력
        downloadview.setVisibility(View.VISIBLE);

        //다운로드 받고 json 파싱을 수행한 후 재출력을 위해서 핸들러를 호출하는
        //스레드를 생성하고 실행
        //이전 스레드가 동작 중이라면 중지
        if (th != null){
            return;
        }

        //이전 스레드가 없다면 스레드를 생성해서 데이터 가져오기 수행
        th = new ThreadEx();
        th.start();

        //listview의 항목을 클릭했을 때 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //하위 activity에게 전달할 데이터 가져오기
                String link = movieList.get(position).getLink();
                //intent 생성
                Intent intent = new Intent(MovieListActivity.this, MovieLinkActivity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });

        //listview의 scroll 이벤트 처리
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //스크롤 상태가 변경될 때 호출되는 메소드 - 다음 페이지 데이터 가져오기를 수행
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag == true) {
                    //페이지 번호를 증가
                    page = page + 1;
                    //전체 데이터를 출력했는지 확인
                    if (page * 10 >= count) {
                        Snackbar.make(view, "더 이상 목록이 없습니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                        return;
                    }
                    //스레드가 동작 중이면 중지
                    if (th != null) {
                        return;
                    }
                    downloadview.setVisibility(View.VISIBLE);
                    th = new ThreadEx();
                    th.start();
                }
            }
            //firstVisibleItem 은 현재 보여지고 있는 항목 중에서 첫번째 항목의 인덱스
            //visibleItemCount 는 보여지고 있는 데이터 개수
            //totalItemCount 는 전체 데이터 개수
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //마지막에서 스크롤 한 것인지 판단 여부를 저장하는 프로퍼티의 값 설정

                //데이터가 있고 첫번째 인덱스와 보여진 데이터 개수를 더한 값이
                //전체 데이터 개수보다 크거나 같다면 lastItemvisibleflag는 true
                lastItemVisibleFlag = totalItemCount > 0
                        && firstVisibleItem + visibleItemCount >= totalItemCount;
            }
        });

        //스와이프 레이아웃의 리프레시 이벤트 처리
        SwipeRefreshLayout swipe_layout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //업데이트 된 데이터가 있는지 확인
                //마지막 업데이트 된 시간을 비교
                page = page + 1;
                //더 이상 가져올 데이터가 없으면 데이터 업데이트 안 함
                if (page * 10 >= count){
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "이미 최신 목록입니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                //다운로드 중이면 다운로드 안함
                if (th != null){
                    return;
                }
                downloadview.setVisibility(View.VISIBLE);
                th = new ThreadEx();
                th.start();
                swipe_layout.setRefreshing(false);
            }
        });

    }
}