package ch.specchio.eav_db;

public class StringServices {
	
	// to far only '\' chars are escaped
	public static String EscapeString(String in)
	{
		StringBuffer sb = new StringBuffer(in);
		StringBuffer out = new StringBuffer();
		
		int i;
		char c;
		for (i=0; i < sb.length(); i++)
		{
			c = sb.charAt(i);
			if(c == '\\')
			{
				// insert a second \ to escape it
				out.append('\\');	
			}	
			out.append(c);			
		}		
		return out.toString();
	}
	
	public static String ConcatString(String str1, String str2, String join_str)
	{
		if(str1 == null && str2 == null) 
			return "";
	
	   if(str1 == null || str1.equals(""))
		   return str2;
	   
	   if(str2 == null || str2.equals(""))
		   return str1;
	   else
		   return str1.concat(join_str + str2);
	}
	
	// return a substring of length len or shorter depending on if the character c was found. if not found with the len characters
	// from the end, the substring with len chars is returned
	public static String intelligent_substr(String input, char c, int len)
	{
		int ind = input.length() - 1;
		int last_char_ind = input.length();
		
		while(ind > input.length() - len)
		{
			if(input.charAt(ind) == c)
				last_char_ind = ind;
			
			ind--;
		}
		
		int ret_ind = last_char_ind;
		
		if(ret_ind == input.length())
			ret_ind = ind;		 
		
		return input.substring(ret_ind);
		
	}

}
