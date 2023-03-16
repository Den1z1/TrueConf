package com.example.trueconf;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class MainFragment extends Fragment {
    private View rootView;

    private CountDownTimer animStartTimer;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isRusLoc = Locale.getDefault().getISO3Language().equals("rus");

        TextView textHello = rootView.findViewById(R.id.text_hello);
        textHello.setText(isRusLoc ? R.string.hello_rus : R.string.hello_en);
        textHello.setOnClickListener(view1 -> animTextCancel(textHello));

        LinearLayout touchLayout = rootView.findViewById(R.id.touch_layout);
        touchLayout.setOnTouchListener((view1, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animTextCancel(textHello);

                textHello.setX(event.getRawX() - (float) textHello.getWidth() / 2);
                textHello.setY((event.getRawY() - textHello.getHeight()) - (float) textHello.getHeight() / 2);

                textHello.setTextColor(isRusLoc ? ContextCompat.getColor(rootView.getContext(), R.color.blue) : ContextCompat.getColor(rootView.getContext(), R.color.red));

                animStartTimer = new CountDownTimer(5000, 5000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        animTextMove(textHello, touchLayout, true);
                    }
                }.start();
            }
            return true;
        });
    }

    private void animTextMove(TextView textHello, LinearLayout touchLayout, boolean isDown) {
        textHello.animate()
                .y(isDown ? touchLayout.getHeight() - textHello.getHeight() : 0)
                .setDuration(2000)
                .withEndAction(() -> animTextMove(textHello, touchLayout, !isDown))
                .start();
    }

    private void animTextCancel(TextView textHello){
        if (animStartTimer != null) {
            animStartTimer.cancel();
            textHello.animate().cancel();
        }
    }
}