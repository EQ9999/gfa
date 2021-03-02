package test.gfa;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import reactor.core.publisher.Mono;

/**
 * 
 * @author deyu.he
 *
 */
public class Test2 {
	ReentrantLock rl =new ReentrantLock();
	public void test() {
		try {
			boolean tryLock = rl.tryLock(1, TimeUnit.SECONDS);
			
			if(tryLock) {
			System.out.println("xxx");
			Thread.sleep(3000);
			}else {
				System.out.println("88888");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			rl.unlock();
		}
	}
	
	
	public static void main(String[] args) {
		Test2 t2=new Test2();
		new Thread() {
			public void run() {

				System.out.println("????xxx");
				t2.test();
			};
		}.start();
		new Thread() {
			public void run() {
				System.out.println("????");
				t2.test();
			};
		}.start();
	}
}
