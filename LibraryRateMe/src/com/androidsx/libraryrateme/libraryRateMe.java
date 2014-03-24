package com.androidsx.libraryrateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

public class libraryRateMe extends DialogFragment{
    
   
    private String appPackageName;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private View mView;
    private View tView;
    

   
    
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
        
        mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
                
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar1);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getActivity(), "Puntuacion: "+String.valueOf(rating), Toast.LENGTH_SHORT).show();       
                
            }
        });
        
        builder   
        .setCustomTitle(tView);
        
        
        
//        .setPositiveButton(R.string.rateme, new DialogInterface.OnClickListener()  {
//               public void onClick(DialogInterface dialog, int id) {
//                    Log.i("Dialogos", "Confirmacion Aceptada.");
//                    //rateApp(appPackageName);
//                     
//                   }
//               })
//        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//               public void onClick(DialogInterface dialog, int id) {
//                        Log.i("Dialogos", "Confirmacion Cancelada.");
//                        dialog.cancel();
//                   }
//               });
 
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
