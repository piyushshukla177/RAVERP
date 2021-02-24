package com.rav.raverp.ui.fragment.Associate;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.AllSupportTicketAdapter;

import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.AllSupportTicketModel;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;

import com.rav.raverp.ui.ConversationActivity;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.PaginationAdapterCallback;
import com.rav.raverp.utils.PaginationScrollListener;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PendingTicketFragment extends Fragment implements PaginationAdapterCallback {

    LoginModel loginModel;

    private static final String TAG = "PendingTicketFragment";

    AllSupportTicketAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final int PAGE_START = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static final int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    String id;
    int roleId;

    private ApiHelper apiHelper;

    PaginationAdapterCallback paginationAdapterCallback;

    TextView no_records_text_view;


    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            AllSupportTicketModel allSupportTicketModel =
                    adapter.getAllTickets().get(itemPosition);
            Intent intent = new Intent(getActivity(), ConversationActivity.class);
            intent.putExtra("ticketNo", allSupportTicketModel.getTicketno());
            intent.putExtra("status", allSupportTicketModel.getStatus());
            startActivity(intent);
        }
    };


    public PendingTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_ticket, container, false);
        paginationAdapterCallback = this;
        loginModel = MyApplication.getLoginModel();
        id = loginModel.getStrLoginID();
        roleId = loginModel.getIntRoleID();
        rv = v.findViewById(R.id.main_recycler);
        no_records_text_view = v.findViewById(R.id.no_records_text_view);
        progressBar = v.findViewById(R.id.main_progress);
        errorLayout = v.findViewById(R.id.error_layout);
        btnRetry = v.findViewById(R.id.error_btn_retry);
        txtError = v.findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = v.findViewById(R.id.main_swiperefresh);


        adapter = new AllSupportTicketAdapter(getActivity(), paginationAdapterCallback, listItemClickListener);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        //init service and load data

        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        loadFirstPage();

       /* btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
            }
        });
*/
        btnRetry.setOnClickListener(view -> loadFirstPage());

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

/*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doRefresh();
                    }
                }, 2000);

            }
        });
*/


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += TOTAL_PAGES;
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        return v;
    }

    /**
     * Triggers the actual background refresh via the {@link SwipeRefreshLayout}
     */
    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callAllSupportTicketApi().isExecuted())
            callAllSupportTicketApi().cancel();
        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getAllTickets().clear();
        adapter.notifyDataSetChanged();
        currentPage = 0;
        loadFirstPage();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callAllSupportTicketApi().enqueue(new Callback<ApiResponse<List<AllSupportTicketModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AllSupportTicketModel>>> call, Response<ApiResponse<List<AllSupportTicketModel>>> response) {
                hideErrorView();

//                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    List<AllSupportTicketModel> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    no_records_text_view.setVisibility(View.GONE);
                } else {
                    rv.removeAllViewsInLayout();
                    no_records_text_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AllSupportTicketModel>>> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    /**
     * @return
     */
    private List<AllSupportTicketModel> fetchResults(Response<ApiResponse<List<AllSupportTicketModel>>> response) {
        return response.body().getBody();
        //TopRatedMovies topRatedMovies = response.body();
        //return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        adapter.addLoadingFooter();
        Log.d(TAG, "loadNextPage: " + currentPage);

        callAllSupportTicketApi().enqueue(new Callback<ApiResponse<List<AllSupportTicketModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AllSupportTicketModel>>> call, Response<ApiResponse<List<AllSupportTicketModel>>> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));
                adapter.removeLoadingFooter();
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    isLoading = false;
                    List<AllSupportTicketModel> results = fetchResults(response);
                    adapter.addAll(results);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AllSupportTicketModel>>> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<ApiResponse<List<AllSupportTicketModel>>> callAllSupportTicketApi() {
        return apiHelper.getAllSupportTicket(id, roleId, currentPage, TOTAL_PAGES);
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!NetworkUtils.isNetworkConnected()) {
            ViewUtils.showOfflineDialog(getActivity(), new DialogActionCallback() {
                @Override
                public void okAction() {
                    loadFirstPage();
                }
            });
            //errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}