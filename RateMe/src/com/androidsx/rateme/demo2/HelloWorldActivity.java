package com.androidsx.rateme.demo2;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidsx.rateme.R;

import com.androidsx.rateme.RateMeDialogTimer;
import com.androidsx.rateme.DialogRateMe;

public class HelloWorldActivity extends Activity {
    private Button btnRateme;
    private static final String MY_PACKAGE_NAME = "com.androidsx.smileys";
    private static final String EMAIL = "yourmail@mail.com";
    private static final int INSTALL_DAYS = 20;
    private static final int LAUNCH_TIMES = 5;

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
        RateMeDialogTimer rateDialog = new RateMeDialogTimer(INSTALL_DAYS,LAUNCH_TIMES);
        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this)) {
            DialogFragment dialog = DialogRateMe.newInstance(
                    MY_PACKAGE_NAME,EMAIL);
            dialog.show(getFragmentManager(), "dialog");
        }
        
    }

    public void alertMenu (){
        
//        DialogFragment dialogo = DialogRateMe.newInstance(
//                getPackageName());
//        dialogo.show(getFragmentManager(), "dialog");
        DialogFragment dialog = DialogRateMe.newInstance(
                MY_PACKAGE_NAME,EMAIL);
        dialog.show(getFragmentManager(), "dialog");
    }
}
