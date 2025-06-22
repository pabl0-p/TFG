/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package jlink.Modifier;

import jlink.Const.AppConst;
import jlink.JLINKImage.JLINKImage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
@WebServlet(name = "ActionModifier", urlPatterns = { "/ActionModifier" })
public class ActionModifier extends HttpServlet {

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

                String file_name;
                JLINKImage image;
                AppUtilsModifier utils;
                String file_title, file_description, storage_date;

                try {
                        image = (JLINKImage) request.getSession().getAttribute("image");
                        // image = (JLINKImage) getServletContext().getAttribute("image");
                        utils = new AppUtilsModifier();
                        file_name = (String) request.getSession().getAttribute("file_name");
                        file_title = (String) request.getSession().getAttribute("file_title");
                        file_description = (String) request.getSession().getAttribute("file_description");
                        storage_date = (String) request.getSession().getAttribute("storage_date");

                        // file_name = (String) getServletContext().getAttribute("file_name");
                        // file_title = (String) getServletContext().getAttribute("file_title");
                        // file_description = (String) getServletContext().getAttribute("file_description");
                        // storage_date = (String) getServletContext().getAttribute("storage_date");

                        try (PrintWriter out = response.getWriter()) {
                                out.println("<!DOCTYPE html>");
                                out.println("<html>");
                                out.println("<head>");
                                out.println("<title>JLINK Modifier</title>");
                                out.println(
                                                "<link rel=\"stylesheet\" type=\"text/css\" href=\""
                                                                + AppConst.CSS_SYLE_PATH + "\" />");
                                out.println("</head>");
                                out.println("<body>");
                                out.println("<div class=\"header\">\n" +
                                                "            <div class=\"header_left\">\n" +
                                                "                <h1>JLINK APPLICATION | Modifier</h1>\n" +
                                                "            </div>\n" +
                                                "            <div class=\"header_right\">\n" +
                                                "                <img class=\"header_image\" src=\""
                                                + AppConst.HEADER_LOGO_PATH
                                                + "\" />\n" +
                                                "            </div>\n" +
                                                "        </div>\n" +
                                                "        <div class=\"main_container\">          \n" +
                                                "            <div class=\"title\">\n" +
                                                "                <h1>Modifing file '" + file_name + ".jpeg'</h1>\n" +
                                                "            </div>\n" +
                                                "            <div class=\"container\">\n" +
                                                "                <div class=\"column\">\n" +
                                                "                    <p>File information:</p>\n" +
                                                "                    <table id=\"content_table\">\n" +
                                                "                    <tr class=\"table-main-row\"><th>File name</th>\n"
                                                +
                                                "                        <th>Title</th>\n" +
                                                "                        <th>Description</th>\n" +
                                                "                        <th>Storage date</th></tr>\n");
                                out.println("<td>" + file_name + "</td>");
                                out.println("<td>" + file_title + "</td>");
                                out.println("<td>" + file_description + "</td>");
                                out.println("<td>" + storage_date + "</td>");
                                out.println("                    </table></br>\n" +
                                                "                    <p>Contains (By clicking on the corresponding row you can modify the configuration of each scene):</p>\n"
                                                +
                                                "                    <table id=\"content_table\">\n" +
                                                "                        <tr class=\"table-main-row\">\n" +
                                                "                            <th>Title</th>\n" +
                                                "                            <th>Description</th>\n" +
                                                "                            <th>Previous image</th>\n" +
                                                "                            <th>Encryption Method</th>\n" +
                                                "                            <th>Replacement Method</th>\n" +
                                                "                            <th>Access Rules</th>\n" +
                                                "                        </tr>\n");
                                utils.containsTable(out, image);
                                out.println("</table>");
                                out.println("<div class=\"text-and-button\">");
                                out.println(
                                                "<p>You can also add or delete an image, or finish the modification and create the new file&nbsp;&nbsp;&nbsp;&nbsp;</p>");
                                out.println("<form method=\"post\" action=\"FinishModifier\">\n" +
                                                "                        <input type=\"submit\" value=\"Finish\" /></td><td>                   \n"
                                                +
                                                "                    </form>");
                                out.println("</div>");
                                out.println("<strong>Add an image:</strong>\n" +
                                                "                    <table>\n" +
                                                "                        <form method=\"post\" action=\"SelectReplacementModifier\", enctype = \"multipart/form-data\">\n"
                                                +
                                                "                            <tr><td>Upload image: </td><td><input type=\"file\" name=\"image\" id=\"imgInp\" required/></td></tr>\n"
                                                +
                                                "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" required/></td></tr>\n"
                                                +
                                                "                            <tr><td>Description: </td><td><textarea name=\"description\"/></textarea></td></tr>\n"
                                                +
                                                "                            <tr><td>Previous image: </td><td><select name=\"previous_image\"  required>");
                                utils.formOptions(out, image);
                                out.println("</select></td></tr>\n" +
                                                "                               <tr><td>Encryption: </td>\n" +
                                                "                                       <td><select name=\"encryption\" id=\"encryption\" >\n" +
                                                "                                               <option value=\"\"></option>\r\n" +
                                                "                                               <option value= \"AES256\">AES 256</option>\r\n" + 
                                                "                                               <option value= \"AES256IV\">AES 256 with IV</option>\r\n" + 
                                                "                                       </select></td></tr>\r\n" + 
                                                "                               <tr><td>Replacement: </td>\r\n" + 
                                                "                                       <td><select name=\"replacement\" id=\"replacement\">\r\n" + 
                                                "                                               <option value=\"\"></option>\r\n" + 
                                                "                                               <option value= \"roi\">ROI</option>\r\n" + 
                                                "                                               <option value= \"replace_img\">Replacement image</option>\r\n" + 
                                                "                                       </select></td></tr>\r\n" + 
                                                "                               <tr><td>Access (Ctrl+Click): </td>\r\n" + 
                                                "                               <td>View<br>\r\n" + 
                                                "                                       <select name=\"view_access\" id=\"view_access\" multiple size=3>\r\n" + 
                                                "                                               <option value= \"admin\">Admin</option>\r\n" + 
                                                "                                               <option value= \"user\">User</option>\r\n" + 
                                                "                                               <option value= \"guest\">Guest</option>\r\n" + 
                                                "                                       </select></td></tr>\r\n" + 
                                                "                               <tr><td></td><td>Edit<br>\r\n" + 
                                                "                                       <select name=\"edit\" id=\"edit\" multiple size=3>\r\n" + 
                                                "                                               <option value= \"admin\">Admin</option>\r\n" + 
                                                "                                               <option value= \"user\">User</option>\r\n" + 
                                                "                                               <option value= \"guest\">Guest</option>\r\n" + 
                                                "                                       </select></td></tr>); " +
                                                "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n"
                                                +
                                                "                        </form>\n" +
                                                "                    </table>");
                                out.println("</br><strong>Delete an image:</strong>\n" +
                                                "                    <table>\n" +
                                                "                        <form method=\"post\" action=\"DeleteLink\">\n"
                                                +
                                                "                            <tr><td>Select image: </td><td><select name=\"deleted_image\"  required>");
                                utils.formDelete(out, image);
                                out.println("</select></td></tr>\n" +
                                                "                            <tr><td><input type=\"submit\" value=\"Next\" /></td><td>                   \n"
                                                +
                                                "                        </form>\n" +
                                                "                    </table>");
                                out.println("</div>\n" +
                                                "                <div class=\"column\">\n" +
                                                "                    <img class=\"image\" id=\"pre-img\" src=\"\"/>\n"
                                                +
                                                "                    <img class=\"image\" id=\"image\" src=\"\">\n" +
                                                "                </div>     \n" +
                                                "            </div>\n" +
                                                "        </div>");
                                out.println("<script type=\"text/javascript\" src=\""
                                                + AppConst.JS_PREVIEW_IMAGE_PATH
                                                + "\"></script>");
                                out.println("<script type=\"text/javascript\" src=\""
                                                + AppConst.JS_CONTAIN_TABLE_MODIFIER_PATH
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
