package com.example.hospitalsystem;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for viewing individual patient's personally identifying data.
 * Also serves as a hub to allow access to updating vital signs record and viewing previous entries.
 */
public class IndividualData extends Activity {
	
	Button update;
	Button viewPrevRecord;
	public final static String HCN_UPDATE = "com.example.hospitalsystem.HEALTH_CARD_NUMBER_UPDATE";
	public final static String HCN_PRE_REC = "com.example.hospitalsystem.HEALTH_CARD_NUMBER_UPDATE";
	String healthCardNumber = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_data);
		
		Intent intent = getIntent();
		String healthCardNumber = intent.getStringExtra(LookUpPatient.HEALTH_CARD_NUM);
		this.healthCardNumber = healthCardNumber;
		PatientSystem patientSystem = null;
		
		File deserialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
		try {
			patientSystem = PatientSystem.deserialize(deserialize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		TextView patientNameLarge= (TextView)findViewById(R.id.individualTextView2);
		patientNameLarge.setText(patientSystem.findPatientByHealthCard(healthCardNumber).getName());
		
		TextView patientNameSmall= (TextView)findViewById(R.id.name);
		patientNameSmall.setText("Name: " + patientSystem.findPatientByHealthCard(healthCardNumber).getName());
		
		TextView IndividualDOBView= (TextView)findViewById(R.id.dob);
		IndividualDOBView.setText("Date of Birth: " + patientSystem.findPatientByHealthCard(healthCardNumber).getDob());
		
		TextView IndividualHCNView= (TextView)findViewById(R.id.healthCardNumber);
		IndividualHCNView.setText("Health Card Number: " + healthCardNumber);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.individual_data, menu);
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

	
	/**
	 * Called upon clicking the update patient record button.
	 * Passes on the health card number of the current patient as an extra and start the VitalSignsUpdate activity.
	 * @param view
	 */
	public void clickUpdate(View view) {
		Intent Intent = new Intent(this, VitalSignsUpdate.class);
		Intent.putExtra(HCN_UPDATE,healthCardNumber);
		startActivity(Intent);
	}
	
	
	/**
	 * Called upon clicking the update view previous records button.
	 * Passes on the health card number of the current patient as an extra and start the PreviousRecord activity.
	 * @param view
	 */
	public void clickPrevRecord(View view){
		Intent Intent = new Intent(this, PreviousRecord.class);
		Intent.putExtra(HCN_PRE_REC,healthCardNumber);
		startActivity(Intent);
	}
	
}
