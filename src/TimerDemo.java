import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class TimerDemo implements ActionListener{
	Timer timer;
	JLabel label;
	int hours=0,minutes=0,seconds=-1;
	public TimerDemo(){
		timer=new Timer(1000,this);
		}
		public void actionPerformed(ActionEvent e){
		           	seconds++;
                	if(seconds==60){
		         	minutes++;
		         	seconds-=60;
		                       if(minutes==60){
		                       	  hours++;
		                       	  minutes-=60;
		          	                     
		          	                      }
		                        }
		            }
	
public int getMinutes(){
	return minutes;
}
public int getSeconds(){
	return seconds;
}
}