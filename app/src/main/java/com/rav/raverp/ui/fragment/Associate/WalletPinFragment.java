package com.rav.raverp.ui.fragment.Associate;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.goodiebag.pinview.Pinview;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.AddCustomerEditCustomer;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.DialogValidateForgetWalletPinBinding;
import com.rav.raverp.databinding.DialogWalletLoginOtpBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletPinFragment extends Fragment {

    private View view;
    Pinview pinview, pinView1, pinView2;
    Button btn_set_pin, btn_verify;
    String msg = "";
    private ApiHelper apiHelper;
    private LoginModel login;
    CardView card1, card2;

    String status = "false";
    TextView tvForgotPin;
    Button btnSendOtp;
    String loginId;
    int roleId;
    TextView tvTimer, tvResend;
    CountDownTimer countDownTimer;

    public WalletPinFragment() {
    }

    public WalletPinFragment(String status) {
        this.status = status;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.toolbar.setTitle("Wallet");
        view        = inflater.inflate(R.layout.walletpin, container, false);
        pinview     = (Pinview) view.findViewById(R.id.pinview);
        pinView1    = (Pinview) view.findViewById(R.id.pinview1);
        pinView2    = (Pinview) view.findViewById(R.id.pinview2);
        card1       =  (CardView) view.findViewById(R.id.card1);
        card2       = (CardView) view.findViewById(R.id.card2);
        btn_verify  = (Button) view.findViewById(R.id.btn_verify);
        btn_set_pin = (Button) view.findViewById(R.id.btn_set_pin);
        btnSendOtp  = (Button) view.findViewById(R.id.btnSendOtp);
        tvForgotPin = view.findViewById(R.id.tvForgotPin);
        apiHelper   = ApiClient.getClient().create(ApiHelper.class);
        login       = MyApplication.getLoginModel();
        loginId     = login.getStrLoginID();
        roleId      = login.getIntRoleID();


        if (status.equalsIgnoreCase("true")) {
            card2.setVisibility(View.VISIBLE);
            card1.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase("false")) {
            card2.setVisibility(View.GONE);
            card1.setVisibility(View.VISIBLE);
        }


        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                pinView1.requestFocus();
            }
        });


        btn_set_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetPin();
            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLoginPin();
            }
        });

        tvForgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSendOtp.setVisibility(View.VISIBLE);
            }
        });

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgetOtp();
            }
        });
        return view;
    }

    private void GenerateWalletPin() {
        String Pinvew = pinview.getValue().toString();
        ViewUtils.startProgressDialog(getActivity());
        Call<ApiResponse> getGenerateAssociateWalletPinsCall =
                apiHelper.getGenerateAssociateWalletPins(loginId, Pinvew, roleId);

        getGenerateAssociateWalletPinsCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            card1.setVisibility(View.GONE);
                            card2.setVisibility(View.VISIBLE);

                        } else if (response.body().getResponse().equalsIgnoreCase("Failure")) {
                            card1.setVisibility(View.VISIBLE);
                            card2.setVisibility(View.GONE);

                        } else if (response.body().getResponse().equalsIgnoreCase("Exists")) {
                            card1.setVisibility(View.GONE);
                            card2.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    private void LoginWalletPin() {

        String PinView2 = pinView2.getValue().toString();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ViewUtils.startProgressDialog(getActivity());

        Call<ApiResponse> getLoginAssociateWalletCall =
                apiHelper.getLoginAssociateWallet(loginId, PinView2, roleId);

        getLoginAssociateWalletCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call,
                                   Response<ApiResponse> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                           // showWalletLoginOtp();
                            Fragment fragment = new WalletListFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (response.body().getResponse().equalsIgnoreCase("Failure")) {
                            Toast.makeText(getContext(), "Please Enter Valid Pin No. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    void sendForgetOtp() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ViewUtils.startProgressDialog(getActivity());
        Call<AddCustomerEditCustomer> sendOtp = apiHelper.getForgotWalletPin(loginId, roleId);
        sendOtp.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call, Response<AddCustomerEditCustomer> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    AddCustomerEditCustomer addCustomerEditCustomer = response.body();
                    if (addCustomerEditCustomer.getResponse().equalsIgnoreCase("Success")) {
                        ViewUtils.showSuccessDialog(getContext(), addCustomerEditCustomer.getMessage(), new DialogActionCallback() {
                            @Override
                            public void okAction() {
                                showFilterDialogValidateOtp();
                                countDownTimer = new CountDownTimer(120000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        tvResend.setVisibility(View.GONE);
                                        tvTimer.setVisibility(View.VISIBLE);
                                        tvTimer.setText("" + String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    }

                                    public void onFinish() {
                                        tvResend.setVisibility(View.VISIBLE);
                                        tvTimer.setVisibility(View.GONE);
                                    }
                                }.start();
                            }
                        });
                    } else {
                        ViewUtils.showErrorDialog(getContext(), addCustomerEditCustomer.getMessage(), new DialogActionCallback() {
                            @Override
                            public void okAction() {
                            }
                        });
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
    }

    private void showFilterDialogValidateOtp() {
        final Dialog filterDialog = new Dialog(getActivity());
        final DialogValidateForgetWalletPinBinding binding = DataBindingUtil.inflate(LayoutInflater.from((getActivity())),
                R.layout.dialog_validate_forget_wallet_pin, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(false);
        filterDialog.setCanceledOnTouchOutside(false);

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 70);
        Window window = filterDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(inset);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        ImageView imgcross = filterDialog.findViewById(R.id.imgcross);
        final EditText etOtp = filterDialog.findViewById(R.id.etOtp);
        Button btnsubmit = filterDialog.findViewById(R.id.btnsubmit);
        tvTimer = filterDialog.findViewById(R.id.tvTimer);
        tvResend = filterDialog.findViewById(R.id.tvResend);


        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgetOtp();
            }
        });

        imgcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter Otp");
                } else if (NetworkUtils.isNetworkConnected()) {
                    verifyOtp(filterDialog, etOtp.getText().toString().trim());
                } else {
                    ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            // checkNetwork();
                        }
                    });
                }
            }
        });

        filterDialog.show();
    }

    private void showWalletLoginOtp() {
        final Dialog dialog = new Dialog(getActivity());
        DialogWalletLoginOtpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.dialog_wallet_login_otp, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable insetDrawable = new InsetDrawable(colorDrawable, 70);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(insetDrawable);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        ImageView imgCross = dialog.findViewById(R.id.imgcross);
        final EditText etOtp = dialog.findViewById(R.id.etOtp);
        Button btnSubmit = dialog.findViewById(R.id.btnsubmit);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter Otp");
                } else if (NetworkUtils.isNetworkConnected()) {
                    verifyWalletLoginOtp(dialog, etOtp.getText().toString().trim());
                } else {
                    ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            // checkNetwork();
                        }
                    });

                }
            }
        });
        dialog.show();
    }

    private void verifyWalletLoginOtp(final Dialog dialog, String otp) {
        ViewUtils.startProgressDialog(getActivity());
        final Call<AddCustomerEditCustomer> validateOtp =
                apiHelper.verifyLoginWalletOtp(loginId, otp, roleId);
        validateOtp.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call,
                                   Response<AddCustomerEditCustomer> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        dialog.dismiss();
                        Fragment fragment = new WalletListFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homepage, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
/*
                        ViewUtils.showSuccessDialog(getActivity(), response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {
                                        card2.setVisibility(View.GONE);
                                        card1.setVisibility(View.VISIBLE);
                                        btnSendOtp.setVisibility(View.GONE);
                                    }
                                });
*/
                    } else {
                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {
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
            public void onFailure(Call<AddCustomerEditCustomer> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    private void verifyOtp(final Dialog dialog, String otp) {
        ViewUtils.startProgressDialog(getActivity());
        final Call<AddCustomerEditCustomer> validateOtp =
                apiHelper.validateWalletPin(loginId, roleId, otp);
        validateOtp.enqueue(new Callback<AddCustomerEditCustomer>() {
            @Override
            public void onResponse(Call<AddCustomerEditCustomer> call,
                                   Response<AddCustomerEditCustomer> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        dialog.dismiss();
                        ViewUtils.showSuccessDialog(getActivity(), response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {
                                        card2.setVisibility(View.GONE);
                                        card1.setVisibility(View.VISIBLE);
                                        btnSendOtp.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {
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
            public void onFailure(Call<AddCustomerEditCustomer> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });


    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            GenerateWalletPin();
            // LoginWalletPin();
        } else {
            ViewUtils.showOfflineDialog(getContext(), new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void SetPin() {
        if (!validatepin()) {
            return;
        }
        checkNetwork();
    }

    private void SetLoginPin() {
        if (!validateloginpin()) {
            return;
        }
        LoginWalletPin();
    }

    private boolean validatepin() {
        if (TextUtils.isEmpty(pinview.getValue().toString().trim())) {
            pinview.requestFocus();
            Toast.makeText(getActivity(), "Please Enter Wallet Pins.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pinview.getValue().length() < 4) {
            pinview.requestFocus();
            Toast.makeText(getActivity(), "Your Wallet Pin must be 4 numeric value", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(pinView1.getValue().toString().trim())) {
            pinView1.requestFocus();
            Toast.makeText(getActivity(), "" + getString(R.string.confirm_pin), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pinview.getValue().toString().equalsIgnoreCase(pinView1.getValue().toString())) {
            pinView1.requestFocus();
            Toast.makeText(getActivity(), "" + getString(R.string.error_pin_mismatching), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateloginpin() {
        if (TextUtils.isEmpty(pinView2.getValue().toString().trim())) {
            pinView2.requestFocus();
            Toast.makeText(getActivity(), "Please Enter Your Pin No.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
