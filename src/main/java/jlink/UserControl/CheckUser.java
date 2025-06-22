package jlink.UserControl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
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

import jlink.Common.AppUtilsCommon;

/*
 * 
 * @author Pablo Pascual
 */

@WebServlet(name = "CheckUser", urlPatterns = { "/CheckUser" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50, // 50MB
        location = "/")
public class CheckUser extends HttpServlet {

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
        
        String username, password, saved_password, role, sql;
        PreparedStatement st;
        RequestDispatcher rd;

        try{
            username = request.getParameter("username");
            password = request.getParameter("password");
            
            username = username.toLowerCase();
            sql = "SELECT * FROM users WHERE username = ?;";
            st = AppUtilsCommon.prepare_query(sql, new String[] {username});
            ResultSet rs = st.executeQuery();

            if(rs != null && rs.next()){
                role = rs.getString("role");
                saved_password = rs.getString("password");
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                boolean matches = encoder.matches(password, saved_password);

                if(matches){
                    request.getSession().setAttribute("username", username);
                    request.getSession().setAttribute("role", role);
                    // getServletContext().setAttribute("username", username);
                    // getServletContext().setAttribute("role", role);
                    rd = request.getRequestDispatcher("/menu.jsp");
                    rd.include(request, response);
                }else{
                    rd = request.getRequestDispatcher("/login_error.jsp");
                    rd.include(request, response);
                }
            }else{
                    rd = request.getRequestDispatcher("/login_error.jsp");
                    rd.include(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
