package com.dhp.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.model.ConnectDB;

public class test {
	@GET
	@Path("/addProduct/{id}/{id_book}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(@PathParam("id") int id, @PathParam("id_book") int id_book) {
		ResultSet rs = null;
		try {
			rs = new ConnectDB().execute("select id_user, id_book, count from cart where id_book = "+id_book+ " and id_user = "+id);
				if (rs.next())
				{
					if ( checkNumberBookOnCart(id_book) <  checkNumberBook(id_book) ) {
					 new ConnectDB().executeUpdate("UPDATE cart SET count = "+ (rs.getInt("count") + 1) + " where id_book = "+ id_book + " and id_user = "+id);	
					
					 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=sucess")).build();
					}
					else
					{
						 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=hethang")).build();
					}
				}
				else
				{
					 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=hethang")).build();
				}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	
	@GET
	@Path("/removeAllProduct/{id}/{id_book}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeAllProduct(@PathParam("id") int id, @PathParam("id_book") int id_book) {
		ResultSet rs = null;
		try {
			rs = new ConnectDB().execute("select id_user, id_book, count from cart where id_book = "+id_book+ " and id_user = "+id);
			
			if (rs.next())
			{
				 new ConnectDB().executeUpdate("UPDATE cart SET count = 0  where id_book = "+ id_book + " and id_user = "+id);	
				 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=sucess")).build();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	
	public int checkNumberBook(int id_book) {
		ResultSet rs = null;
		int number = 0;
		
		try {
			rs = new ConnectDB().execute("select number from book where id="+id_book);
			if(rs.next()) {
				 number = rs.getInt("number");
			}
		}catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return number;
	}
	
	
	public int checkNumberBookOnCart(int id_book) {
		ResultSet rs = null;
		int number = 0;
		
		try {
			
			rs = new ConnectDB().execute("select count from cart where id_book="+id_book);
			System.out.println("select count from cart where id_book="+id_book);
			while(rs.next()) {
				 number += rs.getInt("count");
			}
		}catch (Exception e) {
			e.printStackTrace();
return 0;
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		System.out.println(number);
		return number;
	}
	
	
	@GET
	@Path("/removeProduct/{id}/{id_book}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeProduct(@PathParam("id") int id, @PathParam("id_book") int id_book) {
		ResultSet rs = null;
	
		try {
			rs = new ConnectDB().execute("select id_user, id_book, count from cart where id_book = "+id_book+ " and id_user = "+id);
			
			if (rs.next())
			{
				if(rs.getInt("count") > 0) {
			
				 new ConnectDB().executeUpdate("UPDATE cart SET count = "+ (rs.getInt("count") - 1) + " where id_book = "+ id_book + " and id_user = "+id);	
				 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=sucess")).build();
				}else {
					 return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/cart.php?action=thatbai")).build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return null;
	}
	
	
	@POST
	@Path("/upPoint/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update_with_noreset(@FormParam("id") int id, @FormParam("total") int total) throws URISyntaxException 
	{
		congdiem(id,total);
		cartToOrder(id);
		return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/hoadondiemthuong.php?id="+id)).build();
	}
	
	
	@POST
	@Path("/upnoPoint/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update_with_reset(@FormParam("id") int id, @FormParam("total") int total) throws URISyntaxException 
	{
		
		congdiem(id,total);
		ResetPoint(id);
		cartToOrder(id);

		return  Response.temporaryRedirect(new URI("http://localhost:8081/bai3_htdocs/updateCartToOrderdiemthuong.php?id="+id)).build();
	}
	
	
	private void cartToOrder(int id) {
		ResultSet rs = null;
		
		try {
			rs = new ConnectDB().execute("SELECT * FROM cart where id_user = "+id);
			
			if (rs.next())
			{
				int id_book = rs.getInt("id_book");
				int id_user = rs.getInt("id_user");
				int count = rs.getInt("count");
				
				updateNumberSach(id_book,count);// da mua xac nhan xoa trong cart 
				
				 new ConnectDB().executeUpdate("INSERT INTO `order`(`id_user`, `id_book`, `count`) VALUES ("+id_user +","+ id_book +","+ count +")");
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
	}
	
	
	
	
	private void ResetPoint(int id)
	{
		new ConnectDB().executeUpdate("UPDATE users SET point=0 WHERE id= "+ id);
	}
	
	
	private void congdiem(int id, int total) {
		ResultSet rs = null;
		int newpoint;
		int point ;
		try {
			rs = new ConnectDB().execute("select * from users where id="+ id);
			while (rs.next())
			{
				point = rs.getInt("point");
				//email_send = rs.getString("email");
				
				// lay email
				//emailHandler(email_send,id+"");
				newpoint = point + total/100;
				new ConnectDB().executeUpdate("update users set point = "+ newpoint + " where id="+id);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}

	private void xoaSanPham(int id_user) {
			new ConnectDB().executeUpdate("delete from cart where id_user = " + id_user);
		}
	
	
	

	
	
	@GET
	@Path("/themgiohang/{id}/{id_book}/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response themgiohang(@PathParam("id") Integer id, @PathParam("id_book") Integer id_book) throws URISyntaxException
	{
		if()
		
			
		
	}
	
	
	// them sach 
	private boolean updateNumberSach(int id_book,int value) {
		int tong = 0;
		ResultSet r = null;
		boolean t;
		try {
				r = new ConnectDB().execute("select number from book where id = " + id_book);
				if(r.next()) {
					tong = Integer.parseInt(r.getString(1));
				}
				tong = tong - value;
				new ConnectDB().executeUpdate("UPDATE book SET number= "+ tong +" WHERE id = " + id_book);
				t = true;
		} catch (Exception e) {
			e.printStackTrace();
			t = false;
		} finally {
			try { if (r != null) r.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return t;
	}
	
	

	
	
	@GET
	@Path("/diemthuong/{id}/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String diemthuong(@PathParam("id") String id)
	{
		String diem = "";
		ResultSet r = null;
		try {
				r = new ConnectDB().execute("select point from users where id = " + id);
				if(r.next()) {
					diem = r.getString(1);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (r != null) r.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return diem;
	}
	
	@GET
	@Path("/sendmail/{email}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void SendMail(@PathParam("email") String email,@PathParam("id") String id) {
		
		emailHandler(email, id);
	}
	public int countsp(int id) {
		ResultSet rs = null;
		int c = 0;
		try {
			rs = new ConnectDB().execute("select count(id_user) from cart where id_user =  "+id);
			if (rs.next())
			{
				c = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return c;
	}
	private void emailHandler(String email, String id)
	{
		final String username = "dattranjd@gmail.com";
		final String password = "dat01678797792";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Thanh toán sách" + getNow());
			message.setText("Dear " + email +" Bạn đã thanh toán xong. Cám ơn bạn " );

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	private String getNow()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    return formatter.format(date).toString();
	}
	private String getNow1()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    return formatter.format(date).toString();
	}
	
}
