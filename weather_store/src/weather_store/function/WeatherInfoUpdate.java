package weather_store.function;

import java.util.Scanner;

import weather_store.dao.WeatherDAO;

public class WeatherInfoUpdate implements Service {

	@Override
	public void execute(Scanner sc) {
		System.out.println("======================  ���� ���� ������Ʈ");
		System.out.println("�ȳ� :  ���� ������ ��׶���� ������Ʈ �˴ϴ�. �۾� �Ϸ� �� �ȳ��� �帮�ڽ��ϴ�.");
		new Thread(() -> {
			WeatherDAO.getInstance().weatherSQLInsert();
		}).start();
	}

}