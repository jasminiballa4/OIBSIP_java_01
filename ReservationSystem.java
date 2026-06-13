import java.util.ArrayList;
import java.util.Scanner;

public class ReservationSystem {

    // ───────────────────────────────────────────
    //  Ticket data class
    // ───────────────────────────────────────────
    static class Ticket {
        String pnr;
        String passengerName;
        int    age;
        String trainNumber;
        String trainName;
        String classType;
        String dateOfJourney;
        String from;
        String to;
        String status;

        Ticket(String pnr, String passengerName, int age,
               String trainNumber, String trainName, String classType,
               String dateOfJourney, String from, String to) {
            this.pnr           = pnr;
            this.passengerName = passengerName;
            this.age           = age;
            this.trainNumber   = trainNumber;
            this.trainName     = trainName;
            this.classType     = classType;
            this.dateOfJourney = dateOfJourney;
            this.from          = from;
            this.to            = to;
            this.status        = "Confirmed";
        }
    }

    // ───────────────────────────────────────────
    //  Global state
    // ───────────────────────────────────────────
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static Scanner sc                = new Scanner(System.in);
    static int pnrCounter            = 1;

    // ───────────────────────────────────────────
    //  Entry point
    // ───────────────────────────────────────────
    public static void main(String[] args) {
        // Add some sample tickets so the system isn't empty
        tickets.add(new Ticket("PNR001", "Ravi Kumar",  28, "12345", "Rajdhani Express", "AC 2 Tier (2A)", "2026-06-20", "Chennai",   "Delhi"));
        tickets.add(new Ticket("PNR002", "Priya Singh", 34, "11007", "Deccan Express",   "Sleeper (SL)",   "2026-06-22", "Mumbai",    "Pune"));
        pnrCounter = 3;

        printBanner();
        login();
    }

    // ───────────────────────────────────────────
    //  Banner
    // ───────────────────────────────────────────
    static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       ONLINE RESERVATION SYSTEM              ║");
        System.out.println("║       OIBSIP Java Internship - Task 1        ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    // ───────────────────────────────────────────
    //  Login
    // ───────────────────────────────────────────
    static void login() {
        System.out.println("========== LOGIN ==========");

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter Login ID   : ");
            String id = sc.nextLine().trim();
            System.out.print("Enter Password   : ");
            String pass = sc.nextLine().trim();

            if (id.equals("admin") && pass.equals("1234")) {
                System.out.println("\n✔ Login successful! Welcome, " + id + ".\n");
                mainMenu();
                return;
            } else {
                attempts++;
                System.out.println("✘ Invalid credentials. " + (3 - attempts) + " attempt(s) left.\n");
            }
        }
        System.out.println("Too many failed attempts. Exiting.");
    }

