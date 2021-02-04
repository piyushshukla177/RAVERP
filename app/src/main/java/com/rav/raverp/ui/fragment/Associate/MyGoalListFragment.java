package com.rav.raverp.ui.fragment.Associate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.MyGoalListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.MyGoalListModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGoalListFragment extends Fragment {

    private ApiHelper apiHelper;
    private View view;
    private RecyclerView recyclerMyGoalList;

    private  MyGoalListAdapter myGoalListAdapter;
    private boolean isDialogHided;
    private LoginModel login;
    List<MyGoalListModel> myGoalListModels=new ArrayList<>();



    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            MyGoalListModel myGoalListModel =
                    myGoalListAdapter.getMyGoalListModels().get(itemPosition);


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
        view = inflater.inflate(R.layout.activity_my_goal_list, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        recyclerMyGoalList = view.findViewById(R.id.recycler_view_goal);
        login = MyApplication.getLoginModel();

        recyclerMyGoalList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMyGoalList.getRecycledViewPool().clear();
        recyclerMyGoalList.setNestedScrollingEnabled(true);
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
        String loginid=login.getStrLoginID();
        Integer roleid=login.getIntRoleID();

        Call<ApiResponse<List<MyGoalListModel>>> getMyGoalListModelCall =
                apiHelper.getMyGoalListModel(loginid,roleid);
        getMyGoalListModelCall.enqueue(new Callback<ApiResponse<List<MyGoalListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MyGoalListModel>>> call,
                                   Response<ApiResponse<List<MyGoalListModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {

                             myGoalListModels = response.body().getBody();


                           myGoalListAdapter = new MyGoalListAdapter(getActivity(), listItemClickListener,
                                    myGoalListModels);
                            recyclerMyGoalList.setAdapter(myGoalListAdapter);


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<MyGoalListModel>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }

        });
    }




}





