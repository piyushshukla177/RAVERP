package com.rav.raverp.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.ActivityLogModel;
import com.rav.raverp.databinding.ItemActivitylogBinding;
import java.util.List;


public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.MyViewHolder> {

    private static final String TAG = ActivityLogModel.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<ActivityLogModel> activityLogModels;
    private Context context;

    public ActivityLogAdapter(Context context, List<ActivityLogModel> activityLogModelList) {
        this.context = context;
        this.activityLogModels = activityLogModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemActivitylogBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_activitylog, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != activityLogModels && activityLogModels.size()> position) {
            holder.binding.sno.setText(String.valueOf(position+1));
            holder.binding.log.setText(activityLogModels.get(position).getDtLoginDate());
        }
    }


    @Override
    public int getItemCount() {
        if (null != activityLogModels && activityLogModels.size() > 0) {
            return activityLogModels.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemActivitylogBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemActivitylogBinding getBinding() {
            return binding;
        }
    }

    public List<ActivityLogModel> getActivityLogModel() {
        return activityLogModels;
    }

}

