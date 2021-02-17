package com.rav.raverp.ui.fragment.Associate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ImageCompressTaskListener;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.AddAssociateModal;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.GenderModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.thread.ImageCompressTask;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.BaseActivity;
import com.rav.raverp.ui.EditProfileActivity;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.FileUtil;
import com.rav.raverp.utils.Logger;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class AddAssociateFragment extends Fragment implements StoragePermissionListener {

    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

    private static final String TAG = AddAssociateFragment.class.getSimpleName();
    private StoragePermissionListener storagePermissionListener;
    Uri cameraUri;
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

    private ApiHelper apiHelper;
    View view;
    private Spinner gender_spinner;
    TextView txt_state, txt_city;
    EditText txt_pincode, etDob, etAadharNo, etPanNo;
    Button btnSubmit;

    RadioGroup rgPosition, rgRelation;
    RadioButton rbLeft, rbRight, rbDo, rbSo, rbHo;
    AppCompatEditText txt_customer_name, txt_contact_no, txt_email_id, acetPassword, acetConfirmPassword, txt_address, acetFatherOf;

    String leg = "", relation = "";
    private LoginModel login;
    String id;
    int roleId;

    ImageView ivAadharFront, ivAadharBack, ivPan;
    CircleImageView civProfile;

    private DatePicker datepicker;

    String fileType = "", aadharFront, aadharBack, pan, profile;

    File fileAadharFront = null, fileAadharBack = null, filePan = null, fileProfile = null;

    public AddAssociateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_associate, container, false);
        login = MyApplication.getLoginModel();
        id = login.getStrLoginID();
        setStoragePermissionListener(this);
        findViewById(view);
        GetGender();

        return view;
    }

    void findViewById(View view) {
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        gender_spinner = view.findViewById(R.id.gender_spinner);
        txt_state = view.findViewById(R.id.txt_state);
        txt_city = view.findViewById(R.id.txt_city);
        txt_pincode = view.findViewById(R.id.txt_pincode);
        etDob = view.findViewById(R.id.etDob);
        rgPosition = view.findViewById(R.id.rgPosition);
        rbLeft = view.findViewById(R.id.rbLeft);
        rbRight = view.findViewById(R.id.rbRight);
        rgRelation = view.findViewById(R.id.rgRelation);
        rbDo = view.findViewById(R.id.rbDo);
        rbSo = view.findViewById(R.id.rbSo);
        rbHo = view.findViewById(R.id.rbHo);
        txt_customer_name = view.findViewById(R.id.txt_customer_name);
        acetFatherOf = view.findViewById(R.id.acetFatherOf);
        txt_contact_no = view.findViewById(R.id.txt_contact_no);
        txt_email_id = view.findViewById(R.id.txt_email_id);
        acetPassword = view.findViewById(R.id.acetPassword);
        acetConfirmPassword = view.findViewById(R.id.acetConfirmPassword);
        txt_address = view.findViewById(R.id.txt_address);
        etAadharNo = view.findViewById(R.id.etAadharNo);
        etPanNo = view.findViewById(R.id.etPanNo);
        ivAadharFront = view.findViewById(R.id.ivAadharFront);
        ivAadharBack = view.findViewById(R.id.ivAadharBack);
        ivPan = view.findViewById(R.id.ivPan);
        civProfile = view.findViewById(R.id.civProfile);

        btnSubmit = view.findViewById(R.id.btnsubmit);

        txt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    getCity(txt_pincode.getText().toString().trim());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });


        rgPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLeft:
                        leg = "Left";
                        // do operations specific to this selection
                        break;
                    case R.id.rbRight:
                        leg = "Right";
                        // do operations specific to this selection
                        break;

                }
            }
        });

        rgRelation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbDo:
                        relation = "D/O";
                        break;
                    case R.id.rbSo:
                        relation = "S/O";
                        break;

                    case R.id.rbHo:
                        relation = "H/O";
                        break;
                }
            }
        });


        ivAadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "1";
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }
            }
        });

        ivAadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "2";
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }
            }
        });

        ivPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "3";
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }
            }
        });

        civProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileType = "4";
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pan = etPanNo.getText().toString().trim();

                Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

                Matcher matcher = pattern.matcher(Pan);

                if (leg.equalsIgnoreCase("")) {
                    ViewUtils.showToast("Please Check Position");
                } else if (txt_customer_name.getText().toString().isEmpty()) {
                    txt_customer_name.setError(getString(R.string.error_field_required));
                    requestFocus(txt_customer_name);
                } else if (acetFatherOf.getText().toString().trim().isEmpty()) {
                    acetFatherOf.setError(getString(R.string.error_field_required));
                    requestFocus(acetFatherOf);
                } else if (relation.equalsIgnoreCase("")) {
                    ViewUtils.showToast("Please check relation");
                } else if (gender_spinner.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select Gender");
                } else if (txt_contact_no.getText().toString().isEmpty() || txt_contact_no.getText().length() != 10) {
                    txt_contact_no.setError("Please enter correct mobile no");
                    requestFocus(txt_contact_no);
                } else if (etDob.getText().toString().isEmpty()) {
                    etDob.setError(getString(R.string.error_field_required));
                    requestFocus(etDob);
                } else if (txt_email_id.getText().toString().isEmpty() || !CommonUtils.isValidEmail(txt_email_id.getText().toString().trim())) {
                    txt_email_id.setError("Please enter correct email id");
                    requestFocus(txt_email_id);
                } else if (acetPassword.getText().toString().isEmpty() || acetPassword.getText().length() < 6 || acetPassword.getText().length() > 15) {
                    acetPassword.setError("Password length must be minimum 6 and maximum 15 character");
                    requestFocus(acetPassword);
                } else if (!acetConfirmPassword.getText().toString().equals(acetPassword.getText().toString().trim())) {
                    acetConfirmPassword.setError("Password and confirm password must be same");
                    requestFocus(acetConfirmPassword);
                } else if (txt_pincode.getText().toString().isEmpty() && txt_pincode.getText().length() != 6) {
                    txt_pincode.setError("Please enter correct pinCode");
                    requestFocus(txt_pincode);
                } else if (txt_state.getText().toString().isEmpty()) {
                    txt_state.setError(getString(R.string.error_field_required));
                    requestFocus(txt_state);
                } else if (txt_city.getText().toString().isEmpty()) {
                    txt_city.setError(getString(R.string.error_field_required));
                    requestFocus(txt_city);
                } else if (txt_address.getText().toString().isEmpty()) {
                    txt_address.setError(getString(R.string.error_field_required));
                    requestFocus(txt_address);
                } else if (!etAadharNo.getText().toString().isEmpty() && etAadharNo.getText().length() != 12) {
                    etAadharNo.setError("please enter valid aadhar card no.");
                    requestFocus(etAadharNo);
                } else if (!etPanNo.getText().toString().isEmpty() && !matcher.matches()) {
                    etPanNo.setError("please enter valid pan card no.");
                    requestFocus(etPanNo);
                } else if (NetworkUtils.isNetworkConnected()) {
                    addAssociate();
                } else {
                    ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            addAssociate();
                        }
                    });
                }

            }
        });

    }

    private void GetGender() {

        Call<ApiResponse<List<GenderModel>>> getGenderModelCall =
                apiHelper.getGenderModel();

        getGenderModelCall.enqueue(new Callback<ApiResponse<List<GenderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GenderModel>>> call,
                                   Response<ApiResponse<List<GenderModel>>> response) {


                if (response.isSuccessful()) {
                    final List<GenderModel> getgenderList = new ArrayList<>();
                    GenderModel genderModel = new GenderModel();
                    genderModel.setStrGender("--Select--");
                    genderModel.setIntGenderId(0);
                    getgenderList.add(genderModel);
                    getgenderList.addAll(response.body().getBody());
                    ArrayAdapter<GenderModel> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, getgenderList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    gender_spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GenderModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }

    void getCity(String pinCode) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        String url = "https://api.postalpincode.in/pincode/" + pinCode;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            Log.v("response", response);


                            if (!response.isEmpty()) {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("PostOffice");
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                                    txt_city.setText(jsonObject1.getString("District"));
                                    txt_state.setText(jsonObject1.getString("State"));
                                } else {
                                    txt_city.setText("");
                                    txt_state.setText("");
                                    Toast.makeText(getActivity(), jsonObject.getString("Status"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mProgressDialog.dismiss();
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Something Problem")
                                        .setMessage("Something went wrong.")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setNegativeButton("No", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "Error.Response: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Something Problem")
                                .setMessage("Error.Response: " + error.getMessage())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("No", null).show();

                    }
                }
        );
        MyApplication.getInstance().addToRequestQueue(postRequest);
    }

    private void selectDate() {
        final String[] selectedDate = {""};

        Calendar mCurrentDate = Calendar.getInstance();
        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        datepicker = new DatePicker(getActivity());

                        datepicker.init(year, monthOfYear + 1, dayOfMonth, null);

                        selectedDate[0] = ViewUtils.formatDate(year, monthOfYear, dayOfMonth);

                        etDob.setText(selectedDate[0]);

                    }
                }, year, month, day);

        if (datepicker != null) {
            datePickerDialog.updateDate(datepicker.getYear(), datepicker.getMonth() - 1, datepicker.getDayOfMonth());

        }

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        // TODO Hide Future Date Here
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // TODO Hide Past Date Here
        //set min todays date
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
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

                etDob.setText(selectedDate[0]);

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
        if (datepicker != null) {
            mDatePicker1.updateDate(datepicker.getYear(), datepicker.getMonth() - 1, datepicker.getDayOfMonth());
        }

        mDatePicker1.show();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void addAssociate() {

        MultipartBody.Part pan = null;
        MultipartBody.Part front = null;
        MultipartBody.Part back = null;
        MultipartBody.Part profile = null;

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("filename", RequestBody.create(MediaType.parse("text/plain"), ""));

        /*HashMap<String,> map = new HashMap<>();
        if (filePan != null)
            map.put("PanCardImage", MultipartBody.Part.createFormData("PanCardImage", filePan.getName(), RequestBody.create(MediaType.parse("image/*"), filePan)));
        if (fileAadharFront != null)
            map.put("AadharCardFrontImage", MultipartBody.Part.createFormData("AadharCardFrontImage", fileAadharFront.getName(), RequestBody.create(MediaType.parse("image/*"), fileAadharFront)));
        if (fileAadharBack != null)
            map.put("AadharCardBackImage", MultipartBody.Part.createFormData("AadharCardBackImage", fileAadharBack.getName(), RequestBody.create(MediaType.parse("image/*"), fileAadharBack)));
        if (fileProfile != null)
            map.put("ProfilePic", MultipartBody.Part.createFormData("ProfilePic", fileAadharBack.getName(), RequestBody.create(MediaType.parse("image/*"), fileProfile)));*/



        if (filePan != null)
            pan = MultipartBody.Part.createFormData("PanCardImage", filePan.getName(), RequestBody.create(MediaType.parse("image/*"), filePan));
        if (fileAadharFront != null)
            front = MultipartBody.Part.createFormData("AadharCardFrontImage", fileAadharFront.getName(), RequestBody.create(MediaType.parse("image/*"), fileAadharFront));
        if (fileAadharBack != null)
            back = MultipartBody.Part.createFormData("AadharCardBackImage", fileAadharBack.getName(), RequestBody.create(MediaType.parse("image/*"), fileAadharBack));
        if (fileProfile != null)
            profile = MultipartBody.Part.createFormData("ProfilePic", fileAadharBack.getName(), RequestBody.create(MediaType.parse("image/*"), fileProfile));

        /*if (reportFile != null)
            profilePic = MultipartBody.Part.createFormData("avatar", reportFile.getName(), RequestBody.create(MediaType.parse("image/*"), reportFile));*/


        ViewUtils.startProgressDialog(getActivity());
        final Call<AddAssociateModal> getAddCustomerEditCustomerCall = apiHelper.getAssociate(id, id, txt_customer_name.getText().toString().trim(), leg, gender_spinner.getSelectedItem().toString().trim(), txt_contact_no.getText().toString().trim(), txt_email_id.getText().toString().trim(), etDob.getText().toString().trim(), acetPassword.getText().toString().trim(), relation, acetFatherOf.getText().toString().trim(), txt_pincode.getText().toString().trim(), txt_state.getText().toString().trim(), txt_city.getText().toString().trim(), txt_address.getText().toString().trim(), etAadharNo.getText().toString().trim(),
                etPanNo.getText().toString().trim(),front,back,pan,profile, map);

        getAddCustomerEditCustomerCall.enqueue(new Callback<AddAssociateModal>() {
            @Override
            public void onResponse(Call<AddAssociateModal> call, Response<AddAssociateModal> response) {
                ViewUtils.endProgressDialog();
                /*try {
                    JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("TAG", "onResponse: " + object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                AddAssociateModal associateModal = response.body();
                if (associateModal.getResponse().equalsIgnoreCase("Success")) {
                    ViewUtils.showAssociateDetail(getActivity(), "Name :" + associateModal.getBody().get(0).getDisplayName(), "LoginId :" + associateModal.getBody().get(0).getLoginId(), "Password :" + associateModal.getBody().get(0).getSimplePassword(),
                            new DialogActionCallback() {
                                @Override
                                public void okAction() {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
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


             /*   if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {


                        ViewUtils.showSuccessDialog(getActivity(), response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }
                    else {

                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {
                            ViewUtils.showErrorDialog(getActivity(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {


                                        }
                                    });
                        }
                    }
                }*/
            }


            @Override
            public void onFailure(Call<AddAssociateModal> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

            //   Log.d("ImageCompressor2", "New photo size ==> " + file.length()); //log new file size.
            if (fileType.equals("1")) {
                fileAadharFront = compressed.get(0);
                //aadharFront = fileAadharFront.getAbsolutePath();
                ivAadharFront.setImageURI(Uri.fromFile(fileAadharFront));

            }
            if (fileType.equals("2")) {
                fileAadharBack = compressed.get(0);
                // aadharBack = fileAadharBack.getAbsolutePath();
                ivAadharBack.setImageURI(Uri.fromFile(fileAadharBack));

            }
            if (fileType.equals("3")) {
                filePan = compressed.get(0);
                //pan = filePan.getAbsolutePath();
                ivPan.setImageURI(Uri.fromFile(filePan));
            }
            if (fileType.equals("4")) {
                fileProfile = compressed.get(0);
                //profile = fileProfile.getAbsolutePath();
                civProfile.setImageURI(Uri.fromFile(fileProfile));
            }


            //  profilePicPath = file.getAbsolutePath();
            //  Logger.d(TAG, "File path === " + profilePicPath);
            //showMessage("Profile pic added successfully");
            // binding.profileImageView.setImageURI(Uri.fromFile(file));
            // updateProfile();
            //binding.profileImageView.setImageBitmap(BitmapFactory.decodeFile(profilePicPath));
        }

        @Override
        public void onError(Throwable error) {
            Logger.wtf("ImageCompressor2 ", "Error occurred == " + error);
        }
    };

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
                                //checkStoragePermission();
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

    public void setStoragePermissionListener(StoragePermissionListener storagePermissionListener) {
        this.storagePermissionListener = storagePermissionListener;
    }

}