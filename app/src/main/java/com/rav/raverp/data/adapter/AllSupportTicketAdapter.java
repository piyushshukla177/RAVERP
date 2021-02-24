package com.rav.raverp.data.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.AllSupportTicketModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class AllSupportTicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    private List<AllSupportTicketModel> allSupportTicketModelList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    private ListItemClickListener listItemClickListener;

    public AllSupportTicketAdapter(Context context, PaginationAdapterCallback mCallback, ListItemClickListener listItemClickListener) {
        this.context = context;
        this.mCallback = mCallback;
        allSupportTicketModelList = new ArrayList<>();
        this.listItemClickListener = listItemClickListener;
    }

    public List<AllSupportTicketModel> getAllTickets() {
        return allSupportTicketModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_closed_pending_ticket, parent, false);
                viewHolder = new TicketVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllSupportTicketModel result = allSupportTicketModelList.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final TicketVH ticketVH = (TicketVH) holder;
                ticketVH.tvSupportFor.setText(result.getSupportfor());
                ticketVH.tvSupportType.setText(result.getSupporttype());
                ticketVH.tvStatus.setText(result.getStatus());
                ticketVH.tvSubject.setText("#" + result.getTicketno() + "-" + result.getSubject());
                ticketVH.tvLastUpdate.setText(result.getLastupdated());

                if (result.getStatus().equalsIgnoreCase("Opened")) {
                    ticketVH.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.lightYellow));
                    ticketVH.tvStatus.setTextColor(context.getResources().getColor(R.color.black));
                } else if (result.getStatus().equalsIgnoreCase("Answered")) {
                    ticketVH.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.black));
                    ticketVH.tvStatus.setTextColor(context.getResources().getColor(R.color.white));

                } else if (result.getStatus().equalsIgnoreCase("Closed")) {
                    ticketVH.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
                    ticketVH.tvStatus.setTextColor(context.getResources().getColor(R.color.white));
                }

                ticketVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listItemClickListener.onItemClicked(position);
                    }
                });

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return allSupportTicketModelList == null ? 0 : allSupportTicketModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == allSupportTicketModelList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */

    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here:
     * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
     * <p>
     *
     * @return Glide builder
     */



    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */
    public void add(AllSupportTicketModel r) {
        allSupportTicketModelList.add(r);
        notifyItemInserted(allSupportTicketModelList.size() - 1);
    }

    public void addAll(List<AllSupportTicketModel> moveResults) {
        for (AllSupportTicketModel result : moveResults) {
            add(result);
        }
    }

    public void remove(AllSupportTicketModel r) {
        int position = allSupportTicketModelList.indexOf(r);
        if (position > -1) {
            allSupportTicketModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new AllSupportTicketModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = allSupportTicketModelList.size() - 1;
        AllSupportTicketModel result = getItem(position);

        if (result != null) {
            allSupportTicketModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AllSupportTicketModel getItem(int position) {
        return allSupportTicketModelList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(allSupportTicketModelList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */


    /**
     * Main list's content ViewHolder
     */
    protected class TicketVH extends RecyclerView.ViewHolder {
        TextView tvSupportFor, tvSupportType, tvSubject, tvStatus, tvLastUpdate;

        public TicketVH(View itemView) {
            super(itemView);
            tvSupportFor = itemView.findViewById(R.id.tvSupportFor);
            tvSupportType = itemView.findViewById(R.id.tvSupportType);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }
}
