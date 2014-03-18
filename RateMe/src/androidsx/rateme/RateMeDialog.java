package androidsx.rateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class RateMeDialog extends DialogFragment{
    
    //take the name of the package 
    //final String appPackageName = getPackageName();
    private final String appPackageName = "com.androidsx.smileys";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        
        builder.setMessage(R.string.message)
        .setTitle(R.string.title)       
        .setPositiveButton(R.string.rateme, new DialogInterface.OnClickListener()  {
               public void onClick(DialogInterface dialog, int id) {
                    Log.i("Dialogos", "Confirmacion Aceptada.");
                    rateApp();
                        
                   }
               })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                   }
               });
 
        return builder.create();
    }
    
    private void rateApp (){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+ appPackageName)));
        }
    }
    

    
    

}
