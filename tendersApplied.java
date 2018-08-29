
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

public class tendersApplied extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        
        
            User user = (User) session.getAttribute("user");

                //set content type
                response.setContentType("text/html");
                
                //get printwriter
                PrintWriter out = response.getWriter();
                
                Connection connection = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                ArrayList<tenderApplied> tenderA = new ArrayList<tenderApplied>();
                
                try {
                    connection = datasource.getConnection();
                    
                    //Step 2: create a sql statement string
                    String query = "select bid.tenderNumber,tenders.description,"
                            + "tenders.closingdate,tenders.closingtime,bid.amount,"
                            + "bid.status,bid.username from tenders INNER JOIN bid "
                            + "on tenders.number=bid.tenderNumber";
                    
                    ps = connection.prepareStatement(query);
                    
                    //Step 3: Execute sql query
                    rs = ps.executeQuery(query);
                    
                    while (rs.next()) {
                        if (user.getUsername().equals(rs.getString("username")))  {
                            tenderApplied tenderApp = new tenderApplied();
                            
                            tenderApp.setTenderNumber(rs.getString("tenderNumber"));
                            tenderApp.setDescription(rs.getString("description"));
                            tenderApp.setClosingDate(rs.getString("closingdate"));
                            tenderApp.setClosingTime(rs.getString("closingtime"));
                            tenderApp.setAmount(rs.getString("amount"));
                            tenderApp.setStatus(rs.getString("status"));
                            
                            tenderA.add(tenderApp);
                        }
                        
                    }
                    
                    //Step 4: Process the result set
                } catch (SQLException ex) {
            Logger.getLogger(tendersApplied.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
                    try {
                        connection.close();
                        ps.close();
                        rs.close();
                    } catch (SQLException ex) { 
                Logger.getLogger(tendersApplied.class.getName()).log(Level.SEVERE, null, ex);
            } 
                }
                
                session.setAttribute("tenderA", tenderA);
                url = "/tendersApplied.jsp";
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
                dispatcher.forward(request, response);
            }
    }
    
