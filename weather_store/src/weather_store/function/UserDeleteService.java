package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;

public class UserDeleteService implements Service{

	@Override
	public void Excute(Scanner sc) {
		// TODO Auto-generated method stub
		System.out.println("------------------------------");
		System.out.println("ȸ��Ż��");
		System.out.println("------------------------------");
		System.out.println("���̵� �Է� : ");
		String id = sc.nextLine();
		
		int check = UserDAO.getInstance().userDelete(id);
		
		if(check == 0) {
			System.out.println("ȸ��Ż�� ����");
		} else {
			System.out.println("ȸ��Ż�� ����");
		}
		
	} // end of method
} // end of class
