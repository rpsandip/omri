package com.omri.service.common.util;

public enum AppointmentStatus {
	CREATED(0,"Appointment Created"),IN_RESOURCE_PROCESS(1,"IN Resource Process"), TECHNOLOGIST_REPORT_SUBMITTED(2,"Technologist Report Submitted"),IN_BILLING_PROCESS(3,"IN Billing Process"),CLOSED(4,"Closed"),;
	private int value;
	private String label;
	
	private AppointmentStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }
	public static AppointmentStatus findByValue(int value){
		AppointmentStatus appointmentStatus = null;
		for (AppointmentStatus appointmentStatusStr : AppointmentStatus.values()) {
            if(appointmentStatusStr.value==value){
            	return appointmentStatusStr;
            }
        }
		return appointmentStatus;
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
