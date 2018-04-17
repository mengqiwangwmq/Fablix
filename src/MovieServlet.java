import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this annotation maps this Java Servlet Class to a URL
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MovieServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // change this to your own mysql username and password
        String loginUser = "root";
        String loginPasswd = "2018_Satomi";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        // set response mime type
        response.setContentType("text/html");

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>"+"Movies"+"</title>\n" +
                "    <!-- Custom Theme files -->\n" +
                "    <link href=\"home.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                "    <!-- //Custom Theme files -->\n" +
                "    <!-- Responsive Styles and Valid Styles -->\n" +
                "\n" +
                "</head>\n");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            // create database connection
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // declare statement
            Statement statement = connection.createStatement();
            // prepare query
            String query = "SELECT m.id AS Id, Title, Year, Director, Rating " +
                    "FROM movies AS m, ratings AS r " +
                    "WHERE m.id=r.movieId " +
                    "ORDER BY rating DESC " +
                    "LIMIT 10";
            // execute query
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<body>\n" +
                    "<div width=\"100%\" align=\"center\" valign=\"top\">\n" +
                    "    <table width=\"800\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"\n" +
                    "           class=\"container top-header-left\">\n" +
                    "        <tbody>\n" +
                    "        <!--top margin 顶部空格-->\n" +
                    "        <tr>\n" +
                    "            <td height=\"60\"></td>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "        <!-- 头 header -->\n" +
                    "        <tr>\n" +
                    "            <td>\n" +
                    "                <table class=\"ban\">\n" +
                    "                    <!--head part-->\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            <table class=\"ban_text one mainContent\">\n" +
                    "                                <!--top margin 顶部空格-->\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"top-text\" height=\"40\"></td>\n" +
                    "                                </tr>\n" +
                    "                                <!--big title-->\n" +
                    "                                <tr align=\"center\">\n" +
                    "                                    <td><a class=\"log\" href=\"index.html\">\n" +
                    "                                        A Website\n" +
                    "                                        <img src=\"images/logo.png\"\n" +
                    "                                             style=\"display:inline-block;vertical-align:top;width:40px;height:auto;\">\n" +
                    "                                    </a></td>\n" +
                    "                                </tr>\n" +
                    "                                <!--间隔-->\n" +
                    "                                <tr>\n" +
                    "                                    <td height=\"15\"></td>\n" +
                    "                                </tr>\n" +
                    "                                <!--description-->\n" +
                    "                                <tr align=\"center\">\n" +
                    "                                    <td class=\"line\"><span\n" +
                    "                                            style=\"color:#fff;font-family: Tahoma;font-size:1.6em;font-weight:bold;line-height:1.5em;text-align:center;text-decoration:none;\">In some degree a movie website...</span>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <!--间隔-->\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"l-bottom\"></td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "        <!-- 内容 main -->\n" +
                    "        <tr bgcolor=\"#ffffff\">\n" +
                    "            <td>\n" +
                    "                <table class=\"container-middle\">\n" +
                    "                    <tbody>\n" +
                    "                    <!--顶部空格 top margin-->\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"ser_pad\"></td>\n" +
                    "                    </tr>\n" +
                    "                    <!-- 标题 title -->\n" +
                    "                    <tr>\n" +
                    "                        <td id=\"SubTitle\" class=\"wel_text\">MOVIE</td>\n" +
                    "                    </tr>\n" +
                    "                    <!--分割线图-->\n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\">\n" +
                    "                            <img src=\"images/sub-log.png\" alt=\" \" width=\"160\" height=\"auto\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <!--空行-->\n" +
                    "                    <tr>\n" +
                    "                        <td height=\"50\"></td>\n" +
                    "                    </tr>\n" +
                    "                    <!--表格-->\n");

            StringBuilder insert=new StringBuilder();

            String header[] = {"Title", "Year", "Director", "Rating", "Genre", "Star"};

            // add a row for every movie result
            ArrayList<Map> movie = new ArrayList<>();
            while (resultSet.next()) {
                Map tmpMap = new HashMap<String, String>();
                tmpMap.put("Id", resultSet.getString("Id"));
                for (int i = 0; i < 4; ++i)
                    tmpMap.put(header[i], resultSet.getString(header[i]));
                movie.add(tmpMap);
            }
            for(Map i:movie){
                String movieId= (String) i.get("Id");
                query = "SELECT g.name AS genre " +
                        "FROM genres_in_movies AS gm, genres AS g " +
                        "WHERE gm.genreId=g.id AND gm.movieId=\"" + movieId + "\"";
                ResultSet genreResult = statement.executeQuery(query);
                StringBuilder genre = new StringBuilder();
                while (genreResult.next())
                    genre.append(genreResult.getString("genre")).append("<br/>");
                i.put("Genre",genre.toString());
                query = "SELECT s.name AS star " +
                        "FROM stars_in_movies AS sm, stars AS s " +
                        "WHERE sm.starId=s.id AND sm.movieId=\"" + movieId + "\"";
                ResultSet starResult = statement.executeQuery(query);
                StringBuilder star = new StringBuilder();
                while (starResult.next())
                    star.append(starResult.getString("star")).append("<br/>");
                i.put("Star",star.toString());

                // get a movies from result set
                out.println("<tr>\n" +
                        "                        <td>\n" +
                        "                            <!--图-->\n" +
                        "                            <img src=\"images/7.jpg\" alt=\" \" width=\"250\" height=\"350\" style=\"float:left\">\n" +
                        "                            <table width=\"20\" height=\"20\"style=\"float:left\"></table>\n" +
                        "                            <!-- 情報 information -->\n" +
                        "                            <table class=\"listInformation\">\n" +
                        "                                <tbody>\n" +
                        "                                <tr>\n" +
                        "                                    <td class=\"ser_one\"></td>\n" +
                        "                                </tr>\n" +
                        "\n" +
                        "                                <tr>\n" +
                        "                                    <td>\n" +
                        "                                        <table class=\"ser_left_one\">\n" +
                        "                                            <tbody>\n" +
                        "                                            <tr>\n" +
                        "                                                <td class=\"sub_text\">");
                out.println(i.get("Title"));
                i.remove("Title");
                out.println("</td>\n" +
                        "                                            </tr>\n" +
                        "                                            <tr>\n" +
                        "                                                <td height=\"10\"></td>\n" +
                        "                                            </tr>\n" +
                        "                                            <tr>\n" +
                        "                                                <td class=\"ser_text\">");
                for(String j :header)
                    out.println("<p>" + j+": "+(String)i.get(j) + "</p>");
                out.println("</td>\n" +
                        "                                            </tr>\n" +
                        "                                            </tbody>\n" +
                        "                                        </table>\n" +
                        "                                    </td>\n" +
                        "                                </tr>\n" +
                        "                                <tr>\n" +
                        "                                    <td height=\"15\"></td>\n" +
                        "                                </tr>\n" +
                        "                                </tbody>\n" +
                        "                            </table>");
            }

            out.println("</td>\n" +
                    "                    </tr>\n" +
                    "                    <!--空行-->\n" +
                    "                    <tr>\n" +
                    "                        <td height=\"70\"></td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        <!--bottom margin 底部空格-->\n" +
                    "        <tr>\n" +
                    "            <td class=\"ser_pad\"></td>\n" +
                    "        </tr>\n" +
                    "        </tbody>\n" +
                    "    </table>\n" +
                    "    </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td height=\" 60\"></td>\n" +
                    "    </tr>\n" +
                    "    </tbody>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");


            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
            e.printStackTrace();

            out.println("<body>");
            out.println("<p>");
            out.println("Exception in doGet: " + e.toString());
            out.println("</p>");
            out.print("</body>");
        }

        out.println("</html>");
        out.close();

    }


}