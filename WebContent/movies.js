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
function handleMovieResult(resultData,page,num_per_page,sort_by) {
    console.log("handleMovieResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    //page=resultData["page"];
    let num_page=parseInt(resultData["num_page"]);
    page=parseInt(page);
    num_per_page=parseInt(num_per_page);
    resultData=resultData["content"];

    movieTableBodyElement.empty();

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
    pagination(page,num_per_page,num_page);
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
function getPage(page,num_per_page,sort_by) {
// Makes the HTTP GET request and registers on success callback function handleMovieResult
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movies?page="+page+"&num_per_page="+num_per_page+"&sort_by="+sort_by, // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData)=>handleMovieResult(resultData,page,num_per_page,sort_by) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

function update(){
    let page=$('#gotoPage').find('option:selected').val();
    let num_per_page=$('#num_per_page').val();
    let sort_by=$('#sort_by').find('option:selected').val();
    getPage(page,num_per_page,sort_by);
}
getPage(1,20);


