import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Declaring a WebServlet called ItemServlet, which maps to url "/items"
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/shopping-cart")

public class ShoppingCartServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(); // Get a instance of current session on the request
        HashMap<String, Integer> previousItems = (HashMap<String, Integer>) session.getAttribute("previousItems"); // Retrieve data named "previousItems" from session

        // If "previousItems" is not found on session, means this is a new user, thus we create a new previousItems ArrayList for the user
        if (previousItems == null) {
            previousItems = new HashMap<>();
            session.setAttribute("previousItems", previousItems); // Add the newly created ArrayList to session, so that it could be retrieved next time
        }

        String item = request.getParameter("item"); // Get parameter that sent by GET request url
        String method = request.getParameter("method");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        // In order to prevent multiple clients, requests from altering previousItems ArrayList at the same time, we lock the ArrayList while updating
        synchronized (previousItems) {
            if (item != null) {
                if (method.equals("add")) {
                    if (previousItems.containsKey(item))
                        previousItems.put(item, previousItems.get(item) + 1);
                    else previousItems.put(item, 1); // Add the new item to the previousItems ArrayList
                } else if (method.equals("delete")) {
                    previousItems.remove(item);
                } else if (method.equals("amount")) {
                    String amountStr = request.getParameter("amount");
                    int amount = Integer.parseInt(amountStr);
                    previousItems.put(item, amount);
                }
            }
            // boolean a = method.equals("delete");
            for (String i : previousItems.keySet()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", i);
                jsonObject.addProperty("amount", previousItems.get(i));
                jsonArray.add(jsonObject);
            }
        }
        out.write(jsonArray.toString());
    }
}
