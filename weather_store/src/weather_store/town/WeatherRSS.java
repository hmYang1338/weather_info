package weather_store.town;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import weather_store.util.DBUtil;

public class WeatherRSS {

	// http://www.kma.go.kr/wid/queryDFS.jsp?gridx=59&gridy=125
	private String rssFeed = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%s&gridy=%s"; // �ּ�
	private boolean bFileWrite = false; // ���Ϸ� ������� �����ϴ� ����.
	private PrintStream out;

	public WeatherRSS() {
	}

	public WeatherRSS(boolean isFileWrite) {
		try {
			if (isFileWrite) {
				this.bFileWrite = true;
				String filepath = "file//TBL_WFORECAST_XY_GROUP_DATA.txt";
				out = new PrintStream(new FileOutputStream(filepath));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeOutputStream() { // ��� ��Ʈ�� �ݱ�
		if (out != null) {
			out.close();
		}
	}

	/**
	 * �׽�Ʈ�� ���� ���θ޼ҵ�
	 * 
	 * @throws SQLException
	 */
	public static void main(String[] args) throws Exception {

		WeatherRSS r = new WeatherRSS();

		r.getTownForecast("59", "125"); // ���û���κ��� �ش� ����(X,Y��ǥ)�� ���� ���� ��������

		// ���û���κ��� ��� ���� ���� ������������
		List<List<Map<String, String>>> result = r.getAllTownForecast();

		r.insertData(result); // ���û���κ��� ������ ������ DB�� ����. (���� ����� �ѹ�!?)
		r.updateData(result); // ���û���κ��� ������ ������ DB�� ������Ʈ
		Map<String, String> d = r.getTownForecastFromDB("59", "125"); // DB���� �ش� ����(X,Y ��ǥ)�� ���� ���� ��������.
		System.out.println(d);

		System.out.println("�۾� �Ϸ�");

	}

	/**
	 * ���û���κ��� ��� ���� ���� ������ ��������. :
	 */
	public List<List<Map<String, String>>> getAllTownForecast() {
		DBUtil db = DBUtil.getInstance();
		Connection conn = db.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		// ���� X.Y ��ǥ �׷� ����Ʈ ��ȸ
		// String sql = "select * from TBL_WFORECAST_XY_GROUP ";
		String sql = "select * from TBL_WFORECAST_XY_GROUP where rownum<10";

		// DB�� ���������� �ѹ� �Է��� �Ŀ��� ��ü ���� ���� ��ü����
		// select x, y from TBL_WFORECAST_DATA group by x, y ;

		List<List<Map<String, String>>> result = new ArrayList<>();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String x = rs.getString("x");
				String y = rs.getString("y");
				// xy��ǥ�� �ش��ϴ� ���� ���� ����Ʈ ��������
				List<Map<String, String>> point = getTownForecast(x, y);
				result.add(point);
			}

			System.out.println("ũ��:" + result.size());

		} catch (Exception e) {

		} finally {

			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			db.close(rs, conn, stmt); // Connection ���� ����.
			closeOutputStream(); // ���� ��� �ݱ�.
		}

		return result;
	}

	/**
	 * ���� ���� DB�Է� : TBL_WFORECAST_DATA
	 */
	public void insertData(List<List<Map<String, String>>> result) {
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();

		PreparedStatement pstmt = null;

		String sql = "INSERT INTO TBL_WFORECAST_DATA (X,Y,HOUR,DATA_SEQ,DAY,TEMP,TMX,TMN,"
				+ "SKY,PTY,WFKOR,WFEN,POP,REH,WS,WD,WDKOR,WDEN,R12,S12,R06,S06)	VALUES "
				+ "(:x,:y,:HOUR, :DATA_SEQ, :day, :TEMP,:TMX,:TMN,:SKY,:PTY,:WFKOR,:WFEN,:POP,:REH,:WS,:WD,:WDKOR,:WDEN,"
				+ ":R12,:S12,:R06,:S06)";

		try {

			pstmt = con.prepareStatement(sql);

			// {x=144, y=123, seq=7, hour=24, day=1, temp=22.0, tmx=24.0, tmn=20.0, sky=4,
			// pty=0,
			// wfKor=�帲, wfEn=Cloudy, pop=30, r12=0.0, s12=0.0, ws=3.0, wd=1, wdKor=�ϵ�,
			// wdEn=NE,
			// reh=95, r06=-0.0, s06=0.0}

			for (int i = 0; i < result.size(); i++) {

				List<Map<String, String>> list = result.get(i);

				for (Map<String, String> map : list) {
					System.out.println(i + ". " + map);
					pstmt.setString(1, map.get("x"));
					pstmt.setString(2, map.get("y"));
					pstmt.setString(3, map.get("hour"));
					pstmt.setString(4, map.get("seq"));
					pstmt.setString(5, map.get("day"));
					pstmt.setString(6, map.get("temp"));
					pstmt.setString(7, map.get("tmx"));
					pstmt.setString(8, map.get("tmn"));
					pstmt.setString(9, map.get("sky"));
					pstmt.setString(10, map.get("pty"));
					pstmt.setString(11, map.get("wfKor"));
					pstmt.setString(12, map.get("wfEn"));
					pstmt.setString(13, map.get("pop"));
					pstmt.setString(14, map.get("reh"));
					pstmt.setString(15, map.get("ws"));
					pstmt.setString(16, map.get("wd"));
					pstmt.setString(17, map.get("wdKor"));
					pstmt.setString(18, map.get("wdEn"));
					pstmt.setString(19, map.get("r12"));
					pstmt.setString(20, map.get("s12"));
					pstmt.setString(21, map.get("r06"));
					pstmt.setString(22, map.get("s06"));

					pstmt.executeUpdate(); // ����.
				}
			}

		} catch (Exception e) {

		} finally {
			db.close(con, pstmt); // Connection ���� ����.
		}
	}

	/**
	 * �������� ������Ʈ : TBL_WFORECAST_DATA
	 */
	public void updateData(List<List<Map<String, String>>> result) {
		DBUtil db = DBUtil.getInstance();
		Connection con = db.getConnection();

		PreparedStatement pstmt = null;

		String sql = "UPDATE TBL_WFORECAST_DATA SET ";
		sql += "TEMP      = :TEMP";
		sql += ",TMX      = :TMX";
		sql += ",TMN      = :TMN";
		sql += ",SKY      = :SKY";
		sql += ",PTY      = :PTY";
		sql += ",WFKOR    = :WFKOR";
		sql += ",WFEN     = :WFEN";
		sql += ",POP      = :POP";
		sql += ",REH      = :REH";
		sql += ",WS       = :WS";
		sql += ",WD       = :WD";
		sql += ",WDKOR    = :WDKOR";
		sql += ",WDEN     = :WDEN";
		sql += ",R12      = :R12";
		sql += ",S12      = :S12";
		sql += ",R06      = :R06";
		sql += ",S06      = :S06";
		sql += " WHERE X  = :X and Y = :Y and HOUR = :HOUR";
		// sql += " WHERE X = :X and Y = :Y and DATA_SEQ = :SEQ";

		try {
			pstmt = con.prepareStatement(sql);

			// {x=144, y=123, seq=7, hour=24, day=1, temp=22.0, tmx=24.0, tmn=20.0, sky=4,
			// pty=0,
			// wfKor=�帲, wfEn=Cloudy, pop=30, r12=0.0, s12=0.0, ws=3.0, wd=1, wdKor=�ϵ�,
			// wdEn=NE,
			// reh=95, r06=-0.0, s06=0.0}

			for (int i = 0; i < result.size(); i++) {

				List<Map<String, String>> list = result.get(i);

				for (Map<String, String> map : list) {
					System.out.println(i + ". " + map);
					pstmt.setString(1, map.get("temp"));
					pstmt.setString(2, map.get("tmx"));
					pstmt.setString(3, map.get("tmn"));
					pstmt.setString(4, map.get("sky"));
					pstmt.setString(5, map.get("pty"));
					pstmt.setString(6, map.get("wfKor"));
					pstmt.setString(7, map.get("wfEn"));
					pstmt.setString(8, map.get("pop"));
					pstmt.setString(9, map.get("reh"));
					pstmt.setString(10, map.get("ws"));
					pstmt.setString(11, map.get("wd"));
					pstmt.setString(12, map.get("wdKor"));
					pstmt.setString(13, map.get("wdEn"));
					pstmt.setString(14, map.get("r12"));
					pstmt.setString(15, map.get("s12"));
					pstmt.setString(16, map.get("r06"));
					pstmt.setString(17, map.get("s06"));

					pstmt.setString(18, map.get("x"));
					pstmt.setString(19, map.get("y"));
					// pstmt.setString(20, map.get("seq"));
					pstmt.setString(20, map.get("hour"));

					pstmt.executeUpdate(); // ����.
				}
			}

		} catch (Exception e) {
			System.out.println("������Ʈ�� ���� �߻�:" + e.getMessage());
			e.fillInStackTrace();
		} finally {
			db.close(con, pstmt); // Connection ���� ����.
		}
	}

	/**
	 * ���û���κ��� �ش� ��ǥ�� ���� ������ ��������. (XML)
	 */
	public List<Map<String, String>> getTownForecast(String x, String y) {

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		try {
			SAXBuilder parser = new SAXBuilder();
			parser.setIgnoringElementContentWhitespace(true);

			String url = String.format(rssFeed, x, y);
			Document doc = parser.build(url);
			Element root = doc.getRootElement();
			Element channel = root.getChild("body");
			List<Element> list = channel.getChildren("data");

			for (int i = 0; i < list.size(); i++) {

				Element el = (Element) list.get(i);
				String seq = el.getAttributeValue("seq");
				if (seq.equals("8"))
					break; // 24�ð� �����͸� ����

				Map<String, String> data = new LinkedHashMap<String, String>();
				data.put("x", x);
				data.put("y", y);
				data.put("seq", seq);

				// data����� �ڽĿ�ҵ��� �ϳ��� ������ ���� : (����̸�, ����� �׽�Ʈ���)
				for (Element dataChild : el.getChildren()) {
					// <hour>3</hour>
					data.put("hour", "3");
					data.put(dataChild.getName(), dataChild.getTextTrim());
				}
				System.out.println(data); // �ֿܼ� ���.
				if (bFileWrite)
					out.println(data);// ���Ͽ� ���

				result.add(data);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * DB�κ��� �ش� ��ǥ�� ����ð� ���� ������ ��������.
	 */
	public Map<String, String> getTownForecastFromDB(String x, String y) {

		Map<String, String> data = new LinkedHashMap<String, String>();
		String sql = "SELECT * FROM TBL_WFORECAST_DATA "
				+ "WHERE X = :X AND Y = :Y  AND :HOUR >= HOUR-3 AND  :HOUR < HOUR";

		Connection conn = DBUtil.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); // ���� �ð��� ���
		System.out.println("���� �ð� : " + nowHour);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, x);
			pstmt.setString(2, y);
			pstmt.setString(3, Integer.toString(nowHour));
			pstmt.setString(4, Integer.toString(nowHour));
			rs = pstmt.executeQuery();

			if (rs.next()) {
				data.put("x", rs.getString("x"));
				data.put("y", rs.getString("y"));
				data.put("hour", rs.getString("hour")); // ���׿��� 3�ð� ����
				data.put("seq", rs.getString("data_seq")); // 48�ð��� ���° ���� ����Ŵ
				data.put("day", rs.getString("day")); // 1��°�� (0: ����/1: ����/2: ��)
				data.put("temp", rs.getString("temp")); // ���� �ð��µ�
				data.put("tmx", rs.getString("tmx")); // �ְ� �µ�
				data.put("tmn", rs.getString("tmn")); // ���� �µ�
				data.put("sky", rs.getString("sky")); // �ϴ� �����ڵ� (1: ����, 2: ��������, 3:��������, 4:�帲)
				data.put("pty", rs.getString("pty")); // ���� �����ڵ� (0: ����, 1: ��, 2: ��/��, 3: ��/��, 4: ��)
				data.put("wfkor", rs.getString("wfKor")); // ���� �ѱ���
				data.put("wfEn", rs.getString("wfEn")); // ���� ����
				data.put("pop", rs.getString("pop")); // ���� Ȯ��%
				data.put("r12", rs.getString("r12")); // 12�ð� ���� ������
				data.put("s12", rs.getString("s12")); // 12�ð� ���� ������
				data.put("ws", rs.getString("ws")); // ǳ��(m/s)
				data.put("wd", rs.getString("wd")); // ǳ�� (0~7:��, �ϵ�, ��, ����, ��, ����, ��, �ϼ�)
				// data.put("wdKor",rs.getString("wdKor") ); //ǳ�� �ѱ���
				// data.put("wdEn",rs.getString("wdEn") ); //ǳ�� ����
				data.put("reh", rs.getString("reh")); // ���� %
				data.put("r06", rs.getString("r06")); // 6�ð� ���� ������
				data.put("s06", rs.getString("s06")); // 6�ð� ���� ������

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			DBUtil.close(); // Connection ���� ����.
		}

		return data;
	}

}