package Control;

import Business.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Apply extends HttpServlet {

    @Resource(name = "jdbc/Procurement")
    private DataSource datasource;

    String url = "";
    String message = "";
    User user = new User();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //set content type
        response.setContentType("text/html");

        //get print writer
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        int count = (int) session.getAttribute("count");
        ArrayList<Tender> tenders = (ArrayList<Tender>) session.getAttribute("tenders");
        User user = (User) session.getAttribute("user");
        int amount = (Integer) Integer.parseInt(request.getParameter("amount"));
        String status = "pending";
        
        Bid bid = new Bid();
        bid.setTenderNumber(tenders.get(count).getTenderNumber());
        bid.setUsername(user.getUsername());
        bid.setAmount(amount);
        bid.setStatus("pending");
        
    
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = datasource.getConnection();
            //Step 2: create a sql statement string 
            String query = "insert into bid ( tenderNumber,username,amount,status) values (?,?,?,?);";

            ps = connection.prepareStatement(query);
            ps.setString(1, bid.getTenderNumber());
            ps.setString(2, bid.getUsername());
            ps.setInt(3, bid.getAmount());
            ps.setString(4, bid.getStatus());

            if (ps.executeUpdate() > 0) {
                url = "/supplierHomepage.jsp";
                message = "The tender application is successful";
            } else {
                url = "/tenderApplication.jsp";
                message = "The tender application failed. \n Please try again";
            }

        } catch (SQLException ex) {
            Logger.getLogger(Apply.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Apply.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        request.setAttribute("message", message);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

}
