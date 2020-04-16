import java.util.*;

public class random
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter : ");
		String line = sc.nextLine();
		String output = new String();

		for(int i = 0; i < line.length(); i++)
		{
			char c = line.charAt(i);
		//	output += Integer.parseInt(c,2);
			output += Integer.toBinaryString(c);
		}
		System.out.println(output);
	}
}
