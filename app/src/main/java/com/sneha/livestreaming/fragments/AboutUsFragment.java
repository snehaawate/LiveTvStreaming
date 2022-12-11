package com.sneha.livestreaming.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sneha.livestreaming.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener {

    CardView card_view_rate, card_view_more, card_view_email;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        card_view_rate = view.findViewById(R.id.card_view_rate);
        card_view_more = view.findViewById(R.id.card_view_more);
        card_view_email = view.findViewById(R.id.card_view_email);

        card_view_rate.setOnClickListener(this);
        card_view_more.setOnClickListener(this);
        card_view_email.setOnClickListener(this);
        return view;
    }

/**
* Re direct user to play-store to rate our app
* */
    public void rateMe() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.sneha.livestreaming"));
        startActivity(intent);
    }

    public void searchMe() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "http://play.google.com/store/search?q=Abhi Android"));
        startActivity(intent);
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);

        emailIntent.setData(Uri.parse("mailto:" + "info@abhiandroid.com" + "?subject=" + getActivity().getResources().getString(R.string.app_name) + " feedback"));
        startActivity(Intent.createChooser(emailIntent, "E_mail"));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view_rate:
                rateMe();
                break;
            case R.id.card_view_more:
                searchMe();
                break;
            case R.id.card_view_email:
                sendEmail();
                break;
        }

    }
}
