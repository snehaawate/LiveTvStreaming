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
import com.sneha.livestreaming.database.realmmodel.MyFavoritemodel;
import com.sneha.livestreaming.fragments.FavoriteFragment;
import com.sneha.livestreaming.ui.MyFavoriteDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements Filterable {
    private Activity activity;
    private FavoriteFragment favoriteFragment;
    private List<MyFavoritemodel> myFavoritemodels;
    private List<MyFavoritemodel> myFavoritemodelsFilter;
    public FavoriteAdapter(Activity activity, FavoriteFragment favoriteFragment, List<MyFavoritemodel> myFavoritemodels) {
   this.activity = activity;
   this.favoriteFragment = favoriteFragment;
   this.myFavoritemodels = myFavoritemodels;
   this.myFavoritemodelsFilter = myFavoritemodels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_news_items, parent, false);

        return new FavoriteAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(activity).load(myFavoritemodelsFilter.get(i).getChannels_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.image);
        viewHolder.description.setText(myFavoritemodelsFilter.get(i).getChannel_description()+"");
        viewHolder.title.setText(myFavoritemodelsFilter.get(i).getChannels_name()+"");
        viewHolder.date.setText(myFavoritemodelsFilter.get(i).getCreated_at()+"");
        viewHolder.full_card_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,MyFavoriteDetailActivity.class).putExtra("List",myFavoritemodelsFilter.get(i)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myFavoritemodelsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    myFavoritemodelsFilter = myFavoritemodels;
                } else {
                    List<MyFavoritemodel> filteredList = new ArrayList<>();
                    for (MyFavoritemodel row : myFavoritemodels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChannel_description().toLowerCase().contains(charString.toLowerCase()) || row.getChannel_description().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    myFavoritemodelsFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = myFavoritemodelsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                myFavoritemodelsFilter = (ArrayList<MyFavoritemodel>) filterResults.values;
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
