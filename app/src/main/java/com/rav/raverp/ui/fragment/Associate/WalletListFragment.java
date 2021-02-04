package com.rav.raverp.ui.fragment.Associate;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.FollowUPLeadListAdapter;
import com.rav.raverp.data.adapter.LeadListAdapter;
import com.rav.raverp.data.adapter.SiteVisitRequestAdapter;
import com.rav.raverp.data.adapter.WalletListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.local.prefs.PrefsHelper;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.FollowUpListModel;
import com.rav.raverp.data.model.api.GetWalletListModel;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.data.model.api.WalletAmountListModel;
import com.rav.raverp.databinding.DialogFollowUpLeadListFilterBinding;
import com.rav.raverp.databinding.DialogLeadListFilterBinding;
import com.rav.raverp.databinding.DialogSiteVisitRequestStatusFilterBinding;
import com.rav.raverp.databinding.DialogWalletListFilterBinding;
import com.rav.raverp.databinding.ItemFollowUpLeadListBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.AddWalletActivity;
import com.rav.raverp.ui.FollowUPLeadListActivityDetails;
import com.rav.raverp.ui.LeadListActivityDetails;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.ui.PlotAvailabilityActivityDetails;
import com.rav.raverp.ui.WalletListDetails;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletListFragment extends Fragment {
    private ApiHelper apiHelper;
    private View view;
    private RecyclerView recyclerWalletList;
    private WalletListAdapter walletListAdapter;
    private boolean isDialogHided;
    private Dialog filterDialog;
    public static List<GetWalletListModel> getWalletListModelList;
    String payment;
    TextView txt_R_Platinum;
    private LoginModel login;
    TextView textTotalItemCount;
    int mTotalItemCount;
    String loginid;
    Integer role;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            GetWalletListModel getWalletListModel =
                    walletListAdapter.getGetWalletListModel().get(itemPosition);

            Intent intent = new Intent(getActivity(), WalletListDetails.class);
            intent.putExtra("walletdata", getWalletListModel);
            startActivity(intent);

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toolbar.setTitle("Wallet");
        view = inflater.inflate(R.layout.activity_recycler_view_wallet, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        recyclerWalletList = view.findViewById(R.id.recycler_view);
        txt_R_Platinum = view.findViewById(R.id.txt_R_Platinum);
        recyclerWalletList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerWalletList.getRecycledViewPool().clear();
        login = MyApplication.getLoginModel();
        loginid = login.getStrLoginID();
        role = login.getIntRoleID();

        checkNetwork();
        GetWalletAmount();


        return view;
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            execute();
        } else {
            ViewUtils.showOfflineDialog(getContext(), new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void execute() {

        ViewUtils.startProgressDialog(getActivity());
        String PaymetType = "0";

        Call<ApiResponse<List<GetWalletListModel>>> getFollowUpListModelCall =
                apiHelper.getGetWalletListModel(PaymetType, loginid, role);

        getFollowUpListModelCall.enqueue(new Callback<ApiResponse<List<GetWalletListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetWalletListModel>>> call,
                                   Response<ApiResponse<List<GetWalletListModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            getWalletListModelList = response.body().getBody();

                            walletListAdapter = new WalletListAdapter(getActivity(), listItemClickListener,
                                    response.body().getBody());
                            recyclerWalletList.setAdapter(walletListAdapter);
                            mTotalItemCount = getWalletListModelList.size();
                            setupBadge();

                        } else {
                            mTotalItemCount = 0;
                            setupBadge();
                        }
                    } else {
                        mTotalItemCount = 0;
                        setupBadge();
                    }
                } else {
                    mTotalItemCount = 0;
                    setupBadge();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetWalletListModel>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
                mTotalItemCount = 0;
                setupBadge();
            }
        });
    }

    private void GetWalletAmount() {

        Call<ApiResponse<List<WalletAmountListModel>>> getWalletAmountListModelCall =
                apiHelper.getWalletAmountListModel(loginid, role);
        getWalletAmountListModelCall.enqueue(new Callback<ApiResponse<List<WalletAmountListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<WalletAmountListModel>>> call,
                                   Response<ApiResponse<List<WalletAmountListModel>>> response) {


                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {

                            // Double amount=response.body().getBody().get(0).getRPlantinumWalletAmounts();
                            if (response.body().getBody().get(0).getRPlantinumWalletAmounts() != null)
                                txt_R_Platinum.setText(response.body().getBody().get(0).getRPlantinumWalletAmounts());

                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<List<WalletAmountListModel>>> call, Throwable t) {
                if (!call.isCanceled()) {


                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.findItem(R.id.action_filter);
        MenuItem cart = menu.findItem(R.id.action_cart);
        MenuItemCompat.getActionView(search);
        View actionView = MenuItemCompat.getActionView(cart);
        textTotalItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_filter) {
            if (isDialogHided) {
                filterDialog.show();
                isDialogHided = false;
            } else {
                showFilterDialog();

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textTotalItemCount != null) {
            if (mTotalItemCount == 0) {
                if (textTotalItemCount.getVisibility() != View.GONE) {
                    textTotalItemCount.setVisibility(View.GONE);
                }
            } else {
                textTotalItemCount.setText(String.valueOf(Math.min(mTotalItemCount, 99999)));
                if (textTotalItemCount.getVisibility() != View.VISIBLE) {
                    textTotalItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void showFilterDialog() {
        filterDialog = new Dialog(getContext());
        final DialogWalletListFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_wallet_list_filter, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);


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
        final EditText paymentno = filterDialog.findViewById(R.id.edit_payment_no);

        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = paymentno.getText().toString();
                filterDialog.dismiss();
                isDialogHided = true;
                gopayment();


            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                Intent intent = new Intent(getContext(), AddWalletActivity.class);
                startActivity(intent);
            }
        });
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterDialog.dismiss();
                execute();

            }
        });
    }

    private void gopayment() {

        ViewUtils.startProgressDialog(getActivity());


        Call<ApiResponse<List<GetWalletListModel>>> getFollowUpListModelCall =
                apiHelper.getGetWalletListModel(payment, loginid, role);

        getFollowUpListModelCall.enqueue(new Callback<ApiResponse<List<GetWalletListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetWalletListModel>>> call,
                                   Response<ApiResponse<List<GetWalletListModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            getWalletListModelList = response.body().getBody();
                            walletListAdapter = new WalletListAdapter(getActivity(), listItemClickListener, getWalletListModelList);
                            recyclerWalletList.setAdapter(walletListAdapter);

                            mTotalItemCount = getWalletListModelList.size();
                            setupBadge();
                        } else {
                            mTotalItemCount = 0;
                            setupBadge();
                            getWalletListModelList.clear();
                            walletListAdapter.notifyDataSetChanged();
                            ViewUtils.showErrorDialog(getContext(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });


                        }
                    } else {
                        mTotalItemCount = 0;
                        setupBadge();
                    }
                } else {
                    mTotalItemCount = 0;
                    setupBadge();
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<List<GetWalletListModel>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
                mTotalItemCount = 0;
                setupBadge();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}









