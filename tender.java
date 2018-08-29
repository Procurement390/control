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

public class tender extends HttpServlet {

    @Resource(name = "jdbc/Procurement")
    private DataSource datasource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        if(session != null)
        {
          
              String query = "select * from tenders";

        try {

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList<Tender> tenders = new ArrayList<Tender>();
            connection = datasource.getConnection();
            int count = 0;

            PrintWriter out = response.getWriter();

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                Tender Atender = new Tender();
                Atender.setTenderNumber(rs.getString("number"));
                Atender.setTenderDescription(rs.getString("description"));
                Atender.setClosingDate(rs.getString("closingdate"));
                Atender.setClosingTime(rs.getString("closingtime"));
                Atender.setStatus(rs.getString("status"));

                Atender.setCount(count);
                count = count + 1;

                tenders.add(Atender);
            }

            session.setAttribute("tenders", tenders);

            User user = (User) session.getAttribute("user");
            
            if (user.getRole().equals("DCPO")) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/DCPOHomepage.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/supplierHomepage.jsp");
                dispatcher.forward(request, response);
            }


        } catch (SQLException ex) {
            Logger.getLogger(Tender.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        }
        
        else
        {
            response.sendRedirect("login.jsp");
        }

    }

}
