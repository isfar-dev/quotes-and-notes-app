package com.isfar.quotesnotes.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.activities.AboutActivity;


public class MoreFragment extends Fragment {

    TextView tvRating, tvShare, tvContact, tvPrivacy, tvAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        //================================================
        tvRating = view.findViewById(R.id.tv_rating);
        tvShare = view.findViewById(R.id.tv_share);
        tvContact = view.findViewById(R.id.tv_contact);
        tvPrivacy = view.findViewById(R.id.tv_privacy);
        tvAbout = view.findViewById(R.id.tv_about);
        //================================================


        //=========== Rating App OnClick ===============================
        tvRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        //=========== Share App OnClick ===============================
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app:\n" +
                        "https://play.google.com/store/apps/details?id="
                        + getContext().getPackageName());
                share_intent.setType("text/plain");
                startActivity(Intent.createChooser(share_intent, "share app via"));
            }
        });

        //=========== Contact OnClick ===============================
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:mohammadisfar17@gmail.com"));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "No email app found to handle this action", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //=========== Privacy Policy OnClick ===============================
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://sites.google.com/view/quotesnotesprivacy";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //=========== About App OnClick ===============================
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }//onCreate End...

}//Main End...