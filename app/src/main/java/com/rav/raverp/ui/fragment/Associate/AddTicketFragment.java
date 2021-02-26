package com.rav.raverp.ui.fragment.Associate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;


import android.util.Log;
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


import com.rav.raverp.data.adapter.SendAttachmentAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;

import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.AttachmentModel;

import com.rav.raverp.data.model.api.CommonModel;
import com.rav.raverp.data.model.api.DocumentTypeModel;

import com.rav.raverp.data.model.api.LoginModel;


import com.rav.raverp.data.model.api.SendAttachmentModel;
import com.rav.raverp.data.model.api.SubjectModel;

import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.AppConstants;


import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    ArrayList<SendAttachmentModel> spacecrafts = new ArrayList<>();

    public static List<Uri> uriPath;

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
                   /* if (uriPath != null)
                        uriPath.clear();*/
                    if (spacecrafts != null)
                        spacecrafts.clear();
                    TedBottomPicker.with(getActivity())
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


                    //openFilePicker();
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

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
            showUriList(mediaFiles);
        }
    }*/


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
                    documentTypeModel.setStrdocument("--Select Support Type--");
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


    MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());

        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        //RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), surveyBody);
    }


    void createTicket() {
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (spacecrafts != null) {
            for (int i = 0; i < spacecrafts.size(); i++) {
                parts.add(prepareFilePart(i + "", spacecrafts.get(i).getUri()));

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

    private void showUriList(List<Uri> mediaFiles) {
        SendAttachmentModel s;

        for (Uri path : mediaFiles) {
            s = new SendAttachmentModel();
            s.setName(path.getPath().substring(path.getPath().lastIndexOf("/") + 1));

            s.setUri(Uri.parse(compressImage(path.getPath())));
            spacecrafts.add(s);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvAttachment.setLayoutManager(gridLayoutManager);
        rvAttachment.setAdapter(new SendAttachmentAdapter(getActivity(), spacecrafts, "add"));
        rvAttachment.setHasFixedSize(true);
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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
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