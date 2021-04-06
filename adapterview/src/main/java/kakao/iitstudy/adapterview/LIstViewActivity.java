package kakao.iitstudy.adapterview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LIstViewActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_ist_view);

        listview = (ListView)findViewById(R.id.listview);

        list = new ArrayList<>();
        list.add("IoC");
        list.add("DI");
        list.add("AOP");

        //android.R 안드로이드 제공 리소스
        //R로 시작하면 사용자가 삽입한 리소스
        /*adapter = new ArrayAdapter<>(LIstViewActivity.this, android.R.layout.simple_list_item_1,
                list);*/

        //라디오 버튼을 옆에 배치하는 모양으로 변경
       /* adapter = new ArrayAdapter<>(LIstViewActivity.this, android.R.layout.simple_list_item_single_choice,
                list);*/
        //체크 박스 버튼을 배치하는 모양
        adapter = new ArrayAdapter<>(LIstViewActivity.this, android.R.layout.simple_list_item_multiple_choice,
                list);

        //listview에 adapter출력
        listview.setAdapter(adapter);

        //구분선 설정
        listview.setDivider(new ColorDrawable(Color.GREEN));
        listview.setDividerHeight(5);
        //listview 의 선택모드 설정
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //listview에서 항목을 클릭했을 때
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //첫번째 매개변수는 listview
            //두번째 매개변수는 클릭한 항목 뷰
            //세번째 매개변수는 누른 항목의 인데스
            //네번재 매개변수는 항목 뷰의 아이디
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        item, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        Button btnInset = (Button)findViewById(R.id.btnInsert);
        btnInset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력한 내용 가져오기
                EditText newitem = (EditText)findViewById(R.id.newitem);
                String item = newitem.getText().toString().trim();

                //전송전 유효성 검사를 수행 - null체크, 중복 검사 등
                if (item.length() <= 0){
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "아이템은 필수 입력입니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                //list의 데이터를 순회하면서 모두 대문자로 변경해서 비교하며 중복 검사
                for(String spring:list){
                    if(spring.toUpperCase().equals(item.toLowerCase())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(),
                                "이미 존재하는 아이템 입니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                        return;
                    }
                }

                //list에 삽입
                list.add(item);
                //listview 업데이트
                adapter.notifyDataSetChanged();
                newitem.setText("");
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "아이템이 정상적으로 추가되었습니다.", BaseTransientBottomBar.LENGTH_SHORT).show();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(newitem.getWindowToken(), 0);
            }
        });

        Button btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //작업전 유효성 검사
                //선택된 항목의 인덱스를 들고오기
                /*int pos = listview.getCheckedItemPosition();
                //인덱스는 데이터 범위 내
                if (pos < 0 || pos >= list.size()){
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "아이템을 선택하세요", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }*/

                //여러개 선택해서 삭제
                //선택 항목 관련 정보 가져옮
                SparseBooleanArray sb = listview.getCheckedItemPositions();
                //유효성 검사
                if (sb.size() <= 0){
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "아이템을 선택하세요", BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }
                //배열 순회하면서 true이면 삭제
                for (int i = listview.getCount()-1; i>=0; i--){
                    if (sb.get(i) == true){
                        list.remove(i);
                    }
                }

                //작업 수행
                //데이터 삭제하고 다시 출력하고 선택 된 항목 해재
                //list.remove(pos);
                adapter.notifyDataSetChanged();
                
                listview.clearChoices();
                //수행 결과 출력
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "아이템이 삭제되었습니다.", BaseTransientBottomBar.LENGTH_SHORT).show();
                return;
            }
        });
    }
}