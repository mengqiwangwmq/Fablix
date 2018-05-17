import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI().toLowerCase();

        System.out.println("LoginFilter: " + httpRequest.getRequestURI());

        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("employee") == null && isEmployeeURI(requestURI))
            httpResponse.sendRedirect("/fabflix/_dashboard");
        else if (session.getAttribute("user") == null && isLoginURI(requestURI))
            httpResponse.sendRedirect("/login.html");
        else {
            chain.doFilter(request, response);
            return;
        }

    }

    // Setup your own rules here to allow accessing some resources without logging in
    // Always allow your own login related requests(html, js, servlet, etc..)
    // You might also want to allow some CSS files, etc..

    private boolean isLoginURI(String requestURI) {
        if (requestURI.endsWith("generate-header.js") || requestURI.endsWith("home.css") || requestURI.endsWith("header.html"))
            return false;
        String[] urlSplit = requestURI.split("/");
        for (String i : urlSplit) {
            if (i.equals("images"))
                return false;
            if (i.equals("employee"))
                return false;
            if(i.equals("fabflix"))
                return false;
            String[] tmp = i.split("\\.");
            for (String k : tmp)
                if (k.equals("login"))
                    return false;
        }
        return true;
    }

    private boolean isEmployeeURI(String requestURI) {
        for (String i : requestURI.split("/")) {
            if (i.equals("employee"))
                return true;
        }
        return false;
    }

    /**
     * We need to have these function because this class implements Filter.
     * But we don’t need to put any code in them.
     *
     * @see Filter#init(FilterConfig)
     */

    public void init(FilterConfig fConfig) {
    }

    public void destroy() {
    }


}
