package weather_store.town;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class TownCodeService {
	
	//�ֻ� ��� URL
	private final String topURL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
	//�߰� ��� URL
	private final String mdlURL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.%s.json.txt";		
	//���� ��� URL
	private final String leafURL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf.%s.json.txt";
	
	//KEY����
	private final String KEY_CODE = "code";
	private final String KEY_VALUE = "value";
	private final String KEY_X = "x";
	private final String KEY_Y = "y";
	
	//������ ��带 ������ ����Ʈ
	private List<TownDTO> topList = new ArrayList<TownDTO>();
	private List<TownDTO> mdlList = new ArrayList<TownDTO>();
	private List<TownDTO> leafList = new ArrayList<TownDTO>();
	
	//������
	public TownCodeService() {
		excute();
	}	
	
	
	public void excute() {
		
		JSONArray jsonArray = null;
		
		//�ְ� ���
		jsonArray = getJSON(topURL);
		parseJSON(topList, jsonArray, null, null, false);
		
		System.out.println("=========");
		
		//�߰� ���
		for(TownDTO dto : topList) {
			jsonArray = getJSON(String.format(mdlURL,  dto.getCode()));
			parseJSON(mdlList, jsonArray, dto.getCode(), dto.getName(), false);
		}
		
		
		System.out.println("=========");
		
		//���� ���
		for(TownDTO dto : mdlList) {
			jsonArray = getJSON(String.format(leafURL, dto.getCode()));
			parseJSON(leafList, jsonArray, dto.getCode(), dto.getName(), true);
		}		
	}
	
	//���� ���
	public void outputFile(String path) {		
		
		if(path == null || path.trim().equals("")) {
			path = "file//output.txt";			
		}
		
		System.out.println("���:"
				+ topList.size()+"�� /"+mdlList.size()+"�� /"+leafList.size() + "��");
		
		try {			
			PrintStream out = new PrintStream(new FileOutputStream(path));
			
			//�ְ� ���
			for (TownDTO dto : topList) {
				out.println(dto);
			}
			
			//�߰� ���
			for (TownDTO dto : mdlList) {
				out.println(dto);
			}
			
			//���ϳ��
			for (TownDTO dto : leafList) {
				out.println(dto);
			}
			
			out.close();			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ������ URL�� ���ʷ� JSON�������� ��� 
	 */
	private JSONArray getJSON(String urlStr) {
		URL url = null;
		HttpURLConnection conn = null;
		StringBuffer jsonHtml = new StringBuffer();
		
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			
			if(conn != null) {
				conn.setConnectTimeout(10000); //���� ���� ��� �ð� , 10�� 
				conn.setUseCaches(false); //ĳ����� ������.
				
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader br =
							new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
					
					String line = null;
					while ((line = br.readLine())!= null){
						jsonHtml.append(line + "\n");
					}
					br.close();
				}
				conn.disconnect(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("1."+jsonHtml.toString());
		JSONArray jsonObj = (JSONArray) JSONValue.parse(jsonHtml.toString());
		
		return jsonObj;
	}
	
	/**
	 * �Ľ̵� ��� ����Ʈ -> TownDTO �������� ��ȯ
	 * @param list ��ȯ����� ���� ����Ʈ 
     * @param array ���� ��� ����Ʈ 
     * @param parentCode �θ� ��� �ڵ� 
     * @param parentName �θ� ��� �̸� 
     * @param isLast ���ϳ���ΰ�?
	 */	
	private List<TownDTO> parseJSON(List<TownDTO> list, JSONArray array, 
					 		String parentCode, String parentName, boolean isLast) {
		
		JSONObject data = null;
		TownDTO town = null;
		for (int i = 0; i < array.size(); i++) {
			data = (JSONObject) array.get(i);
			
			if(!isLast) {
				//�ֻ�, �߰� ���
				town = new TownDTO(data.get(KEY_CODE).toString(), 
								   data.get(KEY_VALUE).toString(),
								   parentCode, parentName);
				
			} else {
				//���� ���
				town = new TownDTO(data.get(KEY_CODE).toString(),
						           data.get(KEY_VALUE).toString(),
						           parentCode,
						           parentName,
						           data.get(KEY_X).toString(),
						           data.get(KEY_Y).toString());
				
			}
			System.out.println(town);
			list.add(town);
		}
		return list;
	}
	
	//�׽�Ʈ	
	//JSON lib : http://code.google.com/p/json-simple/
	public static void main(String[] args) {
		TownCodeService t = new TownCodeService();
		t.outputFile(null);
	}
		
}
