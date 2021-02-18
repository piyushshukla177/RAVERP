package com.rav.raverp.ui.fragment.Associate;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.rav.raverp.BuildConfig;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ImageCompressTaskListener;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.ClaimTypeModel;
import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.DocumentTypeModel;
import com.rav.raverp.data.model.api.IFSCCodeModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PaymentModeTypeModel;
import com.rav.raverp.data.model.api.SubjectModel;
import com.rav.raverp.data.thread.ImageCompressTask;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.FileUtil;
import com.rav.raverp.utils.Logger;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class AddTicketFragment extends Fragment implements StoragePermissionListener {
    private static final String TAG = AddTicketFragment.class.getSimpleName();

    private StoragePermissionListener storagePermissionListener;
    //Static Int
    private static final int cameraRequest = 100, galleryRequest = 1001, mediaType = 1;
    String picturePath = "", filename = "", ext = "", encodedString = "";
    //Uri
    Uri cameraUri;
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    ApiHelper apiHelper;

    String loginId, paymentType = "";
    int roleId, subjectId, claimId, documentId;
    private LoginModel loginModel;

    final List<SubjectModel> getSubjectModelList = new ArrayList<>();
    List<ClaimTypeModel> getClaimModelList = new ArrayList<>();
    List<DocumentTypeModel> getDocumentModelList = new ArrayList<>();
    List<PaymentModeTypeModel> getPaymentModeList = new ArrayList<>();

    public AddTicketFragment() {
        // Required empty public constructor
    }

    Spinner spSubject, spClaimType, spDocumentType, spPaymentModeType;

    String subject[] = {"--Select--", "Payment Claim", "Delay in Documents", " Payout Related", "Other"};
    String paymentClaim[] = {"--Select--", "Bank", "Paytm", "G Pay", "Branch"};

    MaterialButton choose_file;
    AppCompatTextView no_file_chosen_text_view;

    ImageView ivAttachment;
    AppCompatButton btnSubmit;
    EditText etQuery, etTransactionNo, etAmount, etDate, etIFSCCode, etBankName, etBranchName;
    LinearLayout llClaimType, llDocumentType, llPaymentModeType, llCommon, llSecond;


    public void setStoragePermissionListener(StoragePermissionListener storagePermissionListener) {
        this.storagePermissionListener = storagePermissionListener;
    }

    File reportFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_ticket, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        loginModel = MyApplication.getLoginModel();
        loginId = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
        setStoragePermissionListener(this);
        checkStoragePermission();
        findViewById(view);
        return view;
    }

    void findViewById(View view) {
        spSubject = view.findViewById(R.id.spSubject);
        spClaimType = view.findViewById(R.id.spClaimType);
        spDocumentType = view.findViewById(R.id.spDocumentType);
        spPaymentModeType = view.findViewById(R.id.spPaymentModeType);
        llClaimType = view.findViewById(R.id.llClaimType);
        llDocumentType = view.findViewById(R.id.llDocumentType);
        llPaymentModeType = view.findViewById(R.id.llPaymentModeType);
        etQuery = view.findViewById(R.id.etQuery);
        etTransactionNo = view.findViewById(R.id.etTransactionNo);
        etAmount = view.findViewById(R.id.etAmount);
        etDate = view.findViewById(R.id.etDate);
        etIFSCCode = view.findViewById(R.id.etIFSCCode);
        etBankName = view.findViewById(R.id.etBankName);
        etBranchName = view.findViewById(R.id.etBranchName);
        llCommon = view.findViewById(R.id.llCommon);
        llSecond = view.findViewById(R.id.llSecond);
        choose_file = view.findViewById(R.id.choose_file);
        no_file_chosen_text_view = view.findViewById(R.id.no_file_chosen_text_view);
        ivAttachment = view.findViewById(R.id.ivAttachment);
        btnSubmit = view.findViewById(R.id.btnSubmit);


        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }
            }
        });


        if (NetworkUtils.isNetworkConnected()) {
            getSubject();
        } else {
            ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                @Override
                public void okAction() {
                    getSubject();
                }
            });
        }

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    subjectId = getSubjectModelList.get(position).getIntsubjectid();
                }
                if (spSubject.getSelectedItem().toString().equalsIgnoreCase("Payment Claim")) {
                    if (NetworkUtils.isNetworkConnected()) {
                        llClaimType.setVisibility(View.VISIBLE);
                        llDocumentType.setVisibility(View.GONE);
                        llDocumentType.setVisibility(View.GONE);
                        getClaim();
                    } else {
                        ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                            @Override
                            public void okAction() {
                                getClaim();
                            }
                        });

                    }
                } else if (spSubject.getSelectedItem().toString().equalsIgnoreCase("Delay in Documents")) {
                    if (NetworkUtils.isNetworkConnected()) {
                        llCommon.setVisibility(View.GONE);
                        llSecond.setVisibility(View.GONE);
                        llClaimType.setVisibility(View.GONE);
                        llDocumentType.setVisibility(View.VISIBLE);
                        llPaymentModeType.setVisibility(View.GONE);
                        getDocument();
                    } else {
                        ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                            @Override
                            public void okAction() {
                                getDocument();
                            }
                        });
                    }

                } else {
                    llCommon.setVisibility(View.GONE);
                    llSecond.setVisibility(View.GONE);
                    llClaimType.setVisibility(View.GONE);
                    llDocumentType.setVisibility(View.GONE);
                    llPaymentModeType.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spClaimType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    claimId = getClaimModelList.get(position).getIntclaimtypeid();

                    if (spClaimType.getSelectedItem().toString().equalsIgnoreCase("Bank")) {
                        if (NetworkUtils.isNetworkConnected()) {
                            llPaymentModeType.setVisibility(View.VISIBLE);
                            llCommon.setVisibility(View.VISIBLE);
                            llSecond.setVisibility(View.GONE);
                            getPaymentMode();
                        } else {
                            ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                                @Override
                                public void okAction() {
                                    getPaymentMode();
                                }
                            });
                        }
                    } else {
                        llPaymentModeType.setVisibility(View.GONE);
                        llCommon.setVisibility(View.VISIBLE);
                        llSecond.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    documentId = getDocumentModelList.get(position).getIntdocumenttypeid();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spPaymentModeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    paymentType = spPaymentModeType.getSelectedItem().toString();
                    if (spPaymentModeType.getSelectedItem().toString().equalsIgnoreCase("Cash")) {
                        llSecond.setVisibility(View.GONE);
                    } else {
                        llSecond.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMinMaxDialog();
            }
        });

        etIFSCCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (NetworkUtils.isNetworkConnected()) {
                        getIFSCCode(etIFSCCode.getText().toString().trim());
                    } else {
                        ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                            @Override
                            public void okAction() {
                                getIFSCCode(etIFSCCode.getText().toString().trim());
                            }
                        });
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spSubject.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select subject");
                } else if (spSubject.getSelectedItem().toString().equalsIgnoreCase("Delay in Documents")) {
                    if (spDocumentType.getSelectedItemPosition() == 0) {
                        ViewUtils.showToast("Please select document");
                    } else if (etQuery.getText().toString().isEmpty()) {
                        ViewUtils.showToast("Please enter query");
                    } /*else if (picturePath.equalsIgnoreCase("")) {
                        ViewUtils.showToast("Please select attachment");
                    } */ else {
                        if (NetworkUtils.isNetworkConnected()) {
                            createTicket();
                            // ViewUtils.showToast("Success");
                            //getActivity().onBackPressed();
                        } else {
                            ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                                @Override
                                public void okAction() {

                                }
                            });
                        }

                    }
                } else if (spSubject.getSelectedItem().toString().equalsIgnoreCase("Payment Claim")) {
                    if (spClaimType.getSelectedItemPosition() == 0) {
                        ViewUtils.showToast("Please select claim type");
                    } else if (spClaimType.getSelectedItem().toString().equalsIgnoreCase("Bank")) {
                        if (spPaymentModeType.getSelectedItemPosition() == 0) {
                            ViewUtils.showToast("Please select payment mode");
                        } else if (spPaymentModeType.getSelectedItem().toString().equalsIgnoreCase("Cash")) {
                            if (etTransactionNo.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter transaction no");
                            } else if (etAmount.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter amount");
                            } else if (etDate.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter date");
                            } else if (etQuery.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter query");
                            } else if (picturePath.equalsIgnoreCase("")) {
                                ViewUtils.showToast("Please select attachment");
                            } else {
                                if (NetworkUtils.isNetworkConnected()) {
                                    createTicket();
                                    //ViewUtils.showToast("Success");
                                    // getActivity().onBackPressed();
                                } else {
                                    ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });
                                }

                            }
                        } else {
                            if (etTransactionNo.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter transaction no");
                            } else if (etAmount.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter amount");
                            } else if (etDate.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter date");
                            } else if (etIFSCCode.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter IFSC code");
                            } else if (etBankName.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Invalid IFSC Code,Please enter valid IFSC code");
                            } else if (etBranchName.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Invalid IFSC Code,Please enter valid IFSC code");
                            } else if (etQuery.getText().toString().isEmpty()) {
                                ViewUtils.showToast("Please enter query");
                            } else if (picturePath.equalsIgnoreCase("")) {
                                ViewUtils.showToast("Please select attachment");
                            } else {
                                if (NetworkUtils.isNetworkConnected()) {
                                    createTicket();
                                    //ViewUtils.showToast("Success");
                                    // getActivity().onBackPressed();
                                } else {
                                    ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });
                                }

                            }
                        }
                    } else {
                        if (etTransactionNo.getText().toString().isEmpty()) {
                            ViewUtils.showToast("Please enter transaction no");
                        } else if (etAmount.getText().toString().isEmpty()) {
                            ViewUtils.showToast("Please enter amount");
                        } else if (etDate.getText().toString().isEmpty()) {
                            ViewUtils.showToast("Please enter date");
                        } else if (etQuery.getText().toString().isEmpty()) {
                            ViewUtils.showToast("Please enter query");
                        } else if (picturePath.equalsIgnoreCase("")) {
                            ViewUtils.showToast("Please select attachment");
                        } else {
                            if (NetworkUtils.isNetworkConnected()) {
                                createTicket();
                                // ViewUtils.showToast("Success");
                                //getActivity().onBackPressed();
                            } else {
                                ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                                    @Override
                                    public void okAction() {

                                    }
                                });
                            }

                        }
                    }
                } else if (etQuery.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter query");
                } /*else if (picturePath.equalsIgnoreCase("")) {
                    ViewUtils.showToast("Please select attachment");
                }*/ else {
                    if (NetworkUtils.isNetworkConnected()) {
                        createTicket();
                        //  ViewUtils.showToast("Success");
                        // getActivity().onBackPressed();
                    } else {
                        ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                            @Override
                            public void okAction() {

                            }
                        });
                    }
                }
            }
        });

    }

    public void selectImageOption() {
        final String[] mimeTypes = {"image/*", "application/pdf"};
        final CharSequence[] items = {getString(R.string.action_take_from_camera),
                getString(R.string.action_choose_from_gallery), getString(R.string.action_cancel)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.title_add_document));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.action_take_from_camera))) {
                    dialog.dismiss();
                    cameraUri = FileUtil.getInstance(getActivity()).createImageUri();
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstants.projectName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == mediaType) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + AppConstants.projectName + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void isStoragePermissionGranted(boolean granted) {
        isPermissionGranted = granted;
    }

    @Override
    public void isUserPressedSetting(boolean pressed) {
        isFromPermissionSettings = pressed;
    }

    protected void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) getActivity(), PERMISSIONS,
                        AppConstants.STORAGE_PERMISSION_REQUEST);
            } else {
                storagePermissionListener.isStoragePermissionGranted(true);
            }
        } else {
            storagePermissionListener.isStoragePermissionGranted(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.STORAGE_PERMISSION_REQUEST) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                ViewUtils.showStoragePermissionDialog(getActivity(),
                        getString(R.string.allow_storage_permission),
                        getString(R.string.msg_storage_permission_denied_explanation),
                        new DialogActionCallback() {
                            @Override
                            public void okAction() {
                                checkStoragePermission();
                            }
                        });
            } else {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    storagePermissionListener.isStoragePermissionGranted(true);
                } else {
                    // User selected the Never Ask Again Option
                    ViewUtils.showAlertDialog(getActivity(), getString(R.string.need_permission),
                            getString(R.string.go_to_settings_give_permissions),
                            getString(R.string.goto_settings),
                            getString(R.string.action_cancel), new DialogActionCallback() {
                                @Override
                                public void okAction() {
                                    storagePermissionListener.isUserPressedSetting(true);
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    getActivity().startActivity(intent);
                                }
                            });
                }
            }
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);

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
                        getActivity().getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
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
            String path = FileUtil.getRealPathFromURI_API11to18(getActivity(), originalUri);
            Logger.d(TAG, "File path === " + path);
            compressFile(path);
        } else {
            String path = FileUtil.getRealPathFromURI(getActivity(), originalUri);
            Logger.d(TAG, "File path === " + path);
            compressFile(path);
        }
    }

    private void compressFile(String path) {
        if (path != null) {
            String ext = CommonUtils.getExt(path);
            Logger.d(TAG, "File path extension === " + ext);
            if (ext != null) {
                ImageCompressTask imageCompressTask = new ImageCompressTask(getActivity(), path, imageCompressTaskListener);
                mExecutorService.execute(imageCompressTask);
                Logger.d(TAG, "File path === " + path);
            }
        } else {
            ViewUtils.showErrorDialog(getActivity(), getString(R.string.error_choose_another_file),
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
            reportFile = compressed.get(0);
            Log.d("ImageCompressor2", "New photo size ==> " + reportFile.length()); //log new file size.
            ivAttachment.setImageURI(Uri.fromFile(reportFile));

        }

        @Override
        public void onError(Throwable error) {
            Logger.wtf("ImageCompressor2 ", "Error occurred == " + error);
        }
    };

    void getSubject() {
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<SubjectModel>>> getSubjectModal = apiHelper.getSubject();
        getSubjectModal.enqueue(new Callback<ApiResponse<List<SubjectModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SubjectModel>>> call, Response<ApiResponse<List<SubjectModel>>> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    SubjectModel subjectModel = new SubjectModel();
                    subjectModel.setStrsubjectname("--Select Subject--");
                    subjectModel.setIntsubjectid(0);
                    getSubjectModelList.add(subjectModel);
                    getSubjectModelList.addAll(response.body().getBody());
                    ArrayAdapter<SubjectModel> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, getSubjectModelList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSubject.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<List<SubjectModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });

    }


    void getClaim() {
        getClaimModelList.clear();
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<ClaimTypeModel>>> responseCall = apiHelper.getClaim();
        responseCall.enqueue(new Callback<ApiResponse<List<ClaimTypeModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ClaimTypeModel>>> call, Response<ApiResponse<List<ClaimTypeModel>>> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    ClaimTypeModel claimTypeModel = new ClaimTypeModel();
                    claimTypeModel.setStrclaimtypename("--Select Claim Type--");
                    claimTypeModel.setIntclaimtypeid(0);
                    getClaimModelList.add(claimTypeModel);
                    getClaimModelList.addAll(response.body().getBody());
                    ArrayAdapter<ClaimTypeModel> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getClaimModelList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spClaimType.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ClaimTypeModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }

    void getDocument() {
        getDocumentModelList.clear();
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<DocumentTypeModel>>> responseCall = apiHelper.getDocument();
        responseCall.enqueue(new Callback<ApiResponse<List<DocumentTypeModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DocumentTypeModel>>> call, Response<ApiResponse<List<DocumentTypeModel>>> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    DocumentTypeModel documentTypeModel = new DocumentTypeModel();
                    documentTypeModel.setStrdocument("--Select Document Type--");
                    documentTypeModel.setIntdocumenttypeid(0);
                    getDocumentModelList.add(documentTypeModel);
                    getDocumentModelList.addAll(response.body().getBody());
                    ArrayAdapter<DocumentTypeModel> documentTypeModelArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getDocumentModelList);
                    documentTypeModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDocumentType.setAdapter(documentTypeModelArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DocumentTypeModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }

    void getPaymentMode() {
        getPaymentModeList.clear();
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<PaymentModeTypeModel>>> responseCall = apiHelper.getPaymentMode();
        responseCall.enqueue(new Callback<ApiResponse<List<PaymentModeTypeModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PaymentModeTypeModel>>> call, Response<ApiResponse<List<PaymentModeTypeModel>>> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    PaymentModeTypeModel paymentModeTypeModel = new PaymentModeTypeModel();
                    paymentModeTypeModel.setStrpaymentype("--Select Payment Mode Type--");
                    paymentModeTypeModel.setIntpaymenttypeid(0);
                    getPaymentModeList.add(paymentModeTypeModel);
                    getPaymentModeList.addAll(response.body().getBody());
                    ArrayAdapter<PaymentModeTypeModel> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getPaymentModeList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPaymentModeType.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PaymentModeTypeModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }

    public void dateMinMaxDialog() {

        final String[] selectedDate = {""};

        Calendar mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                String dob = String.valueOf(new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1).append("-").append(selectedYear));
                Log.d("dob", dob);

                // Log.d( "dob", AppUtils.formatDate( selectedYear, selectedMonth, selectedDay ) );

                selectedDate[0] = ViewUtils.formatDate(selectedYear, selectedMonth, selectedDay);

                etDate.setText(selectedDate[0]);

            }

        }, year, month, day);
        //mDatePicker1.setTitle("Select Date");

        mDatePicker1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        // TODO Hide Future Date Here
        mDatePicker1.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

        // TODO Hide Past Date Here
        //set min todays date
        //mDatePicker1.getDatePicker().setMinDate(System.currentTimeMillis());


        mDatePicker1.show();
    }

    void getIFSCCode(String ifsccode) {
        ViewUtils.startProgressDialog(getActivity());
        Call<IFSCCodeModel> ifscCodeModelCall = apiHelper.getIFSCCode(ifsccode);
        ifscCodeModelCall.enqueue(new Callback<IFSCCodeModel>() {
            @Override
            public void onResponse(Call<IFSCCodeModel> call, Response<IFSCCodeModel> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    IFSCCodeModel ifscCodeModel = new IFSCCodeModel();
                    ifscCodeModel = response.body();
                    etBankName.setText(ifscCodeModel.getBank());
                    etBranchName.setText(ifscCodeModel.getBranch());
                } else {
                    ViewUtils.showToast("Invalid IFSC Code");
                }

            }

            @Override
            public void onFailure(Call<IFSCCodeModel> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });
    }

    void createTicket() {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("filename", RequestBody.create(MediaType.parse("text/plain"), ""));

        MultipartBody.Part profilePic = null;
        if (reportFile != null)
            profilePic = MultipartBody.Part.createFormData("avatar", reportFile.getName(), RequestBody.create(MediaType.parse("image/*"), reportFile));

/*
        if (picturePath != null || !picturePath.equalsIgnoreCase("")) {
            RequestBody requestFile = RequestBody.create(reportFile,
                    MediaType.parse(reportFile.getAbsolutePath()));
             profilePic = MultipartBody.Part.createFormData("",
                    reportFile.getName(), requestFile);
            try {
                Logger.i(TAG, profilePic.body().contentLength() + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/
        ViewUtils.startProgressDialog(getActivity());
        Call<CommonModel> commonModelCall = apiHelper.createTicket(loginId, roleId, subjectId, claimId, documentId,
                paymentType, etTransactionNo.getText().toString().trim(), etAmount.getText().toString().trim(),
                etDate.getText().toString().trim(), etIFSCCode.getText().toString().trim(),
                etBranchName.getText().toString().trim(), etBankName.getText().toString().trim(),
                etQuery.getText().toString().trim(), profilePic, map);
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {

                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            ViewUtils.showSuccessDialog(getActivity(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            getActivity().onBackPressed();
                                           /* Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
                                            startActivity(intent);*/
                                        }
                                    });


                        } else {
                            ViewUtils.showErrorDialog(getActivity(), response.body().getMessage(),
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
            public void onFailure(Call<CommonModel> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }
}