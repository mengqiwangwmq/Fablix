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

// Declaring a WebServlet called ItemServlet, which maps to url "/items"
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/shopping-cart")

public class ShoppingCartServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(); // Get a instance of current session on the request
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems"); // Retrieve data named "previousItems" from session

        // If "previousItems" is not found on session, means this is a new user, thus we create a new previousItems ArrayList for the user
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            session.setAttribute("previousItems", previousItems); // Add the newly created ArrayList to session, so that it could be retrieved next time
        }

        String item = request.getParameter("Item"); // Get parameter that sent by GET request url
        String method = request.getParameter("method");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

        // In order to prevent multiple clients, requests from altering previousItems ArrayList at the same time, we lock the ArrayList while updating
        synchronized (previousItems) {
            if (item != null) {
                if (method.equals("add"))
                    previousItems.add(item); // Add the new item to the previousItems ArrayList

                else if (method.equals("delete"))
                    previousItems.remove(item);
            }
            // boolean a = method.equals("delete");
            for (String i : previousItems)
                jsonArray.add(i);
        }
        out.write(jsonArray.toString());
    }
}
