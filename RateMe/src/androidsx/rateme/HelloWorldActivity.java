package androidsx.rateme;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidsx.libraryrateme.DialogRateMe;

public class HelloWorldActivity extends Activity {
    
    
    private Button btnRateme; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);       
        setupUI();            
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    
    
    public void alertMenu (){
        
        DialogFragment dialogo = DialogRateMe.newInstance(
                getPackageName());
        dialogo.show(getFragmentManager(), "dialog");
    }
    
    

    
}