    // ───────────────────────────────────────────
    //  Main Menu
    // ───────────────────────────────────────────
    static void mainMenu() {
        while (true) {
            System.out.println("========== MAIN MENU ==========");
            System.out.println("  1. View All Bookings");
            System.out.println("  2. Book a Ticket");
            System.out.println("  3. Cancel a Ticket");
            System.out.println("  4. Logout");
            System.out.println("================================");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1" -> viewBookings();
                case "2" -> bookTicket();
                case "3" -> cancelTicket();
                case "4" -> {
                    System.out.println("Logged out. Goodbye!");
                    System.exit(0);
                }
                default  -> System.out.println("✘ Invalid option. Please enter 1, 2, 3 or 4.\n");
            }
        }
    }

    // ───────────────────────────────────────────
    //  View All Bookings
    // ───────────────────────────────────────────
    static void viewBookings() {
        System.out.println("========== ALL BOOKINGS ==========");

        if (tickets.isEmpty()) {
            System.out.println("No bookings found.\n");
            return;
        }

        long confirmed = tickets.stream().filter(t -> t.status.equals("Confirmed")).count();
        long cancelled = tickets.stream().filter(t -> t.status.equals("Cancelled")).count();

        System.out.println("Total: " + tickets.size() + "  |  Confirmed: " + confirmed + "  |  Cancelled: " + cancelled);
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-8s %-16s %-4s %-10s %-20s %-14s %-10s%n",
                "PNR", "Name", "Age", "Train No", "Train Name", "Class", "Status");
        System.out.println("------------------------------------------------------------------");

        for (Ticket t : tickets) {
            System.out.printf("%-8s %-16s %-4d %-10s %-20s %-14s %-10s%n",
                    t.pnr, t.passengerName, t.age,
                    t.trainNumber, t.trainName,
                    t.classType, t.status);
        }
        System.out.println("------------------------------------------------------------------\n");
    }

    // ───────────────────────────────────────────
    //  Book a Ticket
    // ───────────────────────────────────────────
    static void bookTicket() {
        System.out.println("========== BOOK A TICKET ==========");

        System.out.print("Passenger Name   : ");
        String name = sc.nextLine().trim();

        System.out.print("Age              : ");
        int age;
        try {
            age = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✘ Invalid age. Returning to menu.\n");
            return;
        }

        System.out.print("Train Number     : ");
        String trainNo = sc.nextLine().trim();

        // Auto-fill train name based on number
        String trainName = getTrainName(trainNo);
        System.out.println("Train Name       : " + trainName + " (auto-filled)");

        System.out.println("Class Types      : 1-Sleeper(SL)  2-AC 3 Tier(3A)  3-AC 2 Tier(2A)  4-AC First(1A)  5-General(GN)");
        System.out.print("Choose Class (1-5): ");
        String classChoice = sc.nextLine().trim();
        String classType   = getClassType(classChoice);
        if (classType == null) {
            System.out.println("✘ Invalid class. Returning to menu.\n");
            return;
        }

        System.out.print("Date of Journey  : ");
        String date = sc.nextLine().trim();

        System.out.print("From (Station)   : ");
        String from = sc.nextLine().trim();

        System.out.print("To (Station)     : ");
        String to = sc.nextLine().trim();

        if (name.isEmpty() || date.isEmpty() || from.isEmpty() || to.isEmpty()) {
            System.out.println("✘ All fields are required. Returning to menu.\n");
            return;
        }

        // Generate PNR
        String pnr = "PNR" + String.format("%03d", pnrCounter++);

        Ticket t = new Ticket(pnr, name, age, trainNo, trainName, classType, date, from, to);
        tickets.add(t);

        System.out.println("\n✔ Ticket booked successfully!");
        System.out.println("----------------------------------");
        System.out.println("  PNR Number   : " + pnr);
        System.out.println("  Passenger    : " + name + " (Age: " + age + ")");
        System.out.println("  Train        : " + trainNo + " — " + trainName);
        System.out.println("  Class        : " + classType);
        System.out.println("  Journey      : " + from + " → " + to);
        System.out.println("  Date         : " + date);
        System.out.println("  Status       : Confirmed");
        System.out.println("----------------------------------\n");
    }

    // ───────────────────────────────────────────
    //  Cancel a Ticket
    // ───────────────────────────────────────────
    static void cancelTicket() {
        System.out.println("========== CANCEL A TICKET ==========");
        System.out.print("Enter PNR Number : ");
        String pnr = sc.nextLine().trim().toUpperCase();

        Ticket found = null;
        for (Ticket t : tickets) {
            if (t.pnr.equals(pnr)) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("✘ PNR not found. Please check and try again.\n");
            return;
        }

        System.out.println("\n--- Booking Details ---");
        System.out.println("  PNR Number   : " + found.pnr);
        System.out.println("  Passenger    : " + found.passengerName + " (Age: " + found.age + ")");
        System.out.println("  Train        : " + found.trainNumber + " — " + found.trainName);
        System.out.println("  Class        : " + found.classType);
        System.out.println("  Journey      : " + found.from + " → " + found.to);
        System.out.println("  Date         : " + found.dateOfJourney);
        System.out.println("  Status       : " + found.status);
        System.out.println("-----------------------");

        if (found.status.equals("Cancelled")) {
            System.out.println("This ticket is already cancelled.\n");
            return;
        }

        System.out.print("Are you sure you want to cancel? (yes / no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            found.status = "Cancelled";
            System.out.println("✔ Ticket " + found.pnr + " has been cancelled successfully.\n");
        } else {
            System.out.println("Cancellation aborted. Ticket is still active.\n");
        }
    }

    // ───────────────────────────────────────────
    //  Helpers
    // ───────────────────────────────────────────
    static String getTrainName(String trainNo) {
        return switch (trainNo) {
            case "12345" -> "Rajdhani Express";
            case "11007" -> "Deccan Express";
            case "22691" -> "Rajya Rani";
            case "12139" -> "Sewagram Express";
            case "12001" -> "Shatabdi Express";
            case "22222" -> "Duronto Express";
            default      -> "Express Train";
        };
    }

    static String getClassType(String choice) {
        return switch (choice) {
            case "1" -> "Sleeper (SL)";
            case "2" -> "AC 3 Tier (3A)";
            case "3" -> "AC 2 Tier (2A)";
            case "4" -> "AC First Class (1A)";
            case "5" -> "General (GN)";
            default  -> null;
        };
    }
}
