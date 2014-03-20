package androidcourse.t4.t6;

import java.util.ArrayList;

import com.androidsx.libraryrateme.libraryRateMe;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ContactsActivity extends ListActivity {

	private ArrayList<String> contactsList;
	private ContentResolver contentResolver;

	private int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_contacts);

		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getContacts()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete, menu);
		return true;
	}

	private ArrayList<String> getContacts() {
		contactsList = new ArrayList<String>();

		// acces to contentProviders
		contentResolver = getContentResolver();

		// query required data
		Cursor contactsCursor = contentResolver.query(Data.CONTENT_URI, // URI
				new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
						Phone.TYPE }, // projection
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
						+ Phone.NUMBER + " IS NOT NULL", // Selection
				null, // Args
				Data.DISPLAY_NAME + " ASC"); // Sort Order

		// get colum indexes for deisred data

		int nameIndex = contactsCursor.getColumnIndexOrThrow(Data.DISPLAY_NAME);
		int numberIndex = contactsCursor.getColumnIndexOrThrow(Phone.NUMBER);
		int Typetlf = contactsCursor.getColumnIndexOrThrow(Phone.TYPE);

		// read data

		while (contactsCursor.moveToNext()) {
			String name = contactsCursor.getString(nameIndex);
			String number = contactsCursor.getString(numberIndex);
			int tipo = contactsCursor.getInt(Typetlf);
			String type = tipo(tipo);

			contactsList.add(name + " : " + number + "\n" + type);
		}

		return contactsList;
	}

	private String tipo(int Typetlf) {
		String tipo;
		switch (Typetlf) {
		case 1:
			tipo = "HOME";
			break;
		case 2:
			tipo = "MOBILE";
			break;
		case 3:
			tipo = "WORK";
			break;
		default:
			tipo = "OTHER";
			break;
		}
		return tipo;

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		pos = position;
		
		super.onListItemClick(l, v, position, id);
		Toast toast = Toast.makeText(this, contactsList.get(position), 2000);
		toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 50);
		toast.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click

		switch (item.getItemId()) {
		case R.id.action_delete:
			eliminar(pos);

			return true;
		case R.id.RateMe:
		    alertMenu();

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void eliminar(int numero) {
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(Data.CONTENT_URI, null, null, null, null);

		cur.moveToPosition(numero);
		String lookupKey = cur.getString(cur.getColumnIndex(Data.LOOKUP_KEY));
		Uri uri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

		cr.delete(uri, null, null);

		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getContacts()));
		
		Toast toast = Toast.makeText(this, "delete: "+contactsList.get(numero), 2000);
		toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 50);
		toast.show();

	}
	
	private void alertMenu (){
//	    DialogFragment dialogo = libraryRateMe.newInstance(
//                getPackageName());
	    DialogFragment dialogo = libraryRateMe.newInstance(
                "com.androidsx.smileys");
        dialogo.show(getFragmentManager(), "dialog");
	    
	}

}
