package com.rav.raverp.ui.fragment.Associate;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.adapter.SiteVisitRequestStatusListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.SiteVisitRequestName;
import com.rav.raverp.data.model.api.SiteVisitRequestStatusModel;
import com.rav.raverp.databinding.DialogSiteVisitRequestStatusFilterBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.SiteVisitRequestStatusDetails;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteVisitRequestStatusFragment extends Fragment {
    private ApiHelper apiHelper;
    private View view;
    private RecyclerView recyclerSiteVisitRequestStatus;
    private SiteVisitRequestStatusListAdapter siteVisitRequestStatusAdapter;
    private boolean isDialogHided;
    private Dialog filterDialog;
    private SiteVisitRequestStatusModel siteVisitRequestStatusModel;
    private List<SiteVisitRequestStatusModel> siteVisitRequestStatusModels;

    ArrayAdapter<SiteVisitRequestName> adapter;
    String RequestName;
    String MobileNo;
    TextView textTotalItemCount;
    int mTotalItemCount;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            siteVisitRequestStatusModel =
                    siteVisitRequestStatusAdapter.getSiteVisitRequestStatusModel().get(itemPosition);

            Intent intent = new Intent(getActivity(), SiteVisitRequestStatusDetails.class);
            intent.putExtra("sitevisitlist", siteVisitRequestStatusModel);
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

        view = inflater.inflate(R.layout.activity_recycler_view_site_visit_request_status, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        recyclerSiteVisitRequestStatus = view.findViewById(R.id.recycler_view);
        recyclerSiteVisitRequestStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerSiteVisitRequestStatus.getRecycledViewPool().clear();

        if (isDialogHided) {
            isDialogHided = false;

            filterDialog.dismiss();
        }

        GetSiteVisitRequestName();


        return view;
    }

    private void GetSiteVisitRequestName() {

        ViewUtils.startProgressDialog(getActivity());

        Call<ApiResponse<List<SiteVisitRequestName>>> getSiteVisitRequestName =
                apiHelper.getSiteVisitRequestName();

        getSiteVisitRequestName.enqueue(new Callback<ApiResponse<List<SiteVisitRequestName>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SiteVisitRequestName>>> call,
                                   Response<ApiResponse<List<SiteVisitRequestName>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            List<SiteVisitRequestName> siteVisitRequestNameArrayList = new ArrayList<>();
                            //  SiteRequestName=siteVisitRequestNames.get(0).getStrRequesterName();

                            SiteVisitRequestName siteVisitRequestName = new SiteVisitRequestName();
                            siteVisitRequestName.setStrRequesterName("--Select Requester Name--");
                            siteVisitRequestNameArrayList.add(siteVisitRequestName);
                            siteVisitRequestNameArrayList.addAll(response.body().getBody());
                            adapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.simple_spinner_item, siteVisitRequestNameArrayList);
                            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<SiteVisitRequestName>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
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

        if (RequestName.equalsIgnoreCase("--Select Requester Name--")){
            RequestName="";
        }

        Call<ApiResponse<List<SiteVisitRequestStatusModel>>> getSiteVisitRequestStatusModelCall =
                apiHelper.getSiteVisitRequestStatusModel(RequestName,MobileNo);

        getSiteVisitRequestStatusModelCall.enqueue(new Callback<ApiResponse<List<SiteVisitRequestStatusModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SiteVisitRequestStatusModel>>> call,
                                   Response<ApiResponse<List<SiteVisitRequestStatusModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            List<SiteVisitRequestStatusModel> siteVisitRequestStatusModels = response.body().getBody();
                            siteVisitRequestStatusAdapter = new SiteVisitRequestStatusListAdapter(getActivity(), listItemClickListener, siteVisitRequestStatusModels);
                            recyclerSiteVisitRequestStatus.setAdapter(siteVisitRequestStatusAdapter);
                            mTotalItemCount=siteVisitRequestStatusModels.size();
                            setupBadge();


                        } else {
                            recyclerSiteVisitRequestStatus.removeAllViewsInLayout();
                            mTotalItemCount = 0;
                            setupBadge();
                            ViewUtils.showErrorDialog(getContext(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {


                                        }
                                    });


                        }

                    }
                    else {
                        mTotalItemCount = 0;
                        setupBadge();
                    }
                } else {
                    mTotalItemCount = 0;
                    setupBadge();
                }
            }


            @Override
            public void onFailure(Call<ApiResponse<List<SiteVisitRequestStatusModel>>> call, Throwable t) {
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

    private void showFilterDialog()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   {
        filterDialog = new Dialog(getContext());
        final DialogSiteVisitRequestStatusFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_site_visit_request_status_filter, null, false);
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

        binding.requestNameSpinner.setAdapter(adapter);
        binding.requestNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RequestName = binding.requestNameSpinner.getSelectedItem().toString();
                MobileNo=binding.editMobileNo.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnGetVisitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                isDialogHided = true;
                checkNetwork();

            }
        });
    }
}






