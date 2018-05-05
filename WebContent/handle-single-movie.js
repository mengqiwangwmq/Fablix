function handleSingleMovieResult(resultData, addDelete) {
    console.log("handleMovieResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    //movieTableBodyElement.empty();

    // Iterate through resultData, no more than 10 entries
    let title = resultData["title"];
    $('#title').html(title);
    let id = resultData["id"];
    let genre = "";
    for (let j of resultData["genre"]) {
        let param = getParam();
        param.set("genre", j);
        param.set("page", "1");
        param.set("browse", "Genre");
        genre += "<a href='" + urlWithoutParam() + paramToUrl(param) + "'>" + j + "</a> ";
    }
    let star = "";
    for (let j in resultData["star"]) {
        let id = resultData["starId"][j];
        let name = resultData["star"][j];
        star += "<a href='single-star.html?id=" + id + "'>" + name + "</a> ";
    }
    let shopping = "";
    if (addDelete == 0)
        shopping = "<a href='javascript:addToCart(\"" + id + "\")'>ADD TO CART</a>";
    else
        shopping = "<form id=form_" + id + " onsubmit='changeAmount(\""+id+"\");return false;'>" +
            "<lable>Amount:</lable>" +
            "<input id=input_" + id + " value=0 />" +
            "</form>" +
            "<a href='javascript:deleteFromCart(\"" + id + "\")'>DELETE FROM CART" + "</a>\n";


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
        "              <td class=\"sub_text\">" + title + "</td>\n" +
        "            </tr>\n";
    rowHTML +=
        "            <tr>\n" +
        "              <td height=\"10\"></td>\n" +
        "            </tr>\n";
    rowHTML +=
        "            <tr>\n" +
        "              <td class=\"ser_text\">\n" +
        "                <p>Year: " + resultData["year"] + "</p>\n" +
        "                <p>Director: " + resultData["director"] + "</p>\n" +
        "                <p>Rating: " + resultData["rating"] + "</p>\n" +
        "                <p>Genre: " + genre + "</p>\n" +
        "                <p>Star: " + star + "</p>\n" +
        "                <p>" + shopping + "</p>\n" +
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
        "</tr>" +
        "<tr>\n" +
        "  <td height=\"15\"></td>\n" +
        "</tr>\n";

    // Append the row created to the table body, which will refresh the page
    movieTableBodyElement.append(rowHTML);

}

function addToCart(id) {
    $.get("api/shopping-cart?item=" + id.toString() + "&method=add");
}

function deleteFromCart(id) {
    $.get("api/shopping-cart?item=" + id.toString() + "&method=delete");
    location.reload();
}
