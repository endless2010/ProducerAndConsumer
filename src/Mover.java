public class Mover implements Runnable{
	private Semaphore mutex1,mutex2,full1,full2,empty1,empty2;
	private Buffer buf1,buf2;
	private String name;
	private MainFrame frame;
	private static int moveTimes;
	public Mover(MainFrame frame,String name,Semaphore mutex1,Semaphore mutex2,Semaphore full1,Semaphore full2,
	Semaphore empty1,Semaphore empty2,Buffer buf1,Buffer buf2){
		this.mutex1=mutex1;
		this.mutex2=mutex2;
		this.full1=full1;
		this.full2=full2;
		this.empty1=empty1;
		this.empty2=empty2;
		this.buf1=buf1;
		this.buf2=buf2;
		this.name=name;
		this.frame=frame;
	}
	public void run(){
		while(true){
			full1.p();
			empty2.p();
			mutex1.p();
			mutex2.p();
			frame.jta.append("Mover "+name+" move a product from Buffer "+buf1.number+" to Buffer "+buf2.number+"\n");
		    moveTimes++;
			buf1.value--;
			buf2.value++;
			if(buf1.number==1){
				frame.jprogressBar1.setValue(buf1.value);
				frame.jprogressBar1.setString(String.valueOf(buf1.value));
			}else{
				frame.jprogressBar2.setValue(buf1.value);
				frame.jprogressBar2.setString(String.valueOf(buf1.value));
			}
			frame.jprogressBar3.setValue(buf2.value);
			frame.jprogressBar3.setString(String.valueOf(buf2.value));
			mutex1.v();
			mutex2.v();
			empty1.v();
			full2.v();
		}
	}
	public static int getMoveTimes(){
	return moveTimes;
	}
}