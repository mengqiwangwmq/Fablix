$('#body').append("<div id='div'></div>");
$('#div').load("_entrypoint.html",function(){jQuery("#login_form").submit((event) => submitLoginForm(event))});

/**
 * Handle the data returned by LoginServlet
 * @param resultData jsonObject
 */
function handleLoginResult(resultData) {

    console.log("handle login response");
    console.log(resultData);
    console.log(resultData["status"]);

    // If login success, redirect to index.html page
    if (resultData["status"] === "success") {
        window.location.replace("/employee/_dashboard.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {

        console.log("show error message");
        console.log(resultData["message"]);
        jQuery("#login_error_message").text(resultData["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");

    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    $.post(
        "/fabflix/login",
        // Serialize the login form to the data sent by POST request
        jQuery("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));

}

// Bind the submit action of the form to a handler function
//jQuery("#login_form").submit((event) => submitLoginForm(event));