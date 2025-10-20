package com.airline.reservation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class AirlineService {
    public List<Flight> searchFlights(String origin, String destination) throws SQLException {
        List<Flight> result = new ArrayList<>();
        String sql = "SELECT flight_number, origin, destination, total_seats, booked_seats FROM flights WHERE origin = ? AND destination = ? AND booked_seats < total_seats";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);  
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Flight object creation uses the data retrieved from the database
                    Flight f = new Flight(
                        rs.getString("flight_number"), 
                        rs.getString("origin"), 
                        rs.getString("destination"), 
                        rs.getInt("total_seats")
                    );
                    result.add(f);
                }
            }
        } // Connection, Statement, and ResultSet are automatically closed
        return result;
    }

    // Book a flight 
    public Booking bookFlight(String passengerName, String flightNumber) throws SQLException {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check seat availability and lock the row (FOR UPDATE)
            String selectSql = "SELECT total_seats, booked_seats, origin, destination FROM flights WHERE flight_number = ? FOR UPDATE";
            int totalSeats = 0;
            int bookedSeats = 0;
            String origin = null;
            String destination = null;

            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, flightNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        totalSeats = rs.getInt("total_seats");
                        bookedSeats = rs.getInt("booked_seats");
                        origin = rs.getString("origin");
                        destination = rs.getString("destination");
                    } else {
                        conn.rollback();
                        return null; // Flight not found
                    }
                }
            }
            
            if (bookedSeats >= totalSeats) {
                conn.rollback();
                return null; // No seats available
            }

            // 2. Book the seat (Update flights table)
            String updateSql = "UPDATE flights SET booked_seats = booked_seats + 1 WHERE flight_number = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, flightNumber);
                updateStmt.executeUpdate();
            }

            // 3. Create the booking record (Insert into bookings table)
            String insertSql = "INSERT INTO bookings (passenger_name, flight_number) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, passengerName);
                insertStmt.setString(2, flightNumber);
                insertStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            
            // Return a new Booking object for confirmation
            Flight bookedFlight = new Flight(flightNumber, origin, destination, totalSeats);
            // NOTE: bookedSeats + 1 is the new booked count in the DB, but this POJO is just for display
            return new Booking(passengerName, bookedFlight);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}

