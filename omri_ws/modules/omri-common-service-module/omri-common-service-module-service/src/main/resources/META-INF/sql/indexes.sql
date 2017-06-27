create index IX_F0340058 on OMRI_Appointment (appointmetDate);
create index IX_83807AD8 on OMRI_Appointment (clinicId, status);
create index IX_DF6303C5 on OMRI_Appointment (patientId, clinicId, resourceId, specificationId);
create index IX_73A06C7C on OMRI_Appointment (patientId, resourceId, specificationId);
create index IX_E0C9705B on OMRI_Appointment (patientId, status);
create index IX_7420F283 on OMRI_Appointment (procedureId);
create index IX_DDA416D8 on OMRI_Appointment (resourceId);

create index IX_1C3D2C42 on OMRI_Clinic (clinicorganizationId);

create index IX_3C9889F5 on OMRI_Clinic_Resource (clinicId, resourceId);

create index IX_9D4A5F8C on OMRI_CustomUser (lrUserId);

create index IX_B64B7C6E on OMRI_Patient (createdBy);

create index IX_AB349129 on OMRI_Patient_Clinic (clinicId);
create index IX_DC81C071 on OMRI_Patient_Clinic (patientId, createdBy);

create index IX_BBF5CFEF on OMRI_Patient_Clinic_Resource (patientId, clinicId, createdBy);

create index IX_17C009DD on OMRI_Procedure (clinicId, isComplete);
create index IX_B9D88E4A on OMRI_Procedure (clinicId, patientId);

create index IX_7BEBA7CF on OMRI_Resource_Specification (resourceId, specificationId);