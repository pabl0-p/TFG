/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package jlink.Modifier;

import jlink.Const.AppConst;
import jlink.JLINKImage.JLINKImage;
import java.io.File;
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
@WebServlet(name = "SceneModifier", urlPatterns = { "/SceneModifier" })
public class SceneModifier extends HttpServlet {

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

        JLINKImage image, img;
        String scene;
        AppUtilsModifier utils;
        RequestDispatcher rd;

        try {
            scene = request.getParameter("scene");
            image = (JLINKImage) request.getSession().getAttribute("image");

            utils = new AppUtilsModifier();
            utils.getSceneByLabel(image, scene);
            img = utils.getScene();

            
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>\n" +
                        "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "        <title>JSP Page</title>\n" +
                        "        <link rel=\"stylesheet\" type=\"text/css\" href=\"" + AppConst.CSS_SYLE_PATH
                        + "\" />\n" +
                        "    </head>\n" +
                        "    <body>\n" +
                        "        <div class=\"header\">\n" +
                        "            <div class=\"header_left\">\n" +
                        "                <h1>JLINK APPLICATION | Modifier</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"header_right\">\n" +
                        "                <img class=\"header_image\" src=\"appImages/Logo_ETSETB.png\" />\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"main_container\">          \n" +
                        "            <div class=\"title\">\n" +
                        "                <h1>Scene modifier</h1>\n" +
                        "            </div>\n" +
                        "            <div class=\"container\"> \n" +
                        "                <div class=\"column\">\n" +
                        "                    <p>The actual version of this scene is: <q>" + img.getVersion()
                        + "</q></p>    \n" +
                        "                    <p>You can change the textual values that define the scene and the duration (in milliseconds) of the animation transition</p>  \n"
                        +
                        "                    <table>\n" +
                        "                        <form method=\"post\" action=\"SceneReplacementModifier\">\n" +
                        "                            <tr><td>Title: </td><td><input type=\"text\" name=\"title\" value=\""
                        + img.getTitle() + "\"/></td></tr>\n" +
                        "                            <tr><td>Description: </td><td><textarea name=\"description\"/>"
                        + img.getNote() + "</textarea></td></tr>");
                if (!img.isIsMain()) {
                    out.println(
                            "<tr><td>Duration (ms): </td><td><input type=\"number\" name=\"duration\" min=\"1\" value=\""
                                    + img.getLink_duration() + "\"></td></tr>");
                } else {
                    out.println("<input type=\"hidden\" name=\"duration\" value=\"600\">");
                }
                if (!img.getLinked_images().isEmpty()) {
                    out.println("<tr><td>Sprite color: </td><td><select name=\"sprite_color\">");
                    switch (img.getSprite_color()) {
                        case AppConst.SPRITE_PATH:
                            out.println("<option selected>Green</option>");
                            out.println("<option>Blue</option>");
                            out.println("<option>Red</option>");
                            out.println("</select></td></tr>");
                            break;
                        case AppConst.SPRITE_BLUE_PATH:
                            out.println("<option>Green</option>");
                            out.println("<option selected>Blue</option>");
                            out.println("<option>Red</option>");
                            out.println("</select></td></tr>");
                            break;
                        case AppConst.SPRITE_RED_PATH:
                            out.println("<option selected>Green</option>");
                            out.println("<option>Blue</option>");
                            out.println("<option selected>Red</option>");
                            out.println("</select></td></tr>");
                            break;
                    }
                } else {
                    out.println("<input type=\"hidden\" name=\"sprite_color\" value=\"Green\">");
                }
                out.println("<p>If protection change is set to \"No\", related values will be ignored<br>If \"Yes\" is selected and the values are left blank, protection will be removed</p>" +
                            "                        <tr><td>Change protection: </td>\n" + 
                            "                        <td><select name=\"change\" id=\"change\" required>\n" + 
                            "                            <option value= \"Yes\">Yes</option>\n" + 
                            "                            <option value= \"No\">No</option>\n" + 
                            "                        </select></td></tr>\n" + 
                            "                        <tr><td>Encryption: </td>\n" + 
                            "                        <td><select name=\"encryption\" id=\"encryption\" >\n" + 
                            "                            <option value=\"\"></option>\n" +
                            "                            <option value= \"AES256\">AES 256</option>\n" + 
                            "                            <option value= \"AES256IV\">AES 256 with IV</option>\n" + 
                            "                        </select></td></tr>\n" + 
                            "                        <tr><td>Replacement: </td>\n" + 
                            "                        <td><select name=\"replacement\" id=\"replacement\">\n" + 
                            "                            <option value=\"\"></option>\n" + 
                            "                            <option value= \"roi\">ROI</option>\n" + 
                            "                            <option value= \"replace_img\">Replacement image</option>\n" + 
                            "                        </select></td></tr>\n" + 
                            "                        <tr><td>Access (Ctrl+Click): </td>\n" + 
                            "                        <td>View<br>\n" + 
                            "                        <select name=\"view_access\" id=\"view_access\" multiple size=3>\n" + 
                            "                            <option value= \"admin\">Admin</option>\n" + 
                            "                            <option value= \"user\">User</option>\n" + 
                            "                            <option value= \"guest\">Guest</option>\n" + 
                            "                        </select></td></tr>\n" + 
                            "                        <tr><td></td><td>Edit<br>\n" + 
                            "                        <select name=\"edit\" id=\"edit\" multiple size=3>\n" + 
                            "                            <option value= \"admin\">Admin</option>\n" + 
                            "                            <option value= \"user\">User</option>\n" + 
                            "                            <option value= \"guest\">Guest</option>\n" + 
                            "                        </select></td></tr>     ");
                out.println("<input type=\"hidden\" name=\"scene\" value=\"" + scene + "\">  \n" 
                        +           
                        "                            <tr><td><input type=\"submit\" value=\"Do another action\" /></td></tr>  \n"
                        +
                        "                        </form>\n" +
                        "                    </table>\n" +
                        "                </div>\n" +
                        "                <div class=\"column\">\n");
                if (img.getReplacement() != null && img.getReplacement().equals("roi")) {
                    out.println("<img class=\"imageNoProtect\" src=\"" + AppConst.MODIFIER_DIR
                        + File.separator + img.getTitle() + "_original.jpeg\">\n" +
                        "<img class=\"imageProtect\" src=\"" + AppConst.MODIFIER_DIR
                        + File.separator + img.getTitle() + ".jpeg\">\n");
                } else if (img.getReplacement() != null && img.getReplacement().equals("replace_img")) {
                    out.println("<img class=\"imageNoProtect\" src=\"" + AppConst.MODIFIER_DIR
                        + File.separator + img.getTitle() + ".jpeg\">\n" +
                        "<img class=\"imageProtect\" src=\"" + AppConst.MODIFIER_DIR
                        + File.separator + img.getTitle()+ "_replacement.jpeg\">\n");
                } else {
                    out.println("                    <img class=\"imageFinal\" src=\"" + AppConst.MODIFIER_DIR
                        + File.separator + img.getTitle() + ".jpeg\">\n");
                }
                        
                out.println("                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n");
                out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PRIVSEC_SELECT_PATH
                        + "\"></script>" +
                        "    </body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
