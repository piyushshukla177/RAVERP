package com.rav.raverp.ui;


import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.ActivityLogAdapter;
import com.rav.raverp.data.adapter.FollowUPLeadListAdapter;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.ActivityLogModel;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.FollowUpListModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.ActivityChangePasswordBinding;
import com.rav.raverp.databinding.ActivityLogBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogActivity  extends BaseActivity implements ArrowBackPressed {

    private static final String TAG = ActivityLogActivity.class.getSimpleName();
    private Context mContext = ActivityLogActivity.this;
    private ActivityLogBinding binding;
    private ApiHelper apiHelper;
    private LoginModel login;
    private RecyclerView recyclerViewActivityLog;
    private ActivityLogAdapter activityLogAdapter;
    public static List<ActivityLogModel> activityLogModelList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_log);
        binding.setActivityLogActivity(this);
        setToolbarTitle("Activity Log");
        showBackArrow();
        setArrowBackPressed(this);


        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();
        recyclerViewActivityLog = findViewById(R.id.recycler_view);
        recyclerViewActivityLog.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewActivityLog.getRecycledViewPool().clear();
        checkNetwork();

    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            ActivityLog();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void ActivityLog() {

        String loginid = login.getStrLoginID();
        Call<ApiResponse<List<ActivityLogModel>>>ActivityLogModelCall = apiHelper.getActivityLogModel(loginid);
        ActivityLogModelCall.enqueue(new Callback<ApiResponse<List<ActivityLogModel>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<ActivityLogModel>>> call,
                                   @NonNull Response<ApiResponse<List<ActivityLogModel>>> response) {

                if (response.isSuccessful()) {

                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            activityLogModelList= response.body().getBody();
                            activityLogAdapter = new ActivityLogAdapter(mContext,response.body().getBody());
                            recyclerViewActivityLog.setAdapter(activityLogAdapter);



                        } else {



                        }
                    }

                } else {


                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<ActivityLogModel>>> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {

                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void arrowBackPressed() {
        onBackPressed();

    }
}