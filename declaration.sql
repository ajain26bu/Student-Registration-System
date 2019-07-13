create or replace package procedures as
    procedure show_students(stud_curs out sys_refcursor);
    procedure show_courses(stud_curs out sys_refcursor);
    procedure show_classes(stud_curs out sys_refcursor);
    procedure show_enrollments(stud_curs out sys_refcursor);
    procedure show_prerequisites(stud_curs out sys_refcursor);
    procedure show_logs(stud_curs out sys_refcursor);
  	procedure get_students_course_details(studentid in students.sid%type,error_message out varchar2,r_cursor out sys_refcursor);
    procedure get_classes_details(cid in classes.classid%type, error_message out varchar2,r_cursor out sys_refcursor);
    procedure get_prerequisites(my_dept_code in prerequisites.dept_code%type, my_course_no in prerequisites.course_no%type, r_cursor out sys_refcursor);
    procedure enroll_student(studentid in students.sid%type, studentid_classid in classes.classid%type, message out varchar2);
    procedure delete_student(studentid in students.sid%type,message out varchar);
    procedure add_students(sidIn IN students.sid%type, fnameIn IN students.firstname%type,
                          lnameIn IN students.lastname%type, statusIn IN students.status%type,
                          gpaIn IN students.gpa%type, emailIn IN students.email%type,
                          message out varchar2);
    procedure drop_student(studentid in students.sid%type,studentid_classid in classes.classid%type, message out varchar);
	end;
/
show errors
