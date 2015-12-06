import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
public class MainFrame extends JFrame implements ActionListener,ChangeListener{
   
	JTextField numOfProducer1;
	JTextField numOfProducer2;
	JTextField numOfConsumer;
	JSlider jSlider1;
	JSlider jSlider2;
	JSlider jSlider3;
	int nOP1=2;
	int nOP2=2;
	int nOC=2;
	int sOP1=0;
	int sOP2=0;
	int sOC=0;
	int i=0;		
	JTextArea 	jta=new JTextArea();
	JScrollPane jScrollPane=new JScrollPane();
	JButton jbtStart;
	JToggleButton jbtStop;
	JButton jbtEnd;
	JButton jbtTime;
	JProgressBar jprogressBar1;
	JProgressBar jprogressBar2;
	JProgressBar jprogressBar3;
	static final int MINIMUM=0;
	static final int MAXIMUM=10;
    Buffer buffer1=new Buffer(1,10,0);
    Buffer buffer2=new Buffer(2,10,0);
    Buffer buffer3=new Buffer(3,10,0);
    Semaphore mutex1=new Semaphore(1);
    Semaphore mutex2=new Semaphore(1);
    Semaphore mutex3=new Semaphore(1);
    Semaphore full1=new Semaphore(0);
    Semaphore full2=new Semaphore(0);
    Semaphore full3=new Semaphore(0);
    Semaphore empty1=new Semaphore(10);
    Semaphore empty2=new Semaphore(10);
    Semaphore empty3=new Semaphore(10);
    Thread[] thread =new Thread[100];
    TimerDemo time=new TimerDemo();

    Thread move1=new Thread(new Mover(this,"mover1",mutex1,mutex3,full1,full3,empty1,empty3,buffer1,buffer3));	
   
