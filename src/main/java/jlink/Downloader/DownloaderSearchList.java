/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package jlink.Downloader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jlink.Const.AppConst;
import jlink.Common.AppUtilsCommon;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
@WebServlet(name = "DownloaderSearchList", urlPatterns = { "/DownloaderSearchList" })
public class DownloaderSearchList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PreparedStatement statement;
        ResultSet rs;
        String sql;
        String file_name, title, storage_date;

        try {
            file_name = request.getParameter("file_name");
            title = request.getParameter("title");
            storage_date = request.getParameter("storage_date");
            sql = "select * from image";
            if (storage_date.length() != 0) {
                storage_date = storage_date.replace('-', '/');
                sql = "select * from image where storage_date='" + storage_date + "'";
            }
            if (title.length() != 0) {
                sql = "select * from image where title like '%" + title + "%'";
            }
            if (file_name.length() != 0) {
                sql = "select * from image where filename like '%" + file_name + "%'";
            }
            if (title.length() != 0 && storage_date.length() != 0) {
                sql = "select * from image where title like '%" + title + "%'" + " AND " +
                        "storage_date='" + storage_date + "'";
            }
            if (title.length() != 0 && file_name.length() != 0) {
                sql = "select * from image where title like '%" + title + "%'" + " AND " +
                        "filename like '" + file_name + "'";
            }
            if (file_name.length() != 0 && storage_date.length() != 0) {
                sql = "select * from image where filename like '%" + title + "%'" + " AND " +
                        "storage_date='" + storage_date + "'";
            }
            if (file_name.length() != 0 && title.length() != 0 && storage_date.length() != 0) {
                sql = "select * from image where title like '%" + title + "%'" + " AND " +
                        "storage_date='%" + storage_date + "%'" + " AND " +
                        "filename like '%" + file_name + "%'";
            }
            statement = AppUtilsCommon.prepare_query(sql, new String[0]);
            rs = statement.executeQuery();

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>\n" +
                        "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "        <title>JLINK Downloader</title>\n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"" + AppConst.CSS_SYLE_PATH
                        + "\" />\n" +
                        "    </head>\n" +
                        "    <body>\n" +
                        "       <div class=\"header\">\n" +
                        "            <div class=\"header_left\">\n" +
                        "                <h1>JLINK APPLICATION | Downloader</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"header_right\">\n" +
                        "                <img class=\"header_image\" src=\"" + AppConst.HEADER_LOGO_PATH
                        + "\" />\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"main_container\">          \n" +
                        "            <div class=\"title\">\n" +
                        "                <h1>Search result of saved JLINK files</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"container\">");
                out.println("<div class=\"column\">");
                if (!rs.next()) {
                    out.println("<p>No results were found with the entered parameters</p>");
                } else {
                    out.println("<p>Click on the corresponding row to download the file: </p>"
                            + "<table id=\"content_table\">\n" +
                            "                    <tr class=\"table-main-row\"><th>File name</th>\n" +
                            "                        <th>Title</th>\n" +
                            "                        <th>Description</th>\n" +
                            "                        <th>Storage date</th></tr>\n" +
                            "                    <tr>");

                    String url = jlink.Const.AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg";
                    if (rs.getBoolean("main_protected")){
                        url = "appImages/protected_content.jpeg";
                    }
                    
                    out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\""
                            + AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\""
                            + "data-file=\"" + rs.getString("filename") + ".jpeg\"" + "data-preview=\"" + url + "\" " + "onclick=\"downloadFile(this)\">");
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

                        out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\""
                                + AppConst.SAVE_DIR + File.separator + rs.getString("filename") + ".jpeg\""
                                + "data-file=\"" + rs.getString("filename")
                                + ".jpeg\"" + "data-preview=\"" + url + "\" " + "onclick=\"downloadFile(this)\">");
                        out.println("<td>");
                        out.println(rs.getString("filename") + ".jpeg</td>");
                        out.println("<td>" + rs.getString("title") + "</td>");
                        out.println("<td>" + rs.getString("description") + "</td>");
                        out.println("<td>" + rs.getString("storage_date") + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table></br>");
                }
                out.println("<p>Search another image: </p>\n" +
                        "                <table>\n" +
                        "                    <form method=\"post\" action=\"DownloaderSearchList\">\n" +
                        "                        <tr><td>By file name: </td><td><input type=\"text\" name=\"file_name\"/></td></tr>\n"
                        +
                        "                        <tr><td>By Title: </td><td><input type=\"text\" name=\"title\"/></td></tr>\n"
                        +
                        "                        <tr><td>By Storage date: </td><td><input type=\"date\" name=\"storage_date\"/></td></tr>\n"
                        +
                        "                        <tr><td><input type=\"submit\" value=\"Search\" /></td></tr>    \n" +
                        "                    </form>\n" +
                        "                </table></br>\n" +
                        "                <a href =\"menu.jsp\"><button class=\"button-link\">Return to menu</button></a>\n"
                        +
                        "                </div>    \n" +
                        "                <div class=\"column\">\n" +
                        "                    <img class=\"image\" id=\"image\" src=#>\n" +
                        "                </div>\n" +
                        "                </div>    \n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "            <script type=\"text/javascript\" src=" + AppConst.JS_TABLE_VIEW_PATH
                        + "></script>\n" +
                        "            <script type=\"text/javascript\" src=" + AppConst.JS_DOWNLOAD_FILE_PATH
                        + "></script>\n" +
                        "    </body>");
                out.println("</html>");
            }
        } catch (SQLException e) {
            RequestDispatcher rd;

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error_database.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        } catch (Exception e) {
            RequestDispatcher rd;

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
