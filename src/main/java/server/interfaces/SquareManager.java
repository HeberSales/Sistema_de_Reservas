package server.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.models.Reservation;

public interface SquareManager extends Remote {
	
	/** Create a reservation
	 * @return true if reservation was successfully created or false if reservation was not created
	 */
	String createReservation(String userId, String squareId, int year, int month, int day, int hour) throws RemoteException;
	
	/** Remove a reservation
	 * @return true if reservation was successfully removed or false if reservation was not removed
	 */
	String removeReservation(String userId, String squareId, int year, int month, int day, int hour) throws RemoteException;
	
	/** Verify if a reservation exists
	 * @return true if reservation exists or false if reservation does not exist
	 */
	String checkReservation(String squareId, int year, int month, int day, int hour) throws RemoteException;
	
	/** List a square's reservations
	 * @return square's reservations
	 */
	List<Reservation> checkSquareReservations(String squareId) throws RemoteException;
	
	/** List a user's reservations
	 * @return user's reservations
	 */
	List<Reservation> checkUserReservations(String userId) throws RemoteException;
}
