package com.example.hospitalsystem;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * An activity for viewing individual entries in detail.
 */
public class VisitRecord extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visit_record);
		
		Intent intent = getIntent();
		int position = intent.getIntExtra(PreviousRecord.RECORD_POSITION, 0);
		long id = intent.getLongExtra(PreviousRecord.ROW_ID, 0);//leave for further topics
		String healthCardNumber = intent.getStringExtra(PreviousRecord.HCN_VISIT);
		
		PatientSystem patientSystem = null;
		File deserialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
		try {
			patientSystem = PatientSystem.deserialize(deserialize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		TextView VisitRecordtextView2 = (TextView)findViewById(R.id.VisitRecordtextView2);
		VisitRecordtextView2.setText(patientSystem.findPatientByHealthCard(healthCardNumber).getName());
		TextView VisitRecordtextView4 = (TextView)findViewById(R.id.VisitRecordtextView4);
		VisitRecordtextView4.setText(patientSystem
									.findPatientByHealthCard(healthCardNumber)
									.getVitalSignsRecord()
									.get(position)
									.getTime()
									.toString()
									);
		TextView VitalSignsRecord = (TextView)findViewById(R.id.VitalSignsRecord);
		VitalSignsRecord.setText(patientSystem
								.findPatientByHealthCard(healthCardNumber)
								.getVitalSignsRecord()
								.get(position)
								.toString()
								);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visit_record, menu);
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
}
