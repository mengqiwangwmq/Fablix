function handleResult(resultData) {
    $('#movie_table_body').empty();
    for (let i of resultData) {
        //alert();
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/single-movie?id="+i,
            success: (resultData) => handleSingleMovieResult(resultData,1)
        })
    }
    pagination();
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/shopping-cart",
    success: (resultData) => handleResult(resultData)
});
