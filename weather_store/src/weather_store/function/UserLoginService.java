package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;

public class UserLoginService implements Service{

	@Override
	public void Excute(Scanner sc) {
		// TODO Auto-generated method stub
		System.out.println("---------------------");
		System.out.println("�α���");
		System.out.println("---------------------");
		System.out.print("���̵� : ");
		String id = sc.nextLine();
		System.out.print("��й�ȣ : ");
		String pw = sc.nextLine();
		
		String uname = UserDAO.getInstance().userLogin(id, pw);
		
		if(uname == null) {
			System.out.println("�α��� �ȵ�");
		} else {
			System.out.println(uname + "�� �α����Ͽ����ϴ�");
		}
		
	} // end of method
	
	public static void main(String[] args) {
		UserLoginService u = new UserLoginService();
		u.Excute(new Scanner(System.in));
	}
} // end of class
