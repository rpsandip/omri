create index IX_901B57F8 on OMRI_Clinic_Resource (clinicId);

create index IX_9D4A5F8C on OMRI_CustomUser (lrUserId);

create index IX_B64B7C6E on OMRI_Patient (createdBy);

create index IX_AB349129 on OMRI_Patient_Clinic (clinicId);
create index IX_DC81C071 on OMRI_Patient_Clinic (patientId, createdBy);

create index IX_BBF5CFEF on OMRI_Patient_Clinic_Resource (patientId, clinicId, createdBy);

create index IX_6D39D3F on OMRI_Resource_Specification (resourceId);