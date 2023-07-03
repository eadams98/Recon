use recon;

-- Role
INSERT INTO role(name) VALUES ("admin");
INSERT INTO role(name) VALUES ("council");
INSERT INTO role(name) VALUES ("school");
INSERT INTO role(name) VALUES ("contractor");
INSERT INTO role(name) VALUES ("trainee");

-- Schools

INSERT INTO school(email_id, school_name, role_id, password) VALUES ("school.1@yahoo.com", "Aztec", 3, "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW");
INSERT INTO school(email_id, school_name, role_id, password) VALUES ("school.2@yahoo.com", "Native", 3, "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW");

-- Contractors

INSERT INTO contractor(first_name, last_name, email_id, password, role_id) VALUES("Bob", "The Builder", "contractor.1@yahoo.com", "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", 4); #Password: password
INSERT INTO contractor(first_name, last_name, email_id, password, role_id) VALUES("Handy", "Mandy", "contractor.2@yahoo.com", "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", 4); #Password: password
INSERT INTO contractor(first_name, last_name, email_id, password, role_id) VALUES("Agent", "Oso", "contractor.3@yahoo.com", "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", 4); #Password: password

-- Council members

INSERT INTO council(first_name, last_name) VALUES("Joe", "Biden");

-- user

INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","1", 1, "trainee.1@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "2", 2, "trainee.2@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "3", 1, "trainee.3@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "4", 1, "trainee.4@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "5", 2, "trainee.5@yahoo.com", 5);

INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","6", 1, "trainee.6@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","7", 1, "trainee.7@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","8", 1, "trainee.8@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","9", 1, "trainee.9@yahoo.com", 5);
INSERT INTO trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","10", 1, "trainee.10@yahoo.com", 5);

-- user login

INSERT trainee_login(user_id, password, recovery_question, recovery_answer) VALUES(1, "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", "Birth Month?", "May"); -- //password
INSERT trainee_login(user_id, password, recovery_question, recovery_answer) VALUES(2, "$2a$10$FtZ0Y/xpHbWuQwlgEEo5ue6mmEnhLtLdggPoRRoEAPmQdFAJ3Pj1i", "Birth Month?", "May"); -- May@123

-- School to Contractor
INSERT school_to_contractor(school_id, contractor_id, date_assigned) VALUES(1, 1, "1998-05-15");
INSERT school_to_contractor(school_id, contractor_id, date_assigned) VALUES(2, 2, "1998-05-15");


-- School to Trainee
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 1, "1998-05-15");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 3, "2009-12-01");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 2, "2012-08-08");
INSERT tchool_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 4, "2012-08-08");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 5, "2012-08-08");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 6, "2012-08-08");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 7, "2012-08-08");
INSERT school_to_trainee(school_id, trainee_id, date_assigned) VALUES(1, 8, "2012-08-08");

-- Role

INSERT role(name) VALUES("Admin");
INSERT role(name) VALUES("Trainee");
INSERT role(name) VALUES("Contractor");
INSERT role(name) VALUES("Council");
INSERT role(name) VALUES("School");

INSERT INTO report (title, description, grade, submission_date, week_start_date, week_end_date, contractor_link_id, trainee_link_id) VALUES ("title", "description", "A_PLUS", "1998-05-15", "1998-05-17", "1998-05-20", 2, 2);
