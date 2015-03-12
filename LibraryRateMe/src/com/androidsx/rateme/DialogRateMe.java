package com.androidsx.rateme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

public class DialogRateMe extends DialogFragment {
    private static final String TAG = DialogRateMe.class.getSimpleName();

    private static final String MARKET_CONSTANT = "market://details?id=";
    private static final String GOOGLE_PLAY_CONSTANT = "http://play.google.com/store/apps/details?id=";

    // Identify TitleDivider
    private String RESOURCE_NAME = "titleDivider";
    private String DEFAULT_TYPE_RESOURCE = "id";
    private String DEFAULT_PACKAGE = "android";

    // Views
    private View mView;
    private View tView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    // configuration
    private String appPackageName;
    private boolean goToMail;
    private String email;
    private boolean showShareButton;
    private int titleTextColor;
    private int titleBackgroundColor;
    private int dialogColor;
    private int lineDividerColor;
    private int textColor;
    private int logoResId;
    private int rateButtonBackgroundColor;
    private int rateButtonTextColor;
    private int rateButtonPressedBackgroundColor;
    private int defaultStarsSelected;
    private int iconCloseColor;
    private int iconShareColor;
    private boolean showOKButtonByDefault;
    private RateMeOnActionListener onActionListener;
    
    public DialogRateMe() {
        super();
    }

