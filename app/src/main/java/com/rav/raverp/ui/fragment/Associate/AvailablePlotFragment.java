package com.rav.raverp.ui.fragment.Associate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rav.raverp.R;
import com.rav.raverp.data.adapter.PaginationAdapter;
import com.rav.raverp.data.adapter.PlotAvailableListAdapter;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.GetBlockModel;
import com.rav.raverp.data.model.api.GetProjectModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.databinding.DialogPlotFilterBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.ui.PlotAvailabilityActivityDetails;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.PaginationAdapterCallback;
import com.rav.raverp.utils.PaginationScrollListener;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class AvailablePlotFragment extends Fragment implements PaginationAdapterCallback {

    private static final String TAG = "AvailablePlotFragment";

    PaginationAdapter adapter;
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


    private ApiHelper apiHelper;

    PaginationAdapterCallback paginationAdapterCallback;


    private boolean isDialogHided;
    private Dialog filterDialog;
    private Spinner project_name_spinner, block_name_spinner;
    TextView no_records_text_view;
    TextView txt1, txt2;
    TextView textTotalItemCount;
    int mTotalItemCount;
    int ProjectId, BlockId;


    public AvailablePlotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private ListItemClickListener listItemClickListener = new ListItemClickListener() {
        @Override
        public void onItemClicked(int itemPosition) {
            PlotAvailableModel plotAvailable =
                    adapter.getPlots().get(itemPosition);
            Intent intent = new Intent(getActivity(), PlotAvailabilityActivityDetails.class);
            intent.putExtra("ploatData", plotAvailable);
            startActivity(intent);

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_available_plot, container, false);
        paginationAdapterCallback = this;
        rv = v.findViewById(R.id.main_recycler);
        no_records_text_view = v.findViewById(R.id.no_records_text_view);
        progressBar = v.findViewById(R.id.main_progress);
        errorLayout = v.findViewById(R.id.error_layout);
        btnRetry = v.findViewById(R.id.error_btn_retry);
        txtError = v.findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = v.findViewById(R.id.main_swiperefresh);

        adapter = new PaginationAdapter(getActivity(), paginationAdapterCallback, listItemClickListener);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);


        //init service and load data

        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstPage();
            }
        });

        // btnRetry.setOnClickListener(view -> loadFirstPage());

        //  swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

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

        if (isDialogHided) {
            isDialogHided = false;
            filterDialog.dismiss();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += TOTAL_PAGES;
                //   Toast.makeText(getActivity(), currentPage + "", Toast.LENGTH_SHORT).show();
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

    }

    /**
     * Triggers the actual background refresh via the {@link SwipeRefreshLayout}
     */
    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callTopRatedMoviesApi().isExecuted())
            callTopRatedMoviesApi().cancel();
        // TODO: Check if data is stale.
        //  Execute network request if cache is expired; otherwise do not update data.
        adapter.getPlots().clear();
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

        callTopRatedMoviesApi().enqueue(new Callback<ApiResponse<List<PlotAvailableModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlotAvailableModel>>> call, Response<ApiResponse<List<PlotAvailableModel>>> response) {
                hideErrorView();

//                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    List<PlotAvailableModel> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    mTotalItemCount = results.get(0).getTotalRecords();
                    setupBadge();
                    no_records_text_view.setVisibility(View.GONE);

                    /*if (currentPage <= currentPage + TOTAL_PAGES - 1) adapter.addLoadingFooter();
                    else isLastPage = true;*/

                } else {
                    // isLastPage=true;
                    // rv.removeAllViewsInLayout();
                    no_records_text_view.setVisibility(View.VISIBLE);
                    mTotalItemCount = 0;
                    setupBadge();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlotAvailableModel>>> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    /**
     * @return
     */
    private List<PlotAvailableModel> fetchResults(Response<ApiResponse<List<PlotAvailableModel>>> response) {
        return response.body().getBody();
        //TopRatedMovies topRatedMovies = response.body();
        //return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        adapter.addLoadingFooter();
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<ApiResponse<List<PlotAvailableModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlotAvailableModel>>> call, Response<ApiResponse<List<PlotAvailableModel>>> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));
                adapter.removeLoadingFooter();
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    isLoading = false;
                    List<PlotAvailableModel> results = fetchResults(response);
                    adapter.addAll(results);
                    mTotalItemCount = results.get(0).getTotalRecords();
                    setupBadge();
                  /*  if (currentPage <= currentPage + TOTAL_PAGES - 1) adapter.addLoadingFooter();
                    else isLastPage = true;*/
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlotAvailableModel>>> call, Throwable t) {
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
    private Call<ApiResponse<List<PlotAvailableModel>>> callTopRatedMoviesApi() {
        return apiHelper.getPlotAvailabilitylistfilter(ProjectId, BlockId, currentPage, TOTAL_PAGES);
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

    private void GetProject() {
        Call<ApiResponse<List<GetProjectModel>>> getProjectlistCall =
                apiHelper.getProject();
        getProjectlistCall.enqueue(new Callback<ApiResponse<List<GetProjectModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetProjectModel>>> call,
                                   Response<ApiResponse<List<GetProjectModel>>> response) {


                if (response.isSuccessful()) {
                    final List<GetProjectModel> getProjectList = new ArrayList<>();
                    GetProjectModel getProject = new GetProjectModel();
                    getProject.setStrProjectName("--Select Project--");
                    getProject.setIntProjectId(0);
                    getProjectList.add(getProject);
                    getProjectList.addAll(response.body().getBody());
                    ArrayAdapter<GetProjectModel> adapter = new ArrayAdapter<>(getContext(),
                            R.layout.simple_spinner_item, getProjectList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    project_name_spinner.setAdapter(adapter);
                    project_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String projectName = parent.getSelectedItem().toString();
                            if (!projectName.equals("--Select Project--")) {
                                int ids = getProjectList.get(position).getIntProjectId();
                                GetBlock(String.valueOf(ids));
                                ProjectId = ids;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetProjectModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }


    private void GetBlock(String ProjectId) {

        Call<ApiResponse<List<GetBlockModel>>> getBlocklistCall =
                apiHelper.getBlocks(ProjectId);
        getBlocklistCall.enqueue(new Callback<ApiResponse<List<GetBlockModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<GetBlockModel>>> call,
                                   Response<ApiResponse<List<GetBlockModel>>> response) {


                if (response.isSuccessful()) {
                    final List<GetBlockModel> getblockList = new ArrayList<>();
                    GetBlockModel getBlock = new GetBlockModel();
                    getBlock.setStrBlockName("--Select Block--");
                    getBlock.setIntBlockId(0);
                    getblockList.add(getBlock);
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        getblockList.addAll(response.body().getBody());
                        ArrayAdapter<GetBlockModel> adapter = new ArrayAdapter<>(getContext(),
                                R.layout.simple_spinner_item, getblockList);
                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        block_name_spinner.setAdapter(adapter);
                        block_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String projectName = parent.getSelectedItem().toString();
                                if (!projectName.equals("--Select Project--")) {
                                    int ids = getblockList.get(position).getIntBlockId();
                                    BlockId = ids;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<GetBlockModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
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

    //On click of option menu
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
        final DialogPlotFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_plot_filter, null, false);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);
        project_name_spinner = (Spinner) filterDialog.findViewById(R.id.project_name_spinner);
        block_name_spinner = (Spinner) filterDialog.findViewById(R.id.block_name_spinner);

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
        GetProject();

        binding.GetPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.hide();
                isDialogHided = true;
                adapter.getPlots().clear();
                adapter.notifyDataSetChanged();
                currentPage = 0;
                loadFirstPage();
            }
        });
    }

}