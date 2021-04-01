package kakao.iitstudy.localdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ItemActivity extends AppCompatActivity {

    EditText itemid, itemname, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemid = (EditText)findViewById(R.id.itemid);
        itemname = (EditText)findViewById(R.id.itemname);
        quantity = (EditText)findViewById(R.id.quantity);

        Button btnInsert = (Button)findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터베이스 연동 클래스 객체를 생성
                MyDBHandler handler = new MyDBHandler(ItemActivity.this);
                //삽입할 데이터 만들기
                Item item = new Item();
                item.set_itemname(itemname.getText().toString());
                item.set_quantity(Integer.parseInt(quantity.getText().toString()));

                //데이터 삽입
                handler.addItem(item);

                //입력도구들을 초기화
                itemname.setText("");
                quantity.setText("");

                //키보드 숨기기
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(itemname.getWindowToken(), 0  );
                imm.hideSoftInputFromWindow(itemid.getWindowToken(), 0  );
                imm.hideSoftInputFromWindow(quantity.getWindowToken(), 0  );


                //메세지 출력
                Snackbar.make(v, "삽입 성공", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        //조회버튼 처리
        Button btnSelect = (Button)findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = itemname.getText().toString();
                //조회 전 유효성 검사
                if(query == null && query.trim().length() == 0){
                    return;
                }
                //db연동 클래스 객체
                MyDBHandler handler = new MyDBHandler(ItemActivity.this);
                Item item = handler.findItem(query);
                if (item == null){
                    itemid.setText("일치하는 데이터가 없습니다.");
                }else{
                    itemid.setText(item.get_id()+"");
                    quantity.setText(item.get_quantity()+"");
                }
            }
        });

        Button btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = itemname.getText().toString();

                if(query == null && query.trim().length() == 0){
                    itemname.setText("삭제할 이름을 입력하세요");
                }else {
                    MyDBHandler handler = new MyDBHandler(ItemActivity.this);
                    handler.deleteItem(query);
                    itemid.setText("삭제가 완료되었습니다.");
                }
            }
        });
    }
}