package com.rav.raverp.ui.fragment.Associate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.DashboardGoalAdapter;
import com.rav.raverp.data.adapter.MyGoalListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.local.prefs.PrefsHelper;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.DashBoardModal;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.MyGoalListModel;
import com.rav.raverp.data.model.api.WalletAccessResponse;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.ActivityLogActivity;
import com.rav.raverp.ui.ChangePasswordActivity;
import com.rav.raverp.ui.EditProfileActivity;
import com.rav.raverp.ui.LoginActivity;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashBoardAssociateFragment extends Fragment {
    private ApiHelper apiHelper;
    View view;
    private LoginModel login;
    String id;
    int roleid;
    DrawerLayout drawer;
    MaterialCardView m1, m2, m3, m4, m5;

    Toolbar toolbar;

    TextView tvPersonalBv, tvLeftBv, tvLeftTeam, tvRightBv, tvRightTeam, tvCurrent, tvQuarterly, tvLastFy, tvTillDate,
            tvRank, tvAchieved, tvNetWorth, tvFounderClubLyStatus, tvFounderClubCyStatus, tvEliteClubLyStatus, tvEliteClubCyStatus;

    RecyclerView rvGoalList;

    List<MyGoalListModel> myGoalListModels = new ArrayList<>();
    List<MyGoalListModel> myGoalListModels1 = new ArrayList<>();
    DashboardGoalAdapter dashboardGoalAdapter;

    public DashBoardAssociateFragment() {

    }

    public DashBoardAssociateFragment(Toolbar toolbar) {
        this.toolbar = toolbar;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dashboard_associate_fragment, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();
        id = login.getStrLoginID();
        roleid = login.getIntRoleID();
        drawer = view.findViewById(R.id.drawer_layouts);
        m2 = view.findViewById(R.id.m2);
        m3 = view.findViewById(R.id.m3);
        tvPersonalBv = view.findViewById(R.id.tvPersonalBv);
        tvLeftBv = view.findViewById(R.id.tvLeftBv);
        tvLeftTeam = view.findViewById(R.id.tvLeftTeam);
        tvRightBv = view.findViewById(R.id.tvRightBv);
        tvRightTeam = view.findViewById(R.id.tvRightTeam);
        tvCurrent = view.findViewById(R.id.tvCurrent);
        tvQuarterly = view.findViewById(R.id.tvQuarterly);
        tvLastFy = view.findViewById(R.id.tvLastFy);
        tvTillDate = view.findViewById(R.id.tvTillDate);
        tvRank = view.findViewById(R.id.tvRank);
        tvAchieved = view.findViewById(R.id.tvAchieved);
        tvNetWorth = view.findViewById(R.id.tvNetWorth);
        tvFounderClubLyStatus = view.findViewById(R.id.tvFounderClubLyStatus);
        tvFounderClubCyStatus = view.findViewById(R.id.tvFounderClubCyStatus);
        tvEliteClubLyStatus = view.findViewById(R.id.tvEliteClubLyStatus);
        tvEliteClubCyStatus = view.findViewById(R.id.tvEliteClubCyStatus);
        rvGoalList = view.findViewById(R.id.rvGoalList);
        getDashboard();
        getGoal();
        return view;
    }

    private void getDashboard() {
        ViewUtils.startProgressDialog(getActivity());
        Call<DashBoardModal> getDashBoardModal = apiHelper.getDashboard(id, roleid);
        getDashBoardModal.enqueue(new Callback<DashBoardModal>() {
            @Override
            public void onResponse(Call<DashBoardModal> call,
                                   Response<DashBoardModal> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    DashBoardModal dashBoardModal = response.body();
                    if (dashBoardModal.getResponse().equalsIgnoreCase("Success")) {
                        tvPersonalBv.setText("BV " + dashBoardModal.getBody().get(0).getPersonalbv());
                        tvLeftBv.setText("BV " + dashBoardModal.getBody().get(0).getLeftbv());
                        tvLeftTeam.setText(dashBoardModal.getBody().get(0).getLeftteam());
                        tvRightBv.setText("BV " + dashBoardModal.getBody().get(0).getRightbv());
                        tvRightTeam.setText(dashBoardModal.getBody().get(0).getRightteam());
                        tvCurrent.setText(dashBoardModal.getBody().get(0).getCurrentincome());
                        tvQuarterly.setText(dashBoardModal.getBody().get(0).getQuarterlyincome());
                        tvLastFy.setText(dashBoardModal.getBody().get(0).getLastfyincome());
                        tvTillDate.setText(dashBoardModal.getBody().get(0).getTilldateincome());
                        tvRank.setText(dashBoardModal.getBody().get(0).getRank());
                        tvAchieved.setText(dashBoardModal.getBody().get(0).getAchievedrewardscount());
                        tvNetWorth.setText(dashBoardModal.getBody().get(0).getNetworth());
                        tvFounderClubLyStatus.setText(dashBoardModal.getBody().get(0).getFounderclublastyear() + " " + dashBoardModal.getBody().get(0).getFounderclublastyearfp() + "/" + dashBoardModal.getBody().get(0).getFounderclublastyeartarget());
                        tvFounderClubCyStatus.setText(dashBoardModal.getBody().get(0).getFounderclubcurrentyear() + " " + dashBoardModal.getBody().get(0).getFounderclubcurrentyearfp() + "/" + dashBoardModal.getBody().get(0).getFounderclubcurrentyeartarget());
                        tvEliteClubLyStatus.setText(dashBoardModal.getBody().get(0).getEliteclublastyear() + " " + dashBoardModal.getBody().get(0).getEliteclublastyearfp() + "/" + dashBoardModal.getBody().get(0).getEliteclublastyeartarget());
                        tvEliteClubCyStatus.setText(dashBoardModal.getBody().get(0).getEliteclubcurrentyear() + " " + dashBoardModal.getBody().get(0).getEliteclubcurrentyearfp() + "/" + dashBoardModal.getBody().get(0).getEliteclubcurrentyeartarget());
                    }
                }
            }

            @Override
            public void onFailure(Call<DashBoardModal> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    private void PinAccess() {

        ViewUtils.startProgressDialog((getActivity()));
        Call<WalletAccessResponse> getWalletAccessResponseCall =
                apiHelper.getWalletPinAccess(id, roleid);
        getWalletAccessResponseCall.enqueue(new Callback<WalletAccessResponse>() {
            @Override
            public void onResponse(Call<WalletAccessResponse> call,
                                   Response<WalletAccessResponse> response) {
                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getWalletPinStatus().toString().equalsIgnoreCase("false")) {
                            Fragment fragment = new WalletPinFragment("false");
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment);
                            transaction.addToBackStack(null).commit();
                        } else if (response.body().getWalletPinStatus().toString().equalsIgnoreCase("true")) {
                            Fragment fragment = new WalletPinFragment("true");
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment);
                            transaction.addToBackStack(null).commit();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletAccessResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //On click of option menu
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_password) {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.activity_log) {
            Intent intent = new Intent(getContext(), ActivityLogActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.signout) {
            ViewUtils.showConfirmationDialog(getActivity(), getString(R.string.msg_dialog_logout),
                    new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            PrefsHelper.remove(getActivity(), AppConstants.LOGIN);
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    void getGoal() {
        Call<ApiResponse<List<MyGoalListModel>>> getMyGoalListModelCall =
                apiHelper.getMyGoalListModel(id, roleid);
        getMyGoalListModelCall.enqueue(new Callback<ApiResponse<List<MyGoalListModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MyGoalListModel>>> call,
                                   Response<ApiResponse<List<MyGoalListModel>>> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {

                            myGoalListModels = response.body().getBody();
                            for (int i = 0; i < myGoalListModels.size(); i++) {
                                if (myGoalListModels.get(i).isSelected()) {
                                    myGoalListModels1.add(myGoalListModels.get(i));
                                }
                            }
                            dashboardGoalAdapter = new DashboardGoalAdapter(getActivity(), myGoalListModels1);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                            rvGoalList.setLayoutManager(gridLayoutManager);
                            rvGoalList.setAdapter(dashboardGoalAdapter);
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
