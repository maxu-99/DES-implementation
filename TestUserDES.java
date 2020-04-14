/* Author : Mahmudul Hossain (19303235)
 * Purpose : Perform DES(Data Encryption Standard) encryption and decryption
 * 			 using user input
 * Last Modified : 13/04/2020
 */

import java.util.Scanner;


public class TestUserDES
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		//A valid hexadecimal key gets generated from user input		
		String hex = CheckValid.checkInput();

		System.out.println("Enter string to encrypt : ");

		//Plain text to encrypt
		String plaintext = sc.nextLine();

		//Perform DES encryption
		DES des = new DES(plaintext,hex);

		//Display the encrypted text directly to user
		System.out.println("ENCRYPTED : " + des.getEncryptedWord());

		//Decrypt the encrypted text to display if back to user and make 
		//comparisons with the plain text
		System.out.println("Final decryption string : " + des.getDecryptedWord());
	}
}	
