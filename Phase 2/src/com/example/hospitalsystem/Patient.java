package com.example.hospitalsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {

	/** Serialization UID for Patient object. */
	private static final long serialVersionUID = 7106976089219943980L;
	
	/** Name of this Patient. */
	private String name;
	
	/** Health card of this Patient. */
	private String healthCardNumber;
	
	/** Date of birth of this Patient. */
	private Time dob;
	
	/** Arrival time of this Patient. */
	private Time arrivalTime;
	
	/** A list of vital signs for this Patient. */
	private List<VitalSigns> vitalSignsRecord;
	
	/** A list of times representing when this patient has been seen by a doctor. */
	private List<Time> timeSeenByDoctor;
	
	/** An integer between 0-4 inclusive, representing the urgency level according to this hospital's policy. */
	private int urgency;

	/** A list of prescriptions for this Patient. */
	private List<Prescription> prescriptionsRecord;
	
	/**
	 * Constructs a Patient object with name, date of birth, health card number,
	 * and arrival time.
	 * @param name This Patient's name.
	 * @param healthCardNumber This Patient's health card number.
	 * @param dob This Patient's date of birth.
	 * @param arrivalTime This Patient's arrival time.
	 */
	public Patient(String name, String healthCardNumber, Time dob, Time arrivalTime){
		this.name = name;
		this.healthCardNumber = healthCardNumber;
		this.dob = dob;
		vitalSignsRecord = new ArrayList<VitalSigns>();
		this.arrivalTime = arrivalTime;
		this.timeSeenByDoctor = new ArrayList<Time>();
		prescriptionsRecord = new ArrayList<Prescription>();
	}
	
	/**
	 * Constructs a Patient with just name, health card number, and date of birth.
	 * @param name This Patient's name.
	 * @param healthCardNumber This Patient's health card number.
	 * @param dob This patient's date of birth.
	 */
	public Patient(String name, String healthCardNumber, Time dob){
		this.name = name;
		this.healthCardNumber = healthCardNumber;
		this.dob = dob;
		vitalSignsRecord = new ArrayList<VitalSigns>();
		this.arrivalTime = null;
		this.timeSeenByDoctor = new ArrayList<Time>();
		prescriptionsRecord = new ArrayList<Prescription>();
	}
	
	public Time getArrivalTime() {
		return arrivalTime;
	}
	/**
	 * Adds a VitalSigns object v to this Patient's vitalSignsRecord.
	 * @param v A VitalSigns object.
	 */
	public void addVitalSigns(VitalSigns v) {
		vitalSignsRecord.add(v.insert(vitalSignsRecord),v);
		urgency = v.getUrgency();
	}

	/** 
	 * Adds a Prescription object p to this Patient's prescriptionsRecord.
	 * @param p A Prescription object.
	 */
	public void addPrescription(Prescription p){
		prescriptionsRecord.add(0,p);
	}
	
	/**
	 * Adds this time as a time where the patient has been seen by a doctor, maintaining order from most to least recent.
	 * @param t The time this patient has been seen by a doctor.
	 */
	public void addDoctorVisit(Time t){
		timeSeenByDoctor.add(t.getInsertionIndex(timeSeenByDoctor), t);
	}
	
	/**
	 * Gets a list of the times this patient has been seen by a doctor.
	 * @return A list containing every time that a doctor has seen this patient.
	 */
	public List<Time> getDoctorVisits(){
		return timeSeenByDoctor;
	}
	
	/**
	 * Returns this Patient's name.
	 * @return this Patient's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Return this patent's urgency level according to hospital policy.
	 * @return The current urgency level of this patient.
	 */
	public int getUrgency(){
		//Note here a new time with no arguments is just the current time.
		if(new Time().twoYearsPassed(this.dob)){
			return urgency + 1;
		}
		return urgency;
	}
	
	/**
	 * Returns this Patient's date of birth.
	 * @return this Patient's date of birth.
	 */
	public String getDob() {
		return this.dob.toString();
	}

	/**
	 * Creates a new Prescription object and add it to prescriptionsRecord list.
	 * @param m medication
	 * @param i instructions
	 * @param t time this Prescription is recorded.
	 */
	public void createNewPrescription(String m, String i, Time t){
		Prescription pres = new Prescription(m, i, t);
		prescriptionsRecord.add(0, pres);
	}
	
	/**
	 * Return if this Patient's health card number is number.
	 * @param number The health card number being searched.
	 * @return True if this patient has number as its health card number,
	 * false otherwise.
	 */
	public boolean healthCardNumberEquals(String number) {
		return number.equals(this.healthCardNumber);
	}
	
	/**
	 * Returns this Patient's health card number.
	 * @return this Patient's health card number.
	 */
	public String getHealthCardNumber() {
		return this.healthCardNumber;
	}
	
	/**
	 * Returns this Patient's vitalSignsRecord.
	 * @return this Patient's vitalSignsRecord.
	 */
	public List<VitalSigns> getVitalSignsRecord() {
		return this.vitalSignsRecord;
	}
	
	/**
	 * Returns this Patient's prescriptionsRecord.
	 * @return this Patient's prescriptionsRecord.
	 */
	public List<Prescription> getPrescriptionsRecord(){
		return this.prescriptionsRecord;
	}

	public boolean notSeenByDoctor() {
		return this.timeSeenByDoctor.size() == 0;
	}
	
	/**
	 * Returns an ArrayList containing all the times that a patient has had his/her 
	 * vital signs record updated.
	 * @return An ArrayList containing all times of all VitalSigns. 
	 */
	public ArrayList<String> getArrayListofRecordsTime(){
		ArrayList<String> records = new ArrayList<String>();
		for (VitalSigns iterator:this.getVitalSignsRecord()){
			records.add(iterator.getTime().toString());
		}
		return records; 
	}
	
	/**
	 * Serializes this Patient object to a given file.
	 * @param dir The file.
	 * @throws FileNotFoundException
	 */
	public void serialize(File dir) throws FileNotFoundException {
		FileOutputStream stream = new FileOutputStream(dir + "/" + healthCardNumber + ".ser");
		try {
		ObjectOutputStream oos = new ObjectOutputStream(stream);
		oos.writeObject(this);
		oos.close();
		}catch(IOException i)
		{
			i.printStackTrace();
		}
		
	}
	
	/**
	 * Deserializes a Patient object given a serialized Patient object from a file.
	 * @param dir The file.
	 * @return Null if file is not found.
	 * @throws FileNotFoundException
	 */
	public Patient deserialize(File dir) throws FileNotFoundException {
			FileInputStream fis = new FileInputStream(dir + "/" + healthCardNumber + ".ser");
			try {
				ObjectInputStream ois = new ObjectInputStream(fis);
				try {
					Patient patient = (Patient) ois.readObject();
					ois.close();
					return patient;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ois.close();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	/**
	 * Returns a String representation of this Patient object.
	 * @return a String representation of this Patient object.
	 */
	public String toString() {
		String returnString = "";
		if (arrivalTime == null) {
			returnString = "Name: " + name + "\n" + "Health Card Number: "
					+ healthCardNumber + "\n" + "Birth Date: " + dob.dateString()
					+ "\n";
		}
		else {
		    returnString = "Name: " + name + "\n" + "Health Card Number: "
				+ healthCardNumber + "\n" + "Birth Date: " + dob.dateString()
				+ "\n" + "Time of arrival: " + arrivalTime.toString() + "\n";
		}
		for (int i = 0; i < vitalSignsRecord.size(); i++) {
			returnString += "\n" + vitalSignsRecord.get(i);
		}
		return returnString;
	}
	
}
