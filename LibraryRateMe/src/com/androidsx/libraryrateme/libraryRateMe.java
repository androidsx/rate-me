package com.androidsx.libraryrateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

public class libraryRateMe extends DialogFragment{
    
   
    private String appPackageName;
    private RatingBar ratingBar;
    private static final String KEY_SAVE_RATING_BAR_VALUE = "KEY_SAVE_RATING_BAR_VALUE";
   
    
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
        
        View mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar1);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_SAVE_RATING_BAR_VALUE)) {
                ratingBar.setRating(savedInstanceState.getFloat(KEY_SAVE_RATING_BAR_VALUE));
            }
        }
        
        builder.setMessage(R.string.message)
        .setCancelable(true)
        .setView(mView)
        .setTitle(R.string.title).setIcon(R.drawable.icono)
        
        .setPositiveButton(R.string.rateme, new DialogInterface.OnClickListener()  {
               public void onClick(DialogInterface dialog, int id) {
                    Log.i("Dialogos", "Confirmacion Aceptada.");
                    //rateApp(appPackageName);
                     
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
