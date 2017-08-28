package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;
import weather_store.util.SecurityUtil;

public class UserLoginService implements Service {
	private UserDTO dto;
	private String id;

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
			try {
			if (UserDAO.userLogin(id, newpwd).equals(newpwd)) {
				System.out.println("�ȳ� :  �α��ο� �����Ͽ����ϴ�.");
			} else {
				break;
			}
			} catch(NullPointerException e) {
				System.out.println("�ȳ� :  �α��ο� �����Ͽ����ϴ�.");
			}
		}
	} // end of method

	public UserDTO getId() {
		dto = UserDAO.getInstance().userInfo(id);
		System.out.println("�ȳ� :  �α��ο� �����Ͽ����ϴ�. ȯ���մϴ� " + dto.getName() + "��!");
		return dto;
	}

} // end of class
