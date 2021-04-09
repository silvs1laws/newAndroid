package kakao.iitstudy.supportactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TwoFragment extends DialogFragment {
    //대화상자를 초기화하는 메소드 재정의

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Dialog Fragment")
                .setMessage("대화상자 프래그먼트")
                .setPositiveButton("ok", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
