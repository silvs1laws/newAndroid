package kakao.iitstudy.supportactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {
    //화면을 만들어주는 메소드
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //플래그먼트 파일을 자신의 화면으로 사용
        return inflater.inflate(R.layout.fragment1, container, false) ;
    }
}
