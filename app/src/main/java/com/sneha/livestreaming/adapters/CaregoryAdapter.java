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
import com.sneha.livestreaming.fragments.CategoryFragment;
import com.sneha.livestreaming.ui.SelectedCategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class CaregoryAdapter extends RecyclerView.Adapter<CaregoryAdapter.ViewHolder> implements Filterable {
    private Activity activity;
    private CategoryFragment categoryFragment;
    private List<InnerCategoryModel> allDataModel;
    private List<InnerCategoryModel> allDataModelFiltrable;

    public CaregoryAdapter(Activity activity, CategoryFragment categoryFragment, List<InnerCategoryModel> allDataModel) {
        this.activity = activity;
        this.categoryFragment = categoryFragment;
        this.allDataModel = allDataModel;
        this.allDataModelFiltrable = allDataModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_items, parent, false);

        return new CaregoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(activity).load(allDataModelFiltrable.get(i).getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.defaultimage)
                .into(viewHolder.image_gride);

//data(i);
        viewHolder.title_text.setText(allDataModelFiltrable.get(i).getName()+"");
        viewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryFragment.startActivity(new Intent(activity,SelectedCategoryActivity.class).putExtra("Channel_ID",allDataModelFiltrable.get(i).getCat_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return allDataModelFiltrable.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allDataModelFiltrable = allDataModel;
                } else {
                    List<InnerCategoryModel> filteredList = new ArrayList<>();
                    for (InnerCategoryModel row : allDataModel) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    allDataModelFiltrable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allDataModelFiltrable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                allDataModelFiltrable = (ArrayList<InnerCategoryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView full_card;
        AppCompatImageView image_gride;
        TextView title_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            full_card = itemView.findViewById(R.id.full_card);
            image_gride = itemView.findViewById(R.id.image_gride);
            title_text = itemView.findViewById(R.id.title_text);
        }
    }



}
