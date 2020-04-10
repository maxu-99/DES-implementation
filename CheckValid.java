import java.util.Scanner;
public class CheckValid
{
	public static String checkInput()
	{
		boolean valid = false;
		String hex = "";
		Scanner sc = new Scanner(System.in);
		
		while(!valid)
		{
			System.out.println("Enter 16 hexadecimal values for key : ");
		
			try
			{
				hex = sc.nextLine();
				while(hex.length() != 16)
				{
					System.out.println("Enter 16 hexadecimal values for key : ");
					hex = sc.nextLine();
				}
				
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
}	

	
