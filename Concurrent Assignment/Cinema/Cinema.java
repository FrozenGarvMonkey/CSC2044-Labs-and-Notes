/* 
 * Two Theatres = 200 Seats
 * 200 Customers Total
 * Random Delay After Selection Stage (500-1000ms), Then Confirmation
 * 
 */

package Cinema;

import java.util.Random;

/* 
 * Customer Thread :-
 * - Customer carries name, number of seats, and the theater hall generated randomly in the main thread.
 * - Each customer opens a thread of the online Ticket Portal
 */
class Customer extends Thread {

	private TicketPortal ticketPortal;
	private String customerName;
	private int noOfSeatsToBook;
    private int theaterHall;

	public Customer (TicketPortal ticketPortal,String customerName, int noOfSeatsToBook, int theaterHall) {
		this.customerName = customerName;
		this.ticketPortal = ticketPortal;
		this.noOfSeatsToBook = noOfSeatsToBook;
        this.theaterHall = theaterHall;
	}
	
	public void run() {
		ticketPortal.bookTicket(customerName, noOfSeatsToBook, theaterHall);
	}
}

/* 
 * Class TheaterHall :-
 * - Represents each of the theater halls.
 * - Contains the total number of available seats and the seat arrays.
 */
class TheatreHall {
    private int hall_number, availableSeats;
    int[] seats;

    public TheatreHall(int hall_number, int availableSeats){
        this.hall_number = hall_number;
        this.availableSeats = availableSeats;    
        this.seats = new int[availableSeats];
    }

    /* 
     * We initialise our array of seats. Each single seat can be one of three values:
     * - seat[i] = 0 (Seat is Available)
     * - seat[i] = 1 (Seat is Confirmed Booked)
     * - seat[i] = 2 (Seat is Reserved but not confirmed)
     */

    // Returns Total Number of Seats Available
    public int getAvailableSeats() {
        return availableSeats;
    }

    // Returns a single seat's value within the seat array.
    public int getSingleSeat(int number){
        return seats[number];
    }

    /* 
     * Synchronised method to reserve a seat. 
     * Seat value is changed to 2 to indicate a reserved but not confirmed seat.
     * Once the thread delays by a random value between 500ms to 1000ms, it confirms the booking.
     */
    public synchronized void reserveSeat(int number, String name){
        Random rand = new Random();
        // Condition to check if seat is available
        if (getSingleSeat(number) == 0){
            
            System.out.println("Reserving Seat " + number + " for " + name + " in Theater " + hall_number);
            seats[number] = 2;
    
            try {
                Thread.sleep(rand.ints(500,1001).findFirst().getAsInt());
                confirmSeat(number, name);
            }
            catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
            
        }else{
            // If a seat is not available, it checks for the next available seat within the array.
            for (int i = 0; i < getAvailableSeats(); i++){
                if (getSingleSeat(i) == 0){
                    System.out.println("\nSeat " + number + " is already reserved/booked " + name + ". Booking next available seat!" + "(Seat " + i  + ")\n");
                    reserveSeat((i), name);
                }
            }
        }

    }
    // Confirms a seat booking.
    public void confirmSeat(int number, String name){
        System.out.println("Confirming Seat " + number + " for " + name + " in Theater " + hall_number);
        seats[number] = 1;
        availableSeats -= 1;
    }

}

/* 
 * Class TicketPortal :-
 * - Initialises both theater halls
 * - Portal to book tickets based on selected hall
 */
class TicketPortal {
    Random rand = new Random();

    TheatreHall hall_1 = new TheatreHall(1,200);
    TheatreHall hall_2 = new TheatreHall(2,200);

    // Common method for all bookings within a hall.
	public void hallHandle(String name, int numberOfSeats, TheatreHall hall) {

        if (hall.getAvailableSeats() >= numberOfSeats){
            
            for (int i = 0; i < numberOfSeats; i++){
                hall.reserveSeat(rand.ints(0,200).findFirst().getAsInt(), name);
            }
            System.out.println("\nAll your selected seats have been booked " + name + ".\n");
        }else{
            System.out.println("Theater is fully booked!");
        }

	}
    // Condition to check which hall the customer is interested in booking.
	public void bookTicket(String name, int numberOfSeats, int theaterHall) {
    
        if (theaterHall == 1){
            hallHandle(name, numberOfSeats, hall_1);
        }
        else if(theaterHall == 2){
            hallHandle(name, numberOfSeats, hall_2);
        }
        else{
            System.out.println("Theater Hall Not Found!");
        }
	}
}

// Driver Class
public class Cinema{

    // Returns a value between min(inclusive) and max(exclusive)
    public static int randomRange(int min, int max){
        Random rand = new Random();
        return rand.ints(min,max+1).findFirst().getAsInt();
    }
    
    public static void main(String[] args) {
        TicketPortal ticketPortal = new TicketPortal();
        
        // 200 customer threads generated with random values.
        for (int i = 0; i < 200; i++){
            Customer t = new Customer(ticketPortal, String.format("Customer %d", i+1), randomRange(1,3), randomRange(1,2));
            t.start();
        }
    }
}