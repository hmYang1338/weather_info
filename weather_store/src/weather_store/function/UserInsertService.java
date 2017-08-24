package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;

public class UserInsertService implements Service{

	@Override
	public void Excute(Scanner sc) {
		
		System.out.println("==========ȸ������==========");
		System.out.print("���̵� : ");
		String id = sc.nextLine();
		System.out.print("��й�ȣ : ");
		String pw = sc.nextLine();
		System.out.print("�̸� : ");
		String name = sc.nextLine();
		System.out.print("�ּ� : ");
		String addr = sc.nextLine();
		System.out.println("----------------------------");
		
		int check = UserDAO.getInstance().userInsert(new UserDTO(id, pw, name, addr));
		
		if(check == 0) {
			System.out.println("ȸ������ ����");
		} else {
			System.out.println("ȸ������ ����");
		}
		
	} // end of method
	
	public static void main(String[] args) {
		UserInsertService u = new UserInsertService();
		u.Excute(new Scanner(System.in));
		
	} // end of main
} // end of class
