function handleTransactionResult(resultData) {
    window.location.replace("confirm.html?id=" + resultData["id"]);
}

function handleCustomInformationResult(resultData) {
    if (resultData["success"] === "true")
        $.ajax({
            dataType: "json",
            method: "POST",
            url: "api/pay",
            success: handleTransactionResult
        });
    else {
        $('#error_info').html("PAYMENT FAILED!")
    }
}

function confirm() {
    let param = new Map();
    let header = ["first_name", "last_name", "expire_date", "credit_card"];
    for (i of header)
        param.set(i, $('#' + i).val());
    $.ajax({
        daraType: "json",
        method: "POST",
        url: "api/creditcard-confirm" + paramToUrl(param),
        success: handleCustomInformationResult
    });
}
