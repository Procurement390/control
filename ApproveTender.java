package Control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author OK - O
 */
public class ApproveTender extends HttpServlet {

    @Resource(name = "jdbc/Procurement")
    private DataSource datasource;

    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String message = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        int ID = (Integer) Integer.parseInt(request.getParameter("Id"));
        String tenderNumber = request.getParameter("tenderNumber");
        String query1 = "update bid set status = ? where tenderNumber = ?";
        String query2 = "update bid set status = ? where ID = ?";

        try {

            connection = datasource.getConnection();
            
            PreparedStatement ps1 = connection.prepareStatement(query1);
            ps1.setString(1, "rejected");
            ps1.setString(2, tenderNumber);

            PreparedStatement ps2 = connection.prepareStatement(query2);
            ps2.setString(1, "accepted");
            ps2.setInt(2, ID);

            if (ps1.executeUpdate() > 0) {
                if (ps2.executeUpdate() > 0) {
                    message = "Application approved";
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ApproveTender.class.getName()).log(Level.SEVERE, null, ex);
        }

        getServletContext().setAttribute("approveMessage", message);

        request.setAttribute("tender", tenderNumber);
        request.getRequestDispatcher("/TenderApplicants?tender=" + tenderNumber).forward(request, response);

    }
}
