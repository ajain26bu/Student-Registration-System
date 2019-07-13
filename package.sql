/*Procedure Definitions*/
create or replace package body procedures as

    /* View students */
    procedure show_students(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from students;
    end show_students;

    /*View courses */
    procedure show_courses(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from courses;
    end show_courses;

    /*View classes */
    procedure show_classes(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from classes;
    end show_classes;

    /*View enrollments */
    procedure show_enrollments(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from enrollments;
    end show_enrollments;

    /*View prerequisites */
    procedure show_prerequisites(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from prerequisites;
    end show_prerequisites;

    /*View logs */
    procedure show_logs(stud_curs out sys_refcursor) as
    begin
    open stud_curs for
    select * from logs;
    end show_logs;


  /*4. Write a procedure in your package that, for a given student (with sid provided as a
  parameter), can list the sid, lastname, and status of the student as well as all classes the student has
  taken or is taking. For each class, show classid, dept_code, course_no, title, year, and semester.
  (dept_code and course_no should be displayed together, e.g., CS532.) If the student is not in the
  students table, report “The sid is invalid.” If the student has not taken any course, report “The
  student has not taken any course.”*/
	--Question 4 Done
	procedure get_students_course_details(studentid in students.sid%type,error_message out varchar2,r_cursor out sys_refcursor)
	is
    student_course_count number;
	  student_count number;
	begin
	select count(*) into student_course_count from enrollments where sid=studentid;
	select count(*) into student_count from students where sid= studentid;
	if student_count = 0
	 then error_message := 'The sid is invalid.';
	 else
			if student_course_count = 0
				then error_message := 'The student has not taken any courses.';
			else
				open r_cursor for
        select s.sid, s.lastname, s.status, cl.classid, cl.dept_code, cl.course_no, co.title, cl.sect_no, cl.year, cl.semester, e.lgrade
        from students s inner join enrollments e on s.sid = e.sid inner join classes cl on cl.classid = e.classid
        inner join courses co on co.dept_code||co.course_no = cl.dept_code||cl.course_no where e.sid = studentid;

			end if;
	end if;
	end get_students_course_details;



  /*6. Write a procedure in your package that, for a given class (with classid provided as a
  parameter), lists the classid, course title, semester and year of the class as well as all the students
  (show sid and lastname) who have taken or are taking the class. If the class is not in the classes
  table, report “The cid is invalid.” If no student has taken or is taking the class, report “No student is
  enrolled in the class.”*/
  --Question 6 Done
  procedure get_classes_details(cid in classes.classid%type, error_message out varchar2,r_cursor out sys_refcursor)
  is
  class_student_count number;
  class_count number;
  begin
  select count(*) into class_student_count from enrollments where classid = cid;
  select count(*) into class_count from classes where classid = cid;
  if class_count = 0
   then error_message := 'The CID is invalid.';
   else
      if class_student_count = 0
        then error_message := 'No student enrolled in the class';
      else
        open r_cursor for
        select cl.classid, co.title, cl.semester, cl.year, s.sid, s.lastname
        from students s, courses co, classes cl, enrollments e
        where cl.classid = cid and cl.dept_code || cl.course_no = co.dept_code || co.course_no and s.sid = e.sid and e.classid = cl.classid;
        -- from courses co left join classes cl on co.dept_code||co.course_no = cl.dept_code||cl.course_no
        -- left join enrollments e on cl.classid = cid left join students s on e.sid = s.sid;

      end if;
  end if;
  end get_classes_details;



  /*5. Write a procedure in your package that, for a given course (with dept_code and course_no
  as parameters), returns all its prerequisite courses (show dept_code and course_no together as in
  CS532), including both direct and indirect prerequisite courses. If course C1 has course C2 as a
  prerequisite, C2 is a direct prerequisite. In addition, if C2 has course C3 has a prerequisite, then C3
  is an indirect prerequisite for C1. Please also note that indirect prerequisites can be more than two
  levels away.*/
  procedure get_prerequisites(my_dept_code in prerequisites.dept_code%type, my_course_no in prerequisites.course_no%type, r_cursor out sys_refcursor)
  is
  begin
        open r_cursor for
        select p.dept_code, p.course_no, p.pre_dept_code, p.pre_course_no
        from prerequisites p where my_dept_code||my_course_no = p.dept_code||p.course_no;
  end get_prerequisites;


  /*9. Write a procedure in your package to delete a student from the students table based on a
  given sid (as a parameter). If the student is not in the students table, report “The sid is invalid.”
  When a student is deleted, all tuples in the enrollments table involving the student should also be
  deleted (use a trigger to implement this) and this will trigger a number of actions as described in #7.
  */
  procedure delete_student(studentid in students.sid%type, message out varchar)
	is
		studentid_count int;
	begin
		select count(*) into studentid_count from students where sid = studentid;
	if studentid_count=0
	then
		message:='The SID is invalid.';
	else
    delete from enrollments where sid = studentid;
		delete from students where sid = studentid;
		commit;
		message:='student deleted sucessfully';
	end if;
    end delete_student;

  /*7. Write a procedure in your package to enroll a student into a class. The sid of the student
  and the classid of the class are provided as parameters.
  If the student is not in the students table, report “The sid is invalid.”
  If the class is not in the classes table, report “The classid is invalid.”
  If the enrollment of the student into a class would cause “class_size > limit”, reject the enrollment and
  report “The class is closed.”
  If the student is already in the class, report “The student is already in the class.”
  If the student is already enrolled in three other classes in the same semester and the same year, report “Students cannot be
  enrolled in more than three classes in the same semester.” and reject the enrollment.
  If the student is already enrolled in two other classes in the same semester and the same
  year, report “You are overloaded.” and allow the student to be enrolled.

  If the student has not completed the required prerequisite courses with minimum grade “C”, reject the enrollment
  and report “Prerequisite courses have not been completed.” For all the other cases, the requested
  enrollment should be performed. You should make sure that all data are consistent after each
  enrollment. For example, after you successfully enrolled a student into a class, the size of the
  corresponding class should be updated accordingly. Use trigger(s) to implement the updates of
  values caused by successfully enrolling a student into a class. It is recommended that all triggers for
  this project be implemented outside of the package.*/
  procedure enroll_student(studentid in students.sid%type, studentid_classid in classes.classid%type, message out varchar2)
  is
    studentid_count int;
    classid_count int;
    classid_size int;
    classid_limit int;
    studentid_in_classid int;
    studentid_year classes.year%type;
    studentid_semester classes.semester%type;
    studentid_done int;
    --studentid_dept_code classes.dept_code%type;
    --studentid_course_no classes.course_no%type;
    --studentid_pre_dept_code classes.dept_code%type;
    --studentid_pre_course_no classes.course_no%type;
    --studentid_pre_classid classes.classid%type;
    --studentid_prereq_satisfied int;
  begin
    select count(*) into studentid_count from students where sid = studentid;
    select count(*) into classid_count from classes where classid = studentid_classid;
    select class_size into classid_size from classes where classid = studentid_classid;
    select limit into classid_limit from classes where classid = studentid_classid;
    select count(*) into studentid_in_classid from enrollments where sid = studentid and classid = studentid_classid;
    select year, semester into studentid_year, studentid_semester from classes where classid = studentid_classid;
    select count(*)into studentid_done from enrollments where sid = studentid and
    classid in(select classid from classes where year = studentid_year and semester = studentid_semester and classid<>studentid_classid);
    /*Get the department code and course number of the class the student wishes to enroll in.*/
    --select dept_code, course_no into studentid_dept_code, studentid_course_no from classes where classid = studentid_classid;
    /*Get the prerequisite department code and course number of the prerequisite course that the student needs to have taken.*/
    --select pre_dept_code, pre_course_no into studentid_pre_dept_code, studentid_pre_course_no from prerequisites where dept_code||course_no = studentid_dept_code||studentid_course_no;
    /*Get the classid of the prerequisite course the student needs to have taken*/
    --select classid into studentid_pre_classid from classes where dept_code||course_no = studentid_pre_dept_code||studentid_pre_course_no;
    /*Check if the student has completed the prerequisite course*/
    if studentid_count = 0
    then
      message:='The SID is invalid.';
    else
      if classid_count = 0
      then
        message:='The ClassID is invalid.';
      else
        if classid_size - classid_limit = 0
        then
          message:='The class is closed.';
        else
          if studentid_in_classid > 0
          then
            message:='The student is already in the class.';
          else
            if studentid_done>=3
            then
              message:='Students cannot be enrolled in more than three classes in the same semester.';
            else
              if studentid_done = 2
              then
                message:='You are overloaded.';
              end if;
                insert into enrollments(sid, classid, lgrade) values(studentid, studentid_classid, null);
                message:='Enrollment successful.';
                --studentid_classid := studentid_classid + 1;
                UPDATE classes
                SET class_size = classid_size
                WHERE classid = studentid_classid;
            end if;/*Students cannot be enrolled in more than three classes in the same semester.*/
          end if;/*The student is already in the class.*/
        end if;/*The class is closed.*/
      end if;/*The ClassID is invalid.*/
    end if;/*The SID is invalid.*/
  end enroll_student;


  procedure add_students(sidIn IN students.sid%type, fnameIn IN students.firstname%type,
	                        lnameIn IN students.lastname%type, statusIn IN students.status%type,
	                        gpaIn IN students.gpa%type, emailIn IN students.email%type,
	                        message out varchar2) as
	begin
	insert into students(sid, firstname, lastname, status, gpa, email) values(sidIn, fnameIn, lnameIn, statusIn, gpaIn, emailIn);

	message := 'success';

	end add_students;



	procedure drop_student(studentid in students.sid%type,studentid_classid in classes.classid%type, message out varchar)
	is
    more_message varchar(50);
    last_message varchar(50);
    studentid_class_enroll int;
		studentid_prereq int;
    studentid_class_size int;
		studentid_class_size_count int;
		studentid_count int;
    studentid_class int;
    studentid_dept_code classes.dept_code%type;
    studentid_course_no classes.course_no%type;

	begin
	select count(*) into studentid_count from students where sid = studentid;
    select count(*) into studentid_class from classes where classid=studentid_classid;
	if studentid_count=0
	then
		message:='The sid is invalid';
		else
			if studentid_class = 0
			then
				message:='The classid is invalid';
				else
					select count(*) into studentid_class_enroll from enrollments where classid = studentid_classid and sid = studentid;
					if studentid_class_enroll=0
					then
						message:= 'The student is not enrolled in the class';
					else
						select dept_code, course_no into studentid_dept_code, studentid_course_no from classes where classid = studentid_classid;
						select count(*) into studentid_prereq from classes cl, prerequisites p where classid in (select classid from enrollments where classid<>studentid_classid and sid=studentid) and
						cl.dept_code=p.dept_code and cl.course_no = p.course_no and p.pre_dept_code=studentid_dept_code and p.pre_course_no=studentid_course_no;

					if studentid_prereq>0
					then
						message:='The drop is not permitted because another class uses it as a prerequisite';

					else
						delete from enrollments where sid = studentid and classid = studentid_classid;
						message:='Student dropped successfully';

						select class_size into studentid_class_size from classes where classid=studentid_classid;

						select count(*) into studentid_class_size_count from enrollments where sid=studentid;

						if studentid_class_size=0
						then
							more_message:='The class now has no students.';
						end if;

						if studentid_class_size_count=0
						then
							last_message:='This student is not enrolled in any classes.';
						end if;
						message:=message||' '||more_message||' '||last_message;



					end if;
				end if;
			end if;
		end if;
  end drop_student;



end procedures;
/
show errors
