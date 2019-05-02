package com.model;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Response;

public class ConnectDB {
	
	static Connection conn = null;


	public ConnectDB() {
	}
	
	
	public static Connection Connect() throws ClassNotFoundException, SQLException{
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		String connectionUrl = "jdbc:mysql://localhost:3306/demo";
		String connectionUser = "root";
		String connectionPassword = "";
		conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
		return conn;	
	}
	public static ResultSet execute(String sql) {
		try {
			conn = Connect();
		} catch (Exception e1) {}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static boolean executeUpdate(String sql) {
		try {
			conn = Connect();
		} catch (Exception e1) {}
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	public  void thucThiSQL(String sql) throws Exception{
		Connection connect =Connect();
		Statement stmt = connect.createStatement();
		stmt.executeUpdate(sql);
	}
	
	
}
