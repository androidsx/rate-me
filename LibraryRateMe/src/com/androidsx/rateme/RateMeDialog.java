package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidsx.libraryrateme.R;

/**
 * Rate Me dialog. Entry point into the library. Use the {@link com.androidsx.rateme.RateMeDialog.Builder} to
 * construct your instance.
 */
public class RateMeDialog extends DialogFragment {
    private static final String TAG = RateMeDialog.class.getSimpleName();

    private static final String MARKET_CONSTANT = "market://details?id=";
    private static final String GOOGLE_PLAY_CONSTANT = "http://play.google.com/store/apps/details?id=";

    private final static String RESOURCE_NAME = "titleDivider";
    private final static String DEFAULT_TYPE_RESOURCE = "id";
    private final static String DEFAULT_PACKAGE = "android";

    // Views
    private View mView;
    private View tView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    // Configuration. It all comes from the builder. On, on resume, from the saved bundle
    private String appPackageName;
    private int headerBackgroundColor;
    private int headerTextColor;
    private int bodyBackgroundColor;
    private int bodyTextColor;
    private boolean feedbackByEmailEnabled;
    private String feedbackEmail;
    private boolean showShareButton;
    private int appIconResId;
    private int lineDividerColor;
    private int rateButtonBackgroundColor;
    private int rateButtonTextColor;
    private int rateButtonPressedBackgroundColor;
    private int defaultStarsSelected;
    private int iconCloseColor;
    private int iconShareColor;
    private boolean showOKButtonByDefault;
    private RateMeOnActionListener onActionListener;

    public RateMeDialog() {
        // Empty constructor, required for pause/resume
    }

