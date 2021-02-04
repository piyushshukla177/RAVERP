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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.LeadListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.local.prefs.PrefsHelper;
import com.rav.raverp.data.model.api.AddAssociateModal;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.DashBoardModal;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.WalletAccessResponse;
import com.rav.raverp.data.model.api.WalletAmountListModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.ActivityLogActivity;
import com.rav.raverp.ui.ChangePasswordActivity;
import com.rav.raverp.ui.EditProfileActivity;
import com.rav.raverp.ui.LoginActivity;
import com.rav.raverp.ui.fragment.Customer.DashBoardCustomerFragment;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.ViewUtils;

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
    TextView txt_my_wallet ,txt_R_Platinum;
    MaterialCardView m1,m2,m3,m4,m5;

    androidx.appcompat.widget.Toolbar toolbar;

    TextView tvTotalBusiness, tvLeftBusiness, tvRightBusiness, tvMyWallet, tvRPlatinumWallet, tvRGoldWallet, tvTotalPlot,
    tvBookedPlot, tvHoldPlot, tvMember, tvActiveMember, tvInActiveMember;


    public DashBoardAssociateFragment() {

    }

    public DashBoardAssociateFragment(androidx.appcompat.widget.Toolbar toolbar) {
        this.toolbar=toolbar;
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
        view= inflater.inflate(R.layout.dashboard_associate_fragment, container, false);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();
        id = login.getStrLoginID();
        roleid = login.getIntRoleID();
       // txt_my_wallet=(TextView)view.findViewById(R.id.txt_my_wallet);
       // txt_R_Platinum=(TextView)view.findViewById(R.id.txt_R_Platinum);
        tvTotalBusiness=(TextView)view.findViewById(R.id.tvTotalBusiness);
        tvLeftBusiness=(TextView)view.findViewById(R.id.tvLeftBusiness);
        tvRightBusiness=(TextView)view.findViewById(R.id.tvRightBusiness);
        tvMyWallet=(TextView)view.findViewById(R.id.tvMyWallet);
        tvRPlatinumWallet=(TextView)view.findViewById(R.id.tvRPlatinumWallet);
        tvRGoldWallet=(TextView)view.findViewById(R.id.tvRGoldWallet);
        tvTotalPlot=(TextView)view.findViewById(R.id.tvTotalPlot);
        tvBookedPlot=(TextView)view.findViewById(R.id.tvBookedPlot);
        tvHoldPlot=(TextView)view.findViewById(R.id.tvHoldPlot);
        tvMember=(TextView)view.findViewById(R.id.tvMember);
        tvActiveMember=(TextView)view.findViewById(R.id.tvActiveMember);
        tvInActiveMember=(TextView)view.findViewById(R.id.tvInActiveMember);
        drawer =(DrawerLayout)view. findViewById(R.id.drawer_layouts);
        m2=(MaterialCardView)view.findViewById(R.id.m2) ;
        m3=(MaterialCardView)view.findViewById(R.id.m3) ;


        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PinAccess();
            }
        });
        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PlotAvailabilityFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homepage, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        getDashboard();
       return  view;
    }

    private void getDashboard() {

        ViewUtils.startProgressDialog(getActivity());

        Call<DashBoardModal> getDashBoardModal = apiHelper.getDashboard(id,roleid);
        getDashBoardModal.enqueue(new Callback<DashBoardModal>() {
            @Override
            public void onResponse(Call<DashBoardModal> call,
                                   Response<DashBoardModal> response) {


                        ViewUtils.endProgressDialog();

                   if (response.isSuccessful()) {

                            DashBoardModal dashBoardModal = response.body();
                            if(dashBoardModal.getResponse().equalsIgnoreCase("Success")){
                                tvLeftBusiness.setText(dashBoardModal.getBody().get(0).getLeftBV());
                                tvRightBusiness.setText(dashBoardModal.getBody().get(0).getRightBV());
                                tvMyWallet.setText(dashBoardModal.getBody().get(0).getMyWalletAmount());
                                tvRPlatinumWallet.setText(dashBoardModal.getBody().get(0).getRpWalletAmount());
                                tvBookedPlot.setText(dashBoardModal.getBody().get(0).getBook());
                                tvHoldPlot.setText(dashBoardModal.getBody().get(0).getHold());
                                tvInActiveMember.setText(dashBoardModal.getBody().get(0).getInActiveMember());
                                tvActiveMember.setText(dashBoardModal.getBody().get(0).getActiveMember());

                                tvTotalBusiness.setText(dashBoardModal.getBody().get(0).getTotalBV());



                            }
/*
                            if (response.body() != null) {
                                if (response.body().getResponse().equalsIgnoreCase("Success")) {


                                    Double amount=response.body().getBody().get(0).getRPlantinumWalletAmounts();
                                    if(response.body().getBody().get(0).getRPlantinumWalletAmounts()!=null)
                                    txt_R_Platinum.setText(String.valueOf(amount));

                                }
                            }
*/
                       }
                    }


            @Override
            public void onFailure(Call<DashBoardModal>call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }

    private void PinAccess() {

        String loginid=login.getStrLoginID();
        int roleid=login.getIntRoleID();

        ViewUtils.startProgressDialog((getActivity()));

        Call<WalletAccessResponse> getWalletAccessResponseCall =
                apiHelper.getWalletPinAccess(loginid,roleid);

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
                        }
                        else if (response.body().getWalletPinStatus().toString().equalsIgnoreCase("true")) {
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
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //On click of option menu
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
      if (id == R.id.change_password) {

         Intent intent =new Intent(getContext(), ChangePasswordActivity.class);
          startActivity(intent);

            return true;
        }else if (id == R.id.activity_log){
          Intent intent =new Intent(getContext(), ActivityLogActivity.class);
          startActivity(intent);
          return  true;




      }else if (id == R.id.profile){


          Intent intent =new Intent(getContext(), EditProfileActivity.class);
          startActivity(intent);
          return true;




      }else if (id == R.id.signout){

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

}
