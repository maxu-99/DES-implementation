import java.util.*;
import java.math.*;
public class TestDES
{
	public static void main(String[] args)
	{
	//	System.out.println(Character.getNumericValue('1'));	
		System.out.println("Enter 16 hexadecimal values for key: "); 
		Scanner sc = new Scanner(System.in);
///*
		//M
//		String hex = "133457799BBCDFF1";      
   
		String hex = sc.nextLine();
	
//		String hex = "0E329232EA6D0D73";
//		int num = (Integer.parseInt(hex,16));
//		String binary = Integer.toBinaryString(num);
		//Need to check length is 16
		
		String binary = new BigInteger(hex, 16).toString(2);	
		binary = String.format("%64s",binary).replace(" ", "0");	
		System.out.println(binary);

		Key key = new Key(binary);

		System.out.println("Enter string to encrypt : ");

		String plaintext = sc.nextLine();

		DES des = new DES(plaintext,key);
//		*/
	/*
		
		String plain = sc.nextLine();
		String binary = "";
		for(int i = 0; i < plain.length(); i++)
		{
			binary += Integer.toBinaryString(plain.charAt(i));
		}
		
		System.out.println(binary);
	*/	
	}
}	
