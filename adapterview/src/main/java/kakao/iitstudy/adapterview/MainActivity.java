package kakao.iitstudy.adapterview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    //데이터 모임과 어뎁터의 제네릭은 동일시켜 줄 것.
    ArrayList<String> list;
    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        list.add("C");
        list.add("C++");
        list.add("Java");
        list.add("Python");
        list.add("JavaScript");
        list.add("C#");
        list.add("Ruby");
        list.add("Scala");
        list.add("Kotlin");
        list.add("Swift");
        list.add("R");
        list.add("Dart");
        list.add("Go");

        //어댑터 생성 - arraylist를 배열로 만들어도 됩니다.
        //adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);


        //리소스를 이용해서 adapter를 생성
        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.oop,
                android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
    }
}