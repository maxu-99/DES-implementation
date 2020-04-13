import java.util.*;
import java.math.BigInteger;
public class TestDES
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		String hex = CheckValid.checkInput();
		
		String binary = new BigInteger(hex, 16).toString(2);	
		binary = String.format("%64s",binary).replace(" ", "0");	


		String key = binary;
		System.out.println("Enter string to encrypt : ");

		String plaintext = sc.nextLine();

		DES des = new DES(plaintext,key);

		System.out.println("ENCRYPTED : " + des.getEncryptedWord());
		System.out.println("Final decryption string : " + des.getDecryptedWord());
	}
}	
