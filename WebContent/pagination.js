//import {getParam,paramToUrl,urlWithoutParam} from "./param.js";

function curPage(page) {
    return "<strong>" + page.toString() + "</strong> ";
}

function defaultPage(page, num_per_page) {
    return stringPage(page.toString(), page, num_per_page);
}

function stringPage(string, page, num_per_page) {
    let param=getParam();
    param.set("page",page);
    param.set("num_per_page",num_per_page);
    //alert(paramToUrl(param));
    return "<a href='" +urlWithoutParam()+paramToUrl(param)+"'>" +
        string +
        "</a> ";
}

function pagination(page, num_per_page, num_page) {
    let paginationElement = $("#pagination");
    paginationElement.empty();
    let gotoElement = $("#gotoPage");
    gotoElement.empty();
    let rowHtml = "";
    let start = page - 4 <= 1 ? 1 : (page - 4 <= num_page - 9 ? page - 4 : num_page - 9);
    let end = page + 4 <= 9 ? 9 : (page + 4 <= num_page ? page + 4 : num_page);
    let firstMostHtml = page - 5 >= 1 ? stringPage("First", 1, num_per_page) : "";
    let lastMostHtml = page + 5 <= num_page ? stringPage("Last", num_page, num_per_page) : "";

    if (page > 1)
        rowHtml += stringPage("<", page - 1, num_per_page);
    /*if (num_page <= 5)*/
    for (let i = start; i <= end; ++i) {
        if (i == page)
            rowHtml += curPage(i);
        else
            rowHtml += defaultPage(i, num_per_page);
    }
    for (let i = 1; i < num_page; ++i) {
        let iStr = i==null?"1":i.toString();
        if (i == page)
            gotoElement.append("<option value='" + iStr + "' selected='selected'>" + iStr + "</option>\n");
        else
            gotoElement.append("<option value='" + iStr + "'>" + iStr + "</option>\n");
    }
    if (page < num_page)
        rowHtml += stringPage(">", page + 1, num_per_page);
    paginationElement.append(firstMostHtml);
    paginationElement.append(rowHtml);
    paginationElement.append(lastMostHtml);

}


//export default pagination