    private DialogRateMe(Builder builder) {
        this.appPackageName = builder.appPackageName;
        this.goToMail = builder.goToMail;
        this.email = builder.email;
        this.showShareButton = builder.showShareButton;
        this.titleTextColor = builder.titleTextColor;
        this.titleBackgroundColor = builder.titleBackgroundColor;
        this.dialogColor = builder.dialogColor;
        this.lineDividerColor = builder.lineDividerColor;
        this.textColor = builder.textColor;
        this.logoResId = builder.logoResId;
        this.rateButtonBackgroundColor = builder.rateButtonBackgroundColor;
        this.rateButtonTextColor = builder.rateButtonTextColor;
        this.rateButtonPressedBackgroundColor = builder.rateButtonPressedBackgroundColor;
        this.defaultStarsSelected = builder.defaultStarsSelected;
        this.iconCloseColor = builder.iconCloseColor;
        this.iconShareColor = builder.iconShareColor;
        this.showOKButtonByDefault = builder.showOKButtonByDefault;
        this.onActionListener = builder.onActionListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeUiFields();
        Log.d(TAG, "initialize correctly all the components");

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
        
        try{
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    RateMeDialogTimer.clearSharedPreferences(getActivity());
                    Log.d(TAG, "clear preferences");
                    RateMeDialogTimer.setOptOut(getActivity(), true);
                    if (onActionListener != null) {
                        onActionListener.onHandleRateMeAction(RateMeAction.DISMISSED_WITH_CROSS, ratingBar.getRating());
                    }
                }
            });

        } catch (Exception e) {
            Log.d(TAG,"Error closing the dialog" + e);
            dismiss();
        }
        
        try{
            share.setVisibility(showShareButton ? View.VISIBLE : View.GONE);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(shareApp(appPackageName));
                    Log.d(TAG, "share App");
                    if (onActionListener != null) {
                        onActionListener.onHandleRateMeAction(RateMeAction.SHARED_APP, ratingBar.getRating());
                    }

                }
            });
        }catch (Exception e){
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
            this.goToMail = savedInstanceState.getBoolean("goToMail");
            this.email = savedInstanceState.getString("email");
            this.showShareButton = savedInstanceState.getBoolean("showShareButton");
            this.titleTextColor = savedInstanceState.getInt("titleTextColor");
            this.titleBackgroundColor = savedInstanceState.getInt("titleBackgroundColor");
            this.dialogColor = savedInstanceState.getInt("dialogColor");
            this.lineDividerColor = savedInstanceState.getInt("lineDividerColor");
            this.textColor = savedInstanceState.getInt("textColor");
            this.logoResId = savedInstanceState.getInt("logoResId");
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
        outState.putBoolean("goToMail", goToMail);
        outState.putString("email", email);
        outState.putBoolean("showShareButton", showShareButton);
        outState.putInt("titleTextColor", titleTextColor);
        outState.putInt("titleBackgroundColor", titleBackgroundColor);
        outState.putInt("dialogColor", dialogColor);
        outState.putInt("lineDividerColor", lineDividerColor);
        outState.putInt("textColor", textColor);
        outState.putInt("logoResId", logoResId);
        outState.putInt("rateButtonBackgroundColor", rateButtonBackgroundColor);
        outState.putInt("rateButtonTextColor", rateButtonTextColor);
        outState.putInt("rateButtonPressedBackgroundColor", rateButtonPressedBackgroundColor);
        outState.putInt("defaultStarsSelected", defaultStarsSelected );
        outState.putInt("iconCloseColor", iconCloseColor );
        outState.putInt("iconShareColor", iconShareColor);
        outState.putBoolean("showOKButtonByDefault", showOKButtonByDefault);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            onActionListener = (RateMeOnActionListener) activity;
        } catch (ClassCastException e) {
            // throw new ClassCastException("An activity hosting a DialogRateMe fragment must implement the necessary listener interface: " + RateMeOnActionListener.class.getName());
        }
        
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
        mView = getActivity().getLayoutInflater().inflate(R.layout.rateme_dialog_message, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.rateme_dialog_title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        mView.setBackgroundColor(dialogColor);
        tView.setBackgroundColor(titleBackgroundColor);
        ((TextView) tView.findViewById(R.id.dialog_tiitle)).setTextColor(titleTextColor);
        if (logoResId > 0) {
            ((ImageView) mView.findViewById(R.id.app_icon_dialog_rating)).setImageResource(logoResId);
        }
        else {
            ((ImageView) mView.findViewById(R.id.app_icon_dialog_rating)).setVisibility(View.GONE);
        }
        ((TextView) mView.findViewById(R.id.rating_dialog_message)).setTextColor(textColor);

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
                Log.d(TAG, "go to Google Play Store for Rate-Me");
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
                if (goToMail) {
                    DialogFragment dialogMail = DialogFeedback.newInstance(email, titleBackgroundColor, dialogColor, textColor, logoResId, rateButtonTextColor, rateButtonBackgroundColor, lineDividerColor, ratingBar.getRating() );
                    dialogMail.show(getFragmentManager(), "goToMail");
                    dismiss();
                    Log.d(TAG, "got to Mail for explain what is the problem");
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
        Drawable iconClose = getResources().getDrawable(R.drawable.ic_action_cancel);
        Drawable iconShare = getResources().getDrawable(R.drawable.ic_action_share);
        ColorFilter filterIconClose = new LightingColorFilter(colorClose, colorClose);
        ColorFilter filterIconShare = new LightingColorFilter(colorShare, colorShare);
        iconClose.setColorFilter(filterIconClose);
        iconShare.setColorFilter(filterIconShare);
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
        SHARED_APP;
    }

    public static class Builder {
        private String appPackageName;
        private boolean goToMail;
        private String email;
        private boolean showShareButton = false;
        private int titleTextColor = Color.WHITE;
        private int titleBackgroundColor = Color.BLACK;
        private int dialogColor = Color.DKGRAY;
        private int lineDividerColor = dialogColor;
        private int textColor = Color.WHITE;
        private int logoResId = R.drawable.ic_launcher;
        private int rateButtonBackgroundColor = Color.BLACK;
        private int rateButtonTextColor = Color.WHITE;
        private int rateButtonPressedBackgroundColor = Color.GRAY;
        private int defaultStarsSelected = 0;
        private int iconCloseColor = Color.WHITE;
        private int iconShareColor = Color.WHITE;
        private boolean showOKButtonByDefault = true;

        /**
        * Default implementation for the action listener, that just logs every action.
        */
        private RateMeOnActionListener onActionListener = new RateMeOnActionListener() {
            @Override
            public void onHandleRateMeAction(RateMeAction action, float rating) {
            Log.d(TAG, "Action " + action + " (rating: " + rating + ")");
            }
        };

        public Builder(Context ctx) {
            this.appPackageName = ctx.getApplicationContext().getPackageName();
        }

        public Builder(DialogFeedback dialogFeedback) {
            return;
        }

        /**
         * Set the company email when select {@code true} in {@link #setGoToMail(boolean)}
         * if you select {@code false}, no need to configure
         * @param email String variable is a email of company
         * @return
         */
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setShowShareButton(boolean showShareButton) {
            this.showShareButton = showShareButton;
            return this;
        }
        
        /**
         * Set if you want to display a new dialog when the user selects less than 4 star. On this
         * dialog the user may send feedback for your App
         * If you select {@code true} you must set the mail with {@link #setEmail(String)}, if not the App
         * will closed by exception : IllegalArgumentException
         *
         * @param goToMail boolean variable to see new dialog for get feedback in your App
         * @return this builder
         */
        public Builder setGoToMail(boolean goToMail) {
            this.goToMail = goToMail;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setTitleBackgroundColor(int titleBackgroundColor) {
            this.titleBackgroundColor = titleBackgroundColor;
            return this;
        }

        public Builder setDialogColor(int dialogColor) {
            this.dialogColor = dialogColor;
            return this;
        }

        public Builder setLineDividerColor(int lineDividerColor) {
            this.lineDividerColor = lineDividerColor;
            return this;
        }

        /** Set a icon that will take the dialog. If not you set any icon, the icon that will
         * put by default is {@code ic_launcher}
         *
         * @param logoResId App icon
         * @return this builder
         */
        public Builder setLogoResourceId(int logoResId) {
            this.logoResId = logoResId;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
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

        public Builder setDefaultStartSelected(int numStars) {
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
        *
        * @param onActionListener listener for the final user action
        * @return this builder
        */
        public Builder setOnActionListener(RateMeOnActionListener onActionListener) {
            this.onActionListener = onActionListener;
            return this;
        }

        public RateMeOnActionListener getOnActionListener() {
            return onActionListener;
        }

        public DialogRateMe build() {
            if (goToMail && email == null) {
                throw new IllegalArgumentException("You Have to configure the email for the dialog goToMail");
            }
            return new DialogRateMe(this);
        }
        
    }

}