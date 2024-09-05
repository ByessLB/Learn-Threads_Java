import java.util.concurrent.Executors;
// import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/*
    newSingleThreadExecutor
    newCachedThreadPool
    newFixedThreadPool

    isDone(), isCanceled()
    get(), cancel()
 */

public class Main {
    public static void main(String[] args) {
        // Executor ex = Executors.newSingleThreadExecutor();  // newSingleThreadExecutor : 1 seul threads pour le job
        ExecutorService ex = Executors.newCachedThreadPool();

        Runnable task1 = () -> {
            for (int i = 0; i<5; i++) {
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                }
            }
        };
        Runnable task2 = () -> {
            for (int i = 5; i<=9; i++) {
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                }
            }
        };

        // ex.execute(task1);
        // ex.execute(task2);

        Future<String> fut1 = ex.submit(task1, "Je suis Jean, j'ai fini");
        Future<String> fut2 = ex.submit(task2, "Je suis Marie, j'ai fini aussi");

        while (!fut1.isDone() || !fut2.isDone()) {
            System.out.println("On attend...");
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {}
        }

        if (fut1.isDone()) {
            try {
            System.out.println(fut1.get());
            } catch(Exception e) {}
        }

        if (fut2.isDone()) {
            try {
            System.out.println(fut2.get());
            } catch(Exception e) {}
        }

        ex.shutdown();
    }
}
