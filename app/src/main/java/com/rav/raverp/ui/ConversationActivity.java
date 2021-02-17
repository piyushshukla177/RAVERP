package com.rav.raverp.ui;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.ChatListAdapter;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.ChatModel;
import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.ActivityConversationBinding;
import com.rav.raverp.databinding.DialogEditEmailIdProfileBinding;
import com.rav.raverp.databinding.DialogViewTicketBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends BaseActivity implements ArrowBackPressed {
    LoginModel loginModel;
    String loginId;
    int roleId;
    ActivityConversationBinding conversationBinding;
    ApiHelper apiHelper;
    TextView tvTicketNo, tvSubject, tvDetails;
    RecyclerView rvChat;
    RelativeLayout rlSendMsg;
    EditText etSendMsg;
    Button btnSend;
    String ticketNo, isClosed;
    ChatListAdapter chatListAdapter;
    GridLayoutManager gridLayoutManager;
    ChatModel chatModel;
    private Dialog filterDialog;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationBinding = putContentView(R.layout.activity_conversation);
        //setContentView(R.layout.activity_conversation);
        if (getIntent() != null) {
            ticketNo = getIntent().getStringExtra("ticketNo");
            isClosed = getIntent().getStringExtra("status");
        }
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        setToolbarTitle("Conversation");
        showBackArrow();
        setArrowBackPressed(this);
        loginModel = MyApplication.getLoginModel();
        loginId = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
        findViewById();
    }

    void findViewById() {
        tvTicketNo = findViewById(R.id.tvTicketNo);
        tvSubject = findViewById(R.id.tvSubject);
        rvChat = findViewById(R.id.rvChat);
        rlSendMsg = findViewById(R.id.rlSendMsg);
        etSendMsg = findViewById(R.id.etSendMsg);
        btnSend = findViewById(R.id.btnSend);
        tvDetails = findViewById(R.id.tvDetails);

        getChatData();

        if (isClosed.equalsIgnoreCase("true")) {
            rlSendMsg.setVisibility(View.GONE);
        } else {

            final Handler someHandler = new Handler(getMainLooper());
            someHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getChatData();
                    someHandler.postDelayed(this, 5000);
                }
            }, 5000);
          /*  handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    getChatData();

                    handler.postDelayed(runnable, 5000);
                }

            };*/

            rlSendMsg.setVisibility(View.VISIBLE);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSendMsg.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter msg..");
                } else {
                    if (NetworkUtils.isNetworkConnected()) {
                        sendMsg();
                    } else {
                        ViewUtils.showOfflineDialog(ConversationActivity.this, new DialogActionCallback() {
                            @Override
                            public void okAction() {
                            }
                        });
                    }
                }

            }
        });


    /*    handler = new Handler();
        runnable = new Runnable() {
            public void run() {


                handler.postDelayed(runnable, 60000);
            }
        };*/

        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialogEmail();
            }
        });
    }

    void getChatData() {
        //ViewUtils.startProgressDialog(ConversationActivity.this);
        Call<ChatModel> chatModelCall = apiHelper.getChatList(loginId, roleId, ticketNo, isClosed);
        chatModelCall.enqueue(new Callback<ChatModel>() {
            @Override
            public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                ViewUtils.endProgressDialog();
                chatModel = response.body();
                tvTicketNo.setText(chatModel.getObj().getPkTicketno());
                tvSubject.setText(chatModel.getObj().getSubject());
                chatListAdapter = new ChatListAdapter(ConversationActivity.this, chatModel);
                gridLayoutManager = new GridLayoutManager(ConversationActivity.this, 1);
                rvChat.setLayoutManager(gridLayoutManager);
                rvChat.setAdapter(chatListAdapter);
                rvChat.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<ChatModel> call, Throwable t) {

            }
        });

    }


    void sendMsg() {
        ViewUtils.startProgressDialog(ConversationActivity.this);
        Call<CommonModel> commonModelCall = apiHelper.sendMsg(loginId, roleId, ticketNo, etSendMsg.getText().toString().trim());
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                CommonModel commonModel = response.body();

                if (commonModel.getResponse().equalsIgnoreCase("Success")) {
                    getChatData();
                    ViewUtils.endProgressDialog();
                    etSendMsg.setText("");

                } else {
                    ViewUtils.showToast("Some thing went wrong");
                }

            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });
    }


    private void showFilterDialogEmail() {
        filterDialog = new Dialog(this);
        final DialogViewTicketBinding binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialog_view_ticket, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);


        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 70);
        Window window = filterDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(inset);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        TextView tvSubject = filterDialog.findViewById(R.id.tvSubject);
        TextView tvQuery = filterDialog.findViewById(R.id.tvQuery);
        TextView tvIFSC = filterDialog.findViewById(R.id.tvIFSC);
        TextView tvBranchName = filterDialog.findViewById(R.id.tvBranchName);
        TextView tvBankName = filterDialog.findViewById(R.id.tvBankName);
        TextView tvPaymentType = filterDialog.findViewById(R.id.tvPaymentType);
        TextView tvClaimType = filterDialog.findViewById(R.id.tvClaimType);
        TextView tvTransactionDate = filterDialog.findViewById(R.id.tvTransactionDate);
        tvSubject.setText(chatModel.getObj().getSubject());
        tvQuery.setText(chatModel.getObj().getQuery());
        tvIFSC.setText(chatModel.getObj().getIfsc());
        tvBranchName.setText(chatModel.getObj().getStrbranchname());
        tvBankName.setText(chatModel.getObj().getStrbankname());
        tvPaymentType.setText(chatModel.getObj().getStrpaymenttype());
        tvClaimType.setText(chatModel.getObj().getClaimtypename());
        tvTransactionDate.setText(chatModel.getObj().getStrTransactionNo());

        ImageView ivClose = filterDialog.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });


        filterDialog.show();


    }


    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }
}