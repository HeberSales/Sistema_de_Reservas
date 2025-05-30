package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.interfaces.SquareManager;
import server.models.Reservation;

public class Client {
	
    public static void main(String[] args) throws RemoteException {
    	
    	SquareManager squareManager = null;
        Scanner scanner = new Scanner(System.in);
        String serverIp = "localhost";
        
        if (args.length > 0) {
        	serverIp = args[0];
        }
        
        try {
        	System.out.println("Conectando ao servidor: " + serverIp);
            Registry registry = LocateRegistry.getRegistry(serverIp);
            squareManager = (SquareManager) registry.lookup("SquareManager");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println();
        System.out.println("Bem-vindo ao sistema de reserva de quadras!");
        System.out.println("As quadras são reservadas em períodos de 1 hora!");
        
        String options = "-----------------------------------------------------\n"
				+ "1. Cadastrar nova reserva\n"
				+ "2. Desfazer uma reserva\n"
    			+ "3. Consultar se a quadra está livre no horário desejado\n"
    			+ "4. Listar reservas de uma quadra\n"
    			+ "5. Listar reservas de um usuário\n"
    			+ "6. Terminar o cliente\n"
    			+ "0. Mostrar opções novamente\n"
    			+ "-----------------------------------------------------\n";
        
		System.out.print(options);
        
        while(true) {
        	
        	System.out.print("Selecione uma das opções disponíveis (0 para mostrar opções novamente): ");
        	
        	int key;
        	
        	try {
        		key = scanner.nextInt();
        	} catch (InputMismatchException e) {
				System.out.print("Por favor, digite o número da opção desejada: ");
				scanner.next();
				continue;
			}
	        
	        switch (key) {
		        
				case 1: {
					System.out.println("Criando reserva...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					System.out.print("Digite o ID da quadra: ");
					String squareId = scanner.next();
	
					String dateString = scanAndValidateDateString(scanner);
	
					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ squareManager.createReservation(userId, squareId, year, month, day, hour));
					}
					continue;
				}
				
				case 2: {
					System.out.println("Desfazendo reserva...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					System.out.print("Digite o ID da quadra: ");
					String squareId = scanner.next();
	
					String dateString = scanAndValidateDateString(scanner);

					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ squareManager.removeReservation(userId, squareId, year, month, day, hour));
					}
					continue;
				}
				
				case 3: {
					System.out.println("Consultando uma quadra...");
					
					System.out.print("Digite o ID da quadra: ");
					String squareId = scanner.next();
					
					String dateString = scanAndValidateDateString(scanner);
					
					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ squareManager.checkReservation(squareId, year, month, day, hour));
					}
					continue;
				}
				
				case 4: {
					System.out.println("Listando reservas de uma quadra...");
					
					System.out.print("Digite o ID da quadra: ");
					String squareId = scanner.next();
					
					System.out.println("Reservas da quadra " + squareId + ": ");
					
			    	List<Reservation> reservations = squareManager.checkSquareReservations(squareId);
			    	
			    	if (reservations.isEmpty()) {
			    		System.out.println("Não há reservas para a quadra!");
			    		continue;
			    	}
			    	
			        String format = "|%1$-20s|%2$-20s|%3$-25s|\n";
			        System.out.format(format, "ID da quadra", "ID do Usuário", "Data e hora da reserva");
			    	
					for (Reservation r : reservations) {
				        System.out.format(format,  r.getSquareId(), r.getUserId(), r.getDateTime());
			    	}
					
					continue;
				}
				
				case 5: {
					System.out.println("Listando reservas de um usuário...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					
					System.out.println("Reservas do usuário " + userId + ": ");
					
			    	List<Reservation> reservations = squareManager.checkUserReservations(userId);
			    	
			    	if (reservations.isEmpty()) {
			    		System.out.println("Não há reservas desse usuário!");
			    		continue;
			    	}
			    	
			        String format = "|%1$-20s|%2$-20s|%3$-25s|\n";
			        System.out.format(format, "ID da quadra", "ID do Usuário", "Data e hora da reserva");
			    	
					for (Reservation r : reservations) {
				        System.out.format(format,  r.getSquareId(), r.getUserId(), r.getDateTime());
			    	}
					
					continue;
				}
				
				case 6: {
					System.out.println("Finalizando cliente...");
					break;
				}
				
				case 0: {
					System.out.print(options);
					continue;
				}
				
				default:
					System.out.println("Digite um valor válido");
					continue;
			}
	        break;
        }
        
        scanner.close();
    }

	private static String scanAndValidateDateString(Scanner scanner) {
		
        Pattern pattern = Pattern.compile("(\\d{2}h \\d{2}/\\d{2}/\\d{4})", Pattern.CASE_INSENSITIVE);
		boolean matchFound = false;
		String dateString = "";

		do {
			scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
			System.out.println("Insira a data da reserva  (Siga o formato: 13h 12/09/2025): ");
			dateString = scanner.next();
			Matcher matcher = pattern.matcher(dateString);
			matchFound = matcher.find();
		} while (!matchFound);
		
		return dateString;
	}
}
