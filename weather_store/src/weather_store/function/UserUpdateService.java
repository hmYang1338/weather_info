package weather_store.function;

import java.util.Scanner;

import weather_store.dao.UserDAO;
import weather_store.dto.UserDTO;

public class UserUpdateService implements Service {
	private UserDTO dto;
	
	public UserUpdateService(UserDTO dto){
		this.dto = dto;
	}
	

	@Override
	public void execute(Scanner sc) {
		UserDAO dao = UserDAO.getInstance();
		System.out.println("======================  ȸ����������");
		System.out.println("���� �ּ�\t: "+dto.getAddr());
		System.out.print("����\t:");
		dto.setAddr(sc.nextLine());
		System.out.println("���� �̸�\t: "+dto.getName());
		System.out.print("����\t:");
		dto.setName(sc.nextLine());
		int result = dao.userUpdate(dto);
		if(result!=-1&&result!=0) {
			System.out.println("�ȳ� :  ȸ�� ���� ������ �����Ͽ����ϴ�.");
		} else {
			System.out.println("�ȳ� :  ȸ�� ���� ������ �����Ͽ����ϴ�.");
		}
	}

}
