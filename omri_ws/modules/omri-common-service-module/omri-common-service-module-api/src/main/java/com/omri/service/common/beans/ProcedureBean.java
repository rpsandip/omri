package com.omri.service.common.beans;

import java.util.List;

public class ProcedureBean {
	private long procedureId;
	private List<AppointmentBean> appointmentList;
	
	public ProcedureBean(long procedureId){
		this.procedureId=procedureId;
	}
	
	public long getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(long procedureId) {
		this.procedureId = procedureId;
	}
	public List<AppointmentBean> getAppointmentList() {
		return appointmentList;
	}
	public void setAppointmentList(List<AppointmentBean> appointmentList) {
		this.appointmentList = appointmentList;
	} 
}
