import java.util.*;
import java.io.*;

public class TestFile
{
	public static void main(String[] args)
	{
		String filename = "testfile-DES.txt";
		String hexKey = CheckValid.checkInput();

		String plainLine = fileRead(filename, String hexKey);

//		DES des = new DES(plainLine, hexKey);

		fileSave(des);

		System.out.println("Encryption successfull");
		System.out.println("Encrypted text stored in encrypted.txt");
		System.out.println("Deccrypted text stored in decrypted.txt");

	}

	public static String fileRead(String filename, String key)
	{
		Scanner sc;
		File file;
		String line = "";
		try
		{
			file = new File(filename);
			sc = new Scanner(file);

			while(sc.hasNextLine())
			{

				line = line + sc.nextLine();

			}

			sc.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}

		return new String(line);
	}


	public static void fileSave(DES des)
	{
		Scanner sc;
		File encryptedOutput, decryptedOutput;
		PrintWriter pwEncrypted, pwDecrypted;

		try
		{
			encryptedOutput = new File("encrypted.txt");
			decryptedOutput = new File("decrypted.txt");

			pwEncrypted = new PrintWriter(encryptedOutput);
			pwDecrypted = new PrintWriter(decryptedOutput);

			pwEncrypted.println(des.getEncryptedWord());
			pwDecrypted.println(des.getDecryptedWord());

			pwEncrypted.close();
			pwDecrypted.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	

}	
