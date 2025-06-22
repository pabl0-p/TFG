package jlink.Modifier;

import jlink.Const.AppConst;
import jlink.JLINKImage.JLINKImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.image.BufferedImage;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
@WebServlet(name = "SceneReplacementModifier", urlPatterns = { "/SceneReplacementModifier" })
public class SceneReplacementModifier extends HttpServlet {

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
        String scene, title, description, duration, sprite_color;
        AppUtilsModifier utils;
        RequestDispatcher rd;
        String encryption, replacement;
        String[] view_access, edit_access;
        Boolean change=false;

        try {
            scene = request.getParameter("scene");
            image = (JLINKImage) request.getSession().getAttribute("image");

            utils = new AppUtilsModifier();
            utils.getSceneByLabel(image, scene);
            img = utils.getScene();

            scene = request.getParameter("scene");
            title = request.getParameter("title");
            description = request.getParameter("description");
            duration = request.getParameter("duration");
            sprite_color = request.getParameter("sprite_color");

            encryption = request.getParameter("encryption");
            replacement = request.getParameter("replacement");
            view_access = request.getParameterValues("view_access");
            edit_access = request.getParameterValues("edit");
            
            String changeString = request.getParameter("change");
            if("Yes".equals(changeString)) {
                    String i = null;
                    if (img.getReplacement() != null && img.getReplacement().equals("roi")) {
                        i = "_original";
                    } else {
                        i = "";
                    }
                    String basePath = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator;
                    String getPath = basePath + image.getTitle() + i + ".jpeg";
                    String savePath = null;

                    if (replacement != null && replacement.equals("roi")) {
                        savePath = basePath+ image.getTitle() + "_original.jpeg";
                    } else {
                        savePath = basePath + image.getTitle() + ".jpeg";
                    }

                    System.out.println(getPath);
                    System.out.println(savePath);
                    if (!getPath.equals(savePath)) {
                        BufferedImage im = ImageIO.read(new File(getPath));
                        ImageIO.write(im, "jpeg", new File(savePath));
                    }
                }

            if("Yes".equals(changeString)) {
                change = true;
                if(replacement != null && !replacement.isEmpty()){
                    img.setReplacement(replacement);
                }else {
                    img.setReplacement(null);
                }

                if(encryption != null && !encryption.isEmpty()){
                    img.setEncryption(encryption);
                    
                    if(view_access != null){
                        img.setViewAccess(view_access);
                    }else {
                        img.setViewAccess(null);
                    }
                    if(edit_access != null){
                        img.setViewAccess(edit_access);
                    }else {
                        img.setViewAccess(null);
                    }
                }else {
                    img.setEncryption(null);
                }

                
            }

            img.setChange(change);

            HttpSession s = request.getSession();
            s.setAttribute("scene", scene);
            s.setAttribute("title", title);
            s.setAttribute("description", description);
            s.setAttribute("duration", duration);
            s.setAttribute("sprite_color", sprite_color);
            
            try (PrintWriter out = response.getWriter()) {
                if(replacement.equals("roi") && change){
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>JLINK Creator</title>");
                    out.println(
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + AppConst.CSS_SYLE_PATH + "\" />");
                    out.println("</head>");
                    out.println("<body onload=\"getImgSize()\">");
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
                            "                <h1>Adding Privacy and Security</h1>\n" +
                            "            </div>\n" +
                            "            <div class=\"container\">\n"+
                            "                <div class=\"column\">");
                    out.println("               <p>Image size:</p>    \n" +
                            "                    <ul>\n" +
                            "                    <li><p>Width: \n" +
                            "                    <q id=\"img_width\"></q></p></li>\n" +
                            "                    <li><p>Height: \n" +
                            "                    <q id=\"img_height\"></q></p></li>\n" +
                            "                    </ul>\n" +
                            "                    <p>Select ROI:</p>                \n" +
                            "                    <ul>\n" +
                            "                    <li><p>Initial Coord X: \n" +
                            "                    <q id=\"roi_x_display\">Click on the image</q></p></li>\n" +
                            "                    <li><p>Inital Coord Y: \n" +
                            "                    <q id=\"roi_y_display\">Click on the image</q></p></li>\n" +
                            "                    <li><p>ROI Width: \n" +
                            "                    <q id=\"roi_width_display\">Click on the image</q></p></li>\n" +
                            "                    <li><p>ROI Height: \n" +
                            "                    <q id=\"roi_height_display\">Click on the image</q></p></li>\n" +
                            "                    </ul>");
                    out.println("<form id=\"addReplacement\" method=\"post\" action=\"ApplySceneModificator\" enctype = \"multipart/form-data\">\n" +
                            "                    <label for=\"imgInp\">Upload image: </label>\n" +
                            "                    <input type=\"file\" name=\"image\" id=\"imgInp\" required /><br><br>\n" +
                            "                    <input type=\"hidden\" name=\"roi_x\" id=\"roi_x\" value=\"\" />\n" +
                            "                    <input type=\"hidden\" name=\"roi_y\" id=\"roi_y\" value=\"\" />\n" +
                            "                    <input type=\"hidden\" name=\"roi_width\" id=\"roi_width\" value=\"\" />\n" +
                            "                    <input type=\"hidden\" name=\"roi_height\" id=\"roi_height\" value=\"\" />\n" +
                            "                    <input type=\"submit\" value=\"Next\"/>"
                            +
                            "                </form>\n" +
                            "                </div>\n" +
                            "                <div class=\"column\">\n" +
                            "                    <div class=\"images\">\n" +
                            "                        <img class=\"image\" id=\"img\" src=\"" + AppConst.MODIFIER_DIR
                            + File.separator + img.getTitle() + "_original.jpeg\">\n" +
                            "                        <div id=\"roi\"></div>\n"+
                            "                    </div>\n" +
                            "                    <div class=\"images\">\n " +
                            "                        <img class=\"image\" id=\"pre-img\" src=\"\"/>\n" +
                            "                    </div>\n" + 
                            "                </div>\n" +
                            "            </div>\n" +
                            "        </div>");
                    out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_ROI_SELECT_PATH
                            + "\"></script>");
                    out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PREVIEW_IMAGE_PATH
                           + "\"></script>");
                    out.println("</body>");
                    out.println("</html>");


                }else if(replacement.equals("replace_img") && change){
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>JLINK Creator</title>");
                    out.println(
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + AppConst.CSS_SYLE_PATH + "\" />");
                    out.println("</head>");
                    out.println("<body onload=\"getImgSize()\">");
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
                            "                <h1>Adding Privacy and Security</h1>\n" +
                            "            </div>\n" +
                            "            <div class=\"container\">\n"+
                            "                <div class=\"column\">");
                    out.println("<form id=\"addReplacement\" method=\"post\" action=\"ApplySceneModificator\" enctype = \"multipart/form-data\">\n" +
                            "                    <label for=\"imgInp\">Upload image: </label>\n" +
                            "                    <input type=\"file\" name=\"image\" id=\"imgInp\" required /><br><br>\n" +
                            "                    <input type=\"submit\" value=\"Next\"/>\n"
                            +
                            "                </form>\n" +
                            "                </div>\n" +
                            "                <div class=\"column\">\n" +
                            "                        <img class=\"bigimage\" id=\"pre-img\" src=\"\"/>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "        </div>");
                    out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PREVIEW_IMAGE_PATH
                           + "\"></script>");
                    out.println("</body>");
                    out.println("</html>");
                }else{
                    rd = request.getRequestDispatcher("/ApplySceneModificator");
                    rd.forward(request, response);
                }
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

