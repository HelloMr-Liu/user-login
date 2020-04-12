import java.util.concurrent.CyclicBarrier;

// ThreadLocal使用示例 刘梓江牛逼
public class ThreadLocalUtil {
    private static final ThreadLocal<MyThread> testThreadLocal = new ThreadLocal<>();



    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4);
        for (int i = 0; i < 4; i ++) {
            new Thread(new TestThread(barrier),"刘大帅哥").start();
        }
    }

    static class TestThread implements Runnable{
        private CyclicBarrier barrier;

        public TestThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }


        @Override
        public void run() {
            try {
                if (testThreadLocal.get()==null) {
                    testThreadLocal.set(new MyThread());
                }
                barrier.await();
                for (int i = 0; i < 10; i++) {
                    boolean  myFalg=testThreadLocal.get().flag;
                    System.out.println(Thread.currentThread().getName()+"前"+testThreadLocal.get().flag);
                    if (myFalg) {
                        testThreadLocal.get().flag=false;
                        System.out.println(Thread.currentThread().getName()+"修改后"+testThreadLocal.get().flag);
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class MyThread{
    public    boolean  flag=true;

}