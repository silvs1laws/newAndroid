package kakao.iitstudy.supportactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

class Person{
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}

public class RecyclerBasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_basic);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        //layoutmanager 설정 - 세로 방향으로 출력
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(RecyclerBasicActivity.this,
                        LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //adapter 클래스 객체 생성
        PersonAdapter adapter = new PersonAdapter();

        //데이터 추가
        adapter.addItem(new Person("김좌진", "010-0000-0000"));
        adapter.addItem(new Person("김원봉", "010-0000-0001"));

        //연결
        recyclerView.setAdapter(adapter);
    }
}