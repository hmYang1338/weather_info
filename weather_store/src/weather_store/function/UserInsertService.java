package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;

public class UserInsertService implements Service{

	@Override
	public void Excute(Scanner sc) {
		
		System.out.println("------------------------------------");
		System.out.println("ȸ������");
		System.out.println("------------------------------------");
		
		System.out.println("���̵� :");
		String id = sc.nextLine();
		System.out.println("��й�ȣ : ");
		String pw = sc.nextLine();
		System.out.println("�̸� : ");
		String name = sc.nextLine();
		System.out.println("�ּ� : ");
		String addr = sc.nextLine();
		
		int check = UserDAO.getInstance().userInsert(new UserDTO(id, pw, name, addr));
		
		if(check == 0) {
			System.out.println("ȸ������ ����");
		} else {
			System.out.println("ȸ������ ����");
		}
		
	} // end of method
} // end of class
