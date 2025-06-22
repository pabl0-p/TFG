/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package jlink.Creator;

import jlink.Const.AppConst;
import jlink.JLINKImage.JLINKImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
@WebServlet(name = "InitCreator", urlPatterns = { "/InitCreator" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50, // 50MB
        location = "/")
public class InitCreator extends HttpServlet {

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

        JLINKImage image;
        String file_name, file_title, file_description, storage_date;
        Part filePart;
        String appPath;

        try {
            image = (JLINKImage) request.getSession().getAttribute("image");
            file_name = (String) request.getSession().getAttribute("file_name");
            file_title = (String) request.getSession().getAttribute("file_title");
            file_description = (String) request.getSession().getAttribute("file_description");
            storage_date = (String) request.getSession().getAttribute("storage_date");

        //     image = (JLINKImage) getServletContext().getAttribute("image");
        //     file_name = (String) getServletContext().getAttribute("file_name");
        //     file_title = (String) getServletContext().getAttribute("file_title");
        //     file_description = (String) getServletContext().getAttribute("file_description");
        //     storage_date = (String) getServletContext().getAttribute("storage_date");

            appPath = request.getServletContext().getRealPath("");
            filePart = request.getPart("image");
            AppUtilsCreator.setProtection(image, filePart, appPath, request);

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>JLINK Creator</title>");
                out.println(
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + AppConst.CSS_SYLE_PATH + "\" />");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class=\"header\">\n" +
                        "            <div class=\"header_left\">\n" +
                        "                <h1>JLINK APPLICATION | Creator</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"header_right\">\n" +
                        "                <img class=\"header_image\" src=\"" + AppConst.HEADER_LOGO_PATH
                        + "\" />\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"main_container\">          \n" +
                        "            <div class=\"title\">\n" +
                        "                <h1>Creating file '" + file_name + ".jpeg'</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"container\">\n" +
                        "                <div class=\"column\">\n" +
                        "                    <p>File information:</p>\n" +
                        "                    <table id=\"content_table\">\n" +
                        "                    <tr class=\"table-main-row\"><th>File name</th>\n" +
                        "                        <th>Title</th>\n" +
                        "                        <th>Description</th>\n" +
                        "                        <th>Storage date</th></tr>\n" +
                        "                    <tr>");
                out.println("<td>" + file_name + "</td>");
                out.println("<td>" + file_title + "</td>");
                out.println("<td>" + file_description + "</td>");
                out.println("<td>" + storage_date + "</td>");
                out.println("                    </table></br>\n" +
                        "                    <p>Contains:</p>\n" +
                        "                    <table id=\"content_table\">\n" +
                        "                        <tr class=\"table-main-row\">\n" +
                        "                            <th>Title</th>\n" +
                        "                            <th>Description</th>\n" +
                        "                            <th>Previous image</th>\n" +
                        "                        </tr>");
                out.println("<tr onmouseover=\"showImg(this)\" onmouseout=\"hideImg(this)\" data-value=\""
                        + AppConst.CREATOR_DIR + File.separator + image.getTitle().replace(" ", "_")
                        + ".jpeg\">");
                out.println("<td>" + image.getTitle() + "</td>");
                out.println("<td>" + image.getNote() + "</td>");
                out.println("<td>-</td></tr>");
                out.println("</table></br>");
                out.println("                    <p>Add an image:</p>\n" +
                        "                        <form method=\"post\" action=\"SelectReplacement\", enctype = \"multipart/form-data\"><table>\n"
                        +
                        "                            <tr><td>Upload image: </td><td><input type=\"file\" name=\"image\" id=\"imgInp\" required/></td></tr>\n"
                        +
                        "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" required/></td></tr>\n"
                        +
                        "                            <tr><td>Description: </td><td><textarea name=\"description\"/></textarea></td></tr>\n"
                        +
                        "                            <tr><td>Previous image: </td><td><select name=\"previous_image\"  required>\n"
                        +
                        "                            <option selected>" + image.getTitle().replace(" ", "_")
                        + "</option>\n" +
                        "                            </select></td></tr>\n" 
                        +
                        "                           <tr><td>Encryption: </td><td><select name=\"encryption\" id=\"encryption\">\n"+
                        "                               <option value= \"\"</option>"+
                        "                               <option value= \"AES256\">AES 256</option>\n"+
                        "                               <option value= \"AES256IV\">AES 256 with IV</option>\n"+
                        "                           </select></td></tr>\n"
                        +
                        "                           <tr><td>Replacement: </td><td><select name=\"replacement\" id=\"replacement\">\n"+
                        "                               <option value= \"\"</option>"+
                        "                               <option value= \"roi\">ROI</option>\n"+
                        "                               <option value= \"replace_img\">Replacement image</option>\n"+
                        "                           </select></td></tr>\n"
                        +
                        "                           <tr><td>Access (Ctrl+Click): </td><td>View<br><select name=\"view_access\" id=\"view_access\" multiple size=3>\n"+
                        "                               <option value= \"admin\">Admin</option>\n"+
                        "                               <option value= \"user\">User</option>\n"+
                        "                               <option value= \"guest\">Guest</option>\n"+
                        "                           </select></td></tr>" +
                        "                           <tr><td></td><td>Edit<br><select name=\"edit\" id=\"edit\" multiple size=3>\n"+
                        "                               <option value= \"admin\">Admin</option>\n"+
                        "                               <option value= \"user\">User</option>\n"+
                        "                               <option value= \"guest\">Guest</option>\n"+
                        "                           </select></td></tr>\n"
                        +
                        "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n"
                        +
                        "                        </table></form>\n" +
                        "                </div>\n" +
                        "                <div class=\"column\">\n" +
                        "                    <img class=\"image\" id=\"pre-img\" src=\"#\"/>\n" +
                        "                    <img class=\"image\" id=\"image\" src='#'>\n" +
                        "                </div>     \n" +
                        "            </div>\n" +
                        "        </div>");
                out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PREVIEW_IMAGE_PATH
                        + "\"></script>");
                out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_CONTAIN_TABLE_PATH
                        + "\"></script>");
                out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PRIVSEC_SELECT_PATH
                + "\"></script>");
                out.println("</body>");
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
