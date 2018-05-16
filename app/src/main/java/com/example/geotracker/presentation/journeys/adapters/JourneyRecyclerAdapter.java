package com.example.geotracker.presentation.journeys.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geotracker.R;
import com.example.geotracker.presentation.journeys.adapters.datamodel.JourneyItem;
import com.example.geotracker.utils.DateTimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JourneyRecyclerAdapter extends RecyclerView.Adapter<JourneyRecyclerAdapter.ViewHolder> {
    private List<JourneyItem> mDataset;

    public JourneyRecyclerAdapter(@NonNull List<JourneyItem> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_journey, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JourneyItem currentItem = this.mDataset.get(position);
        String startAtHumanReadable = DateTimeUtils.isoUTCDateTimeStringToHumanReadable(currentItem.getJourneyCreationDateTimeUTC());
        holder.rowJourneyTitleTv.setText(currentItem.getJourneyTitle());
        holder.journeyItemHintTv.setVisibility(currentItem.isJourneyActive() ? View.VISIBLE : View.GONE);
        holder.journeyItemHintIv.setVisibility(currentItem.isJourneyActive() ? View.VISIBLE : View.GONE);
        holder.rowJourneySubtitleTv.setText(startAtHumanReadable);
    }

    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    public void newDataset(@NonNull List<JourneyItem> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_journey_title_tv)
        TextView rowJourneyTitleTv;
        @BindView(R.id.row_journey_subtitle_tv)
        TextView rowJourneySubtitleTv;
        @BindView(R.id.journey_item_hint_tv)
        TextView journeyItemHintTv;
        @BindView(R.id.journey_item_hint_iv)
        ImageView journeyItemHintIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
