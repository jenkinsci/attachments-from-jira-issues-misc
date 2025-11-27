import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;


public static void main(String args[]) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("_MMddyy_hhmmss");
		String timeStamp = sdf.format(new Date());
		int delay = testDelay - singleInsertDelay;
		List<String> commandsToSend = new ArrayList<>(); //array to store the commands before sending to console
		
				
		Session session = LinuxConnect.Connect(); //getting the linux session
		
		channel = session.openChannel("shell"); //setting up shell communication to send multiple commands
		DataOutputStream commandToServer = new DataOutputStream(channel.getOutputStream()); //variable to send the commands to the linux console
		
		channel.connect(); //opening communication channel in host
		
		commandsToSend.add("mkdir scriptResults");
		commandsToSend.add("./shell_script &"); //The script takes 20 minutes to complete the execution
	   	
					
		long startTime = System.currentTimeMillis();
		System.out.println("Test Started: "+new Date());
	   	for(int count=0; count<commandsToSend.size(); count++)
		{   //this loop will send the lot_test commands and a delay of 1 second each 10 users
	   		System.out.println(commandsToSend.get(count));
	   	   	commandToServer.writeBytes(commandsToSend.get(count)+"\r");//sending the command
	   	   	commandToServer.flush();//flushing the variable force the terminal to execute the command
	   	   	Thread.sleep(385);
	    }
	   		   	
	   	System.out.println("Waiting 1 minute to validate test execution");//delay added to allow the linux commands start in the console
	   	TimeUnit.MINUTES.sleep(1);
	   	
	   	String readLine = null;
	   	while((readLine = LinuxConnect.VerifyExecution("ps -u user | grep \"shell_script\""))!=null)
	   	{	//this loop will check if the test execution is still in progress
	   		System.out.println(readLine);//prints output of the verification command
	   		System.out.println("Test still running");
	   		System.out.println("Waiting 1 minute(s) for recheck....");
	   		TimeUnit.MINUTES.sleep(1);
	   	}
	   	
	   	   	
	   	long endTime = System.currentTimeMillis();
	   	testExecutionLog.add("Test Completed: "+new Date());
	   	long diff = endTime - startTime; 
	   	String hms = String.format("%02d hour(s), %02d minute(s), %02d second(s)", 
				TimeUnit.MILLISECONDS.toHours(diff),
				TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
				TimeUnit.MILLISECONDS.toSeconds(diff) -	TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));   
	   	System.out.println("Test Elapsed Time: " + hms);
        
        commandToServer.close();//closing linux console
	    channel.disconnect();//closing session
        session.disconnect();//closing connection to server
        
        				       
	}