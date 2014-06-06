package com.androidsx.rateme.demo2;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidsx.rateme.R;

import com.androidsx.rateme.DialogRateMe;
import com.androidsx.rateme.RateMeDialogTimer;

public class HelloWorldActivity extends Activity {
    private Button btnRateme;
    private static final String MY_PACKAGE_NAME = "com.androidsx.smileys";
    private static final String EMAIL_ADDRESS = "hello@world.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);       
        setupUI();            
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hello_world, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public void setupUI (){
        btnRateme = (Button) findViewById(R.id.buttonRateMe);      
        btnRateme.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               alertMenu();
                
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.RateMeOption:
            alertMenu();
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    @Override
    protected void onStart() {
        super.onStart();
        final int launchTimes = 5;
        final int installDate = 20;
        new RateMeDialogTimer(installDate, launchTimes);
        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this)) {
            alertMenu();
        }

    }

    private void alertMenu() {
        boolean showShareButton = true;
        int titleColor = Color.BLACK;
        int dialogColor = Color.GRAY;
        DialogFragment dialog = DialogRateMe.newInstance(MY_PACKAGE_NAME, getString(R.string.email_address),
                showShareButton, titleColor, dialogColor);
        dialog.show(getFragmentManager(), "dialog");
    }
}
