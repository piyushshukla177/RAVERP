package com.rav.raverp.ui.fragment.Customer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.CustomerPlotBookingAccountDetailsAdapter;
import com.rav.raverp.data.adapter.CustomerPlotBookingDetailsAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.CustomerPlotBookingAccountDetailsModel;
import com.rav.raverp.data.model.api.CustomerPlotDetailsModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.CustomerPlotBookingAccountActivityDetails;
import com.rav.raverp.ui.CustomerPlotBookingActivityDetails;
import com.rav.raverp.ui.MainActivity;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlotBookingAccountDetailsFragment extends Fragment {
    private ApiHelper apiHelper, apiHelperlocal;
    private View view;
    private RecyclerView recyclerCustomerPloatList;
    private CustomerPlotBookingAccountDetailsAdapter customerPlotBookingAccountDetailsAdapter;
    private boolean isDialogHided;
    private Dialog filterDialog;
    private Spinner project_name_spinner, block_name_spinner;
    String ProjectId,BlockId;
    TextView  no_records_text_view;
    TextView txt1,txt2;
    private LoginModel login;
    TextView textTotalItemCount;
    int mTotalItemCount;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            CustomerPlotBookingAccountDetailsModel customerPlotDetailsModel =
                    customerPlotBookingAccountDetailsAdapter.getCustomerPlotBookingAccountDetailsModel().get(itemPosition);

            Intent intent = new Intent(getActivity(), CustomerPlotBookingAccountActivityDetails.class);
            intent.putExtra("customerplotdetailsaccountdeta", customerPlotDetailsModel);
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
        // Inflate the layout for this fragment
        MainActivity.toolbar.setTitle("Plot Booking Account Details");
        view = inflater.inflate(R.layout.activity_recycler_view_customer_plot_account_details, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        recyclerCustomerPloatList = view.findViewById(R.id.recycler_view);
        txt1=(TextView)view.findViewById(R.id.txt1);
        txt2=(TextView)view.findViewById(R.id.txt2);
        no_records_text_view=(TextView)view.findViewById(R.id.no_records_text_view);
        recyclerCustomerPloatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCustomerPloatList.getRecycledViewPool().clear();
        login = MyApplication.getLoginModel();

        if (isDialogHided) {
            isDialogHided = false;


            filterDialog.dismiss();
        }
        checkNetwork();



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
        String id=login.getStrLoginID();
        Call<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>> getCustomerPlotBookingAccountDetailsModelCall=
                apiHelper.getCustomerPlotBookingAccountDetailsModel(id);
        getCustomerPlotBookingAccountDetailsModelCall.enqueue(new Callback<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>> call,
                                   Response<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            List<CustomerPlotBookingAccountDetailsModel> customerPlotDetailsModelList = response.body().getBody();

                            customerPlotBookingAccountDetailsAdapter = new CustomerPlotBookingAccountDetailsAdapter(getActivity(), listItemClickListener,
                                    response.body().getBody());
                            recyclerCustomerPloatList.setAdapter(customerPlotBookingAccountDetailsAdapter);
                            mTotalItemCount=customerPlotDetailsModelList.size();
                            setupBadge();

                        }else{
                            mTotalItemCount=0;
                            setupBadge();

                        }
                    }
                    else{
                        mTotalItemCount=0;
                        setupBadge();

                    }
                }
                else{
                    mTotalItemCount=0;
                    setupBadge();

                }

            }

            @Override
            public void onFailure(Call<ApiResponse<List<CustomerPlotBookingAccountDetailsModel>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
                mTotalItemCount=0;

                setupBadge();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);

        setupBadge();

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





}