    public RateMeDialog(String appPackageName,
                        int headerBackgroundColor,
                        int headerTextColor,
                        int bodyBackgroundColor,
                        int bodyTextColor,
                        boolean feedbackByEmailEnabled,
                        String feedbackEmail,
                        boolean showShareButton,
                        int appIconResId,
                        int lineDividerColor,
                        int rateButtonBackgroundColor,
                        int rateButtonTextColor,
                        int rateButtonPressedBackgroundColor,
                        int defaultStarsSelected,
                        int iconCloseColor,
                        int iconShareColor,
                        boolean showOKButtonByDefault,
                        RateMeOnActionListener onActionListener) {
        this.appPackageName = appPackageName;
        this.headerBackgroundColor = headerBackgroundColor;
        this.headerTextColor = headerTextColor;
        this.bodyBackgroundColor = bodyBackgroundColor;
        this.bodyTextColor = bodyTextColor;
        this.feedbackByEmailEnabled = feedbackByEmailEnabled;
        this.feedbackEmail = feedbackEmail;
        this.showShareButton = showShareButton;
        this.appIconResId = appIconResId;
        this.lineDividerColor = lineDividerColor;
        this.rateButtonBackgroundColor = rateButtonBackgroundColor;
        this.rateButtonTextColor = rateButtonTextColor;
        this.rateButtonPressedBackgroundColor = rateButtonPressedBackgroundColor;
        this.defaultStarsSelected = defaultStarsSelected;
        this.iconCloseColor = iconCloseColor;
        this.iconShareColor = iconShareColor;
        this.showOKButtonByDefault = showOKButtonByDefault;
        this.onActionListener = onActionListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeUiFields();
        Log.d(TAG, "All components were initialized successfully");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setIconsTitleColor(iconCloseColor, iconShareColor);

        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating >= 4.0) {
                    rateMe.setVisibility(View.VISIBLE);
                    noThanks.setVisibility(View.GONE);
                } else if (rating > 0.0){
                    noThanks.setVisibility(View.VISIBLE);
                    rateMe.setVisibility(View.GONE);
                } else {
                    noThanks.setVisibility(View.GONE);
                    rateMe.setVisibility(View.GONE);
                }
                defaultStarsSelected = (int) rating;
            }
        });
        ratingBar.setStepSize(1.0f);
        ratingBar.setRating((float) defaultStarsSelected);
        configureButtons();
        
        try {
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    RateMeDialogTimer.clearSharedPreferences(getActivity());
                    Log.d(TAG, "Cleared the shared preferences");
                    RateMeDialogTimer.setOptOut(getActivity(), true);
                    if (onActionListener != null) {
                        onActionListener.onHandleRateMeAction(RateMeAction.DISMISSED_WITH_CROSS, ratingBar.getRating());
                    }
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "Error while closing the dialog", e);
            dismiss();
        }
        
        try {
            share.setVisibility(showShareButton ? View.VISIBLE : View.GONE);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(shareApp(appPackageName));
                    Log.d(TAG, "Share the application");
                    if (onActionListener != null) {
                        onActionListener.onHandleRateMeAction(RateMeAction.SHARED_APP, ratingBar.getRating());
                    }

                }
            });
        } catch (Exception e) {
            Log.d(TAG,"Error showing share button " + e);
            dismiss();
        }

        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null) {
            this.appPackageName = savedInstanceState.getString("appPackageName");
            this.headerBackgroundColor = savedInstanceState.getInt("headerBackgroundColor");
            this.headerTextColor = savedInstanceState.getInt("headerTextColor");
            this.bodyBackgroundColor = savedInstanceState.getInt("bodyBackgroundColor");
            this.bodyTextColor = savedInstanceState.getInt("bodyTextColor");
            this.feedbackByEmailEnabled = savedInstanceState.getBoolean("feedbackByEmailEnabled");
            this.feedbackEmail = savedInstanceState.getString("feedbackEmail");
            this.showShareButton = savedInstanceState.getBoolean("showShareButton");
            this.appIconResId = savedInstanceState.getInt("appIconResId");
            this.lineDividerColor = savedInstanceState.getInt("lineDividerColor");
            this.rateButtonBackgroundColor = savedInstanceState.getInt("rateButtonBackgroundColor");
            this.rateButtonTextColor = savedInstanceState.getInt("rateButtonTextColor");
            this.rateButtonPressedBackgroundColor = savedInstanceState.getInt("rateButtonPressedBackgroundColor");
            this.defaultStarsSelected = savedInstanceState.getInt("defaultStarsSelected");
            this.iconCloseColor = savedInstanceState.getInt("iconCloseColor");
            this.iconShareColor = savedInstanceState.getInt("iconShareColor");
            this.showOKButtonByDefault = savedInstanceState.getBoolean("showOKButtonByDefault");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        outState.putString("appPackageName", appPackageName);
        outState.putInt("headerBackgroundColor", headerBackgroundColor);
        outState.putInt("headerTextColor", headerTextColor);
        outState.putInt("bodyBackgroundColor", bodyBackgroundColor);
        outState.putInt("bodyTextColor", bodyTextColor);
        outState.putBoolean("feedbackByEmailEnabled", feedbackByEmailEnabled);
        outState.putString("feedbackEmail", feedbackEmail);
        outState.putBoolean("showShareButton", showShareButton);
        outState.putInt("appIconResId", appIconResId);
        outState.putInt("lineDividerColor", lineDividerColor);
        outState.putInt("rateButtonBackgroundColor", rateButtonBackgroundColor);
        outState.putInt("rateButtonTextColor", rateButtonTextColor);
        outState.putInt("rateButtonPressedBackgroundColor", rateButtonPressedBackgroundColor);
        outState.putInt("defaultStarsSelected", defaultStarsSelected );
        outState.putInt("iconCloseColor", iconCloseColor );
        outState.putInt("iconShareColor", iconShareColor);
        outState.putBoolean("showOKButtonByDefault", showOKButtonByDefault);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final int titleDividerId = getResources().getIdentifier(RESOURCE_NAME, DEFAULT_TYPE_RESOURCE, DEFAULT_PACKAGE);
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(lineDividerColor);
        }
    }

    private void initializeUiFields() {
        // Main Dialog
        mView = View.inflate(getActivity(), R.layout.rateme_dialog_message, null);
        tView = View.inflate(getActivity(), R.layout.rateme_dialog_title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        mView.setBackgroundColor(bodyBackgroundColor);
        tView.setBackgroundColor(headerBackgroundColor);
        ((TextView) tView.findViewById(R.id.dialog_title)).setTextColor(headerTextColor);
        final View iconImage = mView.findViewById(R.id.app_icon_dialog_rating);
        if (appIconResId == 0) {
            iconImage.setVisibility(View.GONE);
        } else {
            ((ImageView) iconImage).setImageResource(appIconResId);
            iconImage.setVisibility(View.VISIBLE);
        }
        ((TextView) mView.findViewById(R.id.rating_dialog_message)).setTextColor(bodyTextColor);

        rateMe.setBackgroundColor(rateButtonBackgroundColor);
        noThanks.setBackgroundColor(rateButtonBackgroundColor);
        rateMe.setTextColor(rateButtonTextColor);
        noThanks.setTextColor(rateButtonTextColor);

    }

    private void configureButtons() {
        rateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
                Log.d(TAG, "Yes: open the Google Play Store");
                RateMeDialogTimer.setOptOut(getActivity(), true);
                if (onActionListener != null) {
                    onActionListener.onHandleRateMeAction(RateMeAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY, ratingBar.getRating());
                }
                dismiss();
            }
        });

        noThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackByEmailEnabled) {
                    DialogFragment dialogMail = FeedbackDialog.newInstance(feedbackEmail, headerBackgroundColor, bodyBackgroundColor, headerTextColor, bodyTextColor, appIconResId, lineDividerColor, rateButtonTextColor, rateButtonBackgroundColor, ratingBar.getRating());
                    dialogMail.show(getFragmentManager(), "feedbackByEmailEnabled");
                    dismiss();
                    Log.d(TAG, "No: open the feedback dialog");
                } else {
                    dismiss();
                    if (onActionListener != null) {
                        onActionListener.onHandleRateMeAction(RateMeAction.LOW_RATING, ratingBar.getRating());
                    }
                }
                RateMeDialogTimer.setOptOut(getActivity(), true);
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

    private void setIconsTitleColor(int colorClose, int colorShare) {
        getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel)
                .setColorFilter(new LightingColorFilter(colorClose, colorClose));
        getResources().getDrawable(android.R.drawable.ic_menu_share)
                .setColorFilter(new LightingColorFilter(colorShare, colorShare));
    }

    /**
     * Listener for the final action that the user takes.
     */
    public interface RateMeOnActionListener {
        void onHandleRateMeAction(RateMeAction action, float rating);
    }

    /**
     * Different actions that the user can take.
     */
    public enum RateMeAction {
        /** We took them to Google Play. Typically after a good rating. */
        HIGH_RATING_WENT_TO_GOOGLE_PLAY,

        /** After a negative rating, he accepted to give us some feedback. */
        LOW_RATING_GAVE_FEEDBACK,

        /** After a negative rating, he didn't give us some feedback. */
        LOW_RATING_REFUSED_TO_GIVE_FEEDBACK,

        /**
         * Gave a negative rating. Providing feedback is not configured, so the user gave the rating
         * and left.
         */
        LOW_RATING,

        /**
         * Dismissed the dialog with the cross in the upper-right corner. Note that we do NOT track
         * if the user dismisses the dialog through the back button.
         */
        DISMISSED_WITH_CROSS,

        /** Shared the link to the app through the button in the top-right corner. */
        SHARED_APP
    }

    public static class Builder {
        private static final int LINE_DIVIDER_COLOR_UNSET = -1;
        private final String appPackageName;
        private int headerBackgroundColor = Color.BLACK;
        private int headerTextColor = Color.WHITE;
        private int bodyBackgroundColor = Color.DKGRAY;
        private int bodyTextColor = Color.WHITE;
        private boolean feedbackByEmailEnabled = false;
        private String feedbackEmail = null;
        private boolean showShareButton = false;
        private int appIconResId = 0;
        private int lineDividerColor = LINE_DIVIDER_COLOR_UNSET;
        private int rateButtonBackgroundColor = Color.BLACK;
        private int rateButtonTextColor = Color.WHITE;
        private int rateButtonPressedBackgroundColor = Color.GRAY;
        private int defaultStarsSelected = 0;
        private int iconCloseColor = Color.WHITE;
        private int iconShareColor = Color.WHITE;
        private boolean showOKButtonByDefault = true;
        private RateMeOnActionListener onActionListener = new RateMeOnActionListener() {
            @Override
            public void onHandleRateMeAction(RateMeAction action, float rating) {
                Log.d(TAG, "Action " + action + " (rating: " + rating + ")");
            }
        };

        /**
         * @param appPackageName package name of the application. Available in {@code Context.getPackageName()}.
         */
        public Builder(String appPackageName) {
            this.appPackageName = appPackageName;
        }

        public Builder setHeaderBackgroundColor(int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            return this;
        }

        public Builder setHeaderTextColor(int headerTextColor) {
            this.headerTextColor = headerTextColor;
            return this;
        }

        public Builder setBodyBackgroundColor(int bodyBackgroundColor) {
            this.bodyBackgroundColor = bodyBackgroundColor;
            return this;
        }

        public Builder setBodyTextColor(int bodyTextColor) {
            this.bodyTextColor = bodyTextColor;
            return this;
        }

        /**
         * Enables a second dialog that opens if the rating is low, from which the user can send
         * an e-mail to the provided e-mail address.
         */
        public Builder enableFeedbackByEmail(String email) {
            this.feedbackByEmailEnabled = true;
            this.feedbackEmail = email;
            return this;
        }

        public Builder setShowShareButton(boolean showShareButton) {
            this.showShareButton = showShareButton;
            return this;
        }

        public Builder setLineDividerColor(int lineDividerColor) {
            this.lineDividerColor = lineDividerColor;
            return this;
        }
        
        /**
         * Sets an icon to be placed on the left-hand side of the dialog. No icon will show up
         * otherwise.
         *
         * Careful: before 3.0.0, there was a default icon.
         */
        public Builder showAppIcon(int appIconResId) {
            this.appIconResId = appIconResId;
            return this;
        }

        public Builder setRateButtonBackgroundColor(int rateButtonBackgroundColor) {
            this.rateButtonBackgroundColor = rateButtonBackgroundColor;
            return this;
        }

        public Builder setRateButtonTextColor(int rateButtonTextColor) {
            this.rateButtonTextColor = rateButtonTextColor;
            return this;
        }

        public Builder setRateButtonPressedBackgroundColor(int rateButtonPressedBackgroundColor) {
            this.rateButtonPressedBackgroundColor = rateButtonPressedBackgroundColor;
            return this;
        }

        public Builder setDefaultNumberOfStars(int numStars) {
            this.defaultStarsSelected = numStars;
            return this;
        }

        public Builder setIconCloseColorFilter(int iconColor) {
            this.iconCloseColor = iconColor;
            return this;
        }

        public Builder setIconShareColorFilter(int iconColor) {
            this.iconShareColor = iconColor;
            return this;
        }

        public Builder setShowOKButtonByDefault(boolean visible) {
            this.showOKButtonByDefault = visible;
            return this;
        }

        /**
        * Sets a listener that will get notified after the action executes the final action in the
        * dialog, such as rating the app or deciding to leave some feedback. Typically you want to
        * track this to have some usage statistics.
        */
        public Builder setOnActionListener(RateMeOnActionListener onActionListener) {
            this.onActionListener = onActionListener;
            return this;
        }

        public RateMeDialog build() {
            if (lineDividerColor == LINE_DIVIDER_COLOR_UNSET) {
                lineDividerColor = headerBackgroundColor;
            }
            return new RateMeDialog(appPackageName,
                    headerBackgroundColor,
                    headerTextColor,
                    bodyBackgroundColor,
                    bodyTextColor,
                    feedbackByEmailEnabled,
                    feedbackEmail,
                    showShareButton,
                    appIconResId,
                    lineDividerColor,
                    rateButtonBackgroundColor,
                    rateButtonTextColor,
                    rateButtonPressedBackgroundColor,
                    defaultStarsSelected,
                    iconCloseColor,
                    iconShareColor,
                    showOKButtonByDefault,
                    onActionListener);
        }
    }
}
