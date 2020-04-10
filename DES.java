import java.io.*;
import java.util.Scanner;
import java.math.BigInteger;
public class DES
{
	private StringBuilder encrypted;
	private StringBuilder decrypted;
	private Key key;

	private static final int[][][] SBOXES = new int[][][]
	{
		//S1
		{
			{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},	
			{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
			{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
			{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
		},

		//S2
		{
			{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
			{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
			{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
			{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
		},

		//S3
		{
			{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
			{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
			{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
			{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
		},
	
		//S4
		{
			{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
			{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
			{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
			{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
		},

		//S5
		{
			{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
			{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
			{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
			{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3},
		},

		//S6
		{
			{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
			{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
			{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
			{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
		},

		//S7
		{
			{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
			{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
			{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
			{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
		},

		//S8
		{
			{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
			{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
			{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
			{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
		}
	};


	private static final int[][] INITIALPERM = new int[][]
	{
		{58,50,42,34,26,18,10,2},
		{60,52,44,36,28,20,12,4},
		{62,54,46,38,30,22,14,6},
		{64,56,48,40,32,24,16,8},
		{57,49,41,33,25,17,9,1},
		{59,51,43,35,27,19,11,3},
		{61,53,45,37,29,21,13,5},
		{63,55,47,39,31,23,15,7}
	};

	private static final int[][] FINALPERMINV = new int[][]
	{
		{40,8,48,16,56,24,64,32},
		{39,7,47,15,55,23,63,31},
		{38,6,46,14,54,22,62,30},
		{37,5,45,13,53,21,61,29},
		{36,4,44,12,52,20,60,28},
		{35,3,43,11,51,19,59,27},
		{34,2,42,10,50,18,58,26},
		{33,1,41,9,49,17,57,25}
	};

	//PPermutation
	private static final int[][] FPERMUTATION = new int[][]
	{
		{16,7,20,21},
		{29,12,28,17},
		{1,15,23,26},
		{5,18,31,10},
		{2,8,24,14},
		{32,27,3,9},
		{19,13,30,6},
		{22,11,4,25}
	};
		
	private static final int[][] EXPANSION = new int[][]
	{
		{32,1,2,3,4,5},
		{4,5,6,7,8,9},
		{8,9,10,11,12,13},
		{12,13,14,15,16,17},
		{16,17,18,19,20,21},
		{20,21,22,23,24,25},
		{24,25,26,27,28,29},
		{28,29,30,31,32,1}
	};
	
	public DES(String text, String inKey)
	{
		encrypted = new StringBuilder("");
		decrypted = new StringBuilder("");
		this.key = new Key(inKey);

		this.encrypt(text);
		this.decrypt(this.encrypted.toString());

	}
/*
	public DES(File file String inKey)
	{
		this.key = new Key(inKey);
		this.fileHandle(file);
	}
*/
	//Not using plain for now
	public void encrypt(String plain)
	{
		//878787878787
//		String binary = "1000011110000111100001111000011110000111100001111000011110000111";

		//int index = 0;
		String binary = binaryConversion(plain);

		//M
		//String binary = "0000000100100011010001010110011110001001101010111100110111101111";

		StringBuilder binaryEncrypted = new StringBuilder("");

		System.out.println("BINARY P.T : " + binary);
		//Time to encrypt 64 bits p.t to c.t
		for(int index = 0; index < binary.length(); index += 64)
		{
			String message = binary.substring(index, index + 64);
			message = feistel(message,true);
			binaryEncrypted.append(message);
		}

		encrypted.append(binaryEncrypted.toString());
		System.out.println("ENCRYPTED : " + binaryEncrypted.toString());
		System.out.println("ENCRYPTED LENGTH : " + binaryEncrypted.length());

//		encrypted.append(convertBinaryToString(binaryEncrypted.toString()));

//		System.out.println("ENCRYPTED in words : " + encrypted.toString());

	}

	public void decrypt(String cipherbinary)
	{
//		int index = 0;
		//String cipherbinary = binaryConversion(cipher);
		//String cipherbinary = "0000000000000000000000000000000000000000000000000000000000000000";
 
		StringBuilder cipherDecrypted = new StringBuilder("");

		System.out.println("CIPHER P.T : " + cipherbinary);

		for(int index = 0; index < cipherbinary.length(); index += 64)
		{
			String message = cipherbinary.substring(index, index + 64);
			message = feistel(message,false);
			cipherDecrypted.append(message);
		}
		
		System.out.println("DECRYPTED : " + cipherDecrypted.toString());

		//decrypted.append(convertBinaryToString(cipherDecrypted.toString()));
		decrypted.append(cipherDecrypted.toString());

	//	System.out.println("DECRYPTED in words : " + decrypted.toString());
		
	}

	public String getEncryptedWord()
	{
		return new String(convertBinaryToString(encrypted.toString()));
	}


	public String getDecryptedWord()
	{
		return new String(convertBinaryToString(decrypted.toString()));
	}

	public String feistel(String plainbit, boolean isEncrypt)
	{
		//String[] subKeys = ket.getAllKeys();
		String initialPermutedOutput = this.initialPermutation(plainbit);
		System.out.println("First IP : " + initialPermutedOutput);

		String processedOutput = this.round(initialPermutedOutput,isEncrypt);
		System.out.println("After 16 rounds final output : " + processedOutput);

		String finalOutput = this.inversePermutation(processedOutput);

		System.out.println("Final IP inverse output : " + finalOutput);
		
		return finalOutput;

	}
/*
	public String feistelDec(String plainbit)
	{
		//String[] subKeys = ket.getAllKeys();
		String initialPermutedOutput = this.initialPermutation(plainbit);

		String processedOutput = this.roundDec(initialPermutedOutput);

		String finalOutput = this.inversePermutation(processedOutput);
		System.out.println("Final IP inverse output : " + finalOutput);

		return finalOutput;

	}
*/
	//Carry out intial permuation on 64 bits
	public String initialPermutation(String input)
	{
		char[] output = new char[64];
		for(int index = 0; index < 64; index ++)
		{
			output[index] = input.charAt(INITIALPERM[index / 8][index % 8] - 1);
		}
		
		return new String(output);
	}

	public String inversePermutation(String input)
	{	
		char[] output = new char[64];
		for(int index = 0; index < 64; index++)
		{
			output[index] = input.charAt(FINALPERMINV[index / 8][index % 8] - 1);
		}

		return new String(output);
	}


	public String round(String input, boolean isEncrypt)
	{
		//int index = 0;
		String left, right, temp;
		String[] subKeys;

		if(isEncrypt)
		{
			subKeys = key.getAllKeys();
		}
		else
		{
			subKeys = key.getAllKeysReverse();
		}

		for(int index = 0; index < 16; index++)
		{
			left = input.substring(0,32);
			System.out.println("LEFT  : " + left);

			right = input.substring(32,input.length());
			System.out.println("RIGHT : " + right);


			temp = expansion(right);
			System.out.println("After expansion of right : " + temp);

			temp = xor(temp,subKeys[index]);
			System.out.println("After first XOR with key : " + temp);

			temp = substitution(temp);
			System.out.println("After SBOX operation : " + temp);

			temp = fPermutation(temp);

			temp = xor(temp,left);

			input = right + temp;
		}
		
		input = input.substring(32,input.length()) + input.substring(0,32);

		return new String(input);
	}

/*
		while(index < 16)
		{
			right = this.xor(right, this.fFunction(left, subKeys[index++]));
		}

		return new String(left + right);
*/
	
/*
	
	public String roundDec(String input)
	{
		String left, right, temp;
		String[] subKeys = key.getAllKeys();

		for(int index = 15; index >= 0; index--)
		{
			left = input.substring(0,32);
			System.out.println("LEFT  : " + left);

			right = input.substring(32,input.length());
			System.out.println("RIGHT : " + right);


			temp = expansion(right);
			System.out.println("After expansion of right : " + temp);

			temp = xor(temp,subKeys[index]);
			System.out.println("After first XOR with key : " + temp);

			temp = substitution(temp);
			System.out.println("After SBOX operation : " + temp);

			temp = fPermutation(temp);

			temp = xor(temp,left);

			input = right + temp;
		}
		
		input = input.substring(32,input.length()) + input.substring(0,32);

		return new String(input);

	}
	
	
*/

/*
	public String fFunction(String input, String key)
	{
		return fPermutation(substitution(xor(expansion(input),key)));
	}
*/



	//Set bit to 1 if two XOR bits are different
	public String xor(String s1, String s2)
	{
		char[] bits1 = s1.toCharArray();
		char[] bits2 = s2.toCharArray();
		
		char[] outputXOR = new char[s1.length()];

		for(int index = 0; index < s1.length(); index++)
		{
			if(bits1[index] != bits2[index])
			{
				outputXOR[index] = '1';
			}
			else
			{
				outputXOR[index] = '0';
			}
		}

		return new String(outputXOR);
	}

	//Sbox
	/*
	public String substitution(String bitstring)
	{
		StringBuilder output = new StringBuilder("");
		String partbits, col, row;
		
		String sBoxVal;

		int x = 0, y = 0, sBoxNum = 0, index = 0;

		while(index < 48)
		{
			partbits = bitstring.substring(index, index + 6);
			row = bitstring.charAt(0) + bitstring.substring(5,6);
			col = bitstring.substring(1,5);
			x = Integer.parseInt(row,2);
			y = Integer.parseInt(col,2);

			sBoxVal = binaryFormat(SBOXES[sBoxNum][x][y],4);
		//	sBoxVal = 


			output.append(sBoxVal);
			index = index + 6;
			sBoxNum++;
		}

		return output.toString();
	}
	*/

	public String substitution(String input)
	{
		String output = "";
		for(int i = 0; i < 48; i += 6)
		{
			String temp = input.substring(i, i + 6);
			int num = i / 6;
			int row = Integer.parseInt(temp.charAt(0) + "" + temp.charAt(5),2);

			int col = Integer.parseInt(temp.substring(1,5),2);

			output += binaryFormat(SBOXES[num][row][col], 4);
		}

		return output;
	}





	private String binaryFormat(int num, int length)
	{
	/*	
		String binary = Integer.toBinaryString(num);
		//String binary = new BigInteger(num,2).toString(2);
		binary = String.format("%"+ length +"s",binary).replace(' ','0');
		return binary;
	*/
		 
		String binary =	String.format("%"+ length + "s", Integer.toBinaryString(num)).replace(' ', '0');
//		System.out.println("Binary value for S box : " + binary + " for number : " + num);
		return binary;
		
	}


	private String convertBinaryToString(String binary)
	{
		String word = new String(new BigInteger(binary,2).toByteArray());
	
	//	String word = ((char)(Integer.parseInt(binary,2)));
		return word;
	}


	//Expand 32 bit input to 48 bit using the expansion substitution table
	public String expansion(String input)
	{
		char[] newExpansion = new char[48];
		char[] rightInput = input.toCharArray();

		for(int index = 0; index < 48; index++)
		{
			newExpansion[index] = rightInput[EXPANSION[index / 6][index % 6] - 1];
		}

		return new String(newExpansion);
	}		
			

	public String fPermutation(String input)
	{
		char[] output = new char[32];
		for(int index = 0; index < 32; index++)
		{
			output[index] = input.charAt(FPERMUTATION[index / 4][index % 4] - 1);
		}
		
		return new String(output);
	}

	public String binaryConversion(String plain)
	{
		char c;
		String binary = "";
		String total = "";
		for(int i = 0; i < plain.length(); i++)
		{
			c = plain.charAt(i);
			binary = Integer.toBinaryString(c);
			binary = this.pad(binary,8);
			total = total + binary;
		}
		
		if(total.length() % 64 != 0)
		{
			//Final padding to make the whole plain text to a multiple of 64 bits
			int remainder = total.length() % 64;
			for(int ii = 0; ii < 64 - remainder; ii++)
			{
				total = total + "0";
			}
		}

		return total;	
	}

	public String pad(String binary, int l)
	{
		StringBuilder padded = new StringBuilder(binary);

		while(padded.length() < l)
		{
			padded.insert( 0, '0');
		}

		return padded.toString();
	}
/*
	public static void fileOp(File inFile)
	{
		Scanner sc;
		File encryptedOutput, decryptedOutput;
		PrintWriter pwEncrypted, pwDecrypted;

		try
		{
			sc = new Scanner(inFile);
			encryptedOutput = new File("encrypted.txt");
			decryptedOutput = new File("decrypted.txt");
			String line;

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
*/
	
}
