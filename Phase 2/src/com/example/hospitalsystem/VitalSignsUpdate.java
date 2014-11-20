package com.example.hospitalsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
 * This activity allows creation of a new entry and saving it into the database.
 */
public class VitalSignsUpdate extends Activity implements OnClickListener {
	String healthCardNumber = null;
	private Button cTime;
	EditText timeView;
	SimpleDateFormat sdf;
	Calendar currentTime;
	private Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vital_signs_update);	
		timeView = (EditText)findViewById(R.id.vitalSignsTime);
		cTime = (Button) findViewById(R.id.currentTime);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				updateVitalSigns(v);
			}
		});
		currentTime = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String strDate = sdf.format(currentTime.getTime());
		timeView.setText(strDate);
		cTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onClickTime();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vital_signs_update, menu);
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
	 * Executed upon clicking the save button. Creates a new vitalSigns object based on the text in the textViews 
	 * and if valid, saves this to the database and closes this activity. Otherwise, modify a textView containing 
	 * a user friendly message containing the nature of the error in the input.
	 * @param view
	 */
	public void updateVitalSigns(View view){
		//Intent jumpBack = new Intent(this, PreviousRecord.class);
		//jumpBack.putExtra("HealthCardNumber",healthCardNumber);
		//startActivity(jumpBack);
		Boolean properDateTyped = false;
		EditText sysPressure = (EditText)findViewById(R.id.sysPressure);
		EditText diaPressure = (EditText)findViewById(R.id.diaPressure);
		EditText heartRate = (EditText)findViewById(R.id.heartRate);
		EditText temperature = (EditText)findViewById(R.id.temperature);
		TextView vitalSignsTime = (TextView)findViewById(R.id.vitalSignsTime);

		TextView successInfo = (TextView)findViewById(R.id.successInfo);


		try {Time inputTime = new Time(vitalSignsTime.getText().toString());
		properDateTyped = true;
		VitalSigns newVitalSigns = new VitalSigns(
				Float.parseFloat(temperature.getText().toString()),
				Integer.parseInt(sysPressure.getText().toString()),
				Integer.parseInt(diaPressure.getText().toString()),
				Integer.parseInt(heartRate.getText().toString()),
				inputTime
				);
		successInfo.setText("Successfully updated." +"\n" + newVitalSigns.toString());

		PatientSystem patientSystem = null;
		File deserialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
		try {
			patientSystem = PatientSystem.deserialize(deserialize);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Intent intent = getIntent();
		String healthCardNumber = intent.getStringExtra(IndividualData.HCN_UPDATE);
		patientSystem.findPatientByHealthCard(healthCardNumber).addVitalSigns(newVitalSigns);



		File serialize = new File(this.getApplicationContext().getFilesDir().getAbsolutePath());
		try {
			patientSystem.findPatientByHealthCard(healthCardNumber).serialize(serialize);
			finish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}}
		catch (NumberFormatException e){
			if(properDateTyped) {
				successInfo.setText("Please fill in all boxes with valid numbers.");
			}
			else{
				successInfo.setText("Please input a properly formatted date.");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			successInfo.setText("Please input a properly formatted date.");
		}
	}

	
	/**
	 * Executed upon clicking the use current time button. Changes the editText for inputting 
	 * date into one representing the current system time and date.
	 */
	public void onClickTime() {
		timeView.setText(sdf.format(currentTime.getTime()));
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
