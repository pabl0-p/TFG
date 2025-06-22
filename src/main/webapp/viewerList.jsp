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
                                <title>JLINK Viewer</title>
                                <link rel="stylesheet" type="text/css" href=<%=jlink.Const.AppConst.CSS_SYLE_PATH %> />
                            </head>

                            <body>
                                <div class="header">
                                    <div class="header_left">
                                        <h1>JLINK APPLICATION | Viewer</h1>
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
                                            <% 
                                                PreparedStatement statement;
                                                ResultSet rs;
                                                String sql;
                                                try {
                                                    sql = "select * from image";
                                                    statement = jlink.Common.AppUtilsCommon.prepare_query(sql, new String[0]);
                                                    rs = statement.executeQuery();
                                                    if (!rs.next()) {
                                                        out.println("<p>There is no file saved in the database</p>");
                                                    } else {
                                                        out.println("<p>Search an image: </p>\n" +
                                                                " <table>\n" +
                                                                " <form method=\"post\" action=\"ViewerSearchList\">\n" +
                                                                " <tr> <td>By file name: </td> <td><input type=\"text\" name=\"file_name\" /></td> </tr>\n" +
                                                                " <tr> <td>By Title: </td> <td><input type=\"text\" name=\"title\" /></td> </tr>\n" +
                                                                " <tr>  <td>By Storage date: </td> <td><input type=\"date\" name=\"storage_date\" /></td> </tr>\n"
                                                                +
                                                                " <tr>  <td><input type=\"submit\" value=\"Search\" /></td> </tr> \n" +
                                                                " </form>\n" +
                                                                " </table></br>");
                                                        out.println("<p>Click on the corresponding row to view the file: </p>" +
                                                                "<table id=\"content_table\">\n" +
                                                                " <tr class=\"table-main-row\"> <th>File name</th>\n" +
                                                                " <th>Title</th>\n" +
                                                                " <th>Description</th>\n" +
                                                                " <th>Storage date</th> </tr>\n" +
                                                                "<tr>");

                                                        String url = jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg";
                                                        if (rs.getBoolean("main_protected")){
                                                            url = "appImages/protected_content.jpeg";
                                                        }
                                                        out.println(" <tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\""
                                                                + jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\" " +
                                                                "data-preview=\"" + url + "\" " +
                                                                "onclick=\"window.location='Viewer?file_name=" + rs.getString("filename") + "'\"> ");
                                                        out.println("<td>");
                                                        out.println(rs.getString("filename") + ".jpeg</td>");
                                                        out.println("<td>" + rs.getString("title") + "</td>");
                                                        out.println("<td>" + rs.getString("description") + "</td>");
                                                        out.println("<td>" + rs.getString("storage_date") + "</td>");
                                                        out.println("</tr>");
                                                        while (rs.next()) {

                                                            url = jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg";
                                                            if (rs.getBoolean("main_protected")){
                                                                url = "appImages/protected_content.jpeg";
                                                            }
                                                            out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\"" +
                                                                    jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\" " + 
                                                                    "data-preview=\"" + url + "\" " +
                                                                    "onclick=\"window.location='Viewer?file_name=" + rs.getString("filename") + "'\">");
                                                            out.println("<td>");
                                                            out.println(rs.getString("filename") + ".jpeg</td>");
                                                            out.println("<td>" + rs.getString("title") + "</td>");
                                                            out.println("<td>" + rs.getString("description") + "</td>");
                                                            out.println("<td>" + rs.getString("storage_date") + "</td>");
                                                            out.println("</tr>");
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    RequestDispatcher rd;
                                                    out.println(" <!DOCTYPE html>");
                                                    out.println("<html>");
                                                    rd = request.getRequestDispatcher("/error.jsp");
                                                    rd.forward(request, response);
                                                    out.println("  </html>");
                                                }
                                            %>
                                            </table></br>
                                            <a href="menu.jsp"><button class="button-link">Return to menu</button></a>
                                        </div>
                                        <div class="column">
                                            <img class="image" id="image" src=#>
                                        </div>
                                    </div>
                                </div>
                                <script type="text/javascript" src=<%=jlink.Const.AppConst.JS_TABLE_VIEW_PATH
                                    %>></script>
                            </body>

                            </html>