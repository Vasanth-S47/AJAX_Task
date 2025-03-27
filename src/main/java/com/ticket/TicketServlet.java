package com.ticket;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public class TicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static int availableTickets = 20;
    private static final int ticketPrice = 100;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userTickets = (Integer) session.getAttribute("userTickets");
        if (userTickets == null) userTickets = 0;

        int totalPrice = userTickets * ticketPrice;

        JSONObject json = new JSONObject();
        json.put("available", availableTickets);
        json.put("userTickets", userTickets);
        json.put("totalPrice", totalPrice);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userTickets = (Integer) session.getAttribute("userTickets");
        if (userTickets == null) userTickets = 0;

        String action = request.getParameter("action");
        JSONObject json = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int qty = Integer.parseInt(request.getParameter("qty"));

            if ("bookTickets".equals(action)) {
                if (qty <= 0) {
                    json.put("status", "error");
                    json.put("message", "Enter a valid ticket quantity!");
                } else if (qty > availableTickets) {
                    json.put("status", "error");
                    json.put("message", "Not enough tickets available!");
                } else {
                    availableTickets -= qty;
                    userTickets += qty;
                    session.setAttribute("userTickets", userTickets);
                    json.put("status", "success");
                    json.put("message", "Booked " + qty + " tickets!");
                }

            } else if ("cancelTickets".equals(action)) {
                if (qty <= 0) {
                    json.put("status", "error");
                    json.put("message", "Invalid ticket quantity for cancellation!");
                } else if (qty > userTickets) {
                    json.put("status", "error");
                    json.put("message", "You don't have enough tickets to cancel!");
                } else {
                    availableTickets += qty;
                    userTickets -= qty;
                    session.setAttribute("userTickets", userTickets);
                    json.put("status", "success");
                    json.put("message", "Canceled " + qty + " tickets.");
                }
            }
        } catch (NumberFormatException e) {
            json.put("status", "error");
            json.put("message", "Invalid input!");
        }

        response.getWriter().write(json.toString());
    }
}
