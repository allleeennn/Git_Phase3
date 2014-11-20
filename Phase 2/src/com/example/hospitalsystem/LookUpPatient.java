package com.example.hospitalsystem;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity for searching a patient in the database by his/her health card number.
 */
public class LookUpPatient extends Activity implements OnClickListener {
	public final static String HEALTH_CARD_NUM = "com.example.hospitalsystem.HEALTH_CARD_NUMBER";
	private Button search;
	private PatientSystem patientSystem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_up_patient);
		search = (Button) findViewById(R.id.lookUp);
		search.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				onClickSearch();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.look_up_patient, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Executed upon clicking the confirm button. Deserializes and checks the patient database for the specified patient,
	 * pushes it as an extra onto the IndividualData activity, then starts the IndividualData activity if a match is found.
	 * Modifies a textView to display a user friendly error message if patient is not found. 
	 */
	public void onClickSearch() {
		//change this; just there to test navigatability of activities
		Intent searchIntent = new Intent(this, IndividualData.class);
		
		//Get health card number from the text field
		EditText healthCardNumber = (EditText) findViewById(R.id.LookUpEditText1);
		String healthCardNum = healthCardNumber.getText().toString();
		searchIntent.putExtra(HEALTH_CARD_NUM, healthCardNum);
		File deserialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
		try {
			patientSystem = PatientSystem.deserialize(deserialize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (patientSystem.findPatientByHealthCard(healthCardNum) != null) {
			startActivity(searchIntent);
		}
		else {
			TextView error = (TextView) findViewById(R.id.Error);
			error.setText("That health card number is not in the system.\n Please enter a valid health card number");
		}
	}
}
