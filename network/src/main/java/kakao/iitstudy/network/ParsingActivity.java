package kakao.iitstudy.network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ParsingActivity extends AppCompatActivity {


    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage( Message msg) {
            String data = (String)msg.obj;
            TextView tfParsing = (TextView)findViewById(R.id.tfParsing);
            tfParsing.setText(data);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);

        Button btnRead = (Button)findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //스레드를 생성해서 시작
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            //문자열 다운로드
                            URL url = new URL("https://rss.joins.com/");
                            //연결
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setConnectTimeout(30000);
                            con.setUseCaches(false);

                            //문자열을 읽기 위한 스트림 생성
                            BufferedReader br = null;
                            String headerType = con.getContentType();
                            if (headerType.toUpperCase().indexOf("EUC-KR") >=0 ) {
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "EUC-KR"));
                            }else{
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                            }
                            //문자열 읽기
                            StringBuilder sb = new StringBuilder();
                            while(true){
                                String line = br.readLine();
                                if(line == null){
                                    break;
                                }
                                sb.append(line + "\n");

                            }
                            //출력 확인
                            /*Message msg = new Message();
                            msg.obj = sb.toString();
                            handler.sendMessage(msg);*/

                            //html 파싱 수행 파일을 메모리에 펼치기
                            Document doc = Jsoup.parse(sb.toString());
                            //찾고자 하는 선택자를 이용해서 elements를 찾아오기
                            Elements element = doc.select("#rss > div.m1Column > div:nth-child(1) > dl:nth-child(2) > dd:nth-child(3)");
                            //파싱한 결과를 저장할 문자열 객체
                            //listView에 출력할 때는 list를 이용
                            StringBuilder sbResult = new StringBuilder();

                            //하나씩 순회
                            for (Element elements : element){
                               String contents = element.text();
                               sbResult.append(contents + "\n");

                            }

                            Message msg = new Message();
                            msg.obj = sbResult.toString();
                            handler.sendMessage(msg);

                            br.close();
                            con.disconnect();

                        }catch (Exception e){
                            Log.e("예외- html", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });

        //xml Parsing 버튼의 클릭 이벤트 작성
        Button btnReadXML = (Button)findViewById(R.id.btnReadxml);
        btnReadXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            //데이터 다운로드
                            URL url = new URL("http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108");
                            //연결
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setConnectTimeout(30000);
                            con.setUseCaches(false);

                            //문자열을 읽기 위한 스트림 생성
                            BufferedReader br = null;
                            String headerType = con.getContentType();
                            if (headerType.toUpperCase().indexOf("EUC-KR") >=0 ) {
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "EUC-KR"));
                            }else{
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                            }
                            //문자열 읽기
                            StringBuilder sb = new StringBuilder();
                            while(true){
                                String line = br.readLine();
                                if(line == null){
                                    break;
                                }
                                sb.append(line + "\n");

                            }
                            //dom 파싱을 위해서 root 까지 찾아가기
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            InputStream is = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
                            org.w3c.dom.Document doc = builder.parse(is);
                            org.w3c.dom.Element root = doc.getDocumentElement();

                            //title 태그의 내용을 전부 가져오기
                            NodeList titles = root.getElementsByTagName("city");
                            NodeList wfs = root.getElementsByTagName("wf");

                            //결과를 저장할 스트링 객체
                            StringBuilder sbResult = new StringBuilder();
                            //순회
                            for(int i = 0; i<titles.getLength(); i++){
                                //하나 가져오기
                                Node item = titles.item(i);
                                //태그 안의 문자열 가져오기
                                Node contents = item.getFirstChild();
                                String title = contents.getNodeValue();
                                sbResult.append("지역 : " + title + "\t" );

                                item = wfs.item(i);
                                contents = item.getFirstChild();
                                String wf = contents.getNodeValue();
                                sbResult.append("날씨 :" + wf + "\n");


                            }
                            Message msg = new Message();
                            msg.obj = sbResult.toString();
                            handler.sendMessage(msg);

                            br.close();
                            con.disconnect();

                        }catch(Exception e){
                            Log.e("예외", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });

        Button btnReadJSON = (Button)findViewById(R.id.btnReadJSON);
        btnReadJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            //데이터 다운로드
                            URL url = new URL("http://cyberadam.cafe24.com/movie/list");
                            //연결
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setConnectTimeout(30000);
                            con.setUseCaches(false);

                            //문자열을 읽기 위한 스트림 생성
                            BufferedReader br = null;
                            String headerType = con.getContentType();
                            if (headerType.toUpperCase().indexOf("EUC-KR") >=0 ) {
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "EUC-KR"));
                            }else{
                                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                            }
                            //문자열 읽기
                            StringBuilder sb = new StringBuilder();
                            while(true){
                                String line = br.readLine();
                                if(line == null){
                                    break;
                                }
                                sb.append(line + "\n");

                            }
                            //문자열을 json 객체로 변한
                            JSONObject json = new JSONObject(sb.toString());
                            //list 키의 데이터를 배열로 가져오기
                            JSONArray list = json.getJSONArray("list");
                            //결과를 저장할 문자열 생성
                            StringBuilder sbResult = new StringBuilder();
                                                        //배열 순회
                            for (int i =0; i < list.length(); i++){
                                //배열의 데이터를 객체로 가져오기
                                JSONObject movie = list.getJSONObject(i);
                                //title 속성의 내용 가져오기
                                String title = movie.getString("title");
                                String subtitle = movie.getString("subtitle");

                                sbResult.append(title + "-" + subtitle + "\n");
                            }

                            Message msg = new Message();
                            msg.obj = sbResult.toString();
                            handler.sendMessage(msg);

                            br.close();
                            con.disconnect();

                        }catch (Exception e){
                            Log.e("예외 json ", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });
    }
}