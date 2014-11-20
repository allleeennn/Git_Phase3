package com.example.hospitalsystem;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewPatient extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_patient, menu);
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
	
	public void createNewPatient(View view){
		EditText name = (EditText)findViewById(R.id.newPatientName);
		EditText dob = (EditText)findViewById(R.id.patientDOB);
		EditText hcn = (EditText)findViewById(R.id.NewPatientHCN);
		
		TextView success = (TextView)findViewById(R.id.addedNewPatient);
		
		String namePatient = name.getText().toString();
		String dobPatient = dob.getText().toString();
		String hcnPatient = hcn.getText().toString();
		
		String [] parts = dobPatient.split("-");
		String yearStr = parts[0];
		String monthStr = parts[1];
		String dayStr = parts[2];
		
		int year = Integer.parseInt(yearStr);
		int month = Integer.parseInt(monthStr);
		int day = Integer.parseInt(dayStr);
		
		Time dobTime = new Time(year, month, day);
		
		try{
			Patient newPatient = new Patient(namePatient, hcnPatient, dobTime);
			
			success.setText(R.string.Success + namePatient);
			
			PatientSystem patientSystem = null;
			File deserialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
			try {
				patientSystem = PatientSystem.deserialize(deserialize);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			patientSystem.addPatient(newPatient);
			
			File serialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
			try {
				patientSystem.addPatient(newPatient).serialize(serialize);
				finish();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e){
			success.setText(R.string.Fail);
			}
	}
}

