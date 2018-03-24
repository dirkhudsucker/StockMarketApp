package sql;
import java.sql.*;

public class sql {
public static void main(String[] args) {
String URL= "jdbc:sqlserver://localhost:1433;"+"databaseName=stock;integratedSecurity=true;";
Connection con=null;
Statement stmt=null;
ResultSet rs=null;

	try {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		con=DriverManager.getConnection(URL);
		System.out.println("connected");
		String SQL="SELECT * from stockmarket";
		stmt=con.createStatement();
		rs=stmt.executeQuery(SQL);
		while(rs.next()) {
		System.out.println(rs.getString(1));	
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	


	
}
}
