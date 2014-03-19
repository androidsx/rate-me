package com.androidsx.libraryrateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class libraryRateMe extends DialogFragment{
    
    private String appPackageName;
    
    public static libraryRateMe newInstance(String appName) {
        
        libraryRateMe dialogo = new libraryRateMe();
        Bundle args = new Bundle();
        args.putString("name", appName);
        dialogo.setArguments(args);

        return dialogo;
    }

     
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        appPackageName = getArguments().getString("name");
        
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        
        builder.setMessage(R.string.message)
        .setTitle(R.string.title)       
        .setPositiveButton(R.string.rateme, new DialogInterface.OnClickListener()  {
               public void onClick(DialogInterface dialog, int id) {
                    Log.i("Dialogos", "Confirmacion Aceptada.");
                    rateApp(appPackageName);
                        
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
    
    private void rateApp (String name){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ name)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+ name)));
        }
    }

}
