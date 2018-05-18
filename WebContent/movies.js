/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */
//import getParam from "./param.js";
//import pagination from "./pagination.js";

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */


function handleMovieResult(resultData, page, num_per_page, sort_by) {
    console.log("handleMovieResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    //page=resultData["page"];
    let num_page = parseInt(resultData["num_page"]);
    page = parseInt(page);
    num_per_page = parseInt(num_per_page);
    resultData = resultData["content"];

    movieTableBodyElement.empty();

    // Iterate through resultData, no more than 10 entries
    for (let i of resultData) {
        let title = "<a href='single-movie.html?id=" + i["id"] + "'>" + i["title"] + "<a>";
        let genre = "";
        for (let j of i["genre"]) {
            let param = getParam();
            param.set("genre", j);
            param.set("page", "1");
            param.set("browse", "Genre");
            genre += "<a href='" + urlWithoutParam() + paramToUrl(param) + "'>" + j + "</a> ";
        }
        let star = "";
        for (let j in i["star"]) {
            let id = i["starId"][j];
            let name = i["star"][j];
            star += "<a href='single-star.html?id=" + id + "'>" + name + "</a> ";
        }
        handleSingleMovieResult(i, 0);

    }
    for (let i of resultData) {
        let id = i["id"];
        $("#title_" + id).html("<a href='single-movie.html?id=" + id + "'>" + $("#title_" + id).html() + "<a>");
    }
    pagination(page, num_per_page, num_page);
}

function handleGenreResult(resultData) {
    let categoriedElement = $('#categories');
    for (let i of resultData) {
        let rowHtml = "";
        let param = getParam();
        param.set("genre", i["name"]);
        param.set("browse", "Genre");
        param.set("page", 1);
        rowHtml += "<a href='" + urlWithoutParam() + paramToUrl(param) + "'>" + i["name"] + "</a> ";
        categoriedElement.append(rowHtml);
    }
}


let param = getParam();
let page = 1;
let num_per_page = 20;
let sort_by = "rating";
let browse = "All";

if (param != null) {
    let tmp = param.get("page");
    if (tmp != null)
        page = tmp;
    tmp = param.get("num_per_page");
    if (tmp != null)
        num_per_page = tmp;
    tmp = param.get("sort_by");
    if (tmp != null)
        sort_by = tmp;
    tmp = param.get("browse");
    if (tmp != null)
        browse = tmp;
}
$("#num_per_page").attr("value", num_per_page);
$('#sort_by').find("option:contains(" + sort_by + ")").attr("selected", "selected");
$('#browse').find("option:contains(" + browse + ")").attr("selected", "selected");

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Makes the HTTP GET request and registers on success callback function handleMovieResult
if (browse === "All") {

    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movies?page=" + page + "&num_per_page=" + num_per_page + "&sort_by=" + sort_by, // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleMovieResult(resultData, page, num_per_page, sort_by) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}
else if (browse === "Genre") {
    let val = param.get("genre");
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/genres?page=" + page + "&num_per_page=" + num_per_page,
        success: (resultData) => handleGenreResult(resultData, page, num_per_page, sort_by)
    });
    if (val != null) {
        $('#SubTitle').html(val.toUpperCase() + " MOVIES");
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/movies-genre?page=" + page + "&num_per_page=" + num_per_page + "&sort_by=" + sort_by + "&genre=" + val,
            success: (resultData) => handleMovieResult(resultData, page, num_per_page, sort_by)
        });
    } else
        $('#SubTitle').html("GENRES");
} else if (browse === "Alphabet") {
    let val = param.get("alphabet");
    $('#SubTitle').html('ALPHABETS');
    for (let i = 0; i < 25; i++) {
        let tmp = String.fromCharCode((65 + i));
        let param = getParam();
        param.set("alphabet", tmp);
        param.set("page", 1);
        let href = "<a href='" + urlWithoutParam() + paramToUrl(param) + "'>";
        href += tmp + "</a> "
        $('#categories').append(href);
    }
    if (val != null) {
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/movies-alphabet?page=" + page + "&num_per_page=" + num_per_page + "&sort_by=" + sort_by + "&alphabet=" + val,
            success: (resultData) => handleMovieResult(resultData, page, num_per_page, sort_by)
        })
    }
} else if (browse === "Search") {
    let val = param.get("search");
    $('#SubTitle').html('SEARCH MOVIES')
    $('#categories').load("searchbar.html");
    if (val != null) {
        $.ajax({
            dataType: "json",
            method: "GET",
            url: "api/movies-search?page=" + page + "&num_per_page=" + num_per_page + "&sort_by=" + sort_by + "&search=" + val,
            success: (resultData) => handleMovieResult(resultData, page, num_per_page)
        });
    }

}


function update() {
    let param = getParam();

    let page = $('#gotoPage').find('option:selected').val();
    if (page != null)
        param.set("page", page);

    let num_per_page = $('#num_per_page').val();
    if (num_per_page != null && num_per_page > 0)
        param.set("num_per_page", num_per_page);

    let sort_by = $('#sort_by').find('option:selected').val();
    if (sort_by != null)
        param.set("sort_by", sort_by);
    window.location.href = urlWithoutParam() + paramToUrl(param);
}

function updateBrowse() {
    let param = new Map();
    param.set("browse", $('#browse').find('option:selected').val());
    let num_per_page = $('#num_per_page').val();
    param.set("num_per_page", num_per_page);
    let sort_by = $('#sort_by').find('option:selected').val();
    param.set("sort_by", sort_by);
    window.location.href = urlWithoutParam() + paramToUrl(param);
}

function search(){
    let param=new Map();
    param.set("browse", $('#browse').find('option:selected').val());
    let num_per_page = $('#num_per_page').val();
    param.set("num_per_page", num_per_page);
    let sort_by = $('#sort_by').find('option:selected').val();
    param.set("sort_by", sort_by);
    let search=$("#search_bar_input").val();
    param.set("search",search);
    window.location.href = urlWithoutParam() + paramToUrl(param);
}