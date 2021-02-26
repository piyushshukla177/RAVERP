package com.rav.raverp.ui;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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


import com.rav.raverp.databinding.DialogFeedbackBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    SwipeRefreshLayout swipeRefreshLayout;
    ChatModel chatModel;
    private Dialog filterDialog;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static List<Uri> uriPath;
    List<File> file;
    ArrayList<String> filePaths = new ArrayList<String>();

    RecyclerView rvChat, rvSendAttachment, rvAttachment;
    ArrayList<SendAttachmentModel> spacecrafts = new ArrayList<>();


    TextView tvTicketNo, tvSubject, tvStatus, tvSupportFor,
            tvSupportType, tvSubmitted, tvLastUpdated, tvPriority, tvAttachment;
    boolean s = false;


    List<String> as;
    String attachment;
    ChatAttachmentAdapter chatAttachmentAdapter;
    GridLayoutManager gridLayoutManager1;

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
        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);
        tvAttachment = findViewById(R.id.tvAttachment);

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
                etSendMsg.requestFocus();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getChatData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

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
                    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                    llAttachment.setVisibility(View.VISIBLE);
                    rvAttachment.setVisibility(View.VISIBLE);
                    attachment = chatModel.getObj().getStrattachment();
                    as = Arrays.asList(attachment.split(":"));
                    tvAttachment.setText("Attachment(" + as.size() + ")");
                    for (int i = 0; i < as.size(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("attachment", as.get(i));
                        hashMapArrayList.add(hashMap);
                        Log.v("ListAttach1", as.get(i));
                    }
                    gridLayoutManager1 = new GridLayoutManager(ConversationActivity.this, 1);
                    chatAttachmentAdapter = new ChatAttachmentAdapter(ConversationActivity.this, hashMapArrayList, "create");
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
                    tvStatus.setBackgroundColor(ConversationActivity.this.getResources().getColor(R.color.green));
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

        if (spacecrafts != null) {
            for (int i = 0; i < spacecrafts.size(); i++) {
                parts.add(prepareFilePart(i + "", spacecrafts.get(i).getUri()));
            }
        }
        //  Log.v("listAttach", parts.toString());


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

            s.setUri(Uri.parse(compressImage(path.getPath())));
            spacecrafts.add(s);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ConversationActivity.this, 1);
        rvSendAttachment.setLayoutManager(gridLayoutManager);
        rvSendAttachment.setAdapter(new SendAttachmentAdapter(this, spacecrafts, "chat"));
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
        final DialogFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialog_feedback, null, false);
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


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Rav");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}