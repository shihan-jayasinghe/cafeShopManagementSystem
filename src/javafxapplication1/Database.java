/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author SHIHAN
 */
public class Database
{
   public static Connection connect() throws SQLException
   {
      Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe","root","root");
      return con; 
   }
}
