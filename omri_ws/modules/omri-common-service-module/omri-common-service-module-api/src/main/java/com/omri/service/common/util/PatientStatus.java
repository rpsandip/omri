package com.omri.service.common.util;

public enum PatientStatus {
	REFERRAL_RECEIVED(0), LOP_RECEIVED(1), PATIENT_CONTACTED(2);
	
	private int value;
	
	private PatientStatus(int value) {
        this.value = value;
    }
	
	public void setValue(int value) {
        this.value = value;
    }
	public int getValue() {
        return value;
    }
}
