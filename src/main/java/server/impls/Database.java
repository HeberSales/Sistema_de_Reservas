package server.impls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import server.models.Reservation;

public class Database {
	
	public static void createTables() {
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS reservations ("
					+ "userId STRING NOT NULL,"
					+ "squareId STRING NOT NULL,"
					+ "dt DATETIME NOT NULL,"
					+ "PRIMARY KEY(userId, squareId, dt))");
			
			connection.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static String insertReservation(String userId, String squareId, int year, int month, int day, int hour) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		
		userId = userId.toLowerCase();
		squareId = squareId.toLowerCase();
		
		if (isSquareReserved(squareId, year, month, day, hour)) {
			return String.format("ERROR: Square [%s] ALREADY RESERVED at [%s]", squareId, formattedDateTime);
		}
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			statement.executeUpdate(String.format("INSERT INTO reservations VALUES('%s', '%s', '%s')", userId, squareId, formattedDateTime));
			
			connection.close();
			
			return String.format("SUCCESS: Square [%s] was successfully RESERVED at [%s] for user [%s]", squareId, formattedDateTime, userId);
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return String.format("ERROR: Unexpected error while inserting reservation", squareId, formattedDateTime);
	}

	public static String removeReservation(String userId, String squareId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
        
		userId = userId.toLowerCase();
		squareId = squareId.toLowerCase();
        
        if (!isSquareReservedByUser(userId, squareId, year, month, day, hour)) {
        	return String.format("ERROR: Square [%s] is not reserved by user [%s] at [%s]", squareId, userId, formattedDateTime);
        }
        
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			statement.executeUpdate(String.format(
				"DELETE FROM reservations WHERE userId = '%s' AND squareId = '%s' AND dt = '%s'",
				userId, squareId, formattedDateTime));
			
			connection.close();
			
			return String.format("SUCCESS: Square [%s] reservation at [%s] for user [%s] was successfully DELETED", squareId, formattedDateTime, userId);
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return String.format("ERROR: Unexpected error while deleting reservation", squareId, formattedDateTime);
	}
	
	public static List<Reservation> getReservationBySquare(String squareId){
		
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation;
		
		squareId = squareId.toLowerCase();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM reservations WHERE squareId = '%s'", squareId));

			while(rs.next()) {
				reservation = new Reservation(rs.getString("squareId"), rs.getString("userId"), rs.getTimestamp("dt").toString());
				reservations.add(reservation);
			}
			
			connection.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return reservations;
	}
	
	public static List<Reservation> getReservationByUser(String userId){
		
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation;
		
		userId = userId.toLowerCase();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM reservations WHERE userId = '%s'", userId));

			while(rs.next()) {
				reservation = new Reservation(rs.getString("squareId"), rs.getString("userId"), rs.getTimestamp("dt").toString());
				reservations.add(reservation);
			}
			
			connection.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return reservations;
	}
	
	public static boolean isSquareReserved(String squareId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		boolean result = false;
        
		squareId = squareId.toLowerCase();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM reservations WHERE squareId = '%s' AND dt = '%s'", squareId, formattedDateTime));

			result = rs.isBeforeFirst();
			connection.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	public static boolean isSquareReservedByUser(String userId, String squareId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		boolean result = false;
        
		userId = userId.toLowerCase();
		squareId = squareId.toLowerCase();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			ResultSet rs = statement.executeQuery(String.format(
				"SELECT * FROM reservations WHERE userId = '%s' AND squareId = '%s' AND dt = '%s'",
				userId, squareId, formattedDateTime));
			
			result =  rs.isBeforeFirst();
			connection.close();
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
}
