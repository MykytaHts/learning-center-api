INSERT INTO management.users
(
     first_name,
     last_name,
     email,
     password,
     role,
     created_date,
     modified_date,
     created_by,
     modified_by
 )
VALUES ('Mykyta','Lemberg','admin.phantoms@khm.com','hekkoroid','ADMIN',CURRENT_DATE,CURRENT_DATE,'admin','admin'),
 ('Matizaa','Fikallia','student.phantoms@khm.com','hekkoroid','STUDENT',CURRENT_DATE,CURRENT_DATE,'admin','admin'),
 ('Runitta','Rita','instructor.phantoms@khm.com','hekkoroid','INSTRUCTOR',CURRENT_DATE,CURRENT_DATE,'admin','admin')
