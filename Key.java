public class Key
{
	private String[] keySeq;
	
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
		keySeq = new String[16];
		keySeq = generateSubKeys(inKey); 	
	}

	public String[] getAllKeys()
	{
		String[] copyKeys = new String[16];

		for(int ii = 0; ii < keySeq.length; ii++)
		{
			copyKeys[ii] = keySeq[ii];
		}

		return copyKeys;
	}

	public String[] getAllKeysReverse()
	{
		String[] copyKeys = new String[16];

		for(int ii = 0; ii < keySeq.length; ii++)
		{
			copyKeys[ii] = keySeq[keySeq.length - 1 - ii];
		}

		return copyKeys;
	}

	public String getKey(int index)
	{
		return new String(keySeq[index]);
	}

	//Convert 64 bit key to 56 bit key
	public String pcFirst(String key)
	{
		char[] output = new char[56];

		for(int ii = 0; ii < 56; ii++)
		{
			output[ii] = key.charAt(PC1[ii / 7][ii % 7] - 1);
		}

		return new String(output);
	}

	//Convert 56 bit key to 48 bit key
	public String pcSecond(String key)
	{
		char[] output = new char[48];

		for(int ii = 0; ii < 48; ii++)
		{
			output[ii] = key.charAt(PC2[ii / 6][ii % 6] - 1);
		}

		return new String(output);
	}

	//Obtain 16 (48 bit) keys
	public String[] generateSubKeys(String key)
	{
		String[] subKeys = new String[16];

		//Obtain the 56 bit permuted key from 64 bit key
		String firstOutput = this.pcFirst(key);

		String c = firstOutput.substring(0,28);
		String d = firstOutput.substring(28,firstOutput.length());

		for(int round = 1; round <= 16; round++)
		{
			//Left shift 1 bit
			if(round == 1 || round == 2 || round == 9 || round == 16)
			{
				c = c.substring(1,c.length()) + c.charAt(0);
				d = d.substring(1,d.length()) + d.charAt(0);
			}
			//Left shift 2 bits
			else
			{
				c = c.substring(2,c.length()) + c.substring(0,2);
				d = d.substring(2,d.length()) + d.substring(0,2);
			}
			
			//Merge c and d back together
			String merge = c + d;

			//Perform second permutation generating 48 bit key sequence
			subKeys[round - 1] = this.pcSecond(merge);

			System.out.println("Generated key " + round + " : " + subKeys[round - 1]);
		}

		return subKeys;

	}





}


