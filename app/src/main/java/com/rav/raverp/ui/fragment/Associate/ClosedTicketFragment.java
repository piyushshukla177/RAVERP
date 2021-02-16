package com.rav.raverp.ui.fragment.Associate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.PendingClosedTicketAdapter;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PendingClosedTicketModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.ViewUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ClosedTicketFragment extends Fragment {

    RecyclerView rvClosedTicket;
    ApiHelper apiHelper;
    LoginModel loginModel;
    String loginId;
    int roleId;
    TextView no_records_text_view;

    public ClosedTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_closed_ticket, container, false);

        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        loginModel = MyApplication.getLoginModel();
        loginId = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
        rvClosedTicket = view.findViewById(R.id.rvClosedTicket);
        no_records_text_view = view.findViewById(R.id.no_records_text_view);
        getClosedTicket();
        return view;
    }

    void getClosedTicket() {
        ViewUtils.startProgressDialog(getActivity());
        Call<PendingClosedTicketModel> pendingClosedTicketModelCall = apiHelper.getClosedTicket(loginId, roleId);
        pendingClosedTicketModelCall.enqueue(new Callback<PendingClosedTicketModel>() {
            @Override
            public void onResponse(Call<PendingClosedTicketModel> call, Response<PendingClosedTicketModel> response) {
                ViewUtils.endProgressDialog();
                PendingClosedTicketModel pendingClosedTicketModel = response.body();
                if (pendingClosedTicketModel.getResponse().equalsIgnoreCase("Success")) {
                    PendingClosedTicketAdapter pendingClosedTicketAdapter = new PendingClosedTicketAdapter(getActivity(), pendingClosedTicketModel, "true");
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                    rvClosedTicket.setLayoutManager(gridLayoutManager);
                    rvClosedTicket.setAdapter(pendingClosedTicketAdapter);
                } else {
                    rvClosedTicket.removeAllViewsInLayout();
                    no_records_text_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PendingClosedTicketModel> call, Throwable t) {
                ViewUtils.endProgressDialog();
            }
        });

    }

}