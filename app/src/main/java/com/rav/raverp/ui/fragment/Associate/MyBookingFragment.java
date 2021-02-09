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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.CustomerPlotBookingListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.BookingPlotListModal;
import com.rav.raverp.data.model.api.CustomerPlotBookModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.DialogMyBookingFilterBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.MyBookingActivityDetails;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingFragment extends Fragment {
    private ApiHelper apiHelper;
    private View view;
    private RecyclerView recyclerCustomerPlotBookingDetails;
    private CustomerPlotBookingListAdapter customerPlotBookingListAdapter;
    private boolean isDialogHided;
    private Dialog filterDialog;
    private BookingPlotListModal customerPlotDetails;
    private Spinner customer_name_spinner;
    private LoginModel login;
    TextView no_records_text_view;
    int customerId;
    TextView textTotalItemCount;
    int mTotalItemCount;

    boolean state = false;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            customerPlotDetails = customerPlotBookingListAdapter.getCustomerPlotDetails().get(itemPosition);

            Intent intent = new Intent(getActivity(), MyBookingActivityDetails.class);
            intent.putExtra("detail", customerPlotDetails);
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

        view = inflater.inflate(R.layout.activity_recycler_view_cust_plot_book_details, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        recyclerCustomerPlotBookingDetails = view.findViewById(R.id.recycler_view);
        recyclerCustomerPlotBookingDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCustomerPlotBookingDetails.getRecycledViewPool().clear();
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
        String loginid = login.getStrLoginID();
        Integer roleid = login.getIntRoleID();
        Call<BookingPlotListModal> getCustomerPlotBookModelCall =
                apiHelper.getBookingPlotList(loginid, roleid);
        getCustomerPlotBookModelCall.enqueue(new Callback<BookingPlotListModal>() {
            @Override
            public void onResponse(Call<BookingPlotListModal> call,
                                   Response<BookingPlotListModal> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {

                           // List<CustomerPlotDetails> customerPlotDetailsList = response.body().getBody();
                            BookingPlotListModal bookingPlotListModal=response.body();

                            customerPlotBookingListAdapter = new CustomerPlotBookingListAdapter(getActivity(), listItemClickListener,
                                    bookingPlotListModal.getBody());
                            recyclerCustomerPlotBookingDetails.setAdapter(customerPlotBookingListAdapter);
                            mTotalItemCount = bookingPlotListModal.getBody().size();
                            setupBadge();
                        } else {
                            mTotalItemCount = 0;
                            setupBadge();
                            recyclerCustomerPlotBookingDetails.removeAllViewsInLayout();
                            if (state) {
                                ViewUtils.showErrorDialog(getContext(), response.body().getMessage(),
                                        new DialogActionCallback() {
                                            @Override
                                            public void okAction() {


                                            }
                                        });
                            }
                            state = true;

                        }
                    } else {
                        mTotalItemCount = 0;
                        setupBadge();
                        recyclerCustomerPlotBookingDetails.removeAllViewsInLayout();
                    }
                } else {
                    mTotalItemCount = 0;
                    setupBadge();
                    recyclerCustomerPlotBookingDetails.removeAllViewsInLayout();
                }
            }


            @Override
            public void onFailure(Call<BookingPlotListModal> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
                mTotalItemCount = 0;
                setupBadge();
                recyclerCustomerPlotBookingDetails.removeAllViewsInLayout();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // MenuItem search = menu.findItem(R.id.action_filter);
        MenuItem cart = menu.findItem(R.id.action_cart);
        //  MenuItemCompat.getActionView(search);
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
        final DialogMyBookingFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_my_booking_filter, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);
        customer_name_spinner = (Spinner) filterDialog.findViewById(R.id.customer_name_spinner);


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
        GetCustomer();

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.hide();
                isDialogHided = true;
                execute();


            }
        });
    }

    private void GetCustomer() {
        String loginid = login.getStrLoginID();
        Integer roleid = login.getIntRoleID();

        Call<ApiResponse<List<CustomerPlotBookModel>>> getCustomerPlotBookModelCall =
                apiHelper.getCustomerPlotBookModel(loginid, roleid);
        getCustomerPlotBookModelCall.enqueue(new Callback<ApiResponse<List<CustomerPlotBookModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CustomerPlotBookModel>>> call,
                                   final Response<ApiResponse<List<CustomerPlotBookModel>>> response) {


                if (response.isSuccessful()) {
                    final List<CustomerPlotBookModel> getCustomerList = new ArrayList<>();
                    CustomerPlotBookModel getcustomer = new CustomerPlotBookModel();
                    getcustomer.setStrName("--Select--");
                    getcustomer.setBigIntCustomerId(0);
                    getCustomerList.add(getcustomer);
                    getCustomerList.addAll(response.body().getBody());
                    ArrayAdapter<CustomerPlotBookModel> adapter = new ArrayAdapter<>(getActivity(),
                            R.layout.simple_spinner_item, getCustomerList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    customer_name_spinner.setAdapter(adapter);
                    customer_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position > 0) {
                                customerId = getCustomerList.get(position).getBigIntCustomerId();


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

}






