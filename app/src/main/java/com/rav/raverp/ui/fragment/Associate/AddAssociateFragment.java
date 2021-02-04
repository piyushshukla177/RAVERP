package com.rav.raverp.ui.fragment.Associate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

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
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.AddAssociateModal;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.GenderModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddAssociateFragment extends Fragment {

    private ApiHelper apiHelper;
    View view;
    private Spinner gender_spinner;
    TextView txt_state, txt_city;
    EditText txt_pincode, etDob;
    Button btnSubmit;

    RadioGroup rgPosition, rgRelation;
    RadioButton rbLeft, rbRight, rbDo, rbSo, rbHo;
    AppCompatEditText txt_customer_name, txt_contact_no, txt_email_id, acetPassword, acetConfirmPassword, txt_address, acetFatherOf;

    String leg = "", relation = "";
    private LoginModel login;
    String id;
    int roleId;

    private DatePicker datepicker;

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ViewUtils.startProgressDialog(getActivity());
        final Call<AddAssociateModal> getAddCustomerEditCustomerCall = apiHelper.getAssociate(id, id, txt_customer_name.getText().toString().trim(), leg, gender_spinner.getSelectedItem().toString().trim(), txt_contact_no.getText().toString().trim(), txt_email_id.getText().toString().trim(), etDob.getText().toString().trim(), acetPassword.getText().toString().trim(), relation, acetFatherOf.getText().toString().trim(), txt_pincode.getText().toString().trim(), txt_state.getText().toString().trim(), txt_city.getText().toString().trim(), txt_address.getText().toString().trim());

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

}