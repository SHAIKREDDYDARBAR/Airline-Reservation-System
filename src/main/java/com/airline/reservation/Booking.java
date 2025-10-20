package com.airline.reservation;
public class Booking {
    private String passengerName;
    private Flight flight;
    public Booking(String passengerName, Flight flight) {
        this.passengerName = passengerName;
        this.flight = flight;
    }
    public String getPassengerName() {
        return passengerName;
    }
    public Flight getFlight() {
        return flight;
    }
    @Override
    public String toString() {
        return "Booking: passenger=\"" + passengerName + "\", flight="
            + flight.getFlightNumber()
            + " (" + flight.getOrigin() + " → " + flight.getDestination() + ")";
    }
}
