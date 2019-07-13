
CREATE OR REPLACE TRIGGER student_insert_trigger
AFTER
INSERT on students
FOR EACH ROW
DECLARE
    who varchar2(10);
BEGIN
select user into who from dual;
IF INSERTING THEN
    INSERT INTO logs values(srs.nextval,who,SYSDATE,'students','insert',:NEW.sid);
END IF;
END;
/

CREATE OR REPLACE TRIGGER enrollment_insert_trigger
AFTER
INSERT on enrollments
FOR EACH ROW
DECLARE
who varchar2(10);
BEGIN
select user into who from dual;
IF INSERTING THEN
    INSERT INTO logs values(srs.nextval,who,SYSDATE,'Enrollments','insert',:NEW.sid||','||:NEW.classid);
END IF;
END;
/

CREATE OR REPLACE TRIGGER student_delete_trigger
AFTER
DELETE on students
FOR EACH ROW
DECLARE
    who varchar2(10);
BEGIN
select user into who from dual;
IF DELETING THEN
    INSERT INTO logs values(srs.nextval,who,SYSDATE,'students','delete',:OLD.sid);
END IF;
END;
/

CREATE OR REPLACE TRIGGER enrollment_delete_trigger
AFTER
DELETE on enrollments
FOR EACH ROW
DECLARE
who varchar2(10);
BEGIN
select user into who from dual;
IF DELETING THEN
    INSERT INTO logs values(srs.nextval,who,SYSDATE,'Enrollments','delete',:OLD.sid||','||:OLD.classid);
END IF;
END;
/
show errors;
