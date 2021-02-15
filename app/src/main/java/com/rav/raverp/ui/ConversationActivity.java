package com.rav.raverp.ui;


import android.os.Bundle;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.ActivityConversationBinding;

public class ConversationActivity extends BaseActivity implements ArrowBackPressed {
    LoginModel loginModel;
    String loginId;
    int roleId;


    ActivityConversationBinding conversationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationBinding = putContentView(R.layout.activity_conversation);
        //setContentView(R.layout.activity_conversation);
        setToolbarTitle("Conversation");
        showBackArrow();
        setArrowBackPressed(this);
        loginModel = MyApplication.getLoginModel();
        loginId = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
    }

    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }
}