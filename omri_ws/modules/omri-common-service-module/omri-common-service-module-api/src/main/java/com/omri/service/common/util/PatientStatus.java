package com.omri.service.common.util;

public enum PatientStatus {
	REFERRAL_RECEIVED(0,"Referral Received"), LOP_RECEIVED(1,"Lop Received"), PATIENT_CONTACTED(2,"Patient contacted"),PATIENT_SCHEDULED(3,"Patient Scheduled"),PATIENT_CHECKED_IN(4,"Patient Checked In"),PATIENT_CANCELLED(5,"Patient Canceled"),
	PATIENT_RESCHEDULED(6,"Patient Rescheduled"), PATIENT_No_SHOWED(7,"Patient No-showed"),STUDY_COMPLATE(8,"Study Complete"),REPORT_RECEIVED(9,"Report Received"),INVOICE_REPORT_SEND(10,"Invoice/Report Sent"),PAYMENT_RECEIVED(11,"Payment Received");
	
	private int value;
	private String label;
	
	private PatientStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
	
	public static PatientStatus findByValue(int value){
		PatientStatus patientStatus = null;
		for (PatientStatus patientStatusItr : PatientStatus.values()) {
            if(patientStatusItr.value==value){
            	return patientStatusItr;
            }
        }
		return patientStatus;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(int value) {
        this.value = value;
    }
	public int getValue() {
        return value;
    }
}
