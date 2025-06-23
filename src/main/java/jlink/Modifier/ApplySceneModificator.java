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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
@WebServlet(name = "ApplySceneModificator", urlPatterns = { "/ApplySceneModificator" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50, // 50MB
        location = "/")
public class ApplySceneModificator extends HttpServlet {

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
        String scene, title, description, duration, sprite_color, encryption, replacement;
        String[] view_access, edit_access;
        Boolean change;
        AppUtilsModifier utils;
        RequestDispatcher rd;
        Part filePart = null;

        try {

            HttpSession s = request.getSession();
            image = (JLINKImage) request.getSession().getAttribute("image");

            scene = (String) s.getAttribute("scene");
            title = (String) s.getAttribute("title");
            description = (String) s.getAttribute("description");
            duration = (String) s.getAttribute("duration");
            sprite_color = (String) s.getAttribute("sprite_color");

            encryption = (String) s.getAttribute("encryption");
            replacement = (String) s.getAttribute("replacement");
            view_access = (String[]) s.getAttribute("view_access");
            edit_access = (String[]) s.getAttribute("edit_access");
            change = (Boolean) s.getAttribute("change");

            switch (sprite_color) {
                case "Green":
                    sprite_color = AppConst.SPRITE_PATH;
                    break;
                case "Blue":
                    sprite_color = AppConst.SPRITE_BLUE_PATH;
                    break;
                case "Red":
                    sprite_color = AppConst.SPRITE_RED_PATH;
                    break;
            }

            utils = new AppUtilsModifier();
            if ((replacement != null && !replacement.isEmpty()) && change) {
                filePart = request.getPart("image");
            }

            utils.modifySceneInformation(image, scene, title, description, duration, sprite_color, request, filePart, encryption, replacement, view_access, edit_access, change);

            request.getSession().setAttribute("image", image);

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/ActionModifier");
                rd.forward(request, response);
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
