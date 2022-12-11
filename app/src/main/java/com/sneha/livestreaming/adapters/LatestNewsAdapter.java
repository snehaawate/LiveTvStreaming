package com.sneha.livestreaming.adapters;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.database.realmmodel.InnerCategoryModel;
import com.sneha.livestreaming.database.realmmodel.LatestChannelModel;
import com.sneha.livestreaming.fragments.LatestChannelFragment;
import com.sneha.livestreaming.ui.LatestChannelDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class LatestNewsAdapter  extends RecyclerView.Adapter<LatestNewsAdapter.ViewHolder> implements Filterable{
private Activity activity;
private LatestChannelFragment latestChannelFragment;
private List<LatestChannelModel> listOfAllLatestChannels;
private List<LatestChannelModel> listOfAllLatestChannelsFilterd;
    List<InnerCategoryModel> listOfAllLatestChannels1;
    public LatestNewsAdapter(Activity activity, LatestChannelFragment latestChannelFragment, List<LatestChannelModel> listOfAllLatestChannels, List<InnerCategoryModel> listOfAllLatestChannels1)

    {
        this.activity =activity;
        this.latestChannelFragment =latestChannelFragment;
        this.listOfAllLatestChannels =listOfAllLatestChannels;
        this.listOfAllLatestChannelsFilterd =listOfAllLatestChannels;
        this.listOfAllLatestChannels1 =listOfAllLatestChannels1;
    }

    @NonNull
    @Override
    public LatestNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_news_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestNewsAdapter.ViewHolder viewHolder, final int i) {
        Glide.with(activity).load(listOfAllLatestChannelsFilterd.get(i).getChannels_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defaultimage)
                .into(viewHolder.image);
        viewHolder.description.setText(listOfAllLatestChannelsFilterd.get(i).getChannels_name()+"");
        viewHolder.title.setText(listOfAllLatestChannelsFilterd.get(i).getCategory_name()+"");
        //viewHolder.title.setText(listOfAllLatestChannels1.get(i).getName()+"");
        viewHolder.date.setText(listOfAllLatestChannelsFilterd.get(i).getCreated_at()+"");
        viewHolder.full_card_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,LatestChannelDetailActivity.class).putExtra("List",listOfAllLatestChannelsFilterd.get(i)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfAllLatestChannelsFilterd.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listOfAllLatestChannelsFilterd = listOfAllLatestChannels;
                } else {
                    List<LatestChannelModel> filteredList = new ArrayList<>();
                    for (LatestChannelModel row : listOfAllLatestChannels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChannel_description().toLowerCase().contains(charString.toLowerCase()) || row.getChannel_description().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listOfAllLatestChannelsFilterd = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listOfAllLatestChannelsFilterd;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                listOfAllLatestChannelsFilterd = (ArrayList<LatestChannelModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView full_card_vi;
        AppCompatImageView image;
        TextView description,title,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            full_card_vi = itemView.findViewById(R.id.full_card_vi);
            image = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.description);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}
