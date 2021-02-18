package com.rav.raverp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;


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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ImageCompressTaskListener;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.api.AddCustomerEditCustomer;
import com.rav.raverp.data.model.api.ApiResponse;

import com.rav.raverp.data.model.api.CustomerPlotBookModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PaymentGatewayModel;
import com.rav.raverp.data.thread.ImageCompressTask;
import com.rav.raverp.databinding.ActivityAddWalletBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.FileUtil;
import com.rav.raverp.utils.Logger;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class AddWalletActivity extends BaseActivity implements ArrowBackPressed, StoragePermissionListener {

    private static final int mediaType = 1;
    private static final String TAG = AddWalletActivity.class.getSimpleName();
    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    ActivityAddWalletBinding binding;
    private Context mContext = AddWalletActivity.this;
    private ApiHelper apiHelper;
    private LoginModel login;
    private Spinner customer_name_spinner;
    int customerId;
    PaymentGatewayModel paymentGatewayModel;
    String date;

    String id;
    int roleid;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;
    private String profilePicPath, filename;
    private Uri cameraUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_add_wallet);
        setToolbarTitle("Wallet Details");
        showBackArrow();
        setArrowBackPressed(this);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();

        id = login.getStrLoginID();
        roleid = login.getIntRoleID();
        setStoragePermissionListener(this);
        checkStoragePermission();

        customer_name_spinner = (Spinner) findViewById(R.id.customer_spinner);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.PaymentType, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.paymenttypespinner.setAdapter(adapter);

        GetCustomer();

        binding.paymenttypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = binding.paymenttypespinner.getSelectedItem().toString();

                switch (parent.getId()) {

                    case R.id.paymenttypespinner:
                        String payment = parent.getSelectedItem().toString();
                        switch (payment) {
                            case "Cash":
                                binding.l1.setVisibility(View.VISIBLE);
                                binding.l2.setVisibility(View.GONE);
                                binding.l3.setVisibility(View.GONE);
                                break;

                            case "Cheque":
                                binding.l1.setVisibility(View.VISIBLE);
                                binding.l2.setVisibility(View.GONE);
                                binding.l3.setVisibility(View.GONE);
                                break;

                            case "NEFT":
                                binding.l1.setVisibility(View.VISIBLE);
                                binding.l2.setVisibility(View.VISIBLE);
                                binding.l3.setVisibility(View.GONE);
                                break;

                            case "RTGS":
                                binding.l1.setVisibility(View.VISIBLE);
                                binding.l2.setVisibility(View.GONE);
                                binding.l3.setVisibility(View.VISIBLE);
                                break;

                            case "Online":
                                binding.l1.setVisibility(View.GONE);
                                binding.l2.setVisibility(View.GONE);
                                binding.l3.setVisibility(View.GONE);
                                break;
                        }
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        binding.m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.m1.setVisibility(View.GONE);
                binding.m2.setVisibility(View.VISIBLE);
                binding.la1.setVisibility(View.GONE);
                binding.la2.setVisibility(View.GONE);
            }
        });


        binding.m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.m2.setVisibility(View.GONE);
                binding.m1.setVisibility(View.VISIBLE);
                binding.la1.setVisibility(View.VISIBLE);
                binding.la2.setVisibility(View.VISIBLE);
            }
        });

        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = login.getStrLoginID();
                final int roleid = login.getIntRoleID();
                if (binding.editWalletAmount.getText().toString().isEmpty()) {
                    binding.editWalletAmount.setError(getString(R.string.error_field_required));
                    requestFocus(binding.editWalletAmount);
                } else if (Integer.parseInt(binding.editWalletAmount.getText().toString()) <= 0) {
                    binding.editWalletAmount.setError("Please enter valid amount");
                    requestFocus(binding.editWalletAmount);
                } else if (binding.paymenttypespinner.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select payment type");
                } else if (NetworkUtils.isNetworkConnected()) {
                    getPaymentDetail(id, roleid);
                } else {
                    ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            getPaymentDetail(id, roleid);
                        }
                    });

                }
            }
        });

        binding.chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted) {
                    selectImageOption();
                } else {
                    checkStoragePermission();
                }
            }
        });


        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    // Response from Payumoney
                    String payuRespons = transactionResponse.getPayuResponse();
                    try {
                        JSONObject jsonObject = new JSONObject(payuRespons);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        date = jsonObject1.getString("addedon");
                        AddWalletOnline(id, roleid, jsonObject1.getString("amount"), binding.paymenttypespinner.getSelectedItem().toString(), "", "", "", jsonObject1.getString("txnid"), jsonObject1.getString("paymentId"), jsonObject1.getString("status"), jsonObject1.getString("phone"), jsonObject1.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.v("payMoney", payuRespons);
                    // Response from SURl and FURL
                    String merchantResponse = transactionResponse.getTransactionDetails();
                    Log.v("SorFUrl", merchantResponse);
                    Log.e(TAG, "tran " + payuRespons + "---" + merchantResponse);
                    // Toast.makeText(AddWalletActivity.this,"Payment Successfull",Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(AddWalletActivity.this, "Payment Cancel", Toast.LENGTH_SHORT).show();
                }
            }
        }

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
                        AddWalletActivity.this.getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
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


    public void selectImageOption() {
        final String[] mimeTypes = {"image/*", "application/pdf"};
        final CharSequence[] items = {getString(R.string.action_take_from_camera),
                getString(R.string.action_choose_from_gallery), getString(R.string.action_cancel)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddWalletActivity.this);
        builder.setTitle(getString(R.string.title_add_document));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.action_take_from_camera))) {
                    dialog.dismiss();
                    cameraUri = FileUtil.getInstance(AddWalletActivity.this).createImageUri();
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


    private void GetCustomer() {
        String id = login.getStrLoginID();
        int roleid = login.getIntRoleID();
        Call<ApiResponse<List<CustomerPlotBookModel>>> getCustomerPlotBookModelCall = apiHelper.getCustomerPlotBookModel(id, roleid);
        getCustomerPlotBookModelCall.enqueue(new Callback<ApiResponse<List<CustomerPlotBookModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CustomerPlotBookModel>>> call,
                                   Response<ApiResponse<List<CustomerPlotBookModel>>> response) {
                if (response.isSuccessful()) {
                    final List<CustomerPlotBookModel> getCustomerList = new ArrayList<>();
                    CustomerPlotBookModel getcustomer = new CustomerPlotBookModel();
                    getcustomer.setStrName("--Select--");
                    getcustomer.setBigIntCustomerId(0);
                    getCustomerList.add(getcustomer);
                    getCustomerList.addAll(response.body().getBody());
                    ArrayAdapter<CustomerPlotBookModel> adapter = new ArrayAdapter<>(AddWalletActivity.this,
                            R.layout.simple_spinner_item, getCustomerList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    customer_name_spinner.setAdapter(adapter);
                    customer_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                customerId = getCustomerList.get(position).getBigIntCustomerId();


                            } else {

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CustomerPlotBookModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }

    private void showFilterDialog(String transctionId, String paymentId, String amount, String date, String status) {
        final Dialog filterDialog = new Dialog(AddWalletActivity.this);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(false);
        filterDialog.setContentView(R.layout.dialog_payment_response);
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);

        AppCompatTextView tvTransactionId = filterDialog.findViewById(R.id.tvTransactionId);
        AppCompatTextView tvPaymentId = filterDialog.findViewById(R.id.tvPaymentId);
        AppCompatTextView tvAmount = filterDialog.findViewById(R.id.tvAmount);
        AppCompatTextView tvTransactionDate = filterDialog.findViewById(R.id.tvTransactionDate);
        AppCompatTextView tvTransactionStatus = filterDialog.findViewById(R.id.tvTransactionStatus);
        AppCompatButton btnOk = filterDialog.findViewById(R.id.btnOk);

        if (status.equalsIgnoreCase("success")) {
            tvTransactionStatus.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvTransactionStatus.setTextColor(getResources().getColor(R.color.red));
        }

        tvTransactionId.setText(transctionId);
        tvPaymentId.setText(paymentId);
        tvAmount.setText(amount);
        tvTransactionDate.setText(date);
        tvTransactionStatus.setText(status);

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

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getPaymentDetail(final String id, final int roleid) {
        if (binding.paymenttypespinner.getSelectedItem().toString().equalsIgnoreCase("Cash")) {
            if (profilePicPath.equalsIgnoreCase("")) {
                ViewUtils.showToast("Please select image");
            } else if (NetworkUtils.isNetworkConnected()) {
                saveWallet(id, roleid, binding.editWalletAmount.getText().toString().trim(), binding.paymenttypespinner.getSelectedItem().toString(), "", "", filename, "", "", "", login.getStrPhone(), login.getStrEmail());
            } else {
                ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                    @Override
                    public void okAction() {
                        getPaymentDetail(id, roleid);
                    }
                });
            }
        }
        if (binding.paymenttypespinner.getSelectedItem().toString().equalsIgnoreCase("Cheque")) {
            if (profilePicPath.equalsIgnoreCase("")) {
                ViewUtils.showToast("Please select image");
            } else if (NetworkUtils.isNetworkConnected()) {
                saveWallet(id, roleid, binding.editWalletAmount.getText().toString().trim(), binding.paymenttypespinner.getSelectedItem().toString(), "", "", filename, "", "", "", login.getStrPhone(), login.getStrEmail());
            } else {
                ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                    @Override
                    public void okAction() {
                        getPaymentDetail(id, roleid);
                    }
                });
            }
        }
        if (binding.paymenttypespinner.getSelectedItem().toString().equalsIgnoreCase("NEFT")) {
            if (profilePicPath.equalsIgnoreCase("")) {
                ViewUtils.showToast("Please select image");
            } else if (binding.edtNftNo.getText().toString().isEmpty()) {
                binding.edtNftNo.setError(getString(R.string.error_field_required));
                requestFocus(binding.edtNftNo);
            } else if (NetworkUtils.isNetworkConnected()) {
                saveWallet(id, roleid, binding.editWalletAmount.getText().toString().trim(), binding.paymenttypespinner.getSelectedItem().toString(), "", "", filename, "", "", "", login.getStrPhone(), login.getStrEmail());
            } else {
                ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                    @Override
                    public void okAction() {
                        getPaymentDetail(id, roleid);
                    }
                });
            }
        }
        if (binding.paymenttypespinner.getSelectedItem().toString().equalsIgnoreCase("RTGS")) {
            if (profilePicPath.equalsIgnoreCase("")) {
                ViewUtils.showToast("Please select image");
            } else if (binding.edtUtrNo.getText().toString().isEmpty()) {
                binding.edtUtrNo.setError(getString(R.string.error_field_required));
                requestFocus(binding.edtUtrNo);
            } else if (NetworkUtils.isNetworkConnected()) {
                saveWallet(id, roleid, binding.editWalletAmount.getText().toString().trim(), binding.paymenttypespinner.getSelectedItem().toString(), "", "", filename, "", "", "", login.getStrPhone(), login.getStrEmail());
            } else {
                ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                    @Override
                    public void okAction() {
                        getPaymentDetail(id, roleid);
                    }
                });
            }
        }

        if (binding.paymenttypespinner.getSelectedItem().toString().equalsIgnoreCase("Online")) {

            Call<ApiResponse<PaymentGatewayModel>>
                    getPaymentGatewayModelCall = apiHelper.getPaymentGatewayModel(id, roleid,
                    binding.editWalletAmount.getText().toString(), binding.paymenttypespinner.getSelectedItem().toString().toString(), customerId);
            getPaymentGatewayModelCall.enqueue(new Callback<ApiResponse<PaymentGatewayModel>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<PaymentGatewayModel>> call,
                                       @NonNull Response<ApiResponse<PaymentGatewayModel>> response) {

                    if (response.isSuccessful()) {
                        if (response != null) {
                            if (response.body().getResponse().equalsIgnoreCase("Success")) {
                                paymentGatewayModel = response.body().getBody();
                                startPay();

                            } else {


                            }
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<PaymentGatewayModel>> call,
                                      @NonNull Throwable t) {
                    if (!call.isCanceled()) {

                        ViewUtils.showToast(t.getLocalizedMessage());
                    }
                    t.printStackTrace();
                }
            });
        }
    }

    private void AddWalletOnline(String id, int roleid, final String amount, String paymentType, String nftno, String utrno, String slipname, final String txnid, final String paymentId, final String status, String phone, String email) {
        ViewUtils.startProgressDialog(mContext);
        final Call<AddCustomerEditCustomer> getAddCustomerEditCustomerCall =
                apiHelper.getPaymentOnline(id, roleid, amount, paymentType, nftno, utrno, slipname, txnid, paymentId, status, phone, email);

        getAddCustomerEditCustomerCall.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call,
                                   Response<AddCustomerEditCustomer> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {

                    if (response != null) {

                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            showFilterDialog(txnid, paymentId, amount, date, status);
                        } else {
                            ViewUtils.showErrorDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });
                        }

                    }
                } else {
                    ViewUtils.showErrorDialog(mContext, response.body().getMessage(),
                            new DialogActionCallback() {
                                @Override
                                public void okAction() {

                                }
                            });

                }
            }

            @Override
            public void onFailure(Call<AddCustomerEditCustomer> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });

    }


    private void saveWallet(String id, int roleid, final String amount, String paymentType, String nftno, String utrno, String slipname, final String txnid, final String paymentId, final String status, String phone, String email) {

        ViewUtils.startProgressDialog(mContext);
        File reportFile = new File(profilePicPath);
        RequestBody requestFile = RequestBody.create(reportFile,
                MediaType.parse(reportFile.getAbsolutePath()));
        MultipartBody.Part profilePic = MultipartBody.Part.createFormData("",
                reportFile.getName(), requestFile);
        try {
            Logger.i(TAG, profilePic.body().contentLength() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Call<AddCustomerEditCustomer> getAddCustomerEditCustomerCall = apiHelper.getPayment(id, roleid, amount, paymentType, nftno, utrno, slipname, txnid, paymentId, status, phone, email, profilePic);

        getAddCustomerEditCustomerCall.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call,
                                   Response<AddCustomerEditCustomer> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {

                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {

                            ViewUtils.showErrorDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {


                                        }
                                    });

                        } else {
                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            Intent intent = new Intent(AddWalletActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                        }
                    }
                } else {


                }
            }

            @Override
            public void onFailure(Call<AddCustomerEditCustomer> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
        // }

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getImagePath(Uri originalUri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String path = FileUtil.getRealPathFromURI_API11to18(mContext, originalUri);
            Logger.d(TAG, "File path === " + path);
            compressFile(path);

        } else {
            String path = FileUtil.getRealPathFromURI(mContext, originalUri);
            Logger.d(TAG, "File path === " + path);
            compressFile(path);
        }
    }

    private void compressFile(String path) {
        if (path != null) {
            String ext = CommonUtils.getExt(path);
            Logger.d(TAG, "File path extension === " + ext);
            if (ext != null) {
                ImageCompressTask imageCompressTask = new ImageCompressTask(mContext, path, imageCompressTaskListener);
                mExecutorService.execute(imageCompressTask);
                Logger.d(TAG, "File path === " + path);


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
            filename = profilePicPath.substring(profilePicPath.lastIndexOf("/") + 1);
            binding.noFileChosenTextView.setText(filename);
            Logger.d(TAG, "File path === " + profilePicPath);
            //showMessage("Profile pic added successfully");

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isFromPermissionSettings) {
            checkStoragePermission();
        }

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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    void startPay() {
        builder.setAmount(paymentGatewayModel.getAmount())                          // Payment amount
                .setTxnId(paymentGatewayModel.getTransactionId())                     // Transaction ID
                .setPhone(paymentGatewayModel.getMobileNo())                   // User Phone number
                .setProductName(paymentGatewayModel.getProductinfo())                   // Product Name or description
                .setFirstName(paymentGatewayModel.getFirstname())                              // User First name
                .setEmail(paymentGatewayModel.getEmailId())              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(paymentGatewayModel.getMerchanteKey())                        // Merchant key
                .setMerchantId(paymentGatewayModel.getSaltKey());
        try {
            paymentParam = builder.build();
            paymentParam.setMerchantHash(paymentGatewayModel.getHashKey());
            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, AddWalletActivity.this, R.style.AppTheme_default, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

