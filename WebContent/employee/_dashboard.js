function submitAddStarForm(formSubmitEvent) {
    console.log("submit add_star_form");
    formSubmitEvent.preventDefault();
    $.get(
        "/employee/api/add-star",
        $('#add_star_form').serialize(),
        (resultData) => alert(resultData["message"]));
}

function submitGetMetadata(formSubmitEvent) {
    console.log("submit get_metadata");
    formSubmitEvent.preventDefault();
    $.get(
        "/employee/api/get-metadata",
        $('#get_metadata').serialize(),
        (resultDataString) => $('#metadata').html(resultDataString["message"])
    )
}

function submitAddMovieForm(formSubmitEvent){
    console.log("submit add_movie");
    formSubmitEvent.preventDefault();
    $.get(
        "/employee/api/add-movie",
        $('#add_movie_form').serialize(),
        (resultDataString)=>alert(resultDataString["message"])
    )
}

function submitAddFeatureForm(formSubmitEvent){
    console.log("submit add_feature");
    formSubmitEvent.preventDefault();
    $.get(
        "/employee/api/add-feature",
        $('#add_feature_form').serialize(),
        (resultDataString)=>alert(resultDataString["message"])
    )
}

$('#add_star_form').submit((event) => submitAddStarForm(event));
$('#get_metadata').submit((event) => submitGetMetadata(event));
$('#add_movie_form').submit((event)=>submitAddMovieForm(event));
$('#add_feature_form').submit((event)=>submitAddFeatureForm(event));