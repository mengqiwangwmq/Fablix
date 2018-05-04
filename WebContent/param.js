function getParam() {
    let url = window.location.href;
    let param = url.split("?")[1];
    if (param == null) return new Map();
    param = param.split("&");
    let mapParam = new Map();
    for (let i = 0; i < param.length; ++i) {
        param[i] = param[i].split("=");
        mapParam.set(param[i][0], param[i][1]);
    }
    return mapParam;
}

function paramToUrl(param) {
    let tmp = "?";
    param.forEach((item, key, mapObj) => {
        tmp += key + "=" + item + "&";
    });
    return tmp.slice(0, -1);
}

function urlWithoutParam(){
    return window.location.href.split("?")[0];
}

//export default getParam
//export {getParam, paramToUrl, urlWithoutParam}
