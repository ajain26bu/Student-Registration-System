package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtility {
	
	private final String driverName="oracle.jdbc.driver.OracleDriver";
	private final String connectionUrl="jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111";
	private final String username="ajain26";
	private final String password="Alankrit8";
	private Connection connectionObj=null;
	
	public Connection createConnection() throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
		connectionObj = DriverManager.getConnection(connectionUrl,username,password);	
		setConnectionObj(connectionObj);
		return connectionObj;
	}

	public void closeConnection() throws SQLException{
		connectionObj.close();
	}
	
	public Connection getConnectionObj() {
		return connectionObj;
	}

	public void setConnectionObj(Connection connectionObjIn) {
		this.connectionObj = connectionObjIn;
	}
}
