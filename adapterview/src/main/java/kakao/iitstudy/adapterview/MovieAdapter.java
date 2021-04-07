package kakao.iitstudy.adapterview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieAdapter extends BaseAdapter {
    //뷰 전개를 위한 layoutinflater를 생성할 context
    Context context;
    //출력할 데이터
    ArrayList<Movie> list;
    //뷰 전개를 위한 layoutinflater
    LayoutInflater inflater;
    //이미지 뷰에 다운로드 받은 이미지를 출력해주는 핸들러
    Handler handler = new Handler(Looper.getMainLooper()){
       @Override
       public void handleMessage(Message msg){
           Map<String, Object> map = (Map<String, Object>)msg.obj;
           //전달된 데이터 꺼내기
           ImageView imageView = (ImageView)map.get("imageview");
           Bitmap bitmap = (Bitmap)map.get("bitmap");
           //이미지 뷰에 이미지 출력
           imageView.setImageBitmap(bitmap);
       }
    } ;


    //2개의 프로퍼티를 주입받기 위한 생성자
    public MovieAdapter (Context context, ArrayList<Movie> list){
        this.context = context;
        this.list = list;
        //xml 파일을 view 객체로 변환할 layoutinflater
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //출력할 항목 개수
    @Override
    public int getCount() {
        return list.size();
    }
    //기본 모양으로 출력할 때 보여질 문자열을 설정하는 메소드
    @Override
    public Object getItem(int position) {
        return list.get(position).getTitle();
    }
    //항목 뷰의 아이디
    @Override
    public long getItemId(int position) {
        return position;
    }
    //실제 출력될 항목 뷰를 설정하는 메소드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.movie_cell, parent, false);
        }
        //뷰 찾아오기
        TextView titleView = (TextView)convertView.findViewById(R.id.movietitle);
        titleView.setText(list.get(position).getTitle());

        TextView subTitleView = (TextView)convertView.findViewById(R.id.moviesubtitle);
        subTitleView.setText(list.get(position).getSubtitle());
        //평점이 10점 만점에 double
        //rating은 별 5개로 되어있고 float이므로 변환 시켜줘야 함
        RatingBar ratingBar = (RatingBar)convertView.findViewById(R.id.movierating);
        ratingBar.setRating((float)list.get(position).getRating()/2);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movieimage);
        //이미지를 다운로드 받아서 핸들러에게 출력을 요청
        //핸들러에게 imageview 와 bitmap을 전달
        new Thread(){
            public void run(){
                try {
                    //이미지를 다운로드 받을 URL생성
                    URL url = new URL("http://cyberadam.cafe24.com/movieimage/"+
                            list.get(position).getThumbnail());
                    //이미지를 가져오기
                    InputStream is = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    //핸들러에게 전송할 메세지
                    Message msg = new Message();
                    Map<String, Object> map = new HashMap<>();
                    map.put("imageview", imageView);
                    map.put("bitmap", bitmap);
                    msg.obj = map;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    Log.e("이미지 다운로드 실패", e.getLocalizedMessage());
                }
            }
        }.start();

        return convertView;
    }
}
