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
import android.widget.EditText;
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
import com.rav.raverp.data.adapter.LeadListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.databinding.DialogLeadListFilterBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.LeadListActivityDetails;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadListFragment extends Fragment {
    private ApiHelper apiHelper;
    private View view;
    private RecyclerView recyclerLeadList;
    private LeadListAdapter leadListAdapter;
    private boolean isDialogHided;
    private Dialog filterDialog;
    private  LeadListModel listModel;
    private List<LeadListModel> listModelList;

    TextView textTotalItemCount;
    int mTotalItemCount;
    String RequestName;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            LeadListModel leadListModel =
                    leadListAdapter.getLeadListModel().get(itemPosition);

            Intent intent = new Intent(getActivity(), LeadListActivityDetails.class);
            intent.putExtra("leadlist", leadListModel);
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

        view = inflater.inflate(R.layout.activity_recycler_view_lead_list, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        recyclerLeadList = view.findViewById(R.id.recycler_view);
        recyclerLeadList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLeadList.getRecycledViewPool().clear();

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

            Call<ApiResponse<List<LeadListModel>>> getLeadListModelCall =
                    apiHelper.getLeadListModel();

            getLeadListModelCall.enqueue(new Callback<ApiResponse<List<LeadListModel>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<LeadListModel>>> call,
                                       Response<ApiResponse<List<LeadListModel>>> response) {

                    ViewUtils.endProgressDialog();

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse().equalsIgnoreCase("Success")) {
                                listModelList = response.body().getBody();


                                leadListAdapter = new LeadListAdapter(getActivity(), listItemClickListener,
                                        listModelList);
                                recyclerLeadList.setAdapter(leadListAdapter);
                                mTotalItemCount = listModelList.size();
                                setupBadge();
                            }
                }else{
                    mTotalItemCount=0;
                    setupBadge();
                }
            }else{
            mTotalItemCount=0;
            setupBadge();
        }
    }

                @Override
                public void onFailure(Call<ApiResponse<List<LeadListModel>>> call, Throwable t) {
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
        final DialogLeadListFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_lead_list_filter, null, false);
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

        final EditText requestname=filterDialog.findViewById(R.id.edit_requester_name);
        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestName=requestname.getText().toString();

                filterDialog.hide();
                isDialogHided = true;

                GetGoLadlist();


            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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


    private void GetGoLadlist() {

        ViewUtils.startProgressDialog(getActivity());

        Call<ApiResponse<List<LeadListModel>>> getLeadListModelCall =
                apiHelper.getGoLeadListModel(RequestName);
        getLeadListModelCall.enqueue(new Callback<ApiResponse<List<LeadListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<LeadListModel>>> call,
                                   Response<ApiResponse<List<LeadListModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            listModelList = response.body().getBody();
                            leadListAdapter=new LeadListAdapter(getActivity(),listItemClickListener,listModelList);
                            recyclerLeadList.setAdapter(leadListAdapter);
                            mTotalItemCount=listModelList.size();
                            setupBadge();




                        }
                        else{
                            listModelList.clear();
                            leadListAdapter.notifyDataSetChanged();
                            ViewUtils.showErrorDialog(getContext(), response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });
                            mTotalItemCount=0;
                            setupBadge();

                        }

                    }else{
                        mTotalItemCount=0;
                        setupBadge();
                    }
                }else{
                    mTotalItemCount=0;
                    setupBadge();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<LeadListModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                    ViewUtils.endProgressDialog();

                }
                t.printStackTrace();
                mTotalItemCount=0;
                setupBadge();
            }
        });
    }

}



