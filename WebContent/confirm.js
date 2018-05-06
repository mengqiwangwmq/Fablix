function handleTransactionResult(resultData){
    for(let i of resultData){
        let movieId=i["movieId"];
        $.ajax({
            dataType:"json",
            method:"GET",
            url:"api/single-movie?id="+movieId,
            success:(resultData)=>handleSingleMovieResult(resultData,-1),
            async:false
        });
        let saleId=i["saleId"];
        $('#id_'+movieId).html(saleId.toString());
        $('#amount_'+movieId).html(saleId.length);
    }
}

$.ajax({
    dataType:"json",
    method:"GET",
    url:"api/get-transaction"+paramToUrl(getParam()),
    success: handleTransactionResult
});