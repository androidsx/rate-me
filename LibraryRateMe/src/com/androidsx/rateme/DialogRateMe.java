package com.androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
    private View confirDialogTitleView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    // configuration
    private final String appPackageName;
    private final boolean goToMail;
    private final String email;
    private final boolean showShareButton;
    private final int titleTextColor;
    private final int titleBackgroundColor;
    private final int dialogColor;
    private final int lineDividerColor;
    private final int textColor;
    private final int logoResId;
    private final int rateButtonBackgroundColor;
    private final int rateButtonTextColor;
    private final int rateButtonPressedBackgroundColor;
    private final int defaultStarsSelected;
    private final int iconCloseColor;
    private final int iconShareColor;
    private final boolean showOKButtonByDefault;
    private final RateMeOnActionListener onActionListener;

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
        if (!showOKButtonByDefault) {
            ratingBar.setRating((float) defaultStarsSelected);
        }
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
        ratingBar.setRating((float) defaultStarsSelected);
        configureButtons();
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                RateMeDialogTimer.clearSharedPreferences(getActivity());
                Log.d(TAG, "clear preferences");
                RateMeDialogTimer.setOptOut(getActivity(), true);
                onActionListener.onActionPerformed(RateMeAction.DISMISSED_WITH_CROSS, ratingBar.getRating());
            }
        });
        share.setVisibility(showShareButton ? View.VISIBLE : View.GONE);
        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(shareApp(appPackageName));
                Log.d(TAG, "share App");
                onActionListener.onActionPerformed(RateMeAction.SHARED_APP, ratingBar.getRating());
            }
        });

        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create();
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
        GradientDrawable rateMeButton = (GradientDrawable) getResources().getDrawable(R.drawable.customshape);
        GradientDrawable rateMeButtonItemSelected = (GradientDrawable) getResources().getDrawable(
                R.drawable.itemselected);
        rateMeButton.setColor(rateButtonBackgroundColor);
        rateMeButtonItemSelected.setColor(rateButtonPressedBackgroundColor);
        mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        mView.setBackgroundColor(dialogColor);
        tView.setBackgroundColor(titleBackgroundColor);
        ((TextView) tView.findViewById(R.id.title)).setTextColor(titleTextColor);
        ((ImageView) mView.findViewById(R.id.picture)).setImageResource(logoResId);
        ((TextView) mView.findViewById(R.id.phraseCenter)).setTextColor(textColor);
        rateMe.setTextColor(rateButtonTextColor);
        noThanks.setTextColor(rateButtonTextColor);

        // Confirmation Dialog
        confirDialogTitleView = getActivity().getLayoutInflater().inflate(R.layout.confirmationtitledialog, null);
        confirDialogTitleView.setBackgroundColor(dialogColor);
        ((TextView) confirDialogTitleView.findViewById(R.id.confirmDialogTitle)).setTextColor(textColor);
        ((ImageView) confirDialogTitleView.findViewById(R.id.iconConfirmDialog)).setImageResource(logoResId);
    }

    private void configureButtons() {
        rateMe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
                Log.d(TAG, "go to Google Play Store for Rate-Me");
                RateMeDialogTimer.setOptOut(getActivity(), true);
                onActionListener.onActionPerformed(RateMeAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY, ratingBar.getRating());
                dismiss();
            }
        });

        noThanks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goToMail) {
                    confirmGoToMailDialog(getArguments()).show();
                    Log.d(TAG, "got to Mail for explain what is the problem");
                } else {
                    dismiss();
                    onActionListener.onActionPerformed(RateMeAction.LOW_RATING, ratingBar.getRating());
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

    private void goToMail() {
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

        builder.setCustomTitle(confirDialogTitleView).setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToMail();
                        onActionListener.onActionPerformed(RateMeAction.LOW_RATING_GAVE_FEEDBACK, ratingBar.getRating());
                        dismiss();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onActionListener.onActionPerformed(RateMeAction.LOW_RATING_REFUSED_TO_GIVE_FEEDBACK,
                                ratingBar.getRating());
                        dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button cancel = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancel.setTextColor(textColor);
        float size = getResources().getDimension(R.dimen.text_size) / 2;
        cancel.setTextSize(size);
        cancel.setTypeface(null, Typeface.BOLD);
        cancel.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector));
        Button yes = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        yes.setTextColor(textColor);
        yes.setTextSize(size);
        yes.setTypeface(null, Typeface.BOLD);
        yes.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector));
        return dialog;
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
        void onActionPerformed(RateMeAction action, float rating);
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
         * Gave a negative rating. Providing feedback is not configured, so the user gave the rating and left.
         */
        LOW_RATING,

        /**
         * Dismissed the dialog with the cross in the upper-right corner. Note that we do NOT track if the user
         * dismisses the dialog through the back button.
         */
        DISMISSED_WITH_CROSS,

        /** Shared the link to the app through the button in the top-right corner. */
        SHARED_APP;
    }

    public static class Builder {
        private String appPackageName;
        private boolean goToMail;
        private String email;
        private boolean showShareButton;
        private int titleTextColor = Color.WHITE;
        private int titleBackgroundColor = Color.BLACK;
        private int dialogColor = Color.WHITE;
        private int lineDividerColor = Color.GRAY;
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
            public void onActionPerformed(RateMeAction action, float rating) {
                Log.d(TAG, "Action " + action + " (rating: " + rating + ")");
            }
        };

        public Builder(Context ctx) {
            this.appPackageName = ctx.getApplicationContext().getPackageName();
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setShowShareButton(boolean showShareButton) {
            this.showShareButton = showShareButton;
            return this;
        }

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
         * Sets a listener that will get notified after the action executes the final action in the dialog, such as
         * rating the app or deciding to leave some feedback. Typically you want to track this to have some usage
         * statistics.
         * 
         * @param onActionListener listener for the final user action
         * @return this builder
         */
        public Builder setOnActionListener(RateMeOnActionListener onActionListener) {
            this.onActionListener = onActionListener;
            return this;
        }

        public DialogRateMe build() {
            if (goToMail && email == null) {
                throw new IllegalArgumentException("You Have to configure the email for the dialog goToMail");
            }
            return new DialogRateMe(this);
        }

    }

}
