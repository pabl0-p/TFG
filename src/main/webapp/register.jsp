<%-- 
    Document   : login
    Author     : Pablo Pascual
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK&Privsec Login</title>
        <link rel="stylesheet" type="text/css" href=<%= jlink.Const.AppConst.CSS_SYLE_PATH %> />
    </head>
    <body class="login-page">
        <div class="login-box">
            <div class="title">
                <h1> Register </h1>
                <%
                    String error = request.getParameter("error");

                    if(error != null){
                        out.println("<p> This user already exists. </p>");
                    }
                %>
            </div>
            <form method="POST" action="AddUser">
                <div class="text-box">
                    <input type="text" name="username" placeholder="Username" required>
                </div>
                <div class="text-box">
                <input type="password" name="password" placeholder="Password" required>
                </div>
                <button type="submit" class="small-button send">Register</button>
            </form>
            <div class= "buttons">
                <a href="login.jsp"><button class="small-button">Log In</button></a>
                <a href="menu.jsp"><button class="small-button">Guest</button></a>
            </div>
        </div>

    </body>