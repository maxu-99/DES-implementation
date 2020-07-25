/* Author : Mahmudul Hossain (19303235)
 * Purpose : This class contains 16 sub-keys generated from
 * 			 the hexadecimal key input, also including
 * 			 all the necessary functions required to generate
 * 			 these sub keys which will then be used during
 * 			 des encryption and decryption
 * Last modified : 18/04/2020
 */

import java.math.BigInteger;
public class Key
{
	//Store the binary equivalent of the hexadecimal key inputted
	//Size of 64 bits
	private String bin;

	//Store the 48 bit keys generated 
	private String[] keySeq;

	//Permuted choice 1
	//Rearrangment of the 64 bit key to 56 bit key
	//where every multiple of 8th bit of the initial 
	//key is also not present		
	public static final int[][] PC1 = new int[][]
	{
		{57,49,41,33,25,17,9},
		{1,58,50,42,34,26,18},
		{10,2,59,51,43,35,27},
		{19,11,3,60,52,44,36},
		{63,55,47,39,31,23,15},
		{7,62,54,46,38,30,22},
		{14,6,61,53,45,37,29},
		{21,13,5,28,20,12,4}
	};

	//Permuted choice 2
	//Rearrangement of the 56 bit key to 48 bit key
	public static final int[][] PC2 = new int[][]
	{
		{14,17,11,24,1,5},
		{3,28,15,6,21,10},
		{23,19,12,4,26,8},
		{16,7,27,20,13,2},
		{41,52,31,37,47,55},
		{30,40,51,45,33,48},
		{44,49,39,56,34,53},
		{46,42,50,36,29,32}
	};

	//Default constructor
	public Key(String inKey)
	{
		//Store the binary equivalent of the hexadecimal key
		bin = new String(convertHexToBin(inKey));

		//Create 16 entries to store 48-bit sub keys 
		keySeq = new String[16];

		//Create 16 48-bit sub-keys using bin and store them
		keySeq = generateSubKeys(bin); 	
	}

	//Obtain the sequence of sub keys generated in order
	//Used during encryption
	public String[] getAllKeys()
	{
		//Obtain of a copy of all sub keys stored
		String[] copyKeys = new String[16];

		for(int ii = 0; ii < keySeq.length; ii++)
		{
			copyKeys[ii] = keySeq[ii];
		}

		return copyKeys;
	}

	//Obtain the sequence of sub keys generated in reverse order
	//Used during decryption
	public String[] getAllKeysReverse()
	{
		//Obtain of a copy of all sub-keys stored 
		//which will be in reverse order
		String[] copyKeys = new String[16];

		for(int ii = 0; ii < keySeq.length; ii++)
		{
			copyKeys[ii] = keySeq[keySeq.length - 1 - ii];
		}

		return copyKeys;
	}

	//Convert 64 bit key to 56 bit key
	public String pcFirst(String key)
	{
		//Store the updated mapping of 56 bit string
		char[] output = new char[56];
		
		//Set each index in the output array to the 
		//corresponding permuted index of key string 
		//where the permuted index is obtained from PC1
		//Each permuted index has to be subtracted by 1 
		//since array starts from 0
		for(int ii = 0; ii < 56; ii++)
		{
			output[ii] = key.charAt(PC1[ii / 7][ii % 7] - 1);
		}
		
		//Return the newly mapped 56 bit string
		return new String(output);
	}

	//Convert 56 bit key to 48 bit key
	public String pcSecond(String key)
	{
		//Store the updated mapping of 48 bit string
		char[] output = new char[48];

		//Set each index in the output array to the 
		//corresponding permuted index of key string 
		//where the permuted index is obtained from PC2
		//Each permuted index has to be subtracted by 1 
		//since array starts from 0
		for(int ii = 0; ii < 48; ii++)
		{
			output[ii] = key.charAt(PC2[ii / 6][ii % 6] - 1);
		}

		//Return the newly mapped 48 bit string
		return new String(output);
	}

	//Obtain 16 (48 bit) keys using appropriate functions where
	//necessary
	//The key imported is the initial 64 bit hexadecimal key
	public String[] generateSubKeys(String key)
	{
		//16 sub-keys to be stored
		String[] subKeys = new String[16];

		//Obtain the 56 bit permuted key from 64 bit key
		String firstOutput = this.pcFirst(key);

		//Split the 56 bit key string, intially
		
		//Left-half
		String c = firstOutput.substring(0,28);

		//Right half
		String d = firstOutput.substring(28,firstOutput.length());

		//Perform 16 rounds of key generation where in each round
		//the left(c) and right(d) refers to the previous round
		//During the left shift, the left-most bit traverses back to
		//right-most bit for each left and right halves respectively
		//The centre bits traverses by 1 or 2 bits to the left
		for(int round = 1; round <= 16; round++)
		{
			//Left circular shift by 1 bit for these specific rounds
			if(round == 1 || round == 2 || round == 9 || round == 16)
			{
				//Update the left and right half with 1 left shift
				c = c.substring(1,c.length()) + c.charAt(0);
				d = d.substring(1,d.length()) + d.charAt(0);
			}
			//Left circular shift by 2 bits
			else
			{
				//Update the left and right half with 2 left shifts
				c = c.substring(2,c.length()) + c.substring(0,2);
				d = d.substring(2,d.length()) + d.substring(0,2);
			}
			
			//Merge the updated left-half(c) and right-half(d)
			//back together
			String merge = c + d;

			//Perform second permutation on the merged string 
			//generating 48 bit key sequence 
			//The final product is the sub-key generated which is 
			//stored at appropriate index
			subKeys[round - 1] = this.pcSecond(merge);

		}

		return subKeys;

	}
	
	//Convert the hexadecimal string to appropriate binary form
	private String convertHexToBin(String hex)
	{
		//Obtain the binary equivalent 
		String binary = new BigInteger(hex,16).toString(2);

		//Apply necessary padding of 0s where necessary
		binary = String.format("%64s",binary).replace(" ", "0");
		
		//Return the binary equivalent of hexadecimal string
		return binary;
	}

}
