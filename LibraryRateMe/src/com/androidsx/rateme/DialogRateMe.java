package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

import com.androidsx.libraryrateme.R;

public class DialogRateMe extends DialogFragment {
    private static final String EXTRA_PACKAGE_NAME = "package-name";

    private String appPackageName;
    private View mView;
    private View tView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    public static DialogRateMe newInstance(String packageName) {
        DialogRateMe dialogo = new DialogRateMe();
        Bundle args = new Bundle();
        args.putString(EXTRA_PACKAGE_NAME, packageName);
        dialogo.setArguments(args);
        return dialogo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        appPackageName = getArguments().getString(EXTRA_PACKAGE_NAME);
        initializeUiFields();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating >= 4.0) {
                    rateMe.setVisibility(View.VISIBLE);
                    noThanks.setVisibility(View.GONE);
                } else {
                    noThanks.setVisibility(View.VISIBLE);
                    rateMe.setVisibility(View.GONE);
                }
            }
        });
        configureButtons();
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shareApp(appPackageName));
            }
        });
        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create();
    }

    private void initializeUiFields() {
        mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
    }

    private void configureButtons() {
        rateMe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });

        noThanks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMail();
            }
        });
    }

    private void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                    + appPackageName)));
        }
    }

    private void goToMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "some@email.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        try {
            startActivity(Intent.createChooser(intent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            rateApp();
        }
    }

    private Intent shareApp(String appPackageName) {
        Intent shareApp = new Intent();
        shareApp.setAction(Intent.ACTION_SEND);
        shareApp.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + appPackageName);
        shareApp.setType("text/plain");
        return shareApp;
    }

}
