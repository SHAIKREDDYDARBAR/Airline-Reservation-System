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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {    
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        if (origin != null && destination != null) {
            try {
                var flights = service.searchFlights(origin, destination);
                request.setAttribute("flights", flights);
                request.setAttribute("searched", true); 
            } catch (SQLException e) {
                request.getSession().setAttribute("error", "Database connection or SQL error: " + e.getMessage());
                request.setAttribute("flights", null); 
            }
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {    
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
        response.sendRedirect("index.jsp"); 
    }
}

