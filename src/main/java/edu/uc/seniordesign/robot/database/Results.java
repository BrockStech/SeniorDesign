package edu.uc.seniordesign.robot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Results
{
	ResultSet rs = null;
    Connection connection = null;
    PreparedStatement statement = null;
    
	public ArrayList<String> fetchResults()
    {
        ArrayList<String> results = new ArrayList<String>();
		String query = "SELECT scheduled_time FROM schedule";
        try
        {
        	initalizeConnection(query);
            
            while ((rs.next()))
            {
            	results.add(rs.getString("scheduled_time"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                try 
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }
	
	public void insertResults(ArrayList<String> results)
    {  
        String truncateTable = "TRUNCATE TABLE schedule;";
        try
        {
        	connection = JDBCMySQLConnection.getConnection();
        	PreparedStatement truncateStatement = connection.prepareStatement(truncateTable);
        	truncateStatement.executeUpdate();
        	truncateStatement.close();
        	executeInsertQuery(results, connection);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                try 
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
	
	private void initalizeConnection(String query) throws SQLException
	{
		connection = JDBCMySQLConnection.getConnection();
	    statement = connection.prepareStatement(query);
	    rs = statement.executeQuery();
	}
	
	private void executeInsertQuery(ArrayList<String> results, Connection connection) throws SQLException
	{
		String query = "INSERT INTO schedule (id, scheduled_time) VALUES (?,?)";
		int i = 1;
		for (String result : results)
		{
			PreparedStatement insertStatement = connection.prepareStatement(query);
			insertStatement.setInt(1, i);
			insertStatement.setString(2, result);
        	insertStatement.executeUpdate();
        	insertStatement.close();
        	i++;
		}
	}
	
	
}    