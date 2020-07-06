package app.netrix.ngorika.utils;

import android.content.Context;


public class Convert {
	//convert DB to pixel
	public static int PixelFromDp(Context context,float dp){
		return (int)(dp*context.getResources().getDisplayMetrics().density);
	}
	public static int DpFromPixel(Context context,int pixel){
		return (int)(pixel/context.getResources().getDisplayMetrics().density);
	}
	public static int getLimit(String value,String key,int defaultValue){
		int result=defaultValue;
		if(value!=null && value.length()>0){
			int pos=value.indexOf(key);
			String basic=value.substring(pos+key.length()).trim();
			String end="";
			for (int i=0;i<basic.length();i++) {
				Character type=basic.charAt(i);
				if(type!=' '){
					end+=type;
				}else break;
			}
			try{
				result= Integer.parseInt(end);
			}catch(Exception e){
				
			}
			
		}
		return result;	
				
	}
	/*public static void appendLog(String text,String vl)
	{       
	   File logFile = new File(Environment.getExternalStorageDirectory()+"/logMagpi.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	   }
	   try
	   {
		   
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text+"|"+vl);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}	*/
}
