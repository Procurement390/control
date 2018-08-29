package Control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Business.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import javax.annotation.Resource;
import javax.sql.DataSource;
/**
 *
 * @author andy
 */
public class CreateTender extends HttpServlet {

    private static final long serialVersionUID = 1L;

    
    
    @Resource(name = "jdbc/Procurement")
    private DataSource datasource;
    
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        
        response.setContentType("text/html; charset=UTF-8");
        
        String description = request.getParameter("description");
        
        
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        
        Date  closingdate = null;
        
        try { 
            closingdate = formater.parse(request.getParameter("closingdate"));
        } catch (ParseException ex) {
            Logger.getLogger(CreateTender.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        String number = getTenderNumber();
               
        
        
        
        //SQL
        String query = "insert into tenders values(?,?,?,?,?)";
        String createTenderMsg = "";
        String url;
        
        try{
            
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList<Tender> tenders = new ArrayList<Tender>();
            connection = datasource.getConnection();
          
            ps = connection.prepareStatement(query);
            ps.setString(1, number);
            ps.setString(2, description);
            ps.setDate(3, new java.sql.Date(closingdate.getTime()));
            ps.setString(4, "00:00:00");
            ps.setString(5, "active");
                
            
            if(ps.executeUpdate() > 0){
                createTenderMsg = "tender created successfuly";
                url="/tender";
            }
            else{
                createTenderMsg= "Tender creation failed";
                url ="/tenderCreation.jsp";
            }
            
            
            getServletContext().setAttribute("CTMsg", createTenderMsg);
            request.getRequestDispatcher(url).forward(request, response);
            
                
        } catch (SQLException ex) {
            Logger.getLogger(CreateTender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public String getTenderNumber(){
        
        Random rand = new Random();
        String tenderNo = "EU-";
         char random[] = new char[]
       {'x', 'y', 'z', 'a', 'b', 'c',  'e', 'f', 
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        
         for(int i = 0; i<7; i++){
             tenderNo += random[rand.nextInt(random.length)];
         }
         
         return tenderNo.toUpperCase(Locale.US);
    }

}