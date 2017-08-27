package weather_store.start;

import java.util.Scanner;

import weather_store.function.*;

public class WeatherTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String id = null;
		System.out.println("���� ���� �ý��ۿ� ���Ű��� ȯ���մϴ�!");
		login: while (true) {
			System.out.println("���� : ------------------------------------------------------------------------------");
			System.out.println("1. �α���\t\t2. ȸ������\t0. ���α׷� ����");
			System.out.println("-----------------------------------------------------------------------------------");
			switch (sc.nextLine()) {
			case "1":
				// �α���
				UserLoginService login = new UserLoginService();
				login.execute(sc);
				id = login.getId();
				break login;
			case "2":
				// ȸ������
				new UserInsertService().execute(sc);
				break;
			case "0":
				System.exit(0);
				break;
			default:
				System.out.println("�׸� �°� �Է��� �ּ���!");
			}

		}
		while (true) {
			System.out.println("���� : ------------------------------------------------------------------------------");
			System.out.println("1. ���� Ȯ��\t2. ��ȣ ����\t3. ��ǰ ���\t4. ȸ����������\t0. ���α׷� ����");
			System.out.println("-----------------------------------------------------------------------------------");
			switch (sc.nextLine()) {
			case "1":
				new WeatherServeService(id).execute(sc);
				break;
			case "2":
				new WeatherCoordinateService(id).execute(sc);
				break;
			case "3":
				new ProductADService(id).execute(sc);
				break;
			case "4":
				new UserDeleteService(id).execute(sc);
				break;
			case "0":
				System.out.println("���񽺸� �����մϴ�.");
				System.exit(0);
				break;
			default:
				break;
			}
		}

	}// end of main
}// end of class
