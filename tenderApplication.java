package Control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class tenderApplication extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        
        int count = (Integer)Integer.parseInt(request.getParameter("count"));
        
        HttpSession session = request.getSession();
                
        session.setAttribute("count", count);
            
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/tenderApplication.jsp");
        dispatcher.forward(request, response);
        
    }

}
