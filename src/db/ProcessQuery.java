package db;

import oracle.jdbc.OracleTypes;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProcessQuery {
        private final String showAllStudentsPC="{call procedures.show_students(?)}";
        private final String showAllCoursesPC="{call procedures.show_courses(?)}";
	private final String showAllPrequisitesPC="{call procedures.show_prerequisites(?)}";
	private final String showAllClassesPC="{call procedures.show_classes(?)}";
	private final String showAllEnrollmentsPC="{call procedures.show_enrollments(?)}";
	private final String showAllLogsData="{call procedures.show_logs(?)}";
        private final String studentAndCourseDetailsPC = "{call procedures.get_students_course_details(?,?,?)}";
	private final String showClassAndAllStudentsPC = "{call procedures.get_classes_details(?,?,?)}";
	private final String enrollStudentIntoClassPC = "{call procedures.enroll_student(?,?,?)}";
	private final String dropStudentFromCoursePC = "{call procedures.drop_student(?,?,?)}";
	private final String delete_Student = "{call procedures.delete_student(?,?)}";
        private final String showPrerequisites="{call procedures.get_prerequisites(?,?,?)}";
        private final String add_Student="{call procedures.add_students(?,?,?,?,?,?,?)}";
	private final String commitQuery="commit";
	private Connection connectionObj = null;
	private String message = null;
	private ResultSet rsObj = null;

	public ProcessQuery(Connection createConnectionIn) {
		connectionObj = createConnectionIn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
        
        
        //1 Show Students table
	public ResultSet getAllStudentDetails() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllStudentsPC);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}
        
        //1. Show Courses Table
	public ResultSet getAllCourses() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllCoursesPC);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}

        //1. Show Prerequisites
	public ResultSet getAllPreqrequisites() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllPrequisitesPC);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}

        //1
	public ResultSet getAllClasses() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllClassesPC);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}

        //1
	public ResultSet getAllEnrollments() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllEnrollmentsPC);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}
	
        //1
	public ResultSet getAllLogsData() throws SQLException {
		rsObj=null;
		CallableStatement cs = connectionObj.prepareCall(showAllLogsData);
		cs.registerOutParameter(1, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(1);
		cs=null;
		return rsObj;
	}

        //4. Student Registration Details
	public ResultSet getStudentRegistrationDetials(String my_sidIn) throws SQLException {
		rsObj = null;
		CallableStatement cs = connectionObj.prepareCall(studentAndCourseDetailsPC);
		cs.setString(1, my_sidIn);
		cs.registerOutParameter(2, OracleTypes.VARCHAR);
		cs.registerOutParameter(3, OracleTypes.CURSOR);
		cs.execute();
		String msg = cs.getString(2);
		
		if (msg != null ) {
			setMessage(msg);
		} else {
			rsObj = (ResultSet) cs.getObject(3);
		}
		cs = null;
		return rsObj;

	}

        //6. Class Detials
	public ResultSet getClassAndAllStudents(String classId) throws SQLException {
		rsObj = null;
		CallableStatement cs = connectionObj.prepareCall(showClassAndAllStudentsPC);
		cs.setString(1, classId);
		cs.registerOutParameter(2, OracleTypes.VARCHAR);
		cs.registerOutParameter(3, OracleTypes.CURSOR);
		cs.execute();
		String msg = cs.getString(2);
		if (msg != null) {
			setMessage(msg);
		} else {
			rsObj = (ResultSet) cs.getObject(3);
		}
		cs = null;
		return rsObj;
	}
        
        //3. Add a student
        public String addStudent(String sid, String fname, String lname, String status, String gpa, String email) throws SQLException{
            CallableStatement cs = connectionObj.prepareCall(add_Student);
                        cs.setString(1, sid);
			cs.setString(2, fname);
			cs.setString(3, lname);
			cs.setString(4, status);
			cs.setString(5, gpa);
			cs.setString(6, email);
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);
                        cs.execute();
                        String msgVar = cs.getString(7);
                        cs = null;
                        return msgVar;            
        }

        //7. Enroll a student
	public String enrollStudentIntoCourse(String my_sid, String classId) throws SQLException {
		CallableStatement cs = connectionObj.prepareCall(enrollStudentIntoClassPC);
		cs.setString(1, my_sid);
		cs.setString(2, classId);
		cs.registerOutParameter(3, OracleTypes.VARCHAR);

		cs.execute();

		String msgVar = cs.getString(3);
		cs = null;
		return msgVar;
	}

        //8. Drop a student
	public String dropStudentFromClass(String my_sid, String classId) throws SQLException {
		CallableStatement cs = connectionObj.prepareCall(dropStudentFromCoursePC);
		cs.setString(1, my_sid);
		cs.setString(2, classId);
		cs.registerOutParameter(3, OracleTypes.VARCHAR);

		cs.execute();

		String msgVar = cs.getString(3);
		cs = null;
		return msgVar;
	}

        //9. Delete a student
	public String deleteStudent(String my_sid) throws SQLException {
		CallableStatement cs = connectionObj.prepareCall(delete_Student);
		cs.setString(1, my_sid);
		cs.registerOutParameter(2, OracleTypes.VARCHAR);
		cs.execute();
		String mssg4 = cs.getString(2);
		return mssg4;
	}
	
        
        //5. Show Prerequisite
        public ResultSet show_prerequisite(String departmentCodeIn, int courseNumIn) throws SQLException{
                rsObj = null;
		CallableStatement cs = connectionObj.prepareCall(showPrerequisites);
		cs.setString(1, departmentCodeIn);
		cs.setInt(2, courseNumIn);
		cs.registerOutParameter(3, OracleTypes.CURSOR);
		cs.execute();
		rsObj = (ResultSet) cs.getObject(3);
		cs = null;
		return rsObj;
        }
        
	
	public void commitToDB() throws SQLException{
		CallableStatement cs=connectionObj.prepareCall(commitQuery);
		cs.executeQuery();
		cs.close();
		cs=null;
	}
	
	public void closeConnection() throws SQLException {
		rsObj.close();
		rsObj=null;
		if (connectionObj != null)
			connectionObj.close();
	}
	

}
