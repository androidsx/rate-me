package com.androidsx.rateme.demo1;

import com.androidsx.libraryrateme.DialogRateMe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidcourse.t4.t6.R;

@SuppressLint("NewApi")
public class SampleProject extends Activity {

    private Button buttonRateMe;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts); 
		buttonRateMe = (Button)findViewById(R.id.buttonrateme);		
		buttonRateMe.setOnClickListener(new OnClickListener() {          
            @Override
            public void onClick(View v) {
                AlertMenu();            
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click

		switch (item.getItemId()) {
		case R.id.RateMe:
		    AlertMenu();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	@SuppressLint("NewApi")
    private void AlertMenu (){
	    
	    
//	    DialogFragment dialogo = libraryRateMe.newInstance(
//                getPackageName());
	    DialogFragment dialogo = DialogRateMe.newInstance(
                "com.androidsx.smileys");
        dialogo.show(getFragmentManager(), "dialog");
	    
	}

}
