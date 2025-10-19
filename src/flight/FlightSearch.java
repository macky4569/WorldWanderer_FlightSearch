package flight;
//package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * FlightSearch class handles flight search validation for the WorldWanderer website.
 * Validates search parameters against business rules and initializes class attributes
 * if all validations pass.
 */
public class FlightSearch {
    private String departureDate;
    private String departureAirportCode;
    private boolean emergencyRowSeating;
    private String returnDate;
    private String destinationAirportCode;
    private String seatingClass;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;

    // Valid airport codes
    private static final String[] VALID_AIRPORTS = {"syd", "mel", "lax", "cdg", "del", "pvg", "doh"};
    
    // Valid seating classes
    private static final String[] VALID_SEATING_CLASSES = {"economy", "premium economy", "business", "first"};

    /**
     * Validates flight search parameters and initializes class attributes if valid.
     * 
     * @param departureDate The departure date in DD/MM/YYYY format
     * @param departureAirportCode The departure airport code (lowercase)
     * @param emergencyRowSeating Whether emergency row seating is requested
     * @param returnDate The return date in DD/MM/YYYY format
     * @param destinationAirportCode The destination airport code (lowercase)
     * @param seatingClass The seating class (lowercase)
     * @param adultPassengerCount Number of adult passengers
     * @param childPassengerCount Number of child passengers (2-11 years)
     * @param infantPassengerCount Number of infant passengers (<2 years)
     * @return true if all validations pass, false otherwise
     */
    public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating,
                                   String returnDate, String destinationAirportCode, String seatingClass,
                                   int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {
        
        // Validate Condition 1: Total passengers must be 1-9
        if (!isValidTotalPassengers(adultPassengerCount, childPassengerCount, infantPassengerCount)) {
            return false;
        }

        // Validate Condition 2: Children cannot be seated in emergency row or first class
        if (!isValidChildSeating(childPassengerCount, emergencyRowSeating, seatingClass)) {
            return false;
        }

        // Validate Condition 3: Infants cannot be seated in emergency row or business class
        if (!isValidInfantSeating(infantPassengerCount, emergencyRowSeating, seatingClass)) {
            return false;
        }

        // Validate Condition 4: Children ratio - max 2 children per adult
        if (!isValidChildToAdultRatio(adultPassengerCount, childPassengerCount)) {
            return false;
        }

        // Validate Condition 5: Infant ratio - max 1 infant per adult
        if (!isValidInfantToAdultRatio(adultPassengerCount, infantPassengerCount)) {
            return false;
        }

        // Validate Condition 7: Date format validation (must be done before Condition 6)
        if (!isValidDateFormat(departureDate) || !isValidDateFormat(returnDate)) {
            return false;
        }

        // Validate Condition 6: Departure date cannot be in the past
        if (!isDepartureDateValid(departureDate)) {
            return false;
        }

        // Validate Condition 8: Return date must be on or after departure date
        if (!isReturnDateValid(departureDate, returnDate)) {
            return false;
        }

        // Validate Condition 9: Seating class must be valid
        if (!isValidSeatingClass(seatingClass)) {
            return false;
        }

        // Validate Condition 10: Only economy class can have emergency row
        if (!isValidEmergencyRowClass(emergencyRowSeating, seatingClass)) {
            return false;
        }

        // Validate Condition 11: Airport codes must be valid and different
        if (!isValidAirportCodes(departureAirportCode, destinationAirportCode)) {
            return false;
        }

        // All validations passed - initialize class attributes
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.emergencyRowSeating = emergencyRowSeating;
        this.returnDate = returnDate;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass = seatingClass;
        this.adultPassengerCount = adultPassengerCount;
        this.childPassengerCount = childPassengerCount;
        this.infantPassengerCount = infantPassengerCount;

        return true;
    }

    /**
     * Condition 1: Validates total passenger count (1-9).
     */
    private boolean isValidTotalPassengers(int adults, int children, int infants) {
        int total = adults + children + infants;
        return total >= 1 && total <= 9;
    }

    /**
     * Condition 2: Validates that children are not in emergency row or first class.
     */
    private boolean isValidChildSeating(int childCount, boolean emergencyRow, String seatClass) {
        if (childCount > 0) {
            if (emergencyRow || "first".equals(seatClass)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Condition 3: Validates that infants are not in emergency row or business class.
     */
    private boolean isValidInfantSeating(int infantCount, boolean emergencyRow, String seatClass) {
        if (infantCount > 0) {
            if (emergencyRow || "business".equals(seatClass)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Condition 4: Validates child to adult ratio (max 2 children per adult).
     */
    private boolean isValidChildToAdultRatio(int adults, int children) {
        return children <= (adults * 2);
    }

    /**
     * Condition 5: Validates infant to adult ratio (max 1 infant per adult).
     */
    private boolean isValidInfantToAdultRatio(int adults, int infants) {
        return infants <= adults;
    }

    /**
     * Condition 7: Validates date format DD/MM/YYYY with strict validation.
     */
    private boolean isValidDateFormat(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        try {
            // Use strict resolver style to prevent invalid dates like 29/02/2026
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Condition 6: Validates that departure date is not in the past.
     */
    private boolean isDepartureDateValid(String depDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate departure = LocalDate.parse(depDate, formatter);
            LocalDate today = LocalDate.now();
            
            // Departure date must be today or in the future
            return !departure.isBefore(today);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Condition 8: Validates that return date is on or after departure date.
     */
    private boolean isReturnDateValid(String depDate, String retDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate departure = LocalDate.parse(depDate, formatter);
            LocalDate returnD = LocalDate.parse(retDate, formatter);
            
            // Return date must be on or after departure date
            return !returnD.isBefore(departure);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Condition 9: Validates seating class is one of the valid options.
     */
    private boolean isValidSeatingClass(String seatClass) {
        if (seatClass == null) {
            return false;
        }
        
        for (String validClass : VALID_SEATING_CLASSES) {
            if (validClass.equals(seatClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Condition 10: Validates that only economy class can have emergency row.
     */
    private boolean isValidEmergencyRowClass(boolean emergencyRow, String seatClass) {
        if (emergencyRow) {
            return "economy".equals(seatClass);
        }
        return true; // Non-emergency row seating is valid for all classes
    }

    /**
     * Condition 11: Validates airport codes are valid and different.
     */
    private boolean isValidAirportCodes(String departure, String destination) {
        if (departure == null || destination == null) {
            return false;
        }

        // Check if airports are the same
        if (departure.equals(destination)) {
            return false;
        }

        // Validate departure airport
        boolean departureValid = false;
        for (String airport : VALID_AIRPORTS) {
            if (airport.equals(departure)) {
                departureValid = true;
                break;
            }
        }

        // Validate destination airport
        boolean destinationValid = false;
        for (String airport : VALID_AIRPORTS) {
            if (airport.equals(destination)) {
                destinationValid = true;
                break;
            }
        }

        return departureValid && destinationValid;
    }

    // Getter methods for testing purposes
    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public boolean isEmergencyRowSeating() {
        return emergencyRowSeating;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public String getSeatingClass() {
        return seatingClass;
    }

    public int getAdultPassengerCount() {
        return adultPassengerCount;
    }

    public int getChildPassengerCount() {
        return childPassengerCount;
    }

    public int getInfantPassengerCount() {
        return infantPassengerCount;
    }
}