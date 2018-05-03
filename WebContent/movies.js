/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
    console.log("handleMovieResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(10, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML =
            "<tr>\n" +
            "  <td>\n" +
            "    <!--图-->\n" +
            "    <img src=\"images/7.jpg\" alt=\" \" width=\"250\" height=\"350\" style=\"float:left\">\n" +
            "    <table width=\"20\" height=\"20\"style=\"float:left\"></table>\n" +
            "    <!-- 情報 information -->\n" +
            "    <table class=\"listInformation\">\n" +
            "      <tbody>\n" +
            "      <tr>\n" +
            "        <td>\n" +
            "          <table class=\"ser_left_one\">\n" +
            "            <tbody>\n";
        rowHTML +=
            "            <tr>\n" +
            "              <td class=\"sub_text\">" +
            "                <a>" + resultData[i]["title"] + "</a>" +
            "              </td>\n" +
            "            </tr>\n";
        rowHTML +=
            "            <tr>\n" +
            "              <td height=\"10\"></td>\n" +
            "            </tr>\n";
        rowHTML +=
            "            <tr>\n" +
            "              <td class=\"ser_text\">\n" +
            "                <p>Year: " + resultData[i]["year"] + "</p>\n" +
            "                <p>Director: " + resultData[i]["director"] + "</p>\n" +
            "                <p>Rating: " + resultData[i]["rating"] + "</p>\n" +
            "                <p>Genre: " + resultData[i]["genre"] + "</p>\n" +
            "                <p>Star: " + resultData[i]["star"] + "</p>\n" +
            "              </td>\n" +
            "            </tr>\n";
        rowHTML +=
            "            </tbody>\n" +
            "          </table>\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "      </tbody>\n" +
            "    </table>\n" +
            "  </td>\n" +
            "</tr>"+
            "<tr>\n" +
            "  <td height=\"15\"></td>\n" +
            "</tr>\n";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: handleMovieResult // Setting callback function to handle data returned successfully by the StarsServlet
});
