package com.rav.raverp.data.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.data.model.api.PlotCostModal;
import com.rav.raverp.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<PlotAvailableModel> plotAvailableModelList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    private ListItemClickListener listItemClickListener;

    public PaginationAdapter(Context context, PaginationAdapterCallback mCallback,  ListItemClickListener listItemClickListener) {
        this.context = context;
        this.mCallback = mCallback;
        plotAvailableModelList = new ArrayList<>();
        this.listItemClickListener = listItemClickListener;
    }

    public List<PlotAvailableModel> getPlots() {
        return plotAvailableModelList;
    }

    public void setMovies(List<PlotAvailableModel> movieResults) {
        this.plotAvailableModelList = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_available_plot, parent, false);
                viewHolder = new PlotVH(viewItem);
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
        PlotAvailableModel result = plotAvailableModelList.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final PlotVH movieVH = (PlotVH) holder;

                movieVH.sno.setText(result.getSNo() + "");
                movieVH.tvPlotNo.setText(result.getStrPlotNo());
                movieVH.tvStatus.setText(result.getStrBookingPlotStatus());


                movieVH.show_more_text_view.setPaintFlags(
                        movieVH.show_more_text_view.getPaintFlags() |
                                Paint.UNDERLINE_TEXT_FLAG);

                movieVH.show_more_text_view.setOnClickListener(new View.OnClickListener() {
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
        return plotAvailableModelList == null ? 0 : plotAvailableModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == plotAvailableModelList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
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
    public void add(PlotAvailableModel r) {
        plotAvailableModelList.add(r);
        notifyItemInserted(plotAvailableModelList.size() - 1);
    }

    public void addAll(List<PlotAvailableModel> moveResults) {
        for (PlotAvailableModel result : moveResults) {
            add(result);
        }
    }

    public void remove(PlotAvailableModel r) {
        int position = plotAvailableModelList.indexOf(r);
        if (position > -1) {
            plotAvailableModelList.remove(position);
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
        add(new PlotAvailableModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = plotAvailableModelList.size() - 1;
        PlotAvailableModel result = getItem(position);

        if (result != null) {
            plotAvailableModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PlotAvailableModel getItem(int position) {
        return plotAvailableModelList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(plotAvailableModelList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class HeroVH extends RecyclerView.ViewHolder {

        TextView sno, tvPlotNo, tvStatus, show_more_text_view;

        public HeroVH(View itemView) {
            super(itemView);

            sno = itemView.findViewById(R.id.sno);
            tvPlotNo = itemView.findViewById(R.id.tvPlotNo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            show_more_text_view = itemView.findViewById(R.id.show_more_text_view);
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class PlotVH extends RecyclerView.ViewHolder {
        TextView sno, tvPlotNo, tvStatus, show_more_text_view;

        public PlotVH(View itemView) {
            super(itemView);

            sno = itemView.findViewById(R.id.sno);
            tvPlotNo = itemView.findViewById(R.id.tvPlotNo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            show_more_text_view = itemView.findViewById(R.id.show_more_text_view);
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
