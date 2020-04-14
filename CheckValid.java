/* Author : Mahmudul Hossain (19303235)
 * Purpose : This class verifies that the user inputted
 * 			 a valid hexadecimal value with appropriate 
 * 			 size
 * Last Modified : 15/04/2020
 */

import java.util.Scanner;
public class CheckValid
{
	public static String checkInput()
	{
		//Flag to check if all condtions are mey
		boolean valid = false;
		String hex = "";
		Scanner sc = new Scanner(System.in);
		
		while(!valid)
		{
			//User output
			System.out.println("Enter 16 hexadecimal values for key : (The cursor point must match with the values entered)");
			//Just to assist that 16 characters are entered to terminal instead of counting
			//Tried to make it user friendly
			System.out.println("Cursor point :  '");
		
			try
			{
				hex = sc.nextLine();

				//The user input must be a hexadecimal value of size 16
				//Repeat until a valid input is entered
				while(hex.length() != 16 || !checkIfHex(hex))
				{
					System.out.println("Enter 16 hexadecimal values for key : ");
					System.out.println("Cursor point :  '");
		
					hex = sc.nextLine();
				}
				
				//Once all conditions are met set valid to true
				valid = true;
			}
			catch(NumberFormatException e)
			{
				System.err.println(e.getMessage());
				sc.next();
			}
		}
			
		return hex;
				
	}
	
	//Checks if the entered hexadecimal value is actually a
	//hexadecimal value
	private static boolean checkIfHex(String hex)
	{
		boolean valid = true;
		
		//Loop through each character of hex to ensure
		//that the characters is within range from
		//0 to 9 and a/A to f/F
		for(int i = 0; i < hex.length(); i++)
		{
			char c = hex.charAt(i);
			if(Character.digit(c, 16) == -1)
			{
				valid = false;
			}
		}

		return valid;
	}
}
	
