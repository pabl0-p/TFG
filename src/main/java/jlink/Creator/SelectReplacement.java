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
 * @author Pablo Pascual
 */
@WebServlet(name = "SelectReplacement", urlPatterns = { "/SelectReplacement" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50, // 50MB
        location = "/")
public class SelectReplacement extends HttpServlet {

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

        JLINKImage link_image;
        Part filePart;
        String appPath;
        String encryption, replacement;
        String[] view_access, edit_access;

        try {
            link_image = new JLINKImage();
            link_image.setPrevious_image(request.getParameter("previous_image"));
            link_image.setTitle(request.getParameter("title"));
            link_image.setNote(request.getParameter("description"));
            appPath = request.getServletContext().getRealPath("");
            link_image.setAppPath(appPath);
            filePart = request.getPart("image");
            AppUtilsCreator.saveImage(appPath, filePart, link_image.getTitle());
            

            encryption = request.getParameter("encryption");
            replacement = request.getParameter("replacement");
            view_access = request.getParameterValues("view_access");
            edit_access = request.getParameterValues("edit");

            if(replacement != null && !replacement.isEmpty()){
                link_image.setReplacement(replacement);
            }

            if(encryption != null && !encryption.isEmpty()){
                link_image.setEncryption(encryption);
                
                if(view_access != null){
                    link_image.setViewAccess(view_access);
                }
                if(edit_access != null){
                    link_image.setViewAccess(edit_access);
                }
            }
            
            request.getSession().setAttribute("link_image", link_image);

            try (PrintWriter out = response.getWriter()) {
                if(replacement.equals("roi")){
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
                    out.println("<p>Image size:</p>    \n" +
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
                    out.println("<form id=\"SelectReplacement\" method=\"post\" action=\"SelectSprite\", enctype = \"multipart/form-data\">\n" +
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
                            "                        <img class=\"image\" id=\"img\" src=\"" + AppConst.CREATOR_DIR
                            + File.separator + link_image.getTitle().replace(" ", "_") + ".jpeg\">\n" +
                            "                        <div id=\"roi\"></div>\n"+
                            "                    </div>\n" +
                            "                    <div class=\"images\">\n " +
                            "                        <img class=\"image\" id=\"pre-img\" src=\"#\"/>\n" +
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

                }else if(replacement.equals("replace_img")){
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
                    out.println("<form id=\"SelectReplacement\" method=\"post\" action=\"SelectSprite\", enctype = \"multipart/form-data\">\n" +
                            "                    <label for=\"imgInp\">Upload image: </label>\n" +
                            "                    <input type=\"file\" name=\"image\" id=\"imgInp\" required /><br><br>\n" +
                            "                    <input type=\"submit\" value=\"Next\"/>\n"
                            +
                            "                </form>\n" +
                            "                </div>\n" +
                            "                <div class=\"column\">\n" +
                            "                        <img class=\"bigimage\" id=\"pre-img\" src=\"#\"/>\n" +
                            "                </div>\n" +
                            "            </div>\n" +
                            "        </div>");
                    out.println("<script type=\"text/javascript\" src=\"" + AppConst.JS_PREVIEW_IMAGE_PATH
                           + "\"></script>");
                    out.println("</body>");
                    out.println("</html>");
                }else{
                    RequestDispatcher rd = request.getRequestDispatcher("/SelectSprite");
                    rd.forward(request, response);
                }
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

