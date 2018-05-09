function logout(){
    $.post("api/logout");
    window.location.replace("/login.html");
}