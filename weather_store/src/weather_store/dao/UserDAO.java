package weather_store.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import weather_store.dto.UserDTO;
import weather_store.util.DBUtil;

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
			ps = con.prepareStatement("INSERT INTO person (id, pw, name, addr) VALUES (?, ?, ?, ?)");
			ps.setString(1, user.getId());
			ps.setString(2, user.getPw());
			ps.setString(3, user.getName());
			ps.setString(4, user.getAddr());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e + "=>userInsert fail");
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
	 * 
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
			ps = con.prepareStatement("SELECT name FROM person WHERE id = ? and pw = ?");
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();
			while (rs.next()) {
				uname = rs.getString("name");
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
	public ArrayList<UserDTO> allUsers() {
		ArrayList<UserDTO> list = new ArrayList<UserDTO>(); // list�� �ڷḦ ��� �ָӴ� ����
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
