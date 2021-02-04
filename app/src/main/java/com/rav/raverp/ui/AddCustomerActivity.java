package com.rav.raverp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.rav.raverp.data.model.api.AddCustomerEditCustomer;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.CustomerListModel;
import com.rav.raverp.data.model.api.CustomerPlotBookModel;
import com.rav.raverp.data.model.api.GenderModel;
import com.rav.raverp.data.model.api.IsActiveStatusModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PlotBooking;
import com.rav.raverp.data.model.api.SpinnerItems;
import com.rav.raverp.databinding.ActivityAddCustomerBinding;
import com.rav.raverp.databinding.ActivityAddCustomerBindingImpl;
import com.rav.raverp.databinding.ActivityCustomerListDetailsBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerActivity extends   BaseActivity  implements ArrowBackPressed , AdapterView.OnItemSelectedListener {

    ActivityAddCustomerBinding binding;
    private ApiHelper apiHelper;
    private Spinner status_spinner;
    private Spinner gender_spinner;
    private LoginModel login;


    String id;
    int roleid;
    int GenderId, StatusValue;
    String Name, Email, ContactNo, PinCode, State, City, Address;
    int Status, Gender;
    EditText name, emailid, contactno, pincode, address;
    TextView state, city;
    private CustomerListModel customerListModel;

    int customerId=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_add_customer);
        setToolbarTitle("Customer Details");
        showBackArrow();
        setArrowBackPressed(this);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        status_spinner = (Spinner) findViewById(R.id.status_spinner);
        gender_spinner = (Spinner) findViewById(R.id.gender_spinner);
        name = (EditText) findViewById(R.id.txt_customer_name);
        contactno = (EditText) findViewById(R.id.txt_contact_no);
        emailid = (EditText) findViewById(R.id.txt_email_id);
        pincode = (EditText) findViewById(R.id.txt_pincode);
        state = (TextView) findViewById(R.id.txt_state);
        city = (TextView) findViewById(R.id.txt_city);
        address = (EditText) findViewById(R.id.txt_address);
        login = MyApplication.getLoginModel();
        id = login.getStrLoginID();
        roleid = login.getIntRoleID();


        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerListModel!=null) {
                    customerId = customerListModel.getBigIntCustomerId();
                }
                submitForm();
            }
        });
        GetIsActiveStatus();
        GetGender();
        getIntentData();
    }

    private void getIntentData() {


        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("Customerlist")) {
            customerListModel =
                    (CustomerListModel) intent.getSerializableExtra("Customerlist");
            binding.setCustomerListModel(customerListModel);
            Name = String.valueOf(customerListModel.getStrName());
            name.setText(Name);
            ContactNo = (customerListModel.getStrMobileNo());
            contactno.setText(ContactNo);
            Email = (customerListModel.getStrEmail());
            emailid.setText(Email);
            PinCode = (customerListModel.getStrPinCode());
            pincode.setText(PinCode);
            State = (customerListModel.getStrStateName());
            state.setText(State);
            City = (customerListModel.getStrCityName());
            city.setText(City);
            Address = (customerListModel.getStrAddress());
            address.setText(Address);



        }

    }

    private void GetIsActiveStatus() {

        Call<ApiResponse<List<IsActiveStatusModel>>> getIsActiveStatusModelCall =
                apiHelper.getIsActiveStatusModel();

        getIsActiveStatusModelCall.enqueue(new Callback<ApiResponse<List<IsActiveStatusModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<IsActiveStatusModel>>> call,
                                   Response<ApiResponse<List<IsActiveStatusModel>>> response) {


                if (response.isSuccessful()) {
                    final List<IsActiveStatusModel> getIsActiveList = new ArrayList<>();
                    IsActiveStatusModel getIsActive = new IsActiveStatusModel();
                    getIsActive.setText("--Select--");
                    getIsActive.setValue("0");
                    getIsActiveList.add(getIsActive);
                    getIsActiveList.addAll(response.body().getBody());
                    ArrayAdapter<IsActiveStatusModel> adapter = new ArrayAdapter<>(AddCustomerActivity.this,
                            R.layout.simple_spinner_item, getIsActiveList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    status_spinner.setAdapter(adapter);
                    if (customerListModel!=null){
                        for (int i=0;i<getIsActiveList.size();i++){
                            if (customerListModel.getIntIsDelated()==Integer.parseInt(getIsActiveList.get(i).getValue())){
                                status_spinner.setSelection(i);
                            }
                        }
                    }
                    status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                StatusValue = Integer.parseInt(getIsActiveList.get(position).getValue());

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<IsActiveStatusModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });


        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    getCity(pincode.getText().toString().trim());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


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
                    ArrayAdapter<GenderModel> adapter = new ArrayAdapter<>(AddCustomerActivity.this,
                            R.layout.simple_spinner_item, getgenderList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    gender_spinner.setAdapter(adapter);

                    if (customerListModel!=null){
                        for (int j=0;j<getgenderList.size();j++){
                            int a=getgenderList.get(j).getIntGenderId();
                            int b=customerListModel.getIntGenderId();
                            if (a==b){
                                gender_spinner.setSelection(j);

                            }

                        }
                    }

                    gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                GenderId = getgenderList.get(position).getIntGenderId();

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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

    private void submitForm() {
        if (!validateCustomerName()) {
            return;
        }


        if (!validateEmailId()) {
            return;
        }
        if (!validateContactNo()) {
            return;
        }
        if (!validatePincode()) {
            return;
        }
        if (!validateState()) {
            return;
        }

        if (!validateCity()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }
        if (!validateStatus()) {
            return;
        }
        if (!validateGender()) {
            return;
        }

        checkNetwork();
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            AddCustomer();
        } else {
            ViewUtils.showOfflineDialog(this, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void AddCustomer() {
        final Call<AddCustomerEditCustomer> getAddCustomerEditCustomerCall =
                apiHelper.getAddCustomerEditCustomerModel(id, roleid, customerId, binding.txtCustomerName.getText().toString(),
                        binding.txtEmailId.getText().toString(), binding.txtContactNo.getText().toString(),
                        binding.txtPincode.getText().toString(), binding.txtState.getText().toString(),
                        binding.txtCity.getText().toString(), binding.txtAddress.getText().toString(),
                        StatusValue, GenderId);

        getAddCustomerEditCustomerCall.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call,
                                   Response<AddCustomerEditCustomer> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Add Record Successfully...!")) {


                        ViewUtils.showSuccessDialog(AddCustomerActivity.this, response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {

                                        Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }else if (response.body().getResponse().equalsIgnoreCase("Update successfully.")) {


                        ViewUtils.showSuccessDialog(AddCustomerActivity.this, response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {

                                        Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }
                    else {

                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {
                            ViewUtils.showErrorDialog(AddCustomerActivity.this, response.body().getMessage(),
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
            public void onFailure(Call<AddCustomerEditCustomer> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });


        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    getCity(pincode.getText().toString().trim());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    private boolean validateCustomerName() {
        String CustomerName = binding.txtCustomerName.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(CustomerName)) {
            binding.txtCustomerName.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtCustomerName);
            return false;
        } else {
            binding.txtCustomerName.setError(null);
        }
        return true;
    }

    private boolean validateEmailId() {
        String email = binding.txtEmailId.getText().toString().trim();

        if (CommonUtils.isNullOrEmpty(email)) {
            binding.txtEmailId.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtEmailId);
            return false;

        } else if (!CommonUtils.isValidEmail(email)) {
            binding.txtEmailId.setError("Please Enter Valid Email Id.");
            requestFocus(binding.txtEmailId);
            return false;
        } else {
        }
        return true;
    }

    private boolean validateContactNo() {
        String phone = binding.txtContactNo.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(phone)) {
            binding.txtContactNo.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtContactNo);
            return false;

        } else if (!CommonUtils.isValidMobile(phone)) {
            binding.txtContactNo.setError("Please Enter Valid Contact Number.");
            requestFocus(binding.txtContactNo);
            return false;
        } else {
        }
        return true;
    }

    private boolean validatePincode() {
        String pincode = binding.txtPincode.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(pincode)) {
            binding.txtPincode.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtPincode);
            return false;

        } else if (!CommonUtils.isValidPinCode(pincode)) {
            binding.txtPincode.setError("Please Enter Valid PinCode .");
            requestFocus(binding.txtPincode);
            return false;
        } else {
        }
        return true;
    }

    private boolean validateState() {
        String state = binding.txtState.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(state)) {
            binding.txtState.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtState);
            return false;
        } else {
            binding.txtState.setError(null);
        }
        return true;
    }

    private boolean validateCity() {
        String city = binding.txtCity.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(city)) {
            binding.txtCity.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtCity);
            return false;
        } else {
            binding.txtCity.setError(null);
        }
        return true;
    }

    private boolean validateAddress() {
        String address = binding.txtAddress.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(address)) {
            binding.txtAddress.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtAddress);
            return false;
        } else {
            binding.txtAddress.setError(null);
        }
        return true;
    }

    private boolean validateStatus() {
        String statusspinner = binding.statusSpinner.getSelectedItem().toString().trim();
        if (statusspinner.equals("--Select--")) {
            showMessage("Please Select Status .");
            return false;
        }
        return true;
    }

    private boolean validateGender() {
        String genderspinner = binding.genderSpinner.getSelectedItem().toString().trim();
        if (genderspinner.equals("--Select--")) {
            showMessage("Please Select Gender .");
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    void getCity(String pincode) {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        String url = "https://api.postalpincode.in/pincode/" + pincode;
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
                                    city.setText(jsonObject1.getString("District"));
                                    state.setText(jsonObject1.getString("State"));
                                } else {
                                    city.setText("");
                                    state.setText("");
                                    Toast.makeText(AddCustomerActivity.this, jsonObject.getString("Status"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mProgressDialog.dismiss();
                                new AlertDialog.Builder(AddCustomerActivity.this)
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
                        new AlertDialog.Builder(AddCustomerActivity.this)
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


}
