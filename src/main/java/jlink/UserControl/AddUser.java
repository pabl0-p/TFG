package jlink.UserControl;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import jlink.Common.AppUtilsCommon;


/*
 * 
 * @author Pablo Pascual
 */

@WebServlet(name = "AddUser", urlPatterns = { "/AddUser" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50, // 50MB
        location = "/")
public class AddUser extends HttpServlet {

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
        
        String username, passwordd, sql;
        PreparedStatement st;
        RequestDispatcher rd;

        try{
            username = request.getParameter("username");
            passwordd = request.getParameter("password");

            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String enc_password = encoder.encode(passwordd);
            sql = "SELECT * FROM users WHERE LOWER(username) = ?;";
            st = AppUtilsCommon.prepare_query(sql, new String[] {username});
            ResultSet rs = st.executeQuery();

            if(rs != null && !rs.next() && !"guest".equals(username.toLowerCase())){
                sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
                st = AppUtilsCommon.prepare_query(sql, new String[] {username, enc_password, "user"});
                int added = st.executeUpdate();
                if(added>0){
                    rd = request.getRequestDispatcher("/menu.jsp");

                    request.getSession().setAttribute("username", username);
                    request.getSession().setAttribute("role", "user");
                    rd.include(request, response);
                }else{
                    throw new SQLException("The insertion failed");
                }
                    
            }else{
                rd = request.getRequestDispatcher("/register.jsp");
                request.setAttribute("error", "Already exists");
                rd.include(request, response);
            }
        
        } catch (SQLException e) {

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error_database.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }

        } catch (Exception e) {

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
                out.println("</html>");
            }
        }


    }

    /*
     * Handles the HTTP <code>POST</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
