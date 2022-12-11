package com.sneha.livestreaming.adapters;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.model.DataModel;
import com.sneha.livestreaming.ui.MainActivity;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<DataModel> listviewImage;

    public CustomAdapter(MainActivity mainActivity, List<DataModel> listviewImage) {
        this.context = mainActivity;
        this.listviewImage = listviewImage;
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return listviewImage.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_item_row, null);
        }
        AppCompatImageView imageView = convertView.findViewById(R.id.imageViewIcon);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        imageView.setImageResource(listviewImage.get(position).icon);
        textViewName.setText(listviewImage.get(position).name);
        return convertView;
    }
}
