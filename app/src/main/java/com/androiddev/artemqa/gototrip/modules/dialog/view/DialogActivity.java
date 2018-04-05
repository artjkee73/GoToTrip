package com.androiddev.artemqa.gototrip.modules.dialog.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.androiddev.artemqa.gototrip.modules.dialog.presenter.DialogPresenter;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogActivity extends AppCompatActivity implements ContractDialog.View {
    CircleImageView mIvAvatarInterlocutor;
    TextView mTvNameInterlocutor;
    ImageButton mIbSendMessage;
    EditText mEtTextMessage;
    ContractDialog.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        mPresenter = new DialogPresenter();
        mPresenter.attachView(this);
        mIvAvatarInterlocutor = findViewById(R.id.iv_avatar_interlocutor_dialog_a);
        mTvNameInterlocutor = findViewById(R.id.tv_name_interlocutor_dialog_a);
        mIbSendMessage = findViewById(R.id.ib_send_message_dialog_a);
        mEtTextMessage = findViewById(R.id.et_text_message_dialog_a);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
