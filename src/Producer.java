public class Producer implements Runnable {
	private Semaphore mutex, full, empty;
	private Buffer buf;
	private String name;
	private MainFrame frame;
	private static int timesOfProducer;

	public Producer(MainFrame frame, String name, Semaphore mutex,
			Semaphore full, Semaphore empty, Buffer buf) {
		this.mutex = mutex;
		this.full = full;
		this.empty = empty;
		this.buf = buf;
		this.name = name;
		this.frame = frame;
	}

	public void run() {
		while (true) {
			empty.p();
			mutex.p();
			frame.jta.append("Producer " + name + " insert a new product into "
					+ buf.number + "\n");
			timesOfProducer++;
			buf.value++;
			if (buf.number == 1) {
				frame.jprogressBar1.setValue(buf.value);
				frame.jprogressBar1.setString(String.valueOf(buf.value));
			} else {
				frame.jprogressBar2.setValue(buf.value);
				frame.jprogressBar2.setString(String.valueOf(buf.value));
			}
			mutex.v();
			full.v();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}

	public static int getProduceTimes() {
		return timesOfProducer;
	}
}