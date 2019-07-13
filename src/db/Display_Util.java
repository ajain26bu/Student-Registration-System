package db;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Formatter;

public class Display_Util {
	
	public void displayMenu() {
		System.out.println("_________________________________________________\n");
		System.out.println(" Student Registration System using PL/SQL and JDBC ");
		System.out.println("__________________________________________________\n");
		System.out.println("\n Make a choice \n");
		System.out.println("1. View Tables");
                System.out.println("2. Add a Student");
		System.out.println("3. View Student Registration Details");
		System.out.println("4. View Class Details");
		System.out.println("5. Enroll a Student");
		System.out.println("6. Drop a Student");
		System.out.println("7. Delete a Student");
                System.out.println("8. Show prerequisite of a course");
		System.out.println("9. Exit");

	}

	public void displaySubmenu() {
		System.out.println("_________________________________________________\n");
		System.out.println(" View Tables ");
		System.out.println("__________________________________________________\n");
		System.out.println("1. Students");
		System.out.println("2. Courses");
		System.out.println("3. Prerequisites");
		System.out.println("4. Classes");
		System.out.println("5. Enrollments");
		System.out.println("6. Logs");
		System.out.println("7. Return to previous menu");

	}

        private final String formatStr="%-24s";
        
	public void printAttributes(ResultSet rs) throws SQLException {
		Formatter formatter=new Formatter();
		ResultSetMetaData rsmd = rs.getMetaData();
                int i = 1;
                while(i <= rsmd.getColumnCount()){
                    System.out.print(String.format(formatStr, rsmd.getColumnLabel(i)));
                    i++;
                }
		rsmd = null;
		formatter.close();
		formatter=null;
		
	}

	public void printResults(ResultSet rs) throws SQLException {
		Formatter formatter=new Formatter();
		int count = rs.getMetaData().getColumnCount();
		while(rs.next()){
                    int i = 1;
                        while(i <= count){
                            System.out.print( String.format(formatStr, rs.getString(i)));
                            i++;
                        }
			System.out.print("\n");
		}
		formatter.close();
	}

	public void handleNullValues(String stringIn) {
		System.out.println("No "+stringIn+" values to show.");
	}

}
