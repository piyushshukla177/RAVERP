package com.rav.raverp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ImageCompressTaskListener;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.ApiUploadImageResponse;
import com.rav.raverp.data.model.api.EditEmailModel;
import com.rav.raverp.data.model.api.EditMobileModel;
import com.rav.raverp.data.model.api.GetProfileModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.thread.ImageCompressTask;
import com.rav.raverp.databinding.ActivityEditProfileBinding;
import com.rav.raverp.databinding.DialogEditEmailIdProfileBinding;
import com.rav.raverp.databinding.DialogEditMobileNoProfileBinding;
import com.rav.raverp.network.ApiClient;

import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.FileUtil;
import com.rav.raverp.utils.Logger;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ScreenUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class EditProfileActivity extends BaseActivity implements ArrowBackPressed,
        StoragePermissionListener, View.OnClickListener {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private Context mContext = EditProfileActivity.this;
    private ActivityEditProfileBinding binding;
    private ApiHelper apiHelper;
    private boolean isDialogHided;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;
    private boolean isGetProfile, isUpdateProfile;
    private Dialog filterDialog;
    private String profilePicPath;
    private LoginModel login;
    private GetProfileModel getProfile;
    String EditMobileChange,EditEmailChange;
    String msg = "";
    private Uri cameraUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_edit_profile);
        binding.setEditProfileActivity(this);
        setToolbarTitle("Profile");
        showBackArrow();
        setArrowBackPressed(this);

        setStoragePermissionListener(this);
        ScreenUtils.setupUI(binding.parentLayout, EditProfileActivity.this);
        checkStoragePermission();

        login = MyApplication.getLoginModel();


        login = MyApplication.getLoginModel();

        binding.setLogin(login);
        binding.setGetProfile(getProfile);




        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        binding.editemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialogEmail();
            }
        });


        binding.editmobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialogMobile();

            }
        });

        GetProfileApi();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isFromPermissionSettings){
            checkStoragePermission();
        }

    }



    private void GetProfileApi() {

        String loginid=login.getStrLoginID();
        Integer role=login.getIntRoleID();
        Call<ApiResponse<List<GetProfileModel>>> GetProfileCall = apiHelper.getProfile(loginid,role);
        GetProfileCall.enqueue(new Callback<ApiResponse<List<GetProfileModel>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<GetProfileModel>>> call,
                                   @NonNull Response<ApiResponse<List<GetProfileModel>>> response) {

                if (response.isSuccessful()) {

                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            List<GetProfileModel> login = response.body().getBody();
                            getProfile=login.get(0);
                            String email = login.get(0).getStrEmail().toString();
                            binding.setemail.setText(email.toString());
                            String mobile = login.get(0).getStrPhone();
                            binding.setmobile.setText(mobile.toString());
                            String name = login.get(0).getStrDisplayName();
                            String profile = login.get(0).getStrProfilePic();
                            Glide.with(binding.profileImageView.getContext()).load("https://ravgroup.org" + profile)
                                    .placeholder(R.drawable.account)
                                    .into(binding.profileImageView);

                        } else {


                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<GetProfileModel>>> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {

                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }




    private void showFilterDialogEmail() {
        filterDialog = new Dialog(this);
        final DialogEditEmailIdProfileBinding binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialog_edit_email_id_profile, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);
        binding.setGetProfile(getProfile);


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
        binding.btncancel.setOnClickListener(new View.OnClickListener() {
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
                EditEmailChange=  binding.editemailnochange.getText().toString();

                submitFormEmailId();
            }
        });




    }

    public void onClickEditProfilePic(View view) {
        if (isPermissionGranted){
            selectImageOption();
        }else {
            checkStoragePermission();
        }
    }
    private void updateProfile() {
        isUpdateProfile = false;

        File reportFile = new File(profilePicPath);
        RequestBody requestFile = RequestBody.create(reportFile,
                MediaType.parse(reportFile.getAbsolutePath()));
        MultipartBody.Part profilePic = MultipartBody.Part.createFormData("",
                reportFile.getName(), requestFile);
        try {
            Logger.i(TAG, profilePic.body().contentLength() + " -----profilePicFile");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Call<ApiUploadImageResponse> updateProfileCall = apiHelper.getChangeProfile(getProfile.getStrDisplayName(),
                String.valueOf(MyApplication.getLoginId()),profilePic);
        updateProfileCall.enqueue(new Callback<ApiUploadImageResponse>() {
            @Override
            public void onResponse(Call<ApiUploadImageResponse> call, final Response<ApiUploadImageResponse> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response!=null){
                        if (response.body().getResponse().equalsIgnoreCase("Success")){
                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiUploadImageResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    showMessage(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }



    public void selectImageOption() {
        final String[] mimeTypes = {"image/*", "application/pdf"};
        final CharSequence[] items = {getString(R.string.action_take_from_camera),
                getString(R.string.action_choose_from_gallery), getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.title_add_document));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.action_take_from_camera))) {
                    dialog.dismiss();
                    cameraUri = FileUtil.getInstance(mContext).createImageUri();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                    startActivityForResult(intent, AppConstants.FEASIBILITY_REPORT_CAMERA);
                } else if (items[item].equals(getString(R.string.action_choose_from_gallery))) {
                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        StringBuilder mimeTypesStr = new StringBuilder();
                        for (String mimeType : mimeTypes) {
                            mimeTypesStr.append(mimeType).append("|");
                        }
                        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                        startActivityForResult(Intent.createChooser(intent,
                                getString(R.string.hint_select_picture)),
                                AppConstants.FEASIBILITY_REPORT_BELOW_KITKAT_GALLERY);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                        startActivityForResult(intent, AppConstants.FEASIBILITY_REPORT_ABOVE_KITKAT_GALLERY);
                    }
                } else if (items[item].equals(getString(R.string.action_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void GetEmailChange() {
        String id=login.getStrLoginID();
        Integer role=login.getIntRoleID();
        showProgress(true);
        Call<EditEmailModel>getEditEmailCall = apiHelper.getEditEmail(id,EditEmailChange,role);
        getEditEmailCall.enqueue(new Callback<EditEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<EditEmailModel> call,
                                   @NonNull Response<EditEmailModel> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {
                                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }else{

                    }

                }

            }




            @Override
            public void onFailure(@NonNull Call<EditEmailModel> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });


    }

    private void showFilterDialogMobile() {
        filterDialog = new Dialog(this);
        final DialogEditMobileNoProfileBinding binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialog_edit_mobile_no_profile, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);
        binding.setGetProfile(getProfile);



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


        binding.btncancel.setOnClickListener(new View.OnClickListener() {
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
                EditMobileChange=  binding.editmobilenochange.getText().toString();
                submitFormMobile();

            }
        });


    }

    private void submitFormMobile() {
        if (!validateMobileNo()) {
            return;
        }

        filterDialog.dismiss();
        checkNetwork();

    }

    private boolean validateMobileNo() {
        if (!CommonUtils.isValidMobile(EditMobileChange)){
            Toast.makeText(mContext, ""+getString(R.string.Please_Enter_Valid_Mobile_No), Toast.LENGTH_SHORT).show();
            msg = getString(R.string.Please_Enter_Valid_Mobile_No);
            return false;
        }
        return true;
    }


    private void submitFormEmailId() {
        if (!validateEmailId()) {
            return;
        }

        filterDialog.dismiss();
        GetEmailChange();

    }

    private boolean validateEmailId() {
        if (!CommonUtils.isValidEmail(EditEmailChange)){
            Toast.makeText(mContext, ""+getString(R.string.Please_Enter_Valid_Email_ID), Toast.LENGTH_SHORT).show();
            msg = getString(R.string.Please_Enter_Valid_Email_ID);
            return false;
        }
        return true;
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            GetMobileChange();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }



    private void GetMobileChange() {

        String id=login.getStrLoginID();
        Integer role=login.getIntRoleID();
        showProgress(true);
        Call<EditMobileModel>getEditEmailCall = apiHelper.getEditMobile(id,EditMobileChange,role);
        getEditEmailCall.enqueue(new Callback<EditMobileModel>() {
            @Override
            public void onResponse(@NonNull Call<EditMobileModel> call,
                                   @NonNull Response<EditMobileModel> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {

                        ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {


                                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }else{

                    }

                }

            }




            @Override
            public void onFailure(@NonNull Call<EditMobileModel> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.BELOW_KITKAT_GALLERY && data != null) {
                Uri originalUri = data.getData();
                getImagePath(originalUri);
            } else if (requestCode == AppConstants.ABOVE_KITKAT_GALLERY && data != null) {
                Uri originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (originalUri != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                        getImagePath(originalUri);
                    }
                }
            } else if (requestCode == AppConstants.CAMERA) {
                if (cameraUri != null) {
                    getImagePath(cameraUri);
                }
            }
        }
    }

    public void getImagePath(Uri originalUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String path = FileUtil.getRealPathFromURI_API11to18(mContext, originalUri);
            Logger.d(TAG, "File path === "+ path);
            compressFile(path);

        } else {
            String path = FileUtil.getRealPathFromURI(mContext, originalUri);
            Logger.d(TAG, "File path === "+ path);
            compressFile(path);

        }
    }

    private void compressFile(String path) {
        if (path != null) {
            String ext = CommonUtils.getExt(path);
            Logger.d(TAG, "File path extension === "+ ext);
            if(ext != null) {
                ImageCompressTask imageCompressTask = new ImageCompressTask(mContext, path, imageCompressTaskListener);
                mExecutorService.execute(imageCompressTask);
                Logger.d(TAG, "File path === "+ path);


            }
        } else {
            ViewUtils.showErrorDialog(mContext, getString(R.string.error_choose_another_file),
                    new DialogActionCallback() {
                        @Override
                        public void okAction() {

                        }
                    });
        }
    }

    private ImageCompressTaskListener imageCompressTaskListener = new ImageCompressTaskListener() {
        @Override
        public void onComplete(List<File> compressed) {
            File file = compressed.get(0);
            Log.d("ImageCompressor2", "New photo size ==> " + file.length()); //log new file size.
            profilePicPath = file.getAbsolutePath();
            Logger.d(TAG, "File path === "+ profilePicPath);
            //showMessage("Profile pic added successfully");
            binding.profileImageView.setImageURI(Uri.fromFile(file));
            updateProfile();
            //binding.profileImageView.setImageBitmap(BitmapFactory.decodeFile(profilePicPath));
        }
        @Override
        public void onError(Throwable error) {
            Logger.wtf("ImageCompressor2 ", "Error occurred == "+ error);
        }
    };


    @Override
    public void onClick(View v) {

    }

    @Override
    public void isStoragePermissionGranted(boolean granted) {
        isPermissionGranted = granted;
    }

    @Override
    public void isUserPressedSetting(boolean pressed) {
        isFromPermissionSettings = pressed;
    }
}


