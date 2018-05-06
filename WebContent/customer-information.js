function handleCustomInformationResult(resultData){
    if (resultData["success"]==="true")
        window.location.replace("confirm.html");
}

function confirm() {
    let param = new Map();
    let header = ["first_name", "last_name", "expire_date","credit_card"];
    for(i of header)
        param.set(i, $('#'+i).val());
    $.ajax({
        daraType:"json",
        method:"POST",
        url:""+paramToUrl(param),
        success:handleCustomInformationResult
    });
}
