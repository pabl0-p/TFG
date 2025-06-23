<%-- 
    Document   : menu
    Created on : 14-mar-2022, 22:41:19
    Author     : Victor Ojeda and Pablo Pascual
--%>

<%@page import="java.io.File"%>

<%
    if(request.getSession().getAttribute("role")==null && request.getSession().getAttribute("username")==null){
        String role = "guest";
        request.getSession().setAttribute("role", role);
        String username = "Guest";
        request.getSession().setAttribute("username", username);
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JLINK Menu</title>
        <link rel="stylesheet" type="text/css" href=<%= jlink.Const.AppConst.CSS_SYLE_PATH %> />
    </head>
    <body>
        <div class="header">
            <div class="header_left">
                <h1>JLINK APPLICATION | Menu</h1>
            </div>
            <div class="header_center">
                <a href ="login.jsp" ><button class="small-button logout">Log Out</button></a>
            </div>
            <div class="header_right">
                <img class="header_image" src=<%= jlink.Const.AppConst.HEADER_LOGO_PATH %> />
            </div>
        </div>
        <div class="main_container">
            <div class="container">
                <a href ="uploader.jsp" ><button class="button upload">Upload a JLINK file</button></a>
                <a href ="creator.jsp" ><button class="button creator">Create a JLINK file</button></a>
                <a href ="viewerList.jsp" ><button class="button viewer">View a JLINK file</button></a>
                <a href ="modifierList.jsp" ><button class="button modifier">Modify a JLINK file</button></a>
                <a href ="downloadList.jsp" ><button class="button downloader">Download a JLINK file</button></a>
                <a href ="deleteList.jsp" ><button class="button deleter">Delete a JLINK file</button></a>
                <div class="menu-text">
                    <h1>JPEG Linked Media Format (JLINK) Application</h1>
                    <h2>It is based on various Degree Thesis</br>
                        submitted to the Faculty of the</br>
                        Barcelona School of </br>
                        Telecommunications Engineering</br>
                        Polytechnic University of Catalunya</br>
                    </h2>
                    <h2>In partial fulfilment</br>
                        of the requirements for the degree in</br>
                        TELECOMMUNICATIONS TECHNOLOGIES AND</br>
                        SERVICES ENGINEERING
                    </h2>
                </div>
            </div>
        </div>     
                <%
    File f;
    String appPath;
    
    try{
        appPath = request.getServletContext().getRealPath("");
        f = new File(appPath + File.separator + jlink.Const.AppConst.VIEW_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
        f = new File(appPath + File.separator + jlink.Const.AppConst.CREATOR_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }
        f = new File(appPath + File.separator + jlink.Const.AppConst.MODIFIER_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        for(File file: f.listFiles()){
            if (!file.isDirectory()){
                file.delete();
            }
        }

        f = new File(appPath + File.separator + jlink.Const.AppConst.UPLOADER_DIR);
        if (!f.exists()) {
            f.mkdir();
        }

        f = new File(appPath + File.separator + jlink.Const.AppConst.SAVE_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
        
    }catch (Exception e){
        RequestDispatcher rd;

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);
        out.println("</html>");
    }
    
%>
    </body>
</html>
