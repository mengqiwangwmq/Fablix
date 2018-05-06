function handleTransactionResult(resultData){
    for(let i of resultData){
        let movieId=i["movieId"];
        $.ajax({
            dataType:"json",
            method:"GET",
            url:"api/single-movie?id="+movieId,
            success:handleSingleMovieResult,
            async:false
        });
        let saleId=i["saleId"];
        $('#id_'+movieId).html(saleId.toString());
    }
}

$.ajax({
    dataType:"json",
    method:"GET",
    url:"api/get-transaction"+getParam(),
    success: handleTransactionResult
});