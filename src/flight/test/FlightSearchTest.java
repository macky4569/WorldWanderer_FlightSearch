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
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "economy",
                2, 2, 1
        );
        assertTrue(result);
        assertEquals("25/10/2025", flightSearch.getDepartureDate());
        assertEquals("mel", flightSearch.getDestinationAirportCode());
        assertEquals(2, flightSearch.getAdultPassengerCount());
        assertEquals(2, flightSearch.getChildPassengerCount());
        assertEquals(1, flightSearch.getInfantPassengerCount());
    }

    @Test
    void testInvalidTotalPassengers() {
        // More than 9 passengers
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "economy",
                5, 3, 2
        );
        assertFalse(result);
    }

    @Test
    void testChildInEmergencyRow() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", true,
                "30/10/2025", "mel", "economy",
                2, 1, 0
        );
        assertFalse(result);
    }

    @Test
    void testInfantInBusinessClass() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "business",
                2, 0, 1
        );
        assertFalse(result);
    }

    @Test
    void testInvalidChildToAdultRatio() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "economy",
                1, 3, 0
        );
        assertFalse(result);
    }

    @Test
    void testInvalidInfantToAdultRatio() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "economy",
                1, 0, 2
        );
        assertFalse(result);
    }

    @Test
    void testInvalidDepartureDateFormat() {
        boolean result = flightSearch.runFlightSearch(
                "2025-10-25", "syd", false,
                "30/10/2025", "mel", "economy",
                1, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testDepartureDateInPast() {
        boolean result = flightSearch.runFlightSearch(
                "01/01/2020", "syd", false,
                "30/10/2025", "mel", "economy",
                1, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testReturnDateBeforeDeparture() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "20/10/2025", "mel", "economy",
                1, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testInvalidSeatingClass() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "mel", "vip",
                1, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testEmergencyRowInNonEconomyClass() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", true,
                "30/10/2025", "mel", "business",
                2, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testInvalidAirportCodes() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "abc", "economy",
                1, 0, 0
        );
        assertFalse(result);
    }

    @Test
    void testSameDepartureAndDestination() {
        boolean result = flightSearch.runFlightSearch(
                "25/10/2025", "syd", false,
                "30/10/2025", "syd", "economy",
                1, 0, 0
        );
        assertFalse(result);
    }
}
