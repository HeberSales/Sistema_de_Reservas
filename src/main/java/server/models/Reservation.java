package server.models;

import java.io.Serializable;

public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String squareId;
	String userId;
	String dateTime;
	
	public Reservation(String squareId, String userId, String localDateTime) {
		
		this.squareId = squareId;
		this.userId = userId;
		this.dateTime = localDateTime;
	}
	
	public String getSquareId() {
		return squareId;
	}
	
	public void setSquareId(String squareId) {
		this.squareId = squareId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
