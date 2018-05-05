function handleResult(resultData) {
    $('#movie_table_body').empty();
    //alert(resultData);
    for (let i of resultData) {
        let id=i["id"];
        let amount=i["amount"];
        //alert(amount);
        jQuery.ajax({
            dataType: "json",
            method: "GET",
            url: "api/single-movie?id="+i["id"],
            success: (resultData) => handleSingleMovieResult(resultData,1),
            async:false
        });
        $('#input_'+id).attr("value",amount);
    }
    pagination();
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/shopping-cart",
    success: (resultData) => handleResult(resultData)
});

function changeAmount(id){
    $.get("api/shopping-cart?item="+id+"&method=amount&amount="+$('#input_'+id).val());
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/shopping-cart",
        success: (resultData) => handleResult(resultData)
    });
}