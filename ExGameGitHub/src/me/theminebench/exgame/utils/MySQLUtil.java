package me.theminebench.exgame.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

public class MySQLUtil {
	private MySQL MySQL;
	private Connection connection = null;
	
	public MySQLUtil(Plugin plugin){
		MySQL = new MySQL(plugin, "localhost", "3306", "Exerosis", "Exerosis", "jon");	
	}

	public void addData(String table, String columns, String values){
		try {
			connection = MySQL.openConnection();
			Statement statement = connection.createStatement();
			String args = "INSERT INTO " + table + " (" + columns + ") VALUES " + values;
			statement.executeUpdate(args);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getData(String table, String conditions){
		try {
			connection = MySQL.openConnection();
			Statement statement = connection.createStatement();
			String args = "SELECT * FROM " + table + " WHERE" + conditions;
			ResultSet result = statement.executeQuery(args);
			result.next();
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void removeData(String table, String conditions){
		try {
			connection = MySQL.openConnection();
			Statement statement = connection.createStatement();
			String args = "DELETE FROM " + table + " WHERE " +  conditions;
			statement.executeUpdate(args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
