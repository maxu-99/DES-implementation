/* Author : Mahmudul Hossain (19303235)
 * Purpose : This class perform the Data Encryption Standard (DES)
 * 			 encryption and decryption with the help of several
 * 			 functions storing the encrypted and decrypted text in
 * 			 binary form
 * Last Modified : 15/04/2020
 */
	
import java.io.*;
import java.util.Scanner;
import java.math.BigInteger;
public class DES
{
	//Stores the binary form of encrypted text
	private StringBuilder encrypted;

	//Stores the binary form of decrypted text
	private StringBuilder decrypted;

	//The hexadecimal key from Key.java 
	//containing shifted keys
	//Used for encryption and decryption purposes
	private Key key;

	//For all the mappings carried out, the left most bit
	//of a binary string is considered as 1st postion
	//and the right most bit is considered as the last bit
	//We read the binary string from left to right
	
	//SBOXES (4 rows and 16 columns each) containg values from
	//0 to 15
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

	//The first intial permutation on plain text binary 
	//Rearrangement of 64 bit binary input to 64 bit 
	//binary output
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

	//The final permutation after 16 rounds of round function
	//Rearrangement of 64 bit binary input to 64 bit binary
	//output
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
	//The permutation part taking the 32 bit output from SBOX
	//operation
	//Rearrangement of 32 bit binary to 32 bit binary output
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
		
	//The expansion permutation taking the right half of bit string
	//during round function
	//Rearrangement of 32 bit binary to 48 bit binary output
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
	
	//Default constructor than inputs plain text
	//And performs encryption using inKey
	public DES(String text, String inKey)
	{
		//Stores the binary equivalent of encrypted string
		encrypted = new StringBuilder("");

		//Stores the binary equivalent of decrypted string
		decrypted = new StringBuilder("");

		//Generate appropriate keys
		this.key = new Key(inKey);

		//Perform encryption on the plain text
		//Store it in encrypted
		this.encrypt(text);

		//Perform decryption on the plain text
		//Store it in decrypted
		this.decrypt(this.encrypted.toString());

	}

	//Plain text converted to cipher text of binary values
	//Updates the encrypted variable
	public void encrypt(String plain)
	{
		//Convert the entire plain text into binary equivalent
		//The binary text returned has a text length of a 
		//multiple of 64
		String binary = binaryConversion(plain);

		//Store the intermediate form of binary strings generated
		//from encryption
		StringBuilder binaryEncrypted = new StringBuilder("");

		//Loop through the whole binary string 
		//Encrypt 64 bit sections of the binary text
		for(int index = 0; index < binary.length(); index += 64)
		{
			//Generate subsequent 64 bit plain binary to encrypt
			String message = binary.substring(index, index + 64);
			
			//Store the final encrypted 64 bit string
			//true is used as a flag to perform encryption
			//false is used as a flag to perform decryption
			message = feistel(message,true);

			//Update the intermediate binary string 
			//by adding subsequent sections of encrypted 
			//64 bit string of 0s and 1s
			binaryEncrypted.append(message);
		}

		//Update the final state of binaryEncrypted to
		//encrypted StringBuilder
		encrypted.append(binaryEncrypted.toString());

	}

	//Convert the entire cipher text of binary string
	//to plain text of binary string 
	//Updates the decrypted variable
	//Decryption is same as encryption just the keys used in the round function
	//are applied in reversed order with respect to encryption 
	public void decrypt(String cipherbinary)
	{
		//Stores the intermediate form of binary strings generated
		//from decryption 
		StringBuilder cipherDecrypted = new StringBuilder("");

		//Loop through the whole binary cipher text string
		//Decrypt 64 bit sections of the binary text
		for(int index = 0; index < cipherbinary.length(); index += 64)
		{
			//Generate subsequent 64 bit encrypted binary to decrypted 
			String message = cipherbinary.substring(index, index + 64);

			//Store the final decrypted 64 bit string 
			//true is used as a flag to perform encryption
			//false is used as a flag to perform decryption
			message = feistel(message,false);

			//Update the intermediate binary string
			//by adding subsequent sections of decrypted
			//64 bit string of 0s and 1s
			cipherDecrypted.append(message);
		}

		//Update the final state of cipherDecrypted to
		//decrypted StringBuilder
		decrypted.append(cipherDecrypted.toString());

		
	}

	//Convert the binary form of encrypted by assigning character values 
	//with respect to the ASCII table thus returning a string of 
	//encrypted characters
	public String getEncryptedWord()
	{
		return new String(convertBinaryToString(encrypted.toString()));
	}


	//Convert the binary from of decrypted by assigning character values
	//with respect to the ASCII table thus returning a string of 
	//decrypted characters
	public String getDecryptedWord()
	{
		String original = new String(convertBinaryToString(decrypted.toString()));

		//Remove any unnecessary characters that were generated during padding 
		original = removeExcessChar(original);

	   	return original;	
	}

