import java.lang.Thread;

public class App extends Thread {

    public void run() {
        for (int i = 0; i< 5; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        App task1 = new App();
        App task2 = new App();


        task1.start();
        task2.start();
    }
}
