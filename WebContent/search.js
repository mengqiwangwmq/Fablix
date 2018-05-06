/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    let starTableBodyElement = jQuery("#SearchResult_table_body");
    let rowHTML = "";
    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(6, resultDataString.length); i++) {

        // Concatenate the html tags with resultData jsonObject

        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to SingleMovie.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultDataString[i]['id'] + '">'
            + resultDataString[i]["title"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultDataString[i]["year"] + "</th>";
        rowHTML += "<th>" + resultDataString[i]["director"] + "</th>";
        rowHTML += "<th>" + resultDataString[i]["stars"] + "</th>";
        rowHTML += "<th>" + resultDataString[i]["genres"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
    }
    starTableBodyElement.html(rowHTML);



}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */

function submitLoginForm() {
    console.log("submit login form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    //alert();
    jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "form?name=" + $('#search').val(),
            success: (resultDataString) => handleLoginResult(resultDataString)
        }
    );

}