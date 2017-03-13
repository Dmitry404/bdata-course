package loader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

class NumbersSupplier {
  private final int parallelism;
  private CountDownLatch doneSignal = new CountDownLatch(1);

  NumbersSupplier(int parallelism) {
    this.parallelism = parallelism;
  }

  void loadTo(BiConsumer<Integer, String> consumer, int maxId) {
    ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
    forkJoinPool.submit(() -> {
      ThreadLocalRandom.current().ints(Long.MAX_VALUE, 1, maxId)
          .parallel()
          .forEach(id -> consumer.accept(id, Thread.currentThread().getName()));
      doneSignal.countDown();
    });

    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
