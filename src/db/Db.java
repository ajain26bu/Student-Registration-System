package db;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.ResultSet;
import db.ConnectionUtility;
import db.Display_Util;

public class Db {
	public static void main(String[] args) {
                ResultSet rsObj = null;
		String student_sid = null;
                String message = null;
		int navigation = 0;
		try {
			ConnectionUtility conUtil = new ConnectionUtility();
			Display_Util displayObj = new Display_Util();
			ProcessQuery pqObj = new ProcessQuery(conUtil.createConnection());

			Scanner sc = new Scanner(System.in);
			while (1 == 1) {
				displayObj.displayMenu();
				navigation = sc.nextInt();
				pqObj.commitToDB();
				switch (navigation) {
				case 1:
					childNavigation(sc, conUtil.getConnectionObj(), displayObj, pqObj);
					break;
                                
                                case 2:
                                        System.out.print("Enter the sid: ");
                                        String sid = sc.next();
                                        System.out.print("Enter first name: ");
                                        String fname = sc.next();
                                        System.out.print("Enter last name: ");
                                        String lname = sc.next();
                                        System.out.print("Enter status: ");
                                        String status = sc.next();
                                        System.out.print("Enter gpa: ");
                                        String gpa = sc.next();
                                        System.out.print("Enter email: ");
                                        String email = sc.next();
                                        message = pqObj.addStudent(sid, fname, lname, status, gpa, email);

					if (message != null) {
						System.out.println("Message : " + message);
					}

					break;
                                        
				case 3:
					System.out.println("Enter the sid");
					student_sid = sc.next();
					rsObj = pqObj.getStudentRegistrationDetials(student_sid);
					if (rsObj == null) {
						System.out.println("Message: " + pqObj.getMessage());
					} else {
						displayObj.printAttributes(rsObj);
						System.out.println("\n");
						displayObj.printResults(rsObj);
						rsObj = null;
					}
					break;

				case 4:
					System.out.println("Enter the classid ");
					String classId = sc.next();
					rsObj = null;
					rsObj = pqObj.getClassAndAllStudents(classId);

					if (rsObj == null) {
						System.out.println("Message: " + pqObj.getMessage());
					} else {
						displayObj.printAttributes(rsObj);
						System.out.println("\n");
						displayObj.printResults(rsObj);
						rsObj = null;
					}

					break;
				case 5:
					System.out.println("Enter the sid ");
					student_sid = sc.next();
					System.out.println("Enter the classid ");
					classId = sc.next();
					message = pqObj.enrollStudentIntoCourse(student_sid, classId);

					if (message != null) {
						System.out.println("Message: " + message);
					}

					break;
				case 6:
					System.out.println("Enter the sid ");
					student_sid = sc.next();
					System.out.println("Enter the classid ");
					classId = sc.next();
					message = pqObj.dropStudentFromClass(student_sid, classId);
					if (message != null) {
						System.out.println("Message: " + message);
					}

					break;
				case 7:
					System.out.println("Enter the sid ");
					student_sid = sc.next();
					message = pqObj.deleteStudent(student_sid);

					if (message != null) {
						System.out.println("Message: " + message);
					}

					break;
                                case 8:
					System.out.println("Enter dept_code ");
					String departmentCode = sc.next();
                                        System.out.println("Enter course_no ");
                                        int course = sc.nextInt();
					rsObj = pqObj.show_prerequisite(departmentCode, course);
					if (rsObj == null) {
						System.out.println("Message : " + pqObj.getMessage());
					} else {
						displayObj.printAttributes(rsObj);
						System.out.println("\n");
						displayObj.printResults(rsObj);
						rsObj = null;
					}
					break;
				case 9:
					pqObj.closeConnection();
					pqObj = null;
					System.exit(0);
					break;
				}
			}
		} catch (SQLException sqle) {
			System.out.println();
			sqle.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Class not found.");
			cnfe.printStackTrace();
			System.exit(-1);
		}

	}

	public static void childNavigation(Scanner sc, Connection connIn, Display_Util displayObjIn, ProcessQuery pqObjIn)
			throws SQLException {
		ResultSet rsObjSM = null;
		while (1 == 1) {
			displayObjIn.displaySubmenu();
			int optionSubmenu = sc.nextInt();
			System.out.println("-------------------------------------------------\t");
			switch (optionSubmenu) {
			case 1:
				rsObjSM = pqObjIn.getAllStudentDetails();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("student");
				}
				rsObjSM = null;
				break;
			case 2:
				rsObjSM = pqObjIn.getAllCourses();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("course");
				}
				break;
			case 3:
				rsObjSM = pqObjIn.getAllPreqrequisites();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("prerequisite");
				}
				
				break;
			case 4:
				rsObjSM = pqObjIn.getAllClasses();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("class");
				}
				
				break;
			case 5:
				rsObjSM = pqObjIn.getAllEnrollments();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("enrollment");
				}
				break;
			case 6:
				rsObjSM = pqObjIn.getAllLogsData();
				if (rsObjSM != null) {
					displayObjIn.printAttributes(rsObjSM);
					System.out.println("\n");
					displayObjIn.printResults(rsObjSM);
					rsObjSM = null;
				} else {
					displayObjIn.handleNullValues("log");
				}
				break;
			case 7:
				return;

			}

		}

	}
}

