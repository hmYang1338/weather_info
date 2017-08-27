package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.util.SecurityUtil;

public class UserLoginService implements Service {
	private String id = "";

	public UserLoginService() {
	}

	@Override
	public void execute(Scanner sc) {
		// TODO Auto-generated method stub
		while (true) {
			System.out.println("======================  �α���");
			System.out.print("���̵� \t:");
			id = sc.nextLine();
			System.out.print("��й�ȣ \t:");
			String pw = sc.nextLine();
			String newpwd = SecurityUtil.encrypt(pw);
			System.out.println("-----------------------------------------------------------------------------------");
			if (UserDAO.userLogin(id, newpwd).equals(newpwd)) {
				System.out.println("�ȳ� :  �α��ο� �����Ͽ����ϴ�.");
			} else {
				System.out.println("�ȳ� :  �α��ο� �����Ͽ����ϴ�. ȯ���մϴ� " + id + "��!");
				break;
			}
		}
	} // end of method

	public String getId() {
		return id;
	}

} // end of class
