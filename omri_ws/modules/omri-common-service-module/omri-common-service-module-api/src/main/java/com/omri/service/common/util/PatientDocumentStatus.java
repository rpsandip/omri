package com.omri.service.common.util;

public enum PatientDocumentStatus {
	
	PENDING(0,"Pending"), APPROVED(1,"Approved");
	
	private int value;
	private String label;
	
	
	public static PatientDocumentStatus findByValue(int value){
		PatientDocumentStatus patientStatus = null;
		for (PatientDocumentStatus patientDocumentStatusItr : PatientDocumentStatus.values()) {
            if(patientDocumentStatusItr.value==value){
            	return patientDocumentStatusItr;
            }
        }
		return patientStatus;
	}
	
	private PatientDocumentStatus(int value, String label) {
        this.value = value;
        this.label = label;
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
