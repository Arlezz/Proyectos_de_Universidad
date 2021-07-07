package Vista;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Esta clase realiza la coneccion a del programa a MYSQL
 *
 * @author Antony
 * 
 */
public class Conexion {

	private final String URL = "jdbc:mysql://localhost:3306/datosprograma?useSSL=false";
	private final String USERNAME = "root";
	private final String PASSWORD = "password";
	private static Connection conect = null;
	
	
	/**
	 * Retorna la coneccion a la base de datos
	 * 
	 * @return Connection
	 * 
	 * 
	 */
	public Connection getConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conect = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
		} catch (Exception e) {
			System.out.println(e);
		}
		return conect;
	}
	
}
