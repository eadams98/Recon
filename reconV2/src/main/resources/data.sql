use recon;

-- Role
INSERT INTO Role(name) VALUES ("admin");
INSERT INTO Role(name) VALUES ("council");
INSERT INTO Role(name) VALUES ("school");
INSERT INTO Role(name) VALUES ("contractor");
INSERT INTO Role(name) VALUES ("trainee");

-- Schools

INSERT INTO School(school_name) VALUES ("Aztec");

-- Contractors

INSERT INTO Contractor(first_name, last_name, email_id, password, role_id) VALUES("Bob", "The Builder", "contractor.1@yahoo.com", "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", 4); #Password: password
INSERT INTO Contractor(first_name, last_name, email_id, password, role_id) VALUES("Handy", "Mandy", "contractor.2@yahoo.com", "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", 4); #Password: password

-- Council members

INSERT INTO Council(first_name, last_name) VALUES("Joe", "Biden");

-- user

INSERT INTO Trainee(first_name, last_name, supervisor_id, email_id, role_id) Values("Trainee","1", 1, "trainee.1@yahoo.com", 5);
INSERT INTO Trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "2", 2, "trainee.2@yahoo.com", 5);
INSERT INTO Trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "3", 1, "trainee.3@yahoo.com", 5);
INSERT INTO Trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "4", 1, "trainee.4@yahoo.com", 5);
INSERT INTO Trainee(first_name, last_name, supervisor_id, email_id, role_id) VALUES("Trainee", "5", 2, "trainee.5@yahoo.com", 5);

-- user login

INSERT Trainee_login(user_id, password, recovery_question, recovery_answer) VALUES(1, "$2a$10$rCbQgncvYiv/GztuN.GmbuDsG6IeptbC8Bs/nzIqTNxF1G2FtlrpW", "Birth Month?", "May"); -- //password
INSERT Trainee_login(user_id, password, recovery_question, recovery_answer) VALUES(2, "$2a$10$FtZ0Y/xpHbWuQwlgEEo5ue6mmEnhLtLdggPoRRoEAPmQdFAJ3Pj1i", "Birth Month?", "May"); -- May@123

-- Role

INSERT Role(name) VALUES("Admin");
INSERT Role(name) VALUES("Trainee");
INSERT Role(name) VALUES("Contractor");
INSERT Role(name) VALUES("Council");
INSERT Role(name) VALUES("School");