	//The intial permutation function to encrypt and decrypt a 64 bit binary string
	//Imports a 64 bit binary string
	//Exports a 64 bit binary string
	//isEncrypt is true of encryption and false for decryption
	public String feistel(String plainbit, boolean isEncrypt)
	{
		//Perform initial permuation on bit string
		String initialPermutedOutput = this.initialPermutation(plainbit);

		//Obtain the final outputted 64 bit string after 16 rounds of round 
		//function
		String processedOutput = this.round(initialPermutedOutput,isEncrypt);

		//Perform the final permutation on bit string 
		String finalOutput = this.inversePermutation(processedOutput);

		
		return finalOutput;

	}

	//Carry out intial permuation on 64 bit string into
	//a specific arrangement of bits which is 
	//metioned in INITIALPERM above
	public String initialPermutation(String input)
	{
		//Store the updated mapping of 64 bit string 
		char[] output = new char[64];

		//Loop through each index position of 64 bit input string
		//Set each index in the output array to the corresponding 
		//index of input string where the new index position is 
		//obtained from INITIALPERM
		//Each permuted index has to be substracted by 1 since array starts
		//from 0
		for(int index = 0; index < 64; index ++)
		{
			output[index] = input.charAt(INITIALPERM[index / 8][index % 8] - 1);
		}
		
		//Return the newly mapped 64 bit string
		return new String(output);
	}

	//Carry out the final permutation on 64 bit string into
	//a specific arrangement of bits which is 
	//mentioned in FINALPERMINV above
	public String inversePermutation(String input)
	{	
		//Store the updated mapping of 64 bit string
		char[] output = new char[64];

		//Loop through each index position of 64 bit input string
		//Set each index in the output array to the corresponding 
		//index of input string where the new index position is 
		//obtained from FINALPERMINV
		//Each permuted index has to be substracted by 1 since array starts
		//from 0
		for(int index = 0; index < 64; index++)
		{
			output[index] = input.charAt(FINALPERMINV[index / 8][index % 8] - 1);
		}

		//Return the newly mapped 64 bit string
		return new String(output);
	}

	//Perfroms 16 rounds of nesessary functions for 
	//DES encryption and decryption to work correctly
	//isEncrypt is true for encryption
	//isEncrypt is false for decryption
	public String round(String input, boolean isEncrypt)
	{
		//Store the left and right sub strings
		//temp is used to store the intermediate state bits 
		//strings after each function being carried out
		String left, right, temp;

		//Keys to XORed  
		String[] subKeys;

		if(isEncrypt)
		{
			//Obtain keys in order
			subKeys = key.getAllKeys();
		}
		else
		{
			//Obtain keys in reverse order
			subKeys = key.getAllKeysReverse();
		}

		//Perform 16 rounds of specific 
		//bit manipulation on the input string
		//Applying all the 16 keys where required
		for(int index = 0; index < 16; index++)
		{
			//Obtain the left part of input bit string
			left = input.substring(0, 32);

			//Obtain the right part of input bit string
			right = input.substring(32, input.length());

			//Expand the right part of bit string 
			//from 32 bits to 48 bits
			temp = expansion(right);

			//Perform 48 bit XOR operation
			//with specific keys during each interval
			//stored in subKeys array
			temp = xor(temp,subKeys[index]);

			//Perform SBOX operation
			//Converting 48 bit string
			//to 32 bits
			temp = substitution(temp);

			//Rearrange 32 bits in a 
			//specific order through 
			//permutation
			temp = fPermutation(temp);

			//Perform another 32 bit XOR operation
			//with the left substring
			temp = xor(temp, left);

			//Update the input string
			//where the left updated input sub string is
			//the previous right sub strting and 
			//right updated input string is 
			//the previous temp string obtained from running 
			//several functions 
			input = right + temp;
		}
		
		//After 16 rounds being carried out, a final swap function is carried
		//out swapping the left and right substring
		input = input.substring(32,input.length()) + input.substring(0,32);

		return new String(input);
	}

	//Performs XOR operation with two bit strings of equal length
	public String xor(String s1, String s2)
	{
		//Convert the bit string to char array
		char[] bits1 = s1.toCharArray();
		char[] bits2 = s2.toCharArray();
		
		//Check if two bit strings are equal in length
		if(bits1.length != bits2.length)
		{
			throw new IllegalArgumentException("Inputed strings are different ");
		}
		
		//Store the final XORed output
		char[] outputXOR = new char[s1.length()];

		for(int index = 0; index < s1.length(); index++)
		{

			//Set bit to 1 if two XOR bits are different
			if(bits1[index] != bits2[index])
			{
				outputXOR[index] = '1';
			}
			//Else set bit to 0
			else
			{
				outputXOR[index] = '0';
			}
		}

		return new String(outputXOR);
	}

	//Sbox

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
		String binary =	String.format("%"+ length + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
		
	}


	private String convertBinaryToString(String binary)
	{
		String word = new String(new BigInteger(binary,2).toByteArray());
	
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
		String binary = new String();
		String total = new String();
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


	private String removeExcessChar(String text)
	{
		// strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");
 
        // erases all the ASCII control characters
       	text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
         
        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");
 
        return text/*.trim()*/;
	}
	
}
