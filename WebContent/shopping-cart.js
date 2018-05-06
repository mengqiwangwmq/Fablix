function handleShoppingCartResult(resultData) {
    $('#movie_table_body').empty();
    for (let i of resultData) {
        let id=i["id"];
        let amount=i["amount"];
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/single-movie?id="+i["id"],
            success: (resultData) => handleSingleMovieResult(resultData,1),
            async:false
        });
        $('#input_'+id).attr("value",amount);
    }
    if(resultData.length==0) $('#continue').attr("href","");
    pagination();
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/shopping-cart",
    success: (resultData) => handleShoppingCartResult(resultData)
});

function changeAmount(id){
    $.get("api/shopping-cart?item="+id+"&method=amount&amount="+$('#input_'+id).val());
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/shopping-cart",
        success: (resultData) => handleShoppingCartResult(resultData)
    });
}