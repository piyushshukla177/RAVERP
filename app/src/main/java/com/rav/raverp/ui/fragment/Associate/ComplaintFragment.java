package com.rav.raverp.ui.fragment.Associate;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.rav.raverp.BuildConfig;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.ui.AddWalletActivity;
import com.rav.raverp.ui.BaseActivity;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class ComplaintFragment extends Fragment implements StoragePermissionListener {
    private StoragePermissionListener storagePermissionListener;
    //Static Int
    private static final int cameraRequest = 100, galleryRequest = 1001, mediaType = 1;
    String picturePath = "", filename = "", ext = "", encodedString = "";
    //Uri
    Uri fileUri;
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    Spinner spSubject, spClaimType;

    String subject[] = {"--Select--", "Payment Claim", "Delay in Documents", " Payout Related", "Other"};
    String paymentClaim[] = {"--Select--", "Bank", "Paytm", "G Pay", "Branch"};

    MaterialButton choose_file;
    AppCompatTextView no_file_chosen_text_view;

    ImageView ivAttachment;
    AppCompatButton btnSubmit;
    EditText etQuery;
    LinearLayout llPaymentClaim;
    TextView tvClaimType;


    public void setStoragePermissionListener(StoragePermissionListener storagePermissionListener) {
        this.storagePermissionListener = storagePermissionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        setStoragePermissionListener(this);
        checkStoragePermission();
        findViewById(view);
        return view;
    }

    void findViewById(View view) {
        spSubject = view.findViewById(R.id.spSubject);
        choose_file = view.findViewById(R.id.choose_file);
        no_file_chosen_text_view = view.findViewById(R.id.no_file_chosen_text_view);
        spClaimType = view.findViewById(R.id.spClaimType);
        llPaymentClaim = view.findViewById(R.id.llPaymentClaim);
        ivAttachment = view.findViewById(R.id.ivAttachment);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        etQuery = view.findViewById(R.id.etQuery);
        tvClaimType = view.findViewById(R.id.tvClaimType);

        ArrayAdapter<String> spinnerArrayAdapterSubject = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, subject); //selected item will look like a spinner set from XML
        spinnerArrayAdapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubject.setAdapter(spinnerArrayAdapterSubject);

        ArrayAdapter<String> spinnerArrayAdapterClaim = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, paymentClaim); //selected item will look like a spinner set from XML
        spinnerArrayAdapterClaim.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClaimType.setAdapter(spinnerArrayAdapterClaim);


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

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    llPaymentClaim.setVisibility(View.VISIBLE);
                    tvClaimType.setVisibility(View.VISIBLE);
                } else {
                    llPaymentClaim.setVisibility(View.GONE);
                    tvClaimType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spSubject.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select subject");
                } else if (etQuery.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter query");
                } else if (picturePath.equalsIgnoreCase("")) {
                    ViewUtils.showToast("Please select attachment");
                } else {
                    ViewUtils.showToast("Your Complaint submitted successfully");
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });

    }

    public void selectImageOption() {
        final String[] mimeTypes = {"image/*", "application/pdf"};
        final CharSequence[] items = {getString(R.string.action_take_from_camera),
                getString(R.string.action_choose_from_gallery), getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.title_add_document));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.action_take_from_camera))) {
                    dialog.dismiss();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        fileUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(mediaType));

                        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(it, cameraRequest);
                    } else {
                        // create Intent to take a picture and return control to the calling application
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(mediaType); // create a file to save the image
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                        // start the image capture Intent
                        startActivityForResult(intent, cameraRequest);

                    }


                } else if (items[item].equals(getString(R.string.action_choose_from_gallery))) {
                    dialog.dismiss();
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, galleryRequest);

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

        if (requestCode == cameraRequest) {
            if (resultCode == RESULT_OK) {
                picturePath = fileUri.getPath();
                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                no_file_chosen_text_view.setText(filename);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivAttachment.setImageBitmap(bitmap);
            }
        } else if (requestCode == galleryRequest) {
            if (data != null) {
                try {
                    Uri contentURI = data.getData();
                    //get the Uri for the captured image
                    Uri picUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    Log.v("pic", "pic");
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    System.out.println("Image Path : " + picturePath);
                    cursor.close();
                    filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                    no_file_chosen_text_view.setText(filename);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    ivAttachment.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getActivity(), "unable to select image", Toast.LENGTH_LONG).show();
            }


        }
    }

}