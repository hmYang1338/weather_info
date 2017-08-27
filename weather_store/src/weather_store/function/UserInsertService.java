package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;
import weather_store.util.SecurityUtil;

public class UserInsertService implements Service{

	@Override
	public void execute(Scanner sc) {
		
		System.out.println("======================  ȸ������");
		System.out.print("���̵� \t:");
		String id = sc.nextLine();
		System.out.print("��й�ȣ \t:");
		String pw = sc.nextLine();
		String newpwd=SecurityUtil.encrypt(pw);
		System.out.print("�̸� \t:");
		String name = sc.nextLine();
		System.out.print("�ּ� \t:");
		String addr = sc.nextLine();
		System.out.println("-----------------------------------------------------------------------------------");
		
		int check = UserDAO.getInstance().userInsert(new UserDTO(id, newpwd, name, addr));
		
		if(check == 0) {
			System.out.println("�ȳ� :  ȸ������ ����");
		} else {
			System.out.println("�ȳ� :  ȸ������ ����");
		}
		
	} // end of method
	
} // end of class
