public class Consumer implements Runnable {
	private Semaphore mutex, full, empty;
	private Buffer buf;
	private String name;
	private MainFrame frame;
	private static int timesOfConsumer;

	public Consumer(MainFrame frame, String name, Semaphore mutex,
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
			full.p();
			mutex.p();
			frame.jta.append("Consumer " + name + " get a product from "
					+ buf.number + "\n");
			timesOfConsumer++;
			buf.value--;
			frame.jprogressBar3.setValue(buf.value);
			frame.jprogressBar3.setString(String.valueOf(buf.value));
			mutex.v();
			empty.v();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}
		}
	}

	public static int getConsumeTimes() {
		return timesOfConsumer;
	}
}