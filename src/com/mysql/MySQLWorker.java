package com.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.data.Data;

public class MySQLWorker {

	public MySQL mysql;
	String host,port,login,pass,database,table;
	Connection c;
	
	public MySQLWorker(String Host,String Port,String Database,String Login,String Pass,String Table)
	{
		host = Host;
		port = Port;
		database = Database;
		login = Login;
		pass = Pass;
		table = Table;
		Connect();
		TryToCreateTable();
	}
	
	public void Connect()
	{
		mysql = new MySQL(host,port,database,login,pass);
		
		try {
			c = mysql.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void TryToCreateTable()
	{
		Statement state;
		
		try {
			state = c.createStatement();
			state.executeUpdate("CREATE TABLE IF NOT EXISTS " + database + "." + table +
					" (id INT NOT NULL AUTO_INCREMENT,PlayerName TEXT(20),Allowed INT NOT NULL, Disallowed INT NOT NULL,PRIMARY KEY(id));"
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Data GetPlayerStats(String name)
	{
		try {
			if(!mysql.checkConnection())Connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + name + "';");
			
			if(!res.next())return null;
			return new Data(name,res.getInt("Allowed"),res.getInt("Disallowed"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void SetPlayerStats(Data data)
	{
		try {
			if(!mysql.checkConnection())Connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Statement state;
		ResultSet res;
		
		try {
			state = c.createStatement();
			res = state.executeQuery("SELECT * FROM " + database + "." + table + " WHERE PlayerName = '" + data.name + "';");
			
			if(!res.next()) state.executeUpdate("INSERT INTO " + database + "." + table + " (PlayerName,Allowed,Disallowed) VALUES('" + data.name + "','" + data.allowed + "','" + data.disallowed + "');");
			else state.executeUpdate("UPDATE " + database + "." + table + " SET Allowed = '" + data.allowed + "',Disallowed = '" + data.disallowed + "' WHERE PlayerName = '" + data.name + "';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
