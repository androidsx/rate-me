package com.androidsx.rateme.demo1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidsx.rateme.DialogRateMe;
import com.androidsx.rateme.RateMeDialogTimer;
import com.androidsx.rateme.demo.R;

public class SampleProject extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_project);
    }

    public void onPlainRateMeButtonClick(View view) {
        showPlainRateMeDialog();
    }

    public void onCustomRateMeButtonClick(View view) {
        showCustomRateMeDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rateme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.RateMe: {
            showPlainRateMeDialog();
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final int launchTimes = 3;
        final int installDate = 7;

        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this, installDate, launchTimes)) {
            showPlainRateMeDialog();
        }

    }

    private void showPlainRateMeDialog() {
        new DialogRateMe.Builder(this)
                .setEmail("email@example.com")
                .setGoToMail(true)
                .build()
                .show(getFragmentManager(), "plain-dialog");
    }

    private void showCustomRateMeDialog() {
        new DialogRateMe.Builder(this)
                .setEmail("email@example.com")
                .setGoToMail(true)
                .setLogoResourceId(R.drawable.ic_launcher)
                .setTitleBackgroundColor(getResources().getColor(R.color.dialog_primary))
                .setDialogColor(getResources().getColor(R.color.dialog_primary_light))
                .setTextColor(getResources().getColor(R.color.dialog_text_foreground))
                .setRateButtonBackgroundColor(getResources().getColor(R.color.dialog_primary))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.dialog_primary_dark))
                .setShowShareButton(true)
                .build()
                .show(getFragmentManager(), "custom-dialog");
    }
}
