package com.sneha.livestreaming.adapters;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.database.realmmodel.InnerChannelModel;
import com.sneha.livestreaming.ui.SelectedCategoryActivity;
import com.sneha.livestreaming.ui.SelectedChannelDetalActivity;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>  implements Filterable {

    private SelectedCategoryActivity selectedCategoryActivity;
    private List<InnerChannelModel> listOfAllLatestChannels;
    private List<InnerChannelModel> listOfAllLatestChannelsFilterable;

    public AllCategoryAdapter(SelectedCategoryActivity selectedCategoryActivity, List<InnerChannelModel> listOfAllLatestChannels) {
        this.listOfAllLatestChannels = listOfAllLatestChannels;
        this.selectedCategoryActivity = selectedCategoryActivity;
        this.listOfAllLatestChannelsFilterable = listOfAllLatestChannels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_news_items, parent, false);

        return new AllCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.e("TAg",""+listOfAllLatestChannels.get(i).getChannels_image());
        Glide.with(selectedCategoryActivity).load(listOfAllLatestChannelsFilterable.get(i).getChannels_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defaultimage)
                .into(viewHolder.image);
        viewHolder.description.setText(listOfAllLatestChannelsFilterable.get(i).getChannels_name()+"");
        viewHolder.title.setText(listOfAllLatestChannelsFilterable.get(i).getCategory_name()+"");
        viewHolder.date.setText(listOfAllLatestChannelsFilterable.get(i).getCreated_at()+"");
        viewHolder.full_card_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryActivity.startActivity(new Intent(selectedCategoryActivity,SelectedChannelDetalActivity.class).putExtra("List",listOfAllLatestChannelsFilterable.get(i)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfAllLatestChannelsFilterable.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listOfAllLatestChannelsFilterable = listOfAllLatestChannels;
                } else {
                    List<InnerChannelModel> filteredList = new ArrayList<>();
                    for (InnerChannelModel row : listOfAllLatestChannels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChannel_description().toLowerCase().contains(charString.toLowerCase()) || row.getChannel_description().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listOfAllLatestChannelsFilterable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listOfAllLatestChannelsFilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                listOfAllLatestChannelsFilterable = (ArrayList<InnerChannelModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView full_card_vi;
        AppCompatImageView image;
        TextView description, title, date;
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
