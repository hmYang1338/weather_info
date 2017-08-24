package weather_store.function;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import weather_store.dao.WeatherDAO;
import weather_store.dto.WeatherDTO;

public class WeatherServeService implements Service {

	@Override
	public void Excute(Scanner sc) {
		WeatherDAO dao = WeatherDAO.getInstance();

		Map<Long, String> codeList = dao.faveriteLocal("dddd");
		codeList.entrySet().forEach(t -> {
			List<WeatherDTO> list = dao.weatherServe(t.getKey());
			System.out.println("======================  ������� : [["+t.getValue()+"]]");
			list.forEach(x -> {
				StringBuilder sb = new StringBuilder();
				if (x.getDay() == 0) {
					sb.append("���� : ");
				} else if (x.getDay() == 1) {
					sb.append("���� : ");
				} else if (x.getDay() == 2) {
					sb.append("�� : ");
				}
				sb.append(x.getHour());
				sb.append("\t��\t ");
				sb.append("��� : ");
				sb.append(x.getTemp());
				sb.append("��\t����Ȯ�� : ");
				sb.append(x.getPop());
				sb.append("%  ");
				sb.append("<");
				sb.append(x.getWfKOR());
				sb.append(">\t");
				sb.append("ǳ�� : ");
				sb.append(x.getWdKOR());
				sb.append("|");
				sb.append(x.getWs());
				sb.append("m/s");
				sb.append("\t������ : ");
				sb.append(x.getR06());
				sb.append("mm");
				System.out.println(sb.toString());
			});
			System.out.println("-----------------------------------------------------------------------------------");
		});
	}// end of Excute;

	public static void main(String[] args) {
		WeatherServeService w = new WeatherServeService();
		w.Excute(new Scanner(System.in));
	}
}
