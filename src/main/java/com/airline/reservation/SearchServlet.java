package com.airline.reservation;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/search", "/book"}) 
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AirlineService service = new AirlineService();

    // Handles flight search requests (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");

        if (origin != null && destination != null) {
            try {
                var flights = service.searchFlights(origin, destination);
                request.setAttribute("flights", flights);
                // Flag to indicate a search was attempted, even if empty
                request.setAttribute("searched", true); 
            } catch (SQLException e) {
                request.getSession().setAttribute("error", "Database connection or SQL error: " + e.getMessage());
                // Clear search results on error
                request.setAttribute("flights", null); 
            }
        }
        // Forward to the JSP to display the UI and results
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    // Handles booking requests (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if the URL path matches the booking action
        if (request.getServletPath().equals("/book")) {
            String flightNumber = request.getParameter("flightNumber");
            String passengerName = request.getParameter("passengerName");
            
            try {
                Booking booking = service.bookFlight(passengerName, flightNumber);

                if (booking != null) {
                    request.getSession().setAttribute("message", "Booking Successful! Flight " + flightNumber);
                } else {
                    request.getSession().setAttribute("error", "Booking Failed! Flight " + flightNumber + " is full or invalid.");
                }
            } catch (SQLException e) {
                 request.getSession().setAttribute("error", "Database error during booking. Try again. Error: " + e.getMessage());
            }
        }
        
        // Redirect back to the index page to display the message
        response.sendRedirect("index.jsp"); 
    }
}
