/* Author : Mahmudul Hossain (19303235)
 * Purpose : Perfom des encryption on the input file 
 * 			 and output the encrypted file along with
 * 			 the decrypted file which is then compared 
 * 			 with the input file
 * Last modified : 17/04/2020
 */


import java.util.Scanner;
import java.io.*;

public class TestFile
{
	public static void main(String[] args)
	{
		String filename = "testfile-DES.txt";
		//A valid hexadecimal key gets inputted from user
		String hexKey = CheckValid.checkInput();

		//Perform necessary file operation
		fileOp(filename, hexKey);

		//Notify user what files are created
		System.out.println("Encrypted text stored in encrypted.txt");
		System.out.println("Deccrypted text stored in decrypted.txt");

	}

	//Open and read the input filename
	//Encrypts each line read which is saved into encrypted.txt
	//Decrypts each encrypted line which is saved into decrypted.txt
	public static void fileOp(String filename, String key)
	{
		DES des;
		Scanner sc;
		File fileToOpen;
		String line = new String();
		File encryptedOutput, decryptedOutput;
		PrintWriter pwEncrypted, pwDecrypted;
		int lineNum  = 1;

		try
		{
			encryptedOutput = new File("encrypted.txt");
			decryptedOutput = new File("decrypted.txt");

			pwEncrypted = new PrintWriter(encryptedOutput);
			pwDecrypted = new PrintWriter(decryptedOutput);

			fileToOpen = new File(filename);
			sc = new Scanner(fileToOpen);

			//Read every line of input file 
			//Perform DES encryption and decryption
			while(sc.hasNextLine())
			{
				//Plain text to encrypt
				line = sc.nextLine();

				//Perform des encryption and decryption
				des = new DES(line, key);
				//Compare the plain text and decrypted line to ensure successful
				//des encryption and decryption
				//If an error occurs, output the line number where the des failed
				if(!(line.compareTo(des.getDecryptedWord()) == 0))
				{
					System.out.println("ERROR at line number : " + lineNum);
				}
				//Print the encrypted and decrypted texts into appropriate files
				pwEncrypted.println(des.getEncryptedWord());
				pwDecrypted.println(des.getDecryptedWord());

				lineNum++;

			}

			pwEncrypted.close();
			pwDecrypted.close();
			sc.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}

	}

}	
