<%-- 
    Document   : login
    Author     : Pablo Pascual
--%>
<%
    request.getSession().removeAttribute("role");
    request.getSession().removeAttribute("username");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK&Privsec Login</title>
        <link rel="stylesheet" type="text/css" href="<%= jlink.Const.AppConst.CSS_SYLE_PATH %>" />
    </head>
    <body class="login-page">
        <div class="login-box">
            <div class="title">
                <h1> Log In </h1>
            </div>
            <form method="POST" action="CheckUser">
                <div class="text-box">
                    <input type="text" name="username" placeholder="Username" required>
                </div>
                <div class="text-box">
                <input type="password" name="password" placeholder="Password" required>
                </div>
                <button type="submit" class="small-button send">Sign In</button>
            </form>
            <div class = "buttons">
                <a href="register.jsp"><button class="small-button">Register</button></a>
                <a href="menu.jsp"><button class="small-button">Guest</button></a>
            </div>
        </div>

    </body>
</html>
