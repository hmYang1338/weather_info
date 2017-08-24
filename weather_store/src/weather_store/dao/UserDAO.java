package weather_store.dao;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import weather_store.dto.UserDTO;
import weather_store.util.DBUtil;
/**
 * @author hyunmoYang
 */
public class UserDAO {
	private static UserDAO userDAO;

	private UserDAO() {
	}

	public static UserDAO getInstance() {
		if (userDAO == null) {
			userDAO = new UserDAO();
		}
		return userDAO;
	}

	/**
	 * ȸ������
	 * 
	 * @param user
	 * @return result
	 */
	public int userInsert(UserDTO user) {
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();
		PreparedStatement ps = null;
		int result = 0;

		try {
			ps = con.prepareStatement("INSERT INTO person (id, pw, name, addr, salt, passwd) VALUES (?, ?, ?, ?, ?, ?)");
			
			String password = user.getPw();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(password.getBytes("UTF-8"));
			byte[] bytes = md.digest();
			
			ps.setString(1, user.getId());
			ps.setBytes(2, bytes);
			ps.setString(3, user.getName());
			ps.setString(4, user.getAddr());
			ps.setBytes(5, bytes);
			ps.setString(6, user.getPasswd());
			result = ps.executeUpdate();
			
			for(byte b : bytes) {
				System.out.printf("%2X ", b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e + "=>userInsert fail");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			db.close(con, ps);
		}
		return result;
		
		
	} // end of userInsert()

	/**
	 * ȸ�� Ż��
	 * 
	 * @param id
	 * @return result
	 */
	public int userDelete(String id) {
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();
		PreparedStatement ps = null;
		int result = 0;

		try {
			ps = con.prepareStatement("DELETE person WHERE id = ?");
			ps.setString(1, id.trim());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e + "=> userDelete fail");
		} finally {
			db.close(con, ps);
		}
		return result;
	} // end of userDelete()

	/**
	 * �α���
	 * @param id
	 * @param pw
	 * @return uname
	 */
	public String userLogin(String id, String pw) {
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();
		ResultSet rs = null; // select�ÿ� �߰��ؾ� �� �κ�
		PreparedStatement ps = null;
		String uname = "";

		try {
			ps = con.prepareStatement("SELECT name, pw, salt FROM person WHERE id = ?");

			ps.setString(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				byte[] salt = rs.getBytes("salt");		// DB���� ������ salt
//				byte[] passwd = rs.getBytes("pw");		// DB���� ������ ��й�ȣ
				String passwd = rs.getString("pw");
				
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(pw.getBytes("UTF-8"));
				byte[] bytes = md.digest();
				
				System.out.println("db =>");		// db
//				for(byte b : passwd.getBytes()) {
//					System.out.printf("%2X ", b);
//				}
				System.out.println(passwd);
				System.out.println();
				
				System.out.println("input =>");		// �Է�
				for(byte b : bytes) {
					System.out.printf("%2X ", b);
				}
				
				boolean pass = MessageDigest.isEqual(passwd.getBytes(), bytes); //true,false
				System.out.println(pass);
				
				
				if(pass == true) {
					uname = rs.getString("name");
				}
				else {
					System.out.println("���̵�/��й�ȣ�� �߸� �Է��ϼ̽��ϴ�.");
				}
			}
			else {
				System.out.println("id ����");
			}
		} catch (SQLException se) {
			System.out.println(se + "=> userLogin fail");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close(rs, con, ps);
		}
		return uname;
	} // end of userLogin()

	/**
	 * ������ - ȸ�����
	 * 
	 * @return list
	 */
	public List<UserDTO> allUsers() {
		List<UserDTO> list = new ArrayList<UserDTO>(); // list�� �ڷḦ ��� �ָӴ� ����
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();
		ResultSet rs = null; // select�ÿ� �߰��ؾ� �� �κ�
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("SELECT id, name, addr FROM person");
			rs = ps.executeQuery();
			while (rs.next()) { // while�� rs�� ���� Ŀ���� ���� �ο� �ϳ��� �д� �Լ�
								// (���̻� ���� ������ false ��ȯ)
				// HashMap<> hm = new HashMap(); // HashMap�� ���� �ڷ���� �ѹ��� ���� ���� ����
				UserDTO user = new UserDTO();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setAddr(rs.getString("addr"));
				list.add(user);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close(rs, con, ps);
		}
		return list;
	} // end of allUsers()

} // end of class
