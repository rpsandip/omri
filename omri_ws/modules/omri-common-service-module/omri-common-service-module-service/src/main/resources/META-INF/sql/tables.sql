create table OMRI_Appointment (
	appointmentId LONG not null primary key,
	patientId LONG,
	clinicId LONG,
	doctorId LONG,
	lawyerId LONG,
	resourceId LONG,
	specificationId LONG,
	noOfOccurance INTEGER,
	appointmetDate DATE null,
	appointmetEndDate DATE null,
	appointmetProcessTime INTEGER,
	status INTEGER,
	price DOUBLE,
	procedureId LONG,
	technologistComment VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG
);

create table OMRI_Clinic (
	clinicId LONG not null primary key,
	clinicorganizationId LONG,
	clinicorganizationGroupId LONG,
	clinicName VARCHAR(75) null,
	clinicAdminId LONG,
	addressLine1 VARCHAR(75) null,
	addressLine2 VARCHAR(75) null,
	city VARCHAR(75) null,
	state_ VARCHAR(75) null,
	zipcode VARCHAR(75) null,
	country VARCHAR(75) null,
	phoneNo VARCHAR(75) null,
	faxNo VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG
);

create table OMRI_Clinic_Resource (
	clinicId LONG not null,
	specificationId LONG not null,
	resourceId LONG not null,
	operationTime INTEGER,
	price INTEGER,
	specificationName VARCHAR(75) null,
	resourceName VARCHAR(75) null,
	primary key (clinicId, specificationId, resourceId)
);

create table OMRI_CustomUser (
	customUserId LONG not null primary key,
	organizationId LONG,
	organizationGroupId LONG,
	lrUserId LONG,
	parentUserId LONG,
	addressLine1 VARCHAR(75) null,
	addressLine2 VARCHAR(75) null,
	city VARCHAR(75) null,
	state_ VARCHAR(75) null,
	zipcode VARCHAR(75) null,
	phone VARCHAR(75) null,
	fax VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG
);

create table OMRI_Patient (
	patientId LONG not null primary key,
	firstName VARCHAR(75) null,
	lastName VARCHAR(75) null,
	dob DATE null,
	gender VARCHAR(75) null,
	addressLine1 VARCHAR(75) null,
	addressLine2 VARCHAR(75) null,
	phoneNo VARCHAR(75) null,
	cptCode VARCHAR(75) null,
	city VARCHAR(75) null,
	state_ VARCHAR(75) null,
	country VARCHAR(75) null,
	zip VARCHAR(75) null,
	lopNotes VARCHAR(75) null,
	orderNotes VARCHAR(75) null,
	invoiceNotes VARCHAR(75) null,
	otherNotes VARCHAR(75) null,
	reportToDoctor BOOLEAN,
	reportToLawyer BOOLEAN,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG
);

create table OMRI_PatientDetail (
	patientId LONG not null primary key,
	MRIBefore BOOLEAN,
	Claustrophobic BOOLEAN,
	under300lbs BOOLEAN,
	pacemaker BOOLEAN,
	PreviousPatient BOOLEAN,
	MetalInBody BOOLEAN,
	priorSurgery BOOLEAN,
	chanceOfPregnent BOOLEAN,
	overAge60 BOOLEAN,
	labsDone BOOLEAN,
	allergic BOOLEAN,
	diabetic BOOLEAN,
	kidneyProblem BOOLEAN,
	hyperTension BOOLEAN,
	cancer BOOLEAN,
	bloodthinners BOOLEAN,
	allergicToIdodine BOOLEAN,
	claustrophobicDetail VARCHAR(75) null,
	pacemakerDetail VARCHAR(75) null,
	metalInBodyDetail VARCHAR(75) null,
	priorSurgeryDetail VARCHAR(75) null,
	alergicDetail VARCHAR(75) null,
	diabeticDetail VARCHAR(75) null,
	allergicToIdodineDetail VARCHAR(75) null,
	bloodthinnersDetail VARCHAR(75) null,
	detailWithTimeStamp VARCHAR(75) null
);

create table OMRI_Patient_Clinic (
	clinicId LONG not null,
	patientId LONG not null,
	patient_status INTEGER,
	doctorId LONG,
	doctorName VARCHAR(75) null,
	doctorPhoneNo VARCHAR(75) null,
	lawyerId LONG,
	lawyerName VARCHAR(75) null,
	lawyerPhoneNo VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG,
	primary key (clinicId, patientId)
);

create table OMRI_Patient_Clinic_Resource (
	clinicId LONG not null,
	patientId LONG not null,
	resourceId LONG not null,
	specificationId LONG not null,
	noOfOccurance INTEGER,
	procedureId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	createdBy LONG,
	modifiedBy LONG,
	primary key (clinicId, patientId, resourceId, specificationId)
);

create table OMRI_Procedure (
	procedureId LONG not null primary key,
	clinicId LONG,
	patientId LONG,
	isComplete BOOLEAN
);

create table OMRI_Resource (
	resourceId LONG not null primary key,
	resourceName VARCHAR(75) null
);

create table OMRI_Resource_Specification (
	resourceId LONG not null,
	specificationId LONG not null,
	cptCode VARCHAR(75) null,
	primary key (resourceId, specificationId)
);

create table OMRI_Specification (
	specificationId LONG not null primary key,
	specificationName VARCHAR(75) null
);