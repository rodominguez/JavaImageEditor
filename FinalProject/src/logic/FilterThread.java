package logic;

public class FilterThread implements Runnable{
	
	private Thread thread;
	
	private int start, end;
	
	private Filter filter;
	
	public FilterThread(int start, int end) {
		this.start = start;
		this.end = end;
		filter = new Filter();
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		try {
		for (int i = start; i < end; i++) { //From start to end
			for (int j = 0; j < Image.getHeight(); j++) {
				Filter.filter(Image.imageMatrix, i, j);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
