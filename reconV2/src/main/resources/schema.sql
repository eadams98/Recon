use recon;

DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS contractor_to_user;
DROP TABLE IF EXISTS user_login;
DROP TABLE IF EXISTS trainee_login;
DROP TABLE IF EXISTS contractor_to_trainee;
DROP TABLE IF EXISTS school_to_trainee;
DROP TABLE IF EXISTS school_to_contractor;
DROP TABLE IF EXISTS traineerefreshtoken;
DROP TABLE IF EXISTS contractor_refresh_token;
DROP TABLE IF EXISTS school_refresh_token;
--
DROP TABLE IF EXISTS trainee;
DROP TABLE IF EXISTS contractor;
DROP TABLE IF EXISTS school;
DROP TABLE IF EXISTS role;

CREATE TABLE role (
	role_id INTEGER AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE contractor (
	id iNTEGER NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
    email_id VARCHAR(50) UNIQUE,
    role_id INTEGER,
    training_capacity INTEGER,
    password VARCHAR(100) NOT NULL,
    first_login BOOLEAN DEFAULT 1,
	PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

CREATE TABLE trainee (
	trainee_id INTEGER NOT NULL AUTO_INCREMENT,
    supervisor_id INTEGER,
    role_id INTEGER,
	/*role ENUM("admin", "council_member", "school", "contractor", "jr_contractor"),*/
	first_name VARCHAR(20),
	last_name VARCHAR(20),
    email_id VARCHAR(50) UNIQUE,
	PRIMARY KEY (trainee_id),
	FOREIGN KEY (supervisor_id) REFERENCES Contractor(id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);


CREATE TABLE school (
	school_id iNTEGER NOT NULL AUTO_INCREMENT,
    role_id INTEGER,
    email_id VARCHAR(50) UNIQUE,
	school_name VARCHAR(255) NOT NULL,
    password VARCHAR(100) NOT NULL,
	PRIMARY KEY (school_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

DROP TABLE IF EXISTS council;
CREATE TABLE Council (
	id iNTEGER NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	PRIMARY KEY (id)
);


CREATE TABLE contractor_to_trainee (
	id INTEGER NOT NULL AUTO_INCREMENT,
	user_id INTEGER NOT NULL,
	contractor_id INTEGER NOT NULL,
	date_assigned Date,
	PRIMARY KEY(id),
	FOREIGN KEY (user_id) REFERENCES Trainee(trainee_id),
	FOREIGN KEY (contractor_id) REFERENCES Contractor(id)
);

CREATE TABLE school_to_trainee (
	id INTEGER NOT NULL AUTO_INCREMENT,
	school_id INTEGER NOT NULL,
	trainee_id INTEGER NOT NULL,
	date_assigned Date,
	PRIMARY KEY(id),
	FOREIGN KEY (school_id) REFERENCES School(school_id),
	FOREIGN KEY (trainee_id) REFERENCES Trainee(trainee_id)
);

CREATE TABLE school_to_contractor (
	id INTEGER NOT NULL AUTO_INCREMENT,
	school_id INTEGER NOT NULL,
	contractor_id INTEGER NOT NULL,
	date_assigned Date,
	PRIMARY KEY(id),
	FOREIGN KEY (school_id) REFERENCES School(school_id),
	FOREIGN KEY (contractor_id) REFERENCES Contractor(id)
);

DROP TABLE IF EXISTS report;
CREATE TABLE report (
	report_id INTEGER NOT NULL AUTO_INCREMENT,
	title VARCHAR(50),
	description VARCHAR(255),
    rebuttal VARCHAR(255),
	grade ENUM("A_PLUS", "A", "A_MINUS", "B_PLUS", "B", "B_MINUS", "C_PLUS", "C", "C_MINUS", "D_PLUS", "D", "D_MINUS", "F_PLUS", "F", "F_MINUS"),
	submission_date Date,
    week_start_date Date,
    week_end_date Date,
	contractor_link_id INTEGER NOT NULL,
    trainee_link_id INTEGER NOT NULL,
	PRIMARY KEY (report_id),
	FOREIGN KEY (contractor_link_id) REFERENCES Contractor(id),
    FOREIGN KEY (trainee_link_id) REFERENCES Trainee(trainee_id)
);



-- 


CREATE TABLE trainee_login (
	login_id INTEGER AUTO_INCREMENT,
	user_id INTEGER NOT NULL,
	password VARCHAR(100) NOT NULL,
	recovery_question VARCHAR(50),
	recovery_answer VARCHAR(50),
	first_login BOOLEAN DEFAULT 1,
	PRIMARY KEY (login_id),
	FOREIGN KEY (user_id) REFERENCES Trainee(trainee_id)
	
);

CREATE TABLE traineerefreshtoken (
	id INTEGER AUTO_INCREMENT,
    trainee_id INTEGER,
    token VARCHAR(50) UNIQUE NOT NULL,
    expiry_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY(trainee_id) REFERENCES Trainee(trainee_id)
);

CREATE TABLE contractor_refresh_token (
	id INTEGER AUTO_INCREMENT,
    contractor_id INTEGER,
    token VARCHAR(50) UNIQUE NOT NULL,
    expiry_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY(contractor_id) REFERENCES Contractor(id)
);

CREATE TABLE school_refresh_token (
	id INTEGER AUTO_INCREMENT,
    school_id INTEGER,
    token VARCHAR(50) UNIQUE NOT NULL,
    expiry_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY(school_id) REFERENCES School(school_id)
);
