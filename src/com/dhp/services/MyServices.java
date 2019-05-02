package com.dhp.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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


@Path("/services")
public class MyServices {
	private String email_send;
	
	

	@POST
	@Path("/register-admin/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterAdmin(@FormParam("username") String username, @FormParam("name") String name , @FormParam("address") String address , @FormParam("email") String email ,@FormParam("password") String password,@FormParam("repassword") String repassword) throws URISyntaxException {
		if(!password.equals(repassword)) {
			return  Response.temporaryRedirect(new URI("http://localhost/shop/register-admin.php?password=true")).build();
		}
		else
		{
			ResultSet rs = null;
			try {
				rs = new ConnectDB().execute("SELECT id FROM `Customer` WHERE username ='"+username+"'");
				if(rs.next()) { // Tài khoản đã tồn tại
					return  Response.temporaryRedirect(new URI("http://localhost/shop/register-admin.php?exits=true")).build();
				}
				else {
					new ConnectDB().executeUpdate("INSERT INTO Customer(username,password,name,email,address,status) VALUES ('"+username+"','"+password+"','"+name+"','"+email+"','"+address+"',0)");
					return  Response.temporaryRedirect(new URI("http://localhost/shop/list_user.php")).build();

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}
		return null;

	}
	@POST
	@Path("/insert-blog/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response InsertBlog(@FormParam("id_blog") String id_blog , @FormParam("title") String title ,@FormParam("description") String description )
{
	try {
		new ConnectDB().executeUpdate("UPDATE `blog` SET `title`='"+title+"',`description`='"+description+"' WHERE id= "+id_blog );
		return  Response.temporaryRedirect(new URI("http://localhost/shop/edit_blog.php?id="+id_blog+"edit=true")).build();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;

	
}
	
	
	@POST
	@Path("/register-user/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterUser(@FormParam("username") String username, @FormParam("name") String name , @FormParam("address") String address , @FormParam("email") String email ,@FormParam("password") String password,@FormParam("repassword") String repassword) throws URISyntaxException {
		if(!password.equals(repassword)) {
			return  Response.temporaryRedirect(new URI("http://localhost/shop/register-admin.php?password=true")).build();
		}
		else
		{
			ResultSet rs = null;
			try {
				rs = new ConnectDB().execute("SELECT id FROM `Customer` WHERE username ='"+username+"'");
				if(rs.next()) { // Tài khoản đã tồn tại
					return  Response.temporaryRedirect(new URI("http://localhost/shop/register-admin.php?exits=true")).build();
				}
				else {
					new ConnectDB().executeUpdate("INSERT INTO Customer(username,password,name,email,address,status) VALUES ('"+username+"','"+password+"','"+name+"','"+email+"','"+address+"',1)");
					return  Response.temporaryRedirect(new URI("http://localhost/shop/list_user.php")).build();

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}
		return null;

	}
	

	public int checkNumberPhone(int id_phone) {
		ResultSet rs = null;
		int number = 0;
		
		try {
			rs = new ConnectDB().execute("select number from phone where id="+id_phone);
			if(rs.next()) {
				 number = rs.getInt("number");
			}
		}catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		System.out.println("number phone" + number);
		return number;
	}
	
	
	
	public int checkNumberPhoneOnCart (int id_phone) {
		ResultSet rs = null;
		int number = 0;
		
		try {
			rs = new ConnectDB().execute("select count from cart where id_phone = "+ id_phone +" and hethan >"+getCurrentDate());
			System.out.println("select count from cart where id_phone = "+ id_phone +" and hethan > "+getCurrentDate());
			while(rs.next()) {
				number += rs.getInt("count");
			}
		}catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		System.out.println(number);
		return number;
	}

	
	
	@POST
	@Path("/insert-comment")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response InsertComment(@FormParam("yourcomment") String yourcomment,@FormParam("id_user") int id_user ,@FormParam("id_phone") int id_phone ) {
		try { 
		new ConnectDB().executeUpdate("INSERT INTO comment_for_user(description,id_phone,id_user) VALUES ('" 
				 + yourcomment + "," + id_phone  + "," + id_user +");");
			
				return  Response.temporaryRedirect(new URI("http://localhost/shop/product-detail.php?id="+id_phone+"&insert=true")).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

	@POST
	@Path("/insert-brands/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response InsertBrands(@FormParam("name") String name) {
		
		try {
			new ConnectDB().executeUpdate("INSERT INTO brands(name) VALUES ('"+ name +"')");

			return  Response.temporaryRedirect(new URI("http://localhost/shop/insert_brand.php?insert=true")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	@POST
	@Path("/insert-category/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response InsertCategory(@FormParam("name") String name,@FormParam("id_brands") int id_brands) {
		
		try {
			new ConnectDB().executeUpdate("INSERT INTO category(name,id_brands) VALUES ("+name+","+id_brands+")");
			return  Response.temporaryRedirect(new URI("http://localhost/shop/insert_category.php?insert=true")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@GET
	@Path("/xoasanpham/{id}/{id_user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response xoagiohang(@PathParam("id") int id,@PathParam("id_user") int id_user) {
		try {
			new ConnectDB().executeUpdate("DELETE FROM `cart` WHERE id =" + id +" and id_user ="+id_user);
			return Response.temporaryRedirect(new URI("http://localhost/shop/cart.php?delete=success")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	@GET
	@Path("/thanhtoan/{total}/{id_user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response ThanhToan(@PathParam("total") int total,@PathParam("id_user") int id_user) throws URISyntaxException
	{
		ResultSet rs = null;
		UpdateBill(total,id_user);
		
		try {
			rs = new ConnectDB().execute("select id_phone,count from cart where id_user = "+id_user);
			if (rs.next())
			{
				int count = rs.getInt("count");
				int id_phone = rs.getInt("id_phone");
				UpdatePhone(count,id_phone);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		deleteCart(id_user);
		return  Response.temporaryRedirect(new URI("http://localhost/shop/index.php?thanhtoan=true")).build();		
	}
	
	private void UpdateBill( int total , int id_user) {
		new ConnectDB().executeUpdate("INSERT INTO bill(total_price,id_user) VALUES ("+total+","+id_user+")");
	}
	private void deleteCart(int id_user) {
		new ConnectDB().executeUpdate("DELETE FROM `cart` WHERE id_user="+id_user);
	}
	private void UpdatePhone(int count,int id_phone) {
		int oldnumber = checkNumberPhone(id_phone);
		int newnumber = oldnumber - count;
		
		new ConnectDB().executeUpdate("UPDATE `phone` SET `number`="+newnumber+" WHERE id="+id_phone);
	}
	private int countNumberPhoneInUser(int id_user,int id_phone)
	{
		ResultSet rs = null;
		int number = 0;
		
		try {
			rs = new ConnectDB().execute("select count from phone where id_user="+id_user+"and id_phone="+id_phone);
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
	
	
	
	@GET
	@Path("/themgiohang/{id}/{id_phone}/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response themgiohang(@PathParam("id") Integer id, @PathParam("id_phone") Integer id_phone) throws URISyntaxException
	{
		
		if ( checkNumberPhone(id_phone) > checkNumberPhoneOnCart(id_phone) ) {
	
			boolean t;
			ResultSet rs = null;
			try {
				rs = new ConnectDB().execute("SELECT id,count FROM `cart` WHERE id_phone ="+id_phone+ " and id_user = "+id);
					if (rs.next())
					{						
						t = new ConnectDB().executeUpdate("UPDATE cart SET count = "+ (rs.getInt("count") + 1) + ",hethan='"+getNow2()+"' where id_phone = "+ id_phone + " and id_user = "+id);
							return Response.temporaryRedirect(new URI("http://localhost/shop/index.php?insert=success")).build();
						}
						else {
							t = new ConnectDB().executeUpdate("INSERT INTO cart (id_user, id_phone,count,hethan) VALUES ("+id+","+id_phone+",1,'"+getNow2()+"');");
						}
						}
						catch (Exception e) {
								t = false;
							}
						if( t ==true ) {
							return Response.temporaryRedirect(new URI("http://localhost/shop/index.php?insert=success")).build();
						}
						else
						{
							return  Response.temporaryRedirect(new URI("http://localhost/shop/index.php?insert=fail")).build();
						}
		}else {
			return  Response.temporaryRedirect(new URI("http://localhost/shop/index.php?hethang=true")).build();

		}
						
			
		
	}
	
	
	
	@GET
	@Path("/show-hide-comment/{id}/{status}/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response ShowHideComment(@PathParam("id") int id , @PathParam("status") int status) {
		try {
			if(status == 0 ) {
				new ConnectDB().executeUpdate("UPDATE comment_for_user SET status= 1  WHERE id =" + id);

			}else {
				new ConnectDB().executeUpdate("UPDATE comment_for_user SET status= 0  WHERE id =" + id);

			}

			return  Response.temporaryRedirect(new URI("http://localhost/shop/list_phone.php")).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void  UpdateStatus(String table_name,int status,int id) {
		new ConnectDB()
.executeUpdate("UPDATE "+table_name+" SET status = "+ status +"where id="+id);
}
	
	@POST
	@Path("/sign-in-secure/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signIn (@FormParam("username") String username, @FormParam("password") String password) throws URISyntaxException
	{
		ResultSet rs = null;
		try {
			
			rs = new ConnectDB().execute("select * from Customer");
			while (rs.next())
			{
				if(rs.getString("username").equals(username)&&rs.getString("password").equals(password))
				{
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		return false;
	}
	
	
	
	
	@POST
	@Path("/sign-up/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signUp(@FormParam("username") String username ,
			@FormParam("password") String password ,
			@FormParam("familyname") String familyname ,
			@FormParam("email") String email , 
			@FormParam("address") String address) throws URISyntaxException
	{

		ResultSet rs = null;
		try {
			rs = new ConnectDB().execute("select id from Customer where username="+username);
			if ( rs.next() )
			{
				return false;
					
			}
			else
			{
				new ConnectDB().executeUpdate("INSERT INTO `Customer` ( `username`, `password`, `name`, `email`, `address`) VALUES ( '"+ username +"', '"+password +"', '"+familyname +"', '"+email+"', '"+address+"');");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	
	// forgot password
	
	@POST
	@Path("/forgot-pass")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgotPassword(@FormParam("email") String email) throws SQLException {
		int numberRandom = CreateRamdonNumber();
		 String text = numberRandom+"";
		boolean t = new ConnectDB().executeUpdate("UPDATE `Customer` SET `password`="+numberRandom+"  WHERE email='"+email+"'");
		emailHandler(email,numberRandom+"");
		if( t ) {
			try {
				return  Response.temporaryRedirect(new URI("http://localhost/shop/login.php")).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
		{
			try {
				return  Response.temporaryRedirect(new URI("http://localhost/shop/forgot-password.php")).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
	
	
	
	@GET
	@Path("/remove_user/{id}/{name_database}/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(@PathParam("id") int id,@PathParam("name_database") String name_database) throws URISyntaxException {
	
		try 
		{
			 
			 String text = "";
			 switch (name_database) {
			case "blog":
				 text = "http://localhost/shop/list_blog.php?delete=success";
				 break;
			case "Customer":
				text = "http://localhost/shop/list_user.php?delete=success";
				break;
			case "brands":
				text = "http://localhost/shop/list_brand.php?delete=success";
				deleteBrands(id);
				break;
			case "category":
				text = "http://localhost/shop/list_category.php?delete=success";
				deleteCategory(id);
				break;
			case "phone":
				text = "http://localhost/shop/list_phone.php?delete=success";
				break;
				
			default:
				break;
			}
			 
			 new ConnectDB().executeUpdate("DELETE FROM "+name_database+" WHERE id="+id);
			 System.out.println("DELETE FROM "+name_database+" WHERE id="+id);
			return Response.temporaryRedirect(new URI(text)).build();
		}catch(Exception e) {
			return Response.temporaryRedirect(new URI("http://localhost/shop/list_user.php?delete=fail")).build();

		}	
}
	
	private void deleteBrands( int id_brands  )
	{
		new ConnectDB().executeUpdate("DELETE FROM phone WHERE id_brands ="+id_brands);
		new ConnectDB().executeUpdate("DELETE FROM category WHERE id_brands ="+id_brands);
	}
	private void  deleteCategory( int id_category )
	{
		new ConnectDB().executeUpdate("DELETE FROM phone WHERE id_category ="+id_category);
	}
	private void deleteUser( int id_user ) {
		new ConnectDB().executeUpdate("DELETE FROM cart WHERE id_user ="+id_user);
		new ConnectDB().executeUpdate("DELETE FROM bill WHERE id_brands ="+id_user);
	}
	
	
	
	
	
	
	
	private void emailHandler(String email, String text)
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
			message.setSubject("Thay đổi mật khẩu " + getNow());
			message.setText("Dear " + email +" Mã của bạn là  " + text  );

			Transport.send(message);


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
	private String getNow2() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);
        
        Date currentDatePlusOne = c.getTime();
        
		return dateFormat.format(currentDatePlusOne).toString();
        



	}
	
	// create ramdom number 
	public int CreateRamdonNumber() {
		int x =  (int) Math.round( Math.random() *(10000-1000 ) +1000 );
	
	    return x;
	}
	
	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	


}
