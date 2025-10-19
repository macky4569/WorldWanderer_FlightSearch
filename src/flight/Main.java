package flight;

public class Main {
    public static void main(String[] args) {
        FlightSearch fs = new FlightSearch();

        boolean valid = fs.runFlightSearch(
                "25/10/2025", "syd", false, "30/10/2025",
                "mel", "economy", 2, 1, 1
        );

        if (valid) {
            System.out.println("Flight search is valid!");
        } else {
            System.out.println("Invalid flight search parameters.");
        }
    }
}
