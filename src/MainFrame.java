import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.concurrent.Executor;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = -5583765686427846676L;
	
	JTextField numOfProducer1;
	JTextField numOfProducer2;
	JTextField numOfConsumer;
	JSlider jSlider1;
	JSlider jSlider2;
	JSlider jSlider3;
	int nOP1 = 2;
	int nOP2 = 2;
	int nOC = 2;
	int sOP1 = 0;
	int sOP2 = 0;
	int sOC = 0;
	int i = 0;
	JTextArea jta = new JTextArea();
	JScrollPane jScrollPane = new JScrollPane();
	JButton jbtStart;
	JButton jbtEnd;
	JButton jbtTime;
	JProgressBar jprogressBar1;
	JProgressBar jprogressBar2;
	JProgressBar jprogressBar3;
	static final int MINIMUM = 0;
	static final int MAXIMUM = 10;
	Buffer buffer1 = new Buffer(1, 10, 0);
	Buffer buffer2 = new Buffer(2, 10, 0);
	Buffer buffer3 = new Buffer(3, 10, 0);
	Semaphore mutex1 = new Semaphore(1);
	Semaphore mutex2 = new Semaphore(1);
	Semaphore mutex3 = new Semaphore(1);
	Semaphore full1 = new Semaphore(0);
	Semaphore full2 = new Semaphore(0);
	Semaphore full3 = new Semaphore(0);
	Semaphore empty1 = new Semaphore(10);
	Semaphore empty2 = new Semaphore(10);
	Semaphore empty3 = new Semaphore(10);
	Thread[] thread = new Thread[100];
	MyTimer timer = new MyTimer();

	Thread move1 = new Thread(new Mover(this, "mover1", mutex1, mutex3, full1,
			full3, empty1, empty3, buffer1, buffer3));

	Thread move2 = new Thread(new Mover(this, "mover2", mutex2, mutex3, full2,
			full3, empty2, empty3, buffer2, buffer3));
	JMenuBar MBar;
	JMenu menu;

	public MainFrame() {
		numOfProducer1 = new JTextField(2);
		numOfProducer2 = new JTextField(2);
		numOfConsumer = new JTextField(2);
		numOfProducer1.setBackground(Color.lightGray);
		numOfProducer1.setForeground(Color.blue);
		numOfProducer2.setBackground(Color.lightGray);
		numOfProducer2.setForeground(Color.blue);
		numOfConsumer.setBackground(Color.lightGray);
		numOfConsumer.setForeground(Color.blue);
		numOfProducer1.setText("2");
		numOfProducer2.setText("2");
		numOfConsumer.setText("2");
		jbtStart = new JButton("开始");
		jbtEnd = new JButton("结束");
		getRootPane().setDefaultButton(jbtStart);

		jbtTime = new JButton("统计次数");
		jbtStart.addActionListener(this);
		jbtEnd.addActionListener(this);
		jbtTime.addActionListener(this);

		jSlider1 = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
		jSlider2 = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
		jSlider3 = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
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

		Hashtable table = new Hashtable();
		table.put(new Integer(0), new JLabel("慢"));
		table.put(new Integer(25), new JLabel("中"));
		table.put(new Integer(50), new JLabel("快"));
		jSlider1.setLabelTable(table);
		jSlider2.setLabelTable(table);
		jSlider3.setLabelTable(table);

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();

		panel1.setLayout(new GridLayout(6, 2, 0, 0));
		panel1.setBorder(BorderFactory.createTitledBorder("请设置初值"));
		panel1.add(new JLabel("Buffer1的生产者个数"));
		panel1.add(numOfProducer1);
		panel1.add(new JLabel("Buffer2的生产者个数"));
		panel1.add(numOfProducer2);
		panel1.add(new JLabel("Buffer3的消费者个数"));
		panel1.add(numOfConsumer);
		panel1.add(new JLabel("Buffer1的生产者速度"));
		panel1.add(jSlider1);
		panel1.add(new JLabel("Buffer2的生产者速度"));
		panel1.add(jSlider2);
		panel1.add(new JLabel("Buffer3的消费者速度"));
		panel1.add(jSlider3);
		container.add(panel1, BorderLayout.WEST);

		panel2.setLayout(new FlowLayout());
		panel2.add(jbtStart);
		panel2.add(jbtEnd);

		panel2.add(jbtTime);

		container.add(panel2, BorderLayout.SOUTH);

		panel3.setLayout(new GridLayout(6, 1));
		jprogressBar1 = new JProgressBar();
		jprogressBar2 = new JProgressBar();
		jprogressBar3 = new JProgressBar();

		jprogressBar1.setMinimum(MINIMUM);
		jprogressBar1.setMaximum(MAXIMUM);
		jprogressBar1.setStringPainted(true);

		jprogressBar2.setMinimum(MINIMUM);
		jprogressBar2.setMaximum(MAXIMUM);
		jprogressBar2.setStringPainted(true);

		jprogressBar3.setMinimum(MINIMUM);
		jprogressBar3.setMaximum(MAXIMUM);
		jprogressBar3.setStringPainted(true);

		panel3.add(new JLabel("           buffer1的容量"));
		panel3.add(jprogressBar1);
		panel3.add(new JLabel("           buffer2的容量"));
		panel3.add(jprogressBar2);
		panel3.add(new JLabel("           buffer3的容量"));
		panel3.add(jprogressBar3);
		container.add(panel3, BorderLayout.EAST);

		container.add(jScrollPane, BorderLayout.CENTER);
		jScrollPane.getViewport().add(jta, null);

		menu = new JMenu("帮助");
		JMenuItem about = new JMenuItem("关于本软件");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "暂无信息");
			}
		});

		JMenuItem help = new JMenuItem("帮助主题");
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "暂无信息");
			}
		});
		menu.add(about);
		menu.add(help);
		MBar = new JMenuBar();
		MBar.add(menu);
		setJMenuBar(MBar);
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			pack();
		} catch (Exception ex) {
			System.out.println("Look And Feel Exception");
			System.exit(0);
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jbtStart) {
			timer.start();
			nOP1 = Integer.parseInt(numOfProducer1.getText());
			ThreadGroup p1 = new ThreadGroup("Buffer1生产进程组");
			for (i = 0; i < nOP1; i++) {
				thread[i] = new Thread(p1, new Producer(this, "Pro1" + (i + 1),
						mutex1, full1, empty1, buffer1));
			}

			nOP2 = Integer.parseInt(numOfProducer2.getText());
			ThreadGroup p2 = new ThreadGroup("Buffer2生产进程组");
			for (i = nOP1; i < nOP1 + nOP2; i++) {
				thread[i] = new Thread(p2, new Producer(this, "Pro2"
						+ (i - nOP1 + 1), mutex2, full2, empty2, buffer2));
			}

			nOC = Integer.parseInt(numOfConsumer.getText());
			ThreadGroup c = new ThreadGroup("Buffer3消费者进程组");
			for (i = nOP1 + nOP2; i < nOP1 + nOP2 + nOC; i++) {
				thread[i] = new Thread(c,
						new Consumer(this, "Con" + (i - nOP1 - nOP2 + 1),
								mutex3, full3, empty3, buffer3));
			}
			move1.start();
			move2.start();
			for (i = 0; i < nOP1 + nOP2 + nOC; i++) {
				thread[i].start();

			}
		}

		if (e.getSource() == jbtEnd) {
			move1.stop();
			move2.stop();
			for (i = 0; i < nOP1 + nOP2 + nOC; i++) {
				if(thread[i]!=null)
					thread[i].stop();

			}

		}
		if (e.getSource() == jbtTime) {
			String s = "生产者生产了    " + Producer.getProduceTimes() + " 次    "
					+ '\n' + "消费者消费了      " + Consumer.getConsumeTimes()
					+ " 次   " + '\n' + " move进行了       " + Mover.getMoveTimes()
					+ "  次       " + '\n' + " 用时: " + timer.getMinutes() + "分钟,"
					+ timer.getSeconds() + "秒";
			JOptionPane.showMessageDialog(null, s);

		}
	}

	public void stateChanged(ChangeEvent e) {
		if ((JSlider) e.getSource() == jSlider1) {
			sOP1 = jSlider1.getValue();

		}
		if ((JSlider) e.getSource() == jSlider2) {
			sOP2 = jSlider2.getValue();

		}
		if ((JSlider) e.getSource() == jSlider3) {
			sOC = jSlider3.getValue();

		}
	}

	public static void main(String[] args) {
		MainFrame myframe = new MainFrame();
		myframe.setTitle("操作系统课程设计");
		myframe.setVisible(true);
		myframe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		myframe.setSize(900, 300);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int x = (screenWidth - myframe.getWidth()) / 2;
		int y = (screenHeight - myframe.getHeight()) / 2;
		myframe.setLocation(x, y);
	}

}