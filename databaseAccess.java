/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author JOHNTEZ
 */
public class databaseAccess {
            static String url = "jdbc:mysql://localhost:3306/procurement";
            static String username = "root";
            static String password = "";
            static Connection connection=null;
            static PreparedStatement stmt=null;
            static ResultSet rs=null;
public static Connection createConnection(){

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
                   
            System.out.println("successful connection");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
                return connection;
       
}  
}
