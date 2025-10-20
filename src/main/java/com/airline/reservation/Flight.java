package com.airline.reservation;
public class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private int totalSeats;
    private int bookedSeats;
    public Flight(String flightNumber, String origin, String destination, int totalSeats) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.bookedSeats = 0;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public String getOrigin() {
        return origin;
    }
    public String getDestination() {
        return destination;
    }
    public int getTotalSeats() {
        return totalSeats;
    }
    public int getBookedSeats() {
        return bookedSeats;
    }
    public int getAvailableSeats() {
        return totalSeats - bookedSeats;
    }
    public boolean bookSeat() {
        if (bookedSeats < totalSeats) {
            bookedSeats++;
            return true;
        } else {
            return false;
        }
    }
    public boolean cancelSeat() {
        if (bookedSeats > 0) {
            bookedSeats--;
            return true;
        } else {
            return false;
        }
    }
    public String toString() {
        return "Flight[" + flightNumber + "]: " + origin + " → " + destination
            + " | Seats total=" + totalSeats
            + ", available=" + getAvailableSeats();
    }
}
