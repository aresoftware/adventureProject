package com.ideario.bean;

import java.sql.*;

public class DBConneccionSchool {

	
	private final String conectorDrv = "com.mysql.jdbc.Driver"; 
	private final String dbHost = "jdbc:mysql://127.0.0.1:3306/jdcl"; 
	//private final String dbHost = "jdbc:mysql://aa14vfl1xuy8utn.comiwe9j931a.us-east-2.rds.amazonaws.com";
	private final String dbPort = "3306"; 
	private String mensajeError = ""; 

	public Connection getConexion(){ 
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//desa
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdcl?user=root&password=admin");
			//prod
			//connection = DriverManager.getConnection("jdbc:mysql://aa14vfl1xuy8utn.comiwe9j931a.us-east-2.rds.amazonaws.com:3306/ebdb?user=uniandes&password=Un1andes");
			//connection = DriverManager.getConnection("jdbc:mysql://jdcl.cznjtfnisb9r.us-east-2.rds.amazonaws.com:3306/jdcl?user=admin&password=Adventur3");
		/*Statement sql = connection.createStatement();
		ResultSet result = sql.executeQuery("SELECT * FROM usuarios");
		if(result.next() )
		{
			return connection;
			//out.println(result.getString("field") + "<br />");
		}*/
		}catch(Exception x){
			mensajeError = x.getMessage();
			x.printStackTrace();
		}
		
		return connection;
	}
		
	public void cerrarConeccion(Connection conn, PreparedStatement p, ResultSet r){
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		try {
			if (p != null)
				p.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		try {
			if (r != null)
				r.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		
	}
		
		public Connection dameConexion(String username, String password){ 
			
					
		Connection con = null; 

	try{ 
	Class.forName(conectorDrv).newInstance(); 
	}catch(ClassNotFoundException cnfe){ 
	mensajeError = "No se encontro el controlador"; 
	return con; 
	}catch(InstantiationException ie){ 
	mensajeError = "No se puede crear una instancia del controlador"; 
	return con; 
	}catch(IllegalAccessException iae){ 
	mensajeError = "No se puede accesar al controlador"; 
	return con; 
	} 

	try{ 
	con = DriverManager.getConnection(this.dbHost, username, password); 
	}catch(SQLException sqle){
		sqle.printStackTrace();
	mensajeError = "No se puede tener Acceso a la DB"; 
	return con; 
	} 

	return con; 
	} 

	public boolean hasError(){ 
	if(this.mensajeError.length() > 0) 
	return true; 
	return false; 
	} 

	public String getError(){ 
	return this.mensajeError; 
	} 

	public void clearError(){ 
	this.mensajeError = ""; 
	} 

	
	
		
	
	public Connection getConexionOracle(){ 
		Connection connection = null;
		try{
			
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			
			//connection = DriverManager.getConnection("jdbc:oracle:thin:@i795f1p21:41734/mdesa", "gma", "Desa2006");
			
			connection = DriverManager.getConnection("jdbc:oracle:thin:@i795f1p21:41734:mdesa", "gma", "Gma2006");
			               
			
			
		
		
		}catch(Exception x){
			mensajeError = x.getMessage();
			x.printStackTrace();
		}
		
		return connection;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConneccionSchool db = new DBConneccionSchool();
		//Connection conn = db.dameConexion("admin", "M13rd4123");
		Connection conn = db.getConexion();
		
		
		Statement sentencias = null; 
		ResultSet rs = null; 

		if(db.hasError()){ 
		System.out.println (db.getError()); 
		} 

		try{ 

		sentencias = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		rs = sentencias.executeQuery("SELECT * FROM usuarios WHERE 1"); 


		while(rs.next()){	
			System.out.println (rs.getString("usuario")+" clave "+rs.getString("clave")); 
		} 

		}catch(Exception e){ 
			e.printStackTrace();
			System.out.println ("Error Controlado: " + e.getMessage()); 
			return; 
		} 
		

	}

}
