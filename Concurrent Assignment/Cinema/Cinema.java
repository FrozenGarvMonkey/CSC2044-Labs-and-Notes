/* 
 * Two Theatres = 200 Seats
 * 200 Customers Total
 * Random Delay After Selection Stage (500-1000ms), Then Confirmation
 * 
 */

package Cinema;

import java.util.Random;

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

class TheaterHall {
    private int hall_number, availableSeats;
    int[] seats;

    public TheaterHall(int hall_number, int availableSeats){
        this.hall_number = hall_number;
        this.availableSeats = availableSeats;    
        this.seats = new int[availableSeats];
    }

    /* 
     * We initialise our array of seats. Each single seat can be one of three values:
     * - seat[i] = 0 (Seat is Available)
     * - seat[i] = 1 (Seat is Confirmed Booked)
     * - seat[i] = 2 (Seat is Reserved but not confirmed
     */


    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getSingleSeat(int number){
        return seats[number];
    }

    public synchronized void reserveSeat(int number, String name){
        Random rand = new Random();
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
            if(number < 199){
                System.out.println("\nSeat " + number + " is already reserved/booked " + name + ". Booking next seat!" + "(Seat " + (number + 1)  + ")\n");
                reserveSeat((number+1), name);
            }
            int new_number = rand.ints(0,200).findFirst().getAsInt();
            System.out.println("\nSeat " + number + " is already reserved/booked " + name + ". Booking next seat!" + "(Seat " + new_number + ")\n");
            reserveSeat(new_number, name);
        }

    }

    public void confirmSeat(int number, String name){
        System.out.println("Confirming Seat " + number + " for " + name + " in Theater " + hall_number);
        seats[number] = 1;
        availableSeats -= 1;
    }

}

class TicketPortal {
    Random rand = new Random();

    TheaterHall hall_1 = new TheaterHall(1,200);
    TheaterHall hall_2 = new TheaterHall(2,200);


	public void hallHandle(String name, int numberOfSeats, TheaterHall hall) {

        if (hall.getAvailableSeats() >= numberOfSeats){
            
            for (int i = 0; i < numberOfSeats; i++){
                hall.reserveSeat(rand.ints(0,200).findFirst().getAsInt(), name);
            }
            System.out.println("\nAll your selected seats have been booked " + name + ".\n");
        }else{
            System.out.println("Theater is fully booked!");
        }

	}
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

public class Cinema{

    public static int randomRange(int min, int max){
        Random rand = new Random();
        return rand.ints(min,max+1).findFirst().getAsInt();
    }
    
    public static void main(String[] args) {
        TicketPortal ticketPortal = new TicketPortal();
        
        for (int i = 0; i < 200; i++){
            Customer t = new Customer(ticketPortal, String.format("Customer %d", i+1), randomRange(1,3), randomRange(1,2));
            t.start();
        }
    }
}