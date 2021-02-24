package com.example.musicplayer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.musicplayer.R;

public class FragmentTimerDialog extends DialogFragment implements View.OnClickListener {

    private SeekBar mSeekBar;
    private TextView txtDuration;
    private Button btnConfirmTimer;
    private Button btnCancelTimer;
    private View mView;
    private TimerListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (TimerListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_timer, container, false);

        initView();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setUpSeekBarTimer();
        btnConfirmTimer.setOnClickListener(this);
        btnCancelTimer.setOnClickListener(this);

        return mView;
    }

    private void initView() {
        txtDuration = mView.findViewById(R.id.text_timer_duration);
        mSeekBar = mView.findViewById(R.id.seekBarTimer);
        btnConfirmTimer = mView.findViewById(R.id.btn_confirm_timer);
        btnCancelTimer = mView.findViewById(R.id.btn_cancel_timer);
    }

    private void setUpSeekBarTimer() {
        mSeekBar.setMax(12);
        mSeekBar.setProgress(6);
        String currentProgress = mSeekBar.getProgress() * 5 + " min";
        txtDuration.setText(currentProgress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtDuration.setText(progress * 5 + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_timer:
                int countDown = mSeekBar.getProgress() * 5;
                mListener.setOnTimerConfirmed(countDown);
            case R.id.btn_cancel_timer:
                dismiss();
                break;
        }
    }

    public interface TimerListener {
        void setOnTimerConfirmed(int countDown);
    }
}
