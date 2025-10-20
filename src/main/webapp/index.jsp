<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.airline.reservation.Flight" %>
<%@ page import="com.airline.reservation.Booking" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Airline Reservation System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* This is where the CSS errors were. I removed the erroneous external index.css and kept the internal style */
        body { font-family: 'Inter', sans-serif; background-color: #f7f9fb; }
    </style>
</head>
<body class="min-h-screen p-4 sm:p-8">
    <div class="max-w-4xl mx-auto bg-white p-6 rounded-xl shadow-2xl">
        <h1 class="text-3xl font-extrabold text-indigo-700 mb-6 border-b pb-2">Flight Search & Booking</h1>
        
        <%-- 
        JSP code to check for and display messages or errors stored in the session 
        --%>
        <% 
            String message = (String) session.getAttribute("message");
            String error = (String) session.getAttribute("error");
            session.removeAttribute("message");
            session.removeAttribute("error");
            
            if (message != null) { 
        %>
            <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4" role="alert">
                <%= message %>
            </div>
        <% } %>
        <% if (error != null) { %>
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                <%= error %>
            </div>
        <% } %>

        <form action="search" method="get" class="mb-8 p-6 bg-indigo-50 rounded-lg shadow-inner">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
                <div>
                    <label for="origin" class="block text-sm font-medium text-gray-700 mb-1">Origin City</label>
                    <input type="text" id="origin" name="origin" required placeholder="e.g., New Delhi" 
                           class="w-full p-2 border border-indigo-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500">
                </div>
                <div>
                    <label for="destination" class="block text-sm font-medium text-gray-700 mb-1">Destination City</label>
                    <input type="text" id="destination" name="destination" required placeholder="e.g., Mumbai" 
                           class="w-full p-2 border border-indigo-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500">
                </div>
                <button type="submit" class="w-full py-2 px-4 bg-indigo-600 text-white font-semibold rounded-lg shadow-md hover:bg-indigo-700 transition duration-300">
                    Search Flights
                </button>
            </div>
        </form>

        <%-- Display Search Results --%>
        <% 
        List<Flight> flights = (List<Flight>) request.getAttribute("flights"); 
        // Note: The 'var flights' error in the original screenshot is fixed by explicitly casting the request attribute here.

        if (flights != null && !flights.isEmpty()) {
        %>
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Available Flights</h2>
            <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200 rounded-lg overflow-hidden shadow-lg">
                    <thead class="bg-indigo-100">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Flight No.</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Route</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Available Seats</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase tracking-wider">Book</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <% for (Flight f : flights) { %>
                        <tr class="hover:bg-gray-50 transition duration-150">
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600"><%= f.getFlightNumber() %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800"><%= f.getOrigin() %> &rarr; <%= f.getDestination() %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-bold <%= f.getAvailableSeats() > 5 ? "text-green-600" : "text-orange-500" %>">
                                <%= f.getAvailableSeats() %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                <form action="book" method="post" class="flex items-center space-x-2">
                                    <input type="hidden" name="flightNumber" value="<%= f.getFlightNumber() %>">
                                    <input type="text" name="passengerName" placeholder="Your Name" required 
                                           class="p-1 border border-gray-300 rounded-md text-sm focus:ring-indigo-500 focus:border-indigo-500 w-32">
                                    <button type="submit" class="bg-blue-500 text-white px-3 py-1 text-xs rounded-lg shadow hover:bg-blue-600 transition duration-300">
                                        Confirm
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } else if (request.getAttribute("searched") != null && flights == null) { %>
            <p class="mt-4 text-lg text-gray-600">No flights found matching your criteria.</p>
        <% } %>
    </div>
</body>
</html>