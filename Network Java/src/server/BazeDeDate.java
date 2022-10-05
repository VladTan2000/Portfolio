package server;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class BazeDeDate {
	   static final String DB_URL = "jdbc:sqlite:userdata.db";
	   static final String USER = "guest";
	   static final String PASS = "guest123";
	   static final String QUERY = "SELECT * FROM clienti";
	private static final String SQL_INSERT = "INSERT INTO clienti (id, prenume, nume,varsta) VALUES (?,?,?,?)";
	private static final String SQL_WHERE = "SELECT * FROM clienti WHERE varsta > ?";
	private Socket server;
	private BufferedReader reader;
	private PrintWriter writer;
	public BazeDeDate(Socket server)throws IOException {
		this.server = server;
		this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
		this.writer = new PrintWriter(server.getOutputStream(),true);
	}
	   
	   public static void creareTable() {
		   try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			         Statement stmt = conn.createStatement();
			      ) {		      
			          String sql = "CREATE TABLE clienti " +
			                   "(id INTEGER not NULL, " +
			                   " prenume VARCHAR(255), " +
			                   " nume VARCHAR(255), " +
			                   " varsta INTEGER, " +
			                   " PRIMARY KEY ( id ))"; 

			         stmt.executeUpdate(sql);
			         System.out.println("Created table in given database...");   	  
			      } catch (SQLException e) {
			         e.printStackTrace();
			      } 
	   }
	   
	   public void inserareTable(int id, String first, String last, int age) {
		   try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);
			      ) {

			         // Execute a query
			        preparedStatement.setInt(1,id);
			   preparedStatement.setString(2,first);
			   preparedStatement.setString(3,last);
			   preparedStatement.setInt(4,age);
			   preparedStatement.executeUpdate();
			   this.writer.println("Date adaugate");
			      } catch (SQLException e) {
			         e.printStackTrace();
			      } 
	   }
	   
	   public void afisareTable() throws SQLException {
		   String comanda="";
	   	try {
			   Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery(QUERY);
			   while(rs.next()){
				   //Display values
				   comanda=  "ID: "+Integer.toString(rs.getInt("id")) ;
				   comanda+=" Varsta "+Integer.toString(rs.getInt("varsta"));
				   comanda+=" Prenume " +rs.getString("prenume");
                   comanda+=" Nume "+rs.getString("nume");
                   this.writer.println(comanda);
		   }
			         }
			       catch (SQLException e) {
			         e.printStackTrace();
			      }

	   }

	   public void afisareWhereTable(int varsta) throws Exception{
			String comanda="";
			try{
				Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				PreparedStatement preparedStatement=conn.prepareStatement(SQL_WHERE);
				preparedStatement.setInt(1,varsta);
				ResultSet rs=preparedStatement.executeQuery();
				while (rs.next()){
					comanda=  "ID: "+Integer.toString(rs.getInt("id")) ;
					comanda+=" Varsta "+Integer.toString(rs.getInt("varsta"));
					comanda+=" Prenume " +rs.getString("prenume");
					comanda+=" Nume "+rs.getString("nume");
					this.writer.println(comanda);
				}

					}

			catch (SQLException throwables) {
				throwables.printStackTrace();
			}
	   }


}
