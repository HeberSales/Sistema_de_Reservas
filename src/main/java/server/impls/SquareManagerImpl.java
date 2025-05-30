package server.impls;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import server.interfaces.SquareManager;
import server.models.Reservation;

public class SquareManagerImpl extends UnicastRemoteObject implements SquareManager {

	private static final long serialVersionUID = 1L;
	
	public SquareManagerImpl() throws RemoteException {
		super();
	}

	@Override
	public String createReservation(String userId, String squareId, int year, int month, int day, int hour)
			throws RemoteException {
		
		System.out.println(String.format("LOG: Insert reservation process required. USER_ID [%s] - SQUARE_ID [%s] - DATE_TIME [%sh %d/%s/%s]", userId, squareId, hour, day, month, year));

		return Database.insertReservation(userId, squareId, year, month, day, hour);
	}

	@Override
	public String removeReservation(String userId, String squareId, int year, int month, int day, int hour)
			throws RemoteException {
		
		System.out.println(String.format("LOG: Remove reservation process required. USER_ID [%s] - SQUARE_ID [%s] - DATE_TIME [%sh %d/%s/%s]", userId, squareId, hour, day, month, year));
		
		return Database.removeReservation(userId, squareId, year, month, day, hour);
	}

	@Override
	public String checkReservation(String squareId, int year, int month, int day, int hour) throws RemoteException {
		
		System.out.println(String.format("LOG: Check reservation process required SQUARE_ID [%s] - DATE_TIME [%sh %d/%s/%s]", squareId, hour, day, month, year));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);

		if (Database.isSquareReserved(squareId, year, month, day, hour)) {
			return String.format("Square [%s] is RESERVED at [%s]", squareId, formattedDateTime);
		}

		return String.format("Square [%s] is FREE at [%s]", squareId, formattedDateTime);
	}

	@Override
	public List<Reservation> checkSquareReservations(String squareId) throws RemoteException {
		
		System.out.println(String.format("LOG: Check square's reservations process required. SQUARE_ID [%s]", squareId));

		return Database.getReservationBySquare(squareId);
	}

	@Override
	public List<Reservation> checkUserReservations(String userId) throws RemoteException {
		
		System.out.println(String.format("LOG: Check user's reservations process required. USER_ID [%s]", userId));
		
		return Database.getReservationByUser(userId);
	}
}
