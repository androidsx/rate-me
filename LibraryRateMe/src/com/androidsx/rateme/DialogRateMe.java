package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;

import com.androidsx.libraryrateme.R;

public class DialogRateMe extends DialogFragment {
    private static final String TAG = DialogRateMe.class.getSimpleName();

    private static final String EXTRA_PACKAGE_NAME = "package-name";
    private static final String EXTRA_EMAIL = "email-name";
    private static final String EXTRA_SHOW_SHARE = "show-share-button";
    private static final String EXTRA_COLOR_TITLE = "show-color-title";
    private static final String EXTRA_COLOR_DIALOG = "show-color-dialog";
    private static final boolean SHOW_SHARE_DEFAULT = true;
    private static final String MARKET_CONSTANT = "market://details?id=";
    private static final String GOOGLE_PLAY_CONSTANT = "http://play.google.com/store/apps/details?id=";

    private String appPackageName;
    private View mView;
    private View tView;
    private View confirDialogView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    /**
     * @param showShareButton configures whether the dialog will show a share button in the top bar
     */
    public static DialogRateMe newInstance(String packageName, String email, boolean showShareButton, int titleColor,
            int dialogColor) {
        DialogRateMe dialogo = new DialogRateMe();
        Bundle args = new Bundle();
        args.putString(EXTRA_PACKAGE_NAME, packageName);
        args.putString(EXTRA_EMAIL, email);
        args.putBoolean(EXTRA_SHOW_SHARE, showShareButton);
        args.putInt(EXTRA_COLOR_TITLE, titleColor);
        args.putInt(EXTRA_COLOR_DIALOG, dialogColor);
        dialogo.setArguments(args);
        return dialogo;
    }

    /**
     * @see #newInstance(String, String, boolean)
     */
    public static DialogRateMe newInstance(String packageName, String email, int titleColor, int dialogColor) {
        return newInstance(packageName, email, SHOW_SHARE_DEFAULT, titleColor, dialogColor);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        appPackageName = getArguments().getString(EXTRA_PACKAGE_NAME);
        initializeUiFields();
        Log.d(TAG, "initialize correctly all the components");

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
                RateMeDialogTimer.clearSharedPreferences(getActivity());
                Log.d(TAG, "clear preferences");

            }
        });
        share.setVisibility(getArguments().getBoolean(EXTRA_SHOW_SHARE) ? View.VISIBLE : View.GONE);
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shareApp(appPackageName));
                Log.d(TAG, "share App");
            }
        });

        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create();
    }

    private void initializeUiFields() {
        mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        int colorDialog = getArguments().getInt(EXTRA_COLOR_DIALOG);
        mView.setBackgroundColor(colorDialog);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
        int colorTitle = getArguments().getInt(EXTRA_COLOR_TITLE);
        tView.setBackgroundColor(colorTitle);
        confirDialogView = getActivity().getLayoutInflater().inflate(R.layout.confirmationtitledialog, null);
        confirDialogView.setBackgroundColor(colorTitle);
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
                Log.d(TAG, "go to Google Play Store for Rate-Me");
                RateMeDialogTimer.setOptOut(getActivity(), true);
            }
        });

        noThanks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmGoToMailDialog(getArguments()).show();
                Log.d(TAG, "got to Mail for explain what is the problem");
            }
        });
    }

    private void rateApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_CONSTANT + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_CONSTANT + appPackageName)));
        }
    }

    private void goToMail() {
        final String email = getArguments().getString(EXTRA_EMAIL);
        final String subject = getResources().getString(R.string.subject_email);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            startActivity(Intent.createChooser(intent, ""));
        } catch (android.content.ActivityNotFoundException ex) {
            rateApp();
        }
    }

    private Dialog confirmGoToMailDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCustomTitle(confirDialogView).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goToMail();
                dismiss();
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();

    }

    private Intent shareApp(String appPackageName) {
        Intent shareApp = new Intent();
        shareApp.setAction(Intent.ACTION_SEND);
        try {
            shareApp.putExtra(Intent.EXTRA_TEXT, MARKET_CONSTANT + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            shareApp.putExtra(Intent.EXTRA_TEXT, GOOGLE_PLAY_CONSTANT + appPackageName);
        }
        shareApp.setType("text/plain");
        return shareApp;
    }

}
