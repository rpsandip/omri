create index IX_F0340058 on OMRI_Appointment (appointmetDate);
create index IX_83807AD8 on OMRI_Appointment (clinicId, status);
create index IX_DF6303C5 on OMRI_Appointment (patientId, clinicId, resourceId, specificationId);
create index IX_73A06C7C on OMRI_Appointment (patientId, resourceId, specificationId);
create index IX_DDA416D8 on OMRI_Appointment (resourceId);

create index IX_1C3D2C42 on OMRI_Clinic (clinicorganizationId);

create index IX_3C9889F5 on OMRI_Clinic_Resource (clinicId, resourceId);

create index IX_9D4A5F8C on OMRI_CustomUser (lrUserId);

create index IX_B64B7C6E on OMRI_Patient (createdBy);

create index IX_AB349129 on OMRI_Patient_Clinic (clinicId);
create index IX_DC81C071 on OMRI_Patient_Clinic (patientId, createdBy);

create index IX_BBF5CFEF on OMRI_Patient_Clinic_Resource (patientId, clinicId, createdBy);

create index IX_6D39D3F on OMRI_Resource_Specification (resourceId);