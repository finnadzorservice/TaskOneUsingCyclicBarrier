import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;

public class MainCyclicBarrier {
    CyclicBarrier cyclicBarrierSecond;
    CyclicBarrier cyclicBarrierThird;

    public MainCyclicBarrier() {
        cyclicBarrierSecond = new CyclicBarrier(2);
        cyclicBarrierThird = new CyclicBarrier(2);
    }

    public void first() {
            System.out.print("first");
            cyclicBarrierSecond.reset();
    }

    public void second() {
        try {
            cyclicBarrierSecond.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.print("second");
            cyclicBarrierThird.reset();
        }
    }

    public void third() {
        try {
            cyclicBarrierThird.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.print("third");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MainCyclicBarrier mainCyclicBarrier = new MainCyclicBarrier();

        CompletableFuture<Void> doSecond = CompletableFuture.runAsync(mainCyclicBarrier::second);
        CompletableFuture<Void> doThird = CompletableFuture.runAsync(mainCyclicBarrier::third);
        CompletableFuture<Void> doFirst = CompletableFuture.runAsync(mainCyclicBarrier::first);
        doThird.get();
        doFirst.get();
        doSecond.get();
    }
}
