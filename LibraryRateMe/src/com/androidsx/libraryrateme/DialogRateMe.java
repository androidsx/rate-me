package com.androidsx.libraryrateme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class DialogRateMe extends DialogFragment {
	
	private String appPackageName;
	private View mView;
    private View tView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
	
    public static DialogRateMe newInstance(String appName) {
        
    	DialogRateMe dialogo = new DialogRateMe();      
        Bundle args = new Bundle();
        args.putString("name", appName);
        dialogo.setArguments(args);
        return dialogo;
    }
	
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		appPackageName = getArguments().getString("name");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mView = getActivity().getLayoutInflater().inflate(R.layout.library, null);
        tView = getActivity().getLayoutInflater().inflate(R.layout.title, null);
        close = (Button)tView.findViewById(R.id.cerrar);
        
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getActivity(), "Puntuacion: " + String.valueOf(rating), Toast.LENGTH_SHORT).show();

            }
        });
        
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
		
		builder.setView(mView).setCustomTitle(tView)
		
		.setCancelable(false);

		return builder.create();
	}

}
