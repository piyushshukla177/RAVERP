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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.Settings;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.Spinner;


import com.google.android.material.button.MaterialButton;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.AttachmentAdapter;

import com.rav.raverp.data.interfaces.DialogActionCallback;

import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.AttachmentModel;

import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.DocumentTypeModel;

import com.rav.raverp.data.model.api.LoginModel;


import com.rav.raverp.data.model.api.SubjectModel;

import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;

import com.rav.raverp.utils.AppConstants;


import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class AddTicketFragment extends Fragment implements StoragePermissionListener {

    //for file picker
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private final static int FILE_REQUEST_CODE = 1;

    private static final String TAG = AddTicketFragment.class.getSimpleName();

    private StoragePermissionListener storagePermissionListener;

    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;


    ApiHelper apiHelper;

    String loginId, paymentType = "";
    int roleId, subjectId, documentId;

    private LoginModel loginModel;

    List<SubjectModel> getSubjectModelList = new ArrayList<>();
    List<DocumentTypeModel> getDocumentModelList = new ArrayList<>();
    ArrayList<AttachmentModel> spacecrafts = new ArrayList<>();

    Spinner spSupportFor, spSupportType, spPriority;

    String priority[] = {"--Select Priority--", "Low", "Medium", "High"};


    MaterialButton choose_file;
    AppCompatTextView no_file_chosen_text_view;

    AppCompatButton btnSubmit;
    EditText etSubject, etQuery;
    LinearLayout llDocumentType, llPaymentModeType, llCommon, llSecond;
    RecyclerView rvAttachment;


    public void setStoragePermissionListener(StoragePermissionListener storagePermissionListener) {
        this.storagePermissionListener = storagePermissionListener;
    }


    public AddTicketFragment() {
        // Required empty public constructor
    }


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
        spSupportFor = view.findViewById(R.id.spSupportFor);
        etSubject = view.findViewById(R.id.etSubject);
        spSupportType = view.findViewById(R.id.spSupportType);
        spPriority = view.findViewById(R.id.spPriority);
        llDocumentType = view.findViewById(R.id.llDocumentType);
        llPaymentModeType = view.findViewById(R.id.llPaymentModeType);
        etQuery = view.findViewById(R.id.etQuery);
        choose_file = view.findViewById(R.id.choose_file);
        no_file_chosen_text_view = view.findViewById(R.id.no_file_chosen_text_view);
        rvAttachment = view.findViewById(R.id.rvAttachment);
        btnSubmit = view.findViewById(R.id.btnSubmit);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, priority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted) {
                    openFilePicker();
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

        spSupportFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    subjectId = getSubjectModelList.get(position).getIntsubjectid();
                    getDocument(subjectId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spSupportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    paymentType = spPriority.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spSupportFor.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select Support For");
                } else if (spSupportType.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select Support type");
                } else if (etSubject.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter subject");
                } else if (spPriority.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select Support type");
                } else if (etQuery.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter query");
                } else {
                    if (NetworkUtils.isNetworkConnected()) {
                        createTicket();
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
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
            showUriList(mediaFiles);
        }
    }


    void getSubject() {
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<SubjectModel>>> getSubjectModal = apiHelper.getSubject();
        getSubjectModal.enqueue(new Callback<ApiResponse<List<SubjectModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SubjectModel>>> call, Response<ApiResponse<List<SubjectModel>>> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    SubjectModel subjectModel = new SubjectModel();
                    subjectModel.setStrsubjectname("--Select Support For--");
                    subjectModel.setIntsubjectid(0);
                    getSubjectModelList.add(subjectModel);
                    getSubjectModelList.addAll(response.body().getBody());
                    ArrayAdapter<SubjectModel> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, getSubjectModelList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSupportFor.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<List<SubjectModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });

    }


    void getDocument(int subjectId) {
        getDocumentModelList.clear();
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse<List<DocumentTypeModel>>> responseCall = apiHelper.getDocument(subjectId);
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
                    spSupportType.setAdapter(documentTypeModelArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DocumentTypeModel>>> call, Throwable t) {
                ViewUtils.endProgressDialog();

            }
        });
    }


    MultipartBody.Part prepareFilePart(String partName, MediaFile fileUri) {

        File file = new File(fileUri.getPath());

        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        //RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), surveyBody);
    }


    void createTicket() {
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (mediaFiles != null) {
            for (int i = 0; i < mediaFiles.size(); i++) {
                parts.add(prepareFilePart(i + "", mediaFiles.get(i)));

            }
        }
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("filename", RequestBody.create(MediaType.parse("text/plain"), ""));

        MultipartBody.Part profilePic = null;

        ViewUtils.startProgressDialog(getActivity());
        Call<CommonModel> commonModelCall = apiHelper.createTicket(loginId, roleId, subjectId, etSubject.getText().toString().trim(), 0, documentId,
                "", "", "",
                "", "",
                "", "",
                etQuery.getText().toString().trim(), parts, map, spPriority.getSelectedItem().toString());
        commonModelCall.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                if (response.isSuccessful()) {
                    ViewUtils.endProgressDialog();
                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            rvAttachment.removeAllViewsInLayout();
                            ViewUtils.showSuccessDialog(getActivity(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            getActivity().onBackPressed();

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

    private void openFilePicker() {
        Intent intent = new Intent(getActivity(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setSelectedMediaFiles(mediaFiles)
                .setShowFiles(true)
                .setShowImages(true)
                .setShowAudios(false)
                .setShowVideos(false)
                .setIgnoreNoMedia(false)
                .enableVideoCapture(false)
                .enableImageCapture(true)
                .setIgnoreHiddenFile(true)
                .setSkipZeroSizeFiles(true)
                .setIgnoreNoMedia(true)
                .setSuffixes("pdf", "doc", "docx")
                .setMaxSelection(5)
                .build());
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    private void showUriList(List<MediaFile> mediaFiles) {
        AttachmentModel s;

        for (MediaFile path : mediaFiles) {
            s = new AttachmentModel();
            s.setName(path.getPath().substring(path.getPath().lastIndexOf("/") + 1));

            s.setFile(path);
            spacecrafts.add(s);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvAttachment.setLayoutManager(gridLayoutManager);
        rvAttachment.setAdapter(new AttachmentAdapter(getActivity(), spacecrafts));
        rvAttachment.setHasFixedSize(true);
    }


}