<%-- 
    Document   : loader
    Created on : 02-may-2022, 11:16:47
    Author     : Victor Ojeda and Pablo Pascual
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Creator</title>
        <link rel="stylesheet" type="text/css" href=<%= jlink.Const.AppConst.CSS_SYLE_PATH %> />       
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Creator</h1>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= jlink.Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">          
            <div class="title">
                <h1>Building the JLINK file</h1>
            </div>
            <div class="container">
                <div id="loader"></div>
            </div>
        </div>
            <%
//                RequestDispatcher rd;
//                rd = request.getRequestDispatcher("/FinishResult");
//                rd.forward(request, response);
                %>
        <script>
            location.replace("/jlink/FinishResult");
        </script>
    </body>
</html>
