package com.rav.raverp.ui;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.ChatAttachmentAdapter;
import com.rav.raverp.data.adapter.ChatListAdapter;
import com.rav.raverp.data.adapter.SendAttachmentAdapter;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.ChatModel;
import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.SendAttachmentModel;
import com.rav.raverp.databinding.ActivityConversationBinding;

import com.rav.raverp.databinding.DialofFeedbackBinding;
import com.rav.raverp.databinding.DialogEditMobileNoProfileBinding;
import com.rav.raverp.databinding.DialogViewTicketBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConversationActivity extends BaseActivity implements ArrowBackPressed {

    LoginModel loginModel;
    String loginId;
    int roleId;
    ActivityConversationBinding conversationBinding;
    ApiHelper apiHelper;
    LinearLayout llSendMsg, llClosed, llAttachment;
    EditText etSendMsg;
    Button btnSubmit, btnChooseFile, btnReply, btnClosed;
    String ticketNo, status;
    ChatListAdapter chatListAdapter;
    GridLayoutManager gridLayoutManager;
    ChatModel chatModel;
    private Dialog filterDialog;
    public Handler handler = null;
    public static Runnable runnable = null;
    List<Uri> uriPath;
    List<File> file;
    ArrayList<String> filePaths = new ArrayList<String>();

    RecyclerView rvChat, rvSendAttachment, rvAttachment;
    ArrayList<SendAttachmentModel> spacecrafts = new ArrayList<>();


    TextView tvTicketNo, tvSubject, tvStatus, tvSupportFor,
            tvSupportType, tvSubmitted, tvLastUpdated, tvPriority;
    boolean s = false;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationBinding = putContentView(R.layout.activity_conversation);
        //setContentView(R.layout.activity_conversation);
        if (getIntent() != null) {
            ticketNo = getIntent().getStringExtra("ticketNo");
            status = getIntent().getStringExtra("status");
        }

        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        setToolbarTitle("View Ticket");
        showBackArrow();
        setArrowBackPressed(this);
        loginModel = MyApplication.getLoginModel();
        loginId = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
        checkPermissions();
        findViewById();

        if (status.equalsIgnoreCase("closed")) {
            s = true;
            llClosed.setVisibility(View.VISIBLE);
            btnClosed.setEnabled(false);
            btnClosed.setClickable(false);
            btnClosed.setText("Closed");
        } else {
            s = false;
            llClosed.setVisibility(View.GONE);
            btnClosed.setEnabled(true);
            btnClosed.setClickable(true);
            btnClosed.setText("Close");
        }
    }

    void findViewById() {
        rvChat = findViewById(R.id.rvChat);
        llSendMsg = findViewById(R.id.llSendMsg);
        etSendMsg = findViewById(R.id.etSendMsg);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        rvSendAttachment = findViewById(R.id.rvSendAttachment);
        tvTicketNo = findViewById(R.id.tvTicketNo);
        tvSubject = findViewById(R.id.tvSubject);
        tvStatus = findViewById(R.id.tvStatus);
        tvSupportFor = findViewById(R.id.tvSupportFor);
        tvSupportType = findViewById(R.id.tvSupportType);
        tvSubmitted = findViewById(R.id.tvSubmitted);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);
        tvPriority = findViewById(R.id.tvPriority);
        btnReply = findViewById(R.id.btnReply);
        btnClosed = findViewById(R.id.btnClosed);
        llClosed = findViewById(R.id.llClosed);
        llAttachment = findViewById(R.id.llAttachment);
        rvAttachment = findViewById(R.id.rvAttachment);

        if (NetworkUtils.isNetworkConnected()) {
            getChatData();
        } else {
            ViewUtils.showOfflineDialog(ConversationActivity.this, new DialogActionCallback() {
                @Override
                public void okAction() {

                }
            });
        }

     /*   if (isClosed.equalsIgnoreCase("true")) {
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

            rlSendMsg.setVisibility(View.VISIBLE);
        }*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
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


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePaths.clear();
                if (checkPermissions()) {
                    TedBottomPicker.with(ConversationActivity.this)
                            .setPeekHeight(1600)
                            .showTitle(true)
                            .setSelectMaxCount(10)
                            .setCompleteButtonText("Done")
                            .setEmptySelectionText("No Select")
                            .setSelectedUriList(uriPath)
                            .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                                @Override
                                public void onImagesSelected(List<Uri> uriList) {
                                    uriPath = uriList;
                                    showUriList(uriList);
                                }
                            });
                }
            }
        });

        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected()) {
                    closedTicket();
                } else {
                    ViewUtils.showOfflineDialog(ConversationActivity.this, new DialogActionCallback() {
                        @Override
                        public void okAction() {

                        }
                    });
                }
            }
        });


        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSendMsg.setVisibility(View.VISIBLE);
            }
        });


    }

    void getChatData() {
        Call<ChatModel> chatModelCall = apiHelper.getChatList(loginId, roleId, ticketNo, s);
        chatModelCall.enqueue(new Callback<ChatModel>() {
            @Override
            public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                ViewUtils.endProgressDialog();

                chatModel = response.body();

                tvTicketNo.setText("#" + chatModel.getObj().getPkTicketno());
                tvSubject.setText("- " + chatModel.getObj().getSubject());
                tvStatus.setText(chatModel.getObj().getStatus());
                tvSupportFor.setText(chatModel.getObj().getSupportfor());
                tvSupportType.setText(chatModel.getObj().getDocumenttype());
                tvSubmitted.setText(chatModel.getObj().getSubmitted());
                tvLastUpdated.setText(chatModel.getObj().getLastupdated());
                tvPriority.setText(chatModel.getObj().getPriority());

             /*   status = chatModel.getObj().getStatus();
                if (status.equalsIgnoreCase("closed")) {
                    s = true;
                    llClosed.setVisibility(View.VISIBLE);
                } else {
                    s = false;
                    llClosed.setVisibility(View.GONE);
                }*/

                if (chatModel.getObj().getStrattachment().equalsIgnoreCase("")) {
                    llAttachment.setVisibility(View.GONE);
                    rvAttachment.setVisibility(View.GONE);
                } else {

                    llAttachment.setVisibility(View.VISIBLE);
                    rvAttachment.setVisibility(View.VISIBLE);
                    String attachment = chatModel.getObj().getStrattachment();
                    List<String> as = Arrays.asList(attachment.split(":"));

                    for (int i = 0; i < as.size(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("attachment", as.get(i));
                        hashMapArrayList.add(hashMap);

                        Log.v("ListAttach", as.get(i));
                    }
                    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(ConversationActivity.this, 5);
                    ChatAttachmentAdapter chatAttachmentAdapter = new ChatAttachmentAdapter(ConversationActivity.this, hashMapArrayList, "create");
                    rvAttachment.setLayoutManager(gridLayoutManager1);
                    rvAttachment.setAdapter(chatAttachmentAdapter);
                }

                if (chatModel.getObj().getStatus().equalsIgnoreCase("Opened")) {
                    btnClosed.setEnabled(true);
                    btnClosed.setClickable(true);
                    btnClosed.setText("Close");
                    llClosed.setVisibility(View.GONE);
                    tvStatus.setBackgroundColor(ConversationActivity.this.getResources().getColor(R.color.lightYellow));
                    tvStatus.setTextColor(ConversationActivity.this.getResources().getColor(R.color.black));
                } else if (chatModel.getObj().getStatus().equalsIgnoreCase("Answered")) {
                    btnClosed.setEnabled(true);
                    btnClosed.setClickable(true);
                    btnClosed.setText("Close");
                    llClosed.setVisibility(View.GONE);
                    tvStatus.setBackgroundColor(ConversationActivity.this.getResources().getColor(R.color.black));
                    tvStatus.setTextColor(ConversationActivity.this.getResources().getColor(R.color.white));
                } else if (chatModel.getObj().getStatus().equalsIgnoreCase("Closed")) {
                    btnClosed.setEnabled(false);
                    btnClosed.setClickable(false);
                    btnClosed.setText("Closed");
                    llClosed.setVisibility(View.VISIBLE);
                    tvStatus.setBackgroundColor(ConversationActivity.this.getResources().getColor(R.color.lightRed));
                    tvStatus.setTextColor(ConversationActivity.this.getResources().getColor(R.color.white));
                }

                chatListAdapter = new ChatListAdapter(ConversationActivity.this, chatModel);
                gridLayoutManager = new GridLayoutManager(ConversationActivity.this, 1);
                rvChat.setLayoutManager(gridLayoutManager);
                rvChat.setAdapter(chatListAdapter);
                rvChat.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<ChatModel> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });


    }

    MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());

        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        //RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), surveyBody);
    }

    void sendMsg() {
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (uriPath != null) {
            for (int i = 0; i < uriPath.size(); i++) {
                parts.add(prepareFilePart(i + "", uriPath.get(i)));

            }
        }
        Log.v("listAttach", parts.toString());


        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("filename", RequestBody.create(MediaType.parse("text/plain"), ""));

        ViewUtils.startProgressDialog(ConversationActivity.this);
        Call<CommonModel> commonModelCall = apiHelper.sendMsg(loginId, roleId, ticketNo, etSendMsg.getText().toString().trim(), parts, map);
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel.getResponse().equalsIgnoreCase("Success")) {
                    llSendMsg.setVisibility(View.GONE);
                    if (uriPath != null)
                        uriPath.clear();
                    rvSendAttachment.removeAllViewsInLayout();
                    spacecrafts.clear();
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

    private void showUriList(List<Uri> uriList) {
        SendAttachmentModel s;

        for (Uri path : uriList) {
            s = new SendAttachmentModel();
            s.setName(path.getPath().substring(path.getPath().lastIndexOf("/") + 1));

            s.setUri(path);
            spacecrafts.add(s);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ConversationActivity.this, 1);
        rvSendAttachment.setLayoutManager(gridLayoutManager);
        rvSendAttachment.setAdapter(new SendAttachmentAdapter(this, spacecrafts));
    }

    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }

    String[] appPermissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public int PERMISSION_CODE = 100;

    public boolean checkPermissions() {

        List<String> lsitPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {

            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                lsitPermissionsNeeded.add(perm);
            }
        }

        //check for non granted permissions
        if (!lsitPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, lsitPermissionsNeeded.toArray(new String[lsitPermissionsNeeded.size()]), PERMISSION_CODE);
            return false;
        }
        //app has all permissions proceed ahead
        return true;
    }

    void closedTicket() {
        ViewUtils.startProgressDialog(ConversationActivity.this);
        Call<CommonModel> commonModelCall = apiHelper.closedTickets(loginId, roleId, ticketNo, "");
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                ViewUtils.endProgressDialog();
                CommonModel commonModel = response.body();
                if (commonModel.getResponse().equalsIgnoreCase("Success")) {
                    llClosed.setVisibility(View.VISIBLE);
                    llSendMsg.setVisibility(View.GONE);
                    getChatData();
                    showFilterDialogMobile();
                }


                // onBackPressed();
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }


    private void showFilterDialogMobile() {
        filterDialog = new Dialog(this);
        final DialofFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialof_feedback, null, false);
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
        filterDialog.show();


        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        binding.imgcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback(binding.note.getText().toString());
            }
        });


    }

    void feedback(String feedback) {
        ViewUtils.startProgressDialog(ConversationActivity.this);
        Call<CommonModel> commonModelCall = apiHelper.closedTickets(loginId, roleId, ticketNo, feedback);
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                ViewUtils.endProgressDialog();
                CommonModel commonModel = response.body();

                onBackPressed();
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }

}