package com.app.util;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;

public class DBUtil {
 
    public static Connection getConnection() throws SQLException {
        Connection connection=null;
        try{
        	
            if(connection== null) { 
                javax.naming.Context context = new InitialContext();
                if(context == null ) 
                    throw new Exception("Boom - No Context");
                DataSource dataSource = 
                        (DataSource)context.lookup(
                           "java:comp/env/jdbc/workshop_db");

                  if (dataSource != null)
                    connection = dataSource.getConnection();
            
            }
            return connection;
        } catch(SQLException e){
            throw new SQLException(e.toString());
        } catch(Exception e){
            throw new SQLException(e.toString());
        }
    }

    @SuppressWarnings("unused")
   	public static void closeResource(Connection con,PreparedStatement pst,ResultSet rst){
       if (rst != null)
   		try {
   			rst.close();
   		} catch (SQLException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
               
       if (pst != null) {
               try {
               	pst.close();
               } catch (SQLException e) {
               	e.printStackTrace();
               }
       }
       if (con != null) {
               try {
               	con.close();
               } catch (SQLException e) {
               	e.printStackTrace();
               }
       }
     }
 
}