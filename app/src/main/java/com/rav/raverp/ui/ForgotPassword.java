package com.rav.raverp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.ForgotApiResponse;
import com.rav.raverp.data.model.api.ForgotpasswordModel;
import com.rav.raverp.data.model.api.ResetPasswordModel;
import com.rav.raverp.databinding.ActivityForgotPasswordBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    private static final String TAG = ForgotPassword.class.getSimpleName();
    private Context mContext = ForgotPassword.this;
    private ActivityForgotPasswordBinding binding;
    private ApiHelper apiHelper;
    String ForgotLoginId;
    Button submit;
    CountDownTimer countDownTimer;
    String otp, number;
    String Userid;
    String NewPassword, ConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        submit = (Button) findViewById(R.id.btnsubmit);

        binding.forgotPasswordId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        binding.resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetwork();
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otps = binding.otp.getText().toString();
                if (!CommonUtils.isNullOrEmpty(otps)) {
                    if (otp.equalsIgnoreCase(otps)) {
                        binding.l2.setVisibility(View.GONE);
                        binding.l3.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(mContext, "Please Enter Correct Otp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSubmitFrom();
            }
        });
    }

    private void ResetPassword() {

        Call<ResetPasswordModel> getResetPasswordModelCall = apiHelper.getResetPasswordModel(Userid, binding.newPassword.getText().toString());
        getResetPasswordModelCall.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ResetPasswordModel> call,
                                   @NonNull Response<ResetPasswordModel> response) {

                if (response.isSuccessful()) {
                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                            Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                        } else {


                        }
                    }

                } else {


                }
            }

            @Override
            public void onFailure(@NonNull Call<ResetPasswordModel> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });


    }


    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            ForgotExecute();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void ForgotExecute() {

        //show progress bar
        ViewUtils.startProgressDialog(mContext);
        Call<ForgotApiResponse> getForgotpasswordModelCall = apiHelper.getForgotpasswordModel(binding.forgotPasswordLoginId.getText().toString());
        getForgotpasswordModelCall.enqueue(new Callback<ForgotApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForgotApiResponse> call,
                                   @NonNull Response<ForgotApiResponse> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            ForgotpasswordModel forgotpasswordModel = response.body().getBody().get(0);
                            otp = forgotpasswordModel.getOTP();
                            Userid = forgotpasswordModel.getUserId();

                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            binding.l1.setVisibility(View.GONE);
                                            binding.l2.setVisibility(View.VISIBLE);
                                            StartCountDown();
                                            countDownTimer.start();
                                        }
                                    });

                        } else {

                            Toast.makeText(mContext, "Please Enter Valid Login Id", Toast.LENGTH_SHORT).show();


                        }
                    }

                } else {


                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotApiResponse> call,
                                  @NonNull Throwable t) {
                ViewUtils.endProgressDialog();
                if (!call.isCanceled()) {
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });


    }


    private void StartCountDown() {
        countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                String text = String.format(Locale.getDefault(), "%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                binding.timer.setText(text);

                // binding.timer.setText(millisUntilFinished/1000+" ");
                binding.resendotp.setFocusable(false);
                binding.resendotp.setEnabled(false);
                ;
                binding.resendotp.setClickable(false);

            }

            @Override
            public void onFinish() {
                otp = "";
                binding.timer.setText("");
                binding.resendotp.setFocusable(true);
                binding.resendotp.setEnabled(true);
                binding.resendotp.setClickable(true);
            }
        };

    }


    private void submitForm() {
        if (!validateForgotLoginId()) {
            return;
        }


        checkNetwork();
    }

    private void resetSubmitFrom() {
        if (!validateNewPassword()) {
            return;
        }
        if (!validateConfirmPassword()) {
            return;


        }
        ResetPassword();
    }

    private boolean validateNewPassword() {
        NewPassword = binding.newPassword.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(NewPassword)) {
            binding.newPassword.setError("Please Enter New Password.");
            requestFocus(binding.newPassword);
            return false;

        } else if (!CommonUtils.isPasswordValid(NewPassword)) {
            binding.newPassword.setError(getString(R.string.invalid_password));
            requestFocus(binding.newPassword);
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword() {
        ConfirmPassword = binding.confirmPassword.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(ConfirmPassword)) {
            binding.confirmPassword.setError("Please Retype New Password");
            requestFocus(binding.confirmPassword);
            return false;
        } else if (!NewPassword.equalsIgnoreCase(ConfirmPassword)) {
            binding.confirmPassword.setError(getString(R.string.error_password_mismatching));
            requestFocus(binding.confirmPassword);
            return false;
        }
        return true;
    }


    private boolean validateForgotLoginId() {
        ForgotLoginId = binding.forgotPasswordLoginId.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(ForgotLoginId)) {
            binding.forgotPasswordLoginId.setError("Please Enter your Login Id.");
            requestFocus(binding.forgotPasswordLoginId);
            return false;
        } else

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


}

