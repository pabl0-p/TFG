<%-- Document : modifierList Created on : 26-abr-2022, 18:19:35 Author : Victor Ojeda and Pablo Pascual--%>

    <%@page import="java.io.File" %>
        <%@page import="java.sql.DriverManager" %>
            <%@page import="java.sql.ResultSet" %>
                <%@page import="java.sql.PreparedStatement" %>
                    <%@page import="java.sql.Connection" %>
                        <%@page contentType="text/html" pageEncoding="UTF-8" %>
                            <!DOCTYPE html>
                            <html>

                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                                <title>JLINK Modifier</title>
                                <link rel="stylesheet" type="text/css" href=<%=jlink.Const.AppConst.CSS_SYLE_PATH %> />
                            </head>

                            <body>
                                <div class="header">
                                    <div class="header_left">
                                        <h1>JLINK APPLICATION | Modifier</h1>
                                    </div>
                                    <div class="header_right">
                                        <img class="header_image" src=<%=jlink.Const.AppConst.HEADER_LOGO_PATH %> />
                                    </div>
                                </div>
                                <div class="main_container">
                                    <div class="title">
                                        <h1>List of saved JLINK files</h1>
                                    </div>
                                    <div class="container">
                                        <div class="column">
                                            <% Connection connection; PreparedStatement statement; ResultSet rs; String
                                                sql; try{
                                                sql="select * from image"; 
                                                statement= jlink.Common.AppUtilsCommon.prepare_query(sql, new String[0]);;
                                                rs=statement.executeQuery(); if(!rs.next()){ out.println("<p>There is no file saved in the database</p>");
                                                } else {
                                                out.println("<p>Search an image: </p>\n" +
                                                " <form method=\"post\" action=\"ModifierSearchList\">\n" +
                                                " <table>\n" +
                                                        " <tr>\n" +
                                                        "   <td>By file name: </td>\n" +
                                                        "   <td><input type=\"text\" name=\"file_name\" /></td>\n" +
                                                        "</tr>\n" +
                                                        " <tr>\n" +
                                                        "   <td>By Title: </td>\n" +
                                                        "   <td><input type=\"text\" name=\"title\" /></td>\n" +
                                                        "</tr>\n" +
                                                        " <tr>\n" +
                                                        "   <td>By Storage date: </td>\n" +
                                                        "   <td><input type=\"date\" name=\"storage_date\" /></td>\n" +
                                                        "</tr>\n" +
                                                        " <tr>\n" +
                                                        "   <td><input type=\"submit\" value=\"Search\" /></td>\n" +
                                                        "</tr> \n" +
                                                        " </form>\n" +
                                                    " </table></br>");
                                                out.println("<p>Click on the corresponding row to modify the file: </p>"
                                                + "<table id=\"content_table\">\n" +
                                                    " <tr class=\"table-main-row\">\n" +
                                                    "   <th>File name</th>\n" +
                                                        " <th>Title</th>\n" +
                                                        " <th>Description</th>\n" +
                                                        " <th>Storage date</th>\n" +
                                                    "</tr>\n" +
                                                    " <tr>");
                                                        String url = jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg";
                                                        if (rs.getBoolean("main_protected")){
                                                            url = "appImages/protected_content.jpeg";
                                                        }
                                                        out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" "+ 
                                                        "data-value=\"" + jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\" "
                                                        + "data-preview=\"" + url + "\" "
                                                        + "onclick=\"window.location='InitModifier?file_name=" + rs.getString("filename") + "'\">");
                                                        out.println("<td>");
                                                            out.println(rs.getString("filename")+".jpeg</td>");
                                                        out.println("<td>" + rs.getString("title")+"</td>");
                                                        out.println("<td>" + rs.getString("description")+"</td>");
                                                        out.println("<td>" + rs.getString("storage_date")+"</td>");
                                                        out.println("</tr>");
                                                    while (rs.next()) {
                                                        url = jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg";
                                                        if (rs.getBoolean("main_protected")){
                                                            url = "appImages/protected_content.jpeg";
                                                        }
                                                        out.println("<tr onmouseover=\"showImg(this)\" " +
                                                            "onmouseout=\"hideImg(this)\" " +
                                                            "data-value=\"" + jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\" " +
                                                            "data-preview=\"" + url + "\" " +
                                                            "onclick=\"window.location='InitModifier?file_name=" + rs.getString("filename") + "'\">");
                                                        out.println("<td>");
                                                            out.println(rs.getString("filename")+".jpeg</td>");
                                                        out.println("<td>" + rs.getString("title")+"</td>");
                                                        out.println("<td>" + rs.getString("description")+"</td>");
                                                        out.println("<td>" + rs.getString("storage_date")+"</td>");
                                                        out.println("</tr>");
                                                    }
                                                    }
                                                    }catch (Exception e){
                                                        RequestDispatcher rd;
                                                        e.printStackTrace();
                                                        out.println("<!DOCTYPE html>");
                                                        out.println("<html>");
                                                        rd = request.getRequestDispatcher("/error.jsp");
                                                        rd.forward(request, response);
                                                        out.println("</html>");
                                                    }
                                                    %>
                                                </table></br>
                                                <a href="menu.jsp"><button class="button-link">Return to
                                                        menu</button></a>
                                        </div>
                                        <div class="column">
                                            <img class="image" id="image" src="">
                                        </div>
                                    </div>
                                </div>
                                <script type="text/javascript" src=<%=jlink.Const.AppConst.JS_TABLE_VIEW_PATH
                                    %>></script>
                            </body>

                            </html>