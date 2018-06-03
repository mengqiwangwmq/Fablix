let pastQuery = [];
let pastSuggestions = [];

$("#movie_fulltext_search").submit((formSubmitEvent) => {
    console.log("submit fulltext search");
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    let param = new Map();
    param.set("browse", "Fulltext Search");
    param.set("num_per_page", 20);
    param.set("sort_by", "Rating");
    param.set("search", $("#full_search_bar_input").val());
    window.location.href = "movies.html" + paramToUrl(param);
});


function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")
    //console.log("sending AJAX request to backend Java Servlet")

    if (query.length < 3) return;
    let pastIndex = pastQuery.indexOf(query);
    if (pastIndex >= 0) {
        console.log("lookup cache successful");
        let suggestions=pastSuggestions[pastIndex];
        console.log(suggestions)
        doneCallback(suggestions);
    }
    else
        jQuery.ajax({
            "method": "GET",
            "url": "api/movies-fulltext-search?num_per_page=10&search=" + encodeURI(query),
            "success": (data) => handleLookupAjaxSuccess(data, query, doneCallback),
            "error": (errorData) => {
                console.log("lookup ajax error");
                console.log(errorData);
            }
        });
}


function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful");
    data = data.content;
    let titles = [];
    for (let i of data) {
        titles.push({value: i.title,data:{category:'movie'}});
    }
    let suggestions = {suggestions: titles};
    console.log(suggestions);
    pastQuery.push(query);
    pastSuggestions.push(suggestions);

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback(suggestions);
}

function handleSelectSuggestion(suggestion) {
    let select = suggestion["value"];
    console.log("you select " + select);
    window.location.href = "single-movie.html?title=" + select;
}

$('#full_search_bar_input').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: (query, doneCallback) => handleLookup(query, doneCallback),
    onSelect: (suggestion) => handleSelectSuggestion(suggestion),
    // set the groupby name in the response json data field
    //groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});