    Thread move2=new  Thread(new Mover(this,"mover2",mutex2,mutex3,full2,full3,empty2,empty3,buffer2,buffer3));
   JMenuBar MBar;
   JMenu menu;
	public MainFrame(){
		numOfProducer1=new JTextField(2);
		numOfProducer2=new JTextField(2);
		numOfConsumer=new JTextField(2);
		numOfProducer1.setBackground(Color.lightGray);
		numOfProducer1.setForeground(Color.blue);
		numOfProducer2.setBackground(Color.lightGray);
		numOfProducer2.setForeground(Color.blue);
		numOfConsumer.setBackground(Color.lightGray);
		numOfConsumer.setForeground(Color.blue);
		numOfProducer1.setText("2");
		numOfProducer2.setText("2");
		numOfConsumer.setText("2");
		jbtStart=new JButton("��ʼ");
		jbtStop=new JToggleButton("��ͣ");
		jbtEnd=new JButton("����");
		getRootPane().setDefaultButton(jbtStart);
	
		jbtTime=new JButton("ͳ�ƴ���");
        jbtStart.addActionListener(this);
     	jbtStop.addActionListener(this);
    	jbtEnd.addActionListener(this);
    	jbtTime.addActionListener(this);
    		
		jSlider1=new JSlider(JSlider.HORIZONTAL,0,50,25);
		jSlider2=new JSlider(JSlider.HORIZONTAL,0,50,25);		
		jSlider3=new JSlider(JSlider.HORIZONTAL,0,50,25); 
		jSlider1.addChangeListener(this);
		jSlider2.addChangeListener(this);	
		jSlider3.addChangeListener(this);   	
        jSlider1.setPaintTicks(true);
	 jSlider1.setMajorTickSpacing(20);
	 jSlider1.setMinorTickSpacing(10);
	 jSlider1.setPaintLabels(true);
     jSlider2.setPaintTicks(true);
	 jSlider2.setMajorTickSpacing(20);
	 jSlider2.setMinorTickSpacing(10);
	 jSlider2.setPaintLabels(true);
	      jSlider3.setPaintTicks(true);
	 jSlider3.setMajorTickSpacing(20);
	 jSlider3.setMinorTickSpacing(10);
	 jSlider3.setPaintLabels(true);
	
			 Hashtable table=new Hashtable();
	 table.put(new Integer(0),new JLabel("��"));
	 table.put(new Integer(25),new JLabel("��"));
	 table.put(new Integer(50),new JLabel("��"));
	 jSlider1.setLabelTable(table);
	 jSlider2.setLabelTable(table);
	 jSlider3.setLabelTable(table);
    		
		Container container=this.getContentPane();
		container.setLayout(new BorderLayout());
		JPanel panel1=new JPanel();
		JPanel panel2=new JPanel();
		JPanel panel3=new JPanel();
		
		panel1.setLayout(new GridLayout(6,2,0,0));
		panel1.setBorder(BorderFactory.createTitledBorder("�����ó�ֵ"));
		panel1.add(new JLabel("Buffer1�������߸���"));
		panel1.add(numOfProducer1);
		panel1.add(new JLabel("Buffer2�������߸���"));
		panel1.add(numOfProducer2);
		panel1.add(new JLabel("Buffer3�������߸���"));
		panel1.add(numOfConsumer);
		panel1.add(new JLabel("Buffer1���������ٶ�"));
		panel1.add(jSlider1);
		panel1.add(new JLabel("Buffer2���������ٶ�"));
		panel1.add(jSlider2);
		panel1.add(new JLabel("Buffer3���������ٶ�"));
		panel1.add(jSlider3);		
		container.add(panel1,BorderLayout.WEST);
		
		panel2.setLayout(new FlowLayout());
	    panel2.add(jbtStart);
	    panel2.add(jbtStop);
	    panel2.add(jbtEnd);

	    panel2.add(jbtTime);
	  
	    container.add(panel2,BorderLayout.SOUTH);	
	
		panel3.setLayout(new GridLayout(6,1));
		jprogressBar1=new 	JProgressBar ();
		jprogressBar2=new 	JProgressBar ();
		jprogressBar3=new 	JProgressBar ();
	 
	    jprogressBar1.setMinimum(MINIMUM);
	    jprogressBar1.setMaximum(MAXIMUM);
	    jprogressBar1.setStringPainted(true);
	     
	    jprogressBar2.setMinimum(MINIMUM);
	    jprogressBar2.setMaximum(MAXIMUM);
	    jprogressBar2.setStringPainted(true);
	      
	    jprogressBar3.setMinimum(MINIMUM);
	    jprogressBar3.setMaximum(MAXIMUM);
        jprogressBar3.setStringPainted(true);

        panel3.add(new JLabel("           buffer1������"));
        panel3.add(jprogressBar1);
        panel3.add(new JLabel("           buffer2������"));
        panel3.add(jprogressBar2);
        panel3.add(new JLabel("           buffer3������"));
        panel3.add(jprogressBar3);
        container.add(panel3,BorderLayout.EAST);
        
	    container.add(jScrollPane,BorderLayout.CENTER);
	    jScrollPane.getViewport().add(jta,null);
	    
	    menu=new JMenu("����");
	    JMenuItem about=new JMenuItem("���ڱ����");
	    about.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		JOptionPane.showMessageDialog(null,"������Ϣ");
	    	}
	    });
	    	
	    JMenuItem help=new JMenuItem("��������"); 
	       help.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		JOptionPane.showMessageDialog(null,"������Ϣ");
	    	}
	    });
	    menu.add(about);
	    menu.add(help);   
	    MBar=new JMenuBar();
	    MBar.add(menu);
	    setJMenuBar(MBar);
	    try{
				//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);
				pack();
			}
			catch(Exception ex){
				System.out.println("Look And Feel Exception");
				System.exit(0);
			}
        
	 }
	  
	public void actionPerformed(ActionEvent e){

	    if(e.getSource()==jbtStart){
	    	time.timer.start();
			nOP1=Integer.parseInt(numOfProducer1.getText());
	        ThreadGroup p1=new ThreadGroup("Buffer1����������");			
            for(i=0;i<nOP1;i++){
        	   thread[i]=new Thread(p1,new Producer(this,"Pro1"+(i+1),mutex1,full1,empty1,buffer1));			
			}
		

			nOP2=Integer.parseInt(numOfProducer2.getText());
			ThreadGroup p2=new ThreadGroup("Buffer2����������");
			for(i=nOP1;i<nOP1+nOP2;i++){
        	   thread[i]=new Thread(p2,new Producer(this,"Pro2"+(i-nOP1+1),mutex2,full2,empty2,buffer2));
			}			
		
			nOC=Integer.parseInt(numOfConsumer.getText());
			ThreadGroup c=new ThreadGroup("Buffer3�����߽�����");
			for(i=nOP1+nOP2;i<nOP1+nOP2+nOC;i++){
        	   thread[i]=new Thread(c,new Consumer(this,"Con"+(i-nOP1-nOP2+1),mutex3,full3,empty3,buffer3));
			}		
	        	move1.start();
	    		move2.start();
	    			for(i=0;i<nOP1+nOP2+nOC;i++){
	    		thread[i].start();
	    	
	        }
	    }

	     if(e.getSource()==jbtEnd){
	    	   	move1.stop();
	    		move2.stop();
	    			for(i=0;i<nOP1+nOP2+nOC;i++){
	    		thread[i].stop();
	    	
	        }
	        
	    
	    }
	    if(e.getSource()==jbtTime){
	        String s="������������    "+Producer.getProduceTimes()+" ��    "+'\n'+
	           "������������      "+Consumer.getConsumeTimes()+" ��   "    +'\n'+
	           " move������       "+Mover.getMoveTimes()+"  ��       "+'\n'+
	           " ��ʱ: "+time.getMinutes()+"����,"+time.getSeconds()+"��";
	           JOptionPane.showMessageDialog(null,s);
	           
	    }      
	     if(e.getActionCommand().equals("��ͣ")){
	     	time.timer.stop();
	    	   	move1.suspend();
	    		move2.suspend();
	    			for(i=0;i<nOP1+nOP2+nOC;i++){
	    		thread[i].suspend();
	        }
          jbtStop.setText("����");
	    }
	    	  
	    	    if(e.getActionCommand().equals("����")){
	    	time.timer.restart();
	    	   	move1.resume();
	    		move2.resume();
	    			for(i=0;i<nOP1+nOP2+nOC;i++){
	    		thread[i].resume();
	    		jbtStop.setText("��ͣ");
	        }
	    }  
 
	}
	public void stateChanged(ChangeEvent e){
		if((JSlider)e.getSource()==jSlider1){
			sOP1=jSlider1.getValue();
			
		}
		if((JSlider)e.getSource()==jSlider2){
			sOP2=jSlider2.getValue();
		
		}
		if((JSlider)e.getSource()==jSlider3){
			sOC=jSlider3.getValue();
		
		}				
	}
    public static void main(String[] args){
		MainFrame myframe=new MainFrame();
		myframe.setTitle("����ϵͳ�γ����");
		myframe.setVisible(true);
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myframe.setSize(900,300);
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth=screenSize.width;
		int screenHeight=screenSize.height;
		int x=(screenWidth-myframe.getWidth())/2;
		int y=(screenHeight-myframe.getHeight())/2;
		myframe.setLocation(x,y);  
	}	 
	
} 