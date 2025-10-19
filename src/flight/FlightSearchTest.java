package flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchTest {

    private FlightSearch flightSearch;

    @BeforeEach
    void setUp() {
        flightSearch = new FlightSearch();
    }

    @Test
    void testValidFlightSearch() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "economy",
                2,
                2,
                1
        );
        assertTrue(result);
        assertEquals("syd", flightSearch.getDepartureAirportCode());
        assertEquals("mel", flightSearch.getDestinationAirportCode());
        assertEquals("economy", flightSearch.getSeatingClass());
        assertEquals(2, flightSearch.getAdultPassengerCount());
        assertEquals(2, flightSearch.getChildPassengerCount());
        assertEquals(1, flightSearch.getInfantPassengerCount());
    }

    @Test
    void testInvalidTotalPassengers() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "economy",
                5,
                3,
                2
        );
        assertFalse(result); // total passengers = 10 -> invalid
    }

    @Test
    void testChildSeatingInFirstClass() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "first",
                1,
                1,
                0
        );
        assertFalse(result); // children cannot be in first class
    }

    @Test
    void testInfantSeatingInBusinessClass() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "business",
                1,
                0,
                1
        );
        assertFalse(result); // infants cannot be in business class
    }

    @Test
    void testInvalidChildToAdultRatio() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "economy",
                1,
                3,
                0
        );
        assertFalse(result); // 3 children but only 1 adult -> invalid
    }

    @Test
    void testInvalidInfantToAdultRatio() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "economy",
                1,
                0,
                2
        );
        assertFalse(result); // 2 infants but only 1 adult -> invalid
    }

    @Test
    void testInvalidDepartureDateFormat() {
        boolean result = flightSearch.runFlightSearch(
                "2025-10-25",
                "syd",
                false,
                "30/10/2025",
                "mel",
                "economy",
                1,
                0,
                0
        );
        assertFalse(result); // invalid date format
    }

    @Test
    void testReturnBeforeDeparture() {
        boolean result = flightSearch.runFlightSearch(
                "30/10/2025",
                "syd",
                false,
                "25/10/2025",
                "mel",
                "economy",
                1,
                0,
                0
        );
        assertFalse(result); // return date before departure
    }

    @Test
    void testInvalidSeatingForEmergencyRow() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                true,
                "30/10/2025",
                "mel",
                "business",
                1,
                0,
                0
        );
        assertFalse(result); // only economy can have emergency row
    }

    @Test
    void testInvalidAirportCodes() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "syd",
                "economy",
                1,
                0,
                0
        );
        assertFalse(result); // departure and destination same
    }

    @Test
    void testInvalidDepartureAirport() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "abc",
                false,
                "30/10/2025",
                "mel",
                "economy",
                1,
                0,
                0
        );
        assertFalse(result); // invalid departure airport
    }

    @Test
    void testInvalidDestinationAirport() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025",
                "syd",
                false,
                "30/10/2025",
                "xyz",
                "economy",
                1,
                0,
                0
        );
        assertFalse(result); // invalid destination airport
    }
}
