package kakao.iitstudy.supportactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //프로퍼티 생성해서 대입
        drawerLayout = findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open,
                R.string.drawer_close);

        //삼선 아이콘 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        //항목 뷰 눌렀을 때 수행할 코드 작성
        NavigationView navigationView = findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.menu_drawer_home){
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    "홈 선택", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                        if (item.getItemId() == R.id.menu_drawer_message){
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    "메세지 선택", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                        if (item.getItemId() == R.id.menu_drawer_add){
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    "추가", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                }
        );
    }

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    //메뉴를 눌렀을 때 호출되는 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}