package kakao.iitstudy.adapterview;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_lite);

        //SimpleAdapter를 사용하는 listview 출력
        ListView simpleListView = (ListView)findViewById(R.id.simpleListView);
        ListView cursorListView = (ListView)findViewById(R.id.cursorListView);

        //simpleadapter는 map의 list로 데이터를 생성해야 한다.
        List<Map<String, String>> simpleData = new ArrayList<>();
        //데이터베이스 자겨오기
        JobDBHelper helper = new JobDBHelper(SQLiteActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //데이터 읽는 sql 실행
        Cursor cursor = db.rawQuery("select * from job_data", null);

        while (cursor.moveToNext()){
            HashMap<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(1));
            map.put("content", cursor.getString(2));
            simpleData.add(map);
        }

        //simpleadapter 생성
        //네번째 매개변수가 map에서 출력할 키의 배열
        SimpleAdapter simpleAdapter = new SimpleAdapter(SQLiteActivity.this, simpleData,
                android.R.layout.simple_list_item_2, new String[]{"name", "content"},
                new int[]{android.R.id.text1, android.R.id.text2});

        simpleListView.setAdapter(simpleAdapter);

        CursorAdapter cursorAdapter = new SimpleCursorAdapter(SQLiteActivity.this,
                android.R.layout.simple_list_item_2, cursor ,
                new String[]{"name", "content"},
                new int[]{android.R.id.text1, android.R.id.text2});

        cursorListView.setAdapter(cursorAdapter);
    }
}