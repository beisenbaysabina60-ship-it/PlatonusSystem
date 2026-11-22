package kz.platonus.concurrency;

import kz.platonus.model.Grade;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GradeConcurrencyService {

    private final ExecutorService executorService;

    public GradeConcurrencyService() {
        this.executorService =
                Executors.newFixedThreadPool(3);
    }

    // Default thread pool
    public Future<Double> calculateAverageAsync(
            List<Grade> grades) {

        GradeProcessingTask task =
                new GradeProcessingTask(grades);

        return executorService.submit(task);
    }

    // Custom thread pool
    public Future<Double> calculateAverageAsync(
            List<Grade> grades, int threadCount) {

        if (threadCount <= 0) {
            throw new IllegalArgumentException(
                    "Thread count must be positive");
        }

        ExecutorService customExecutor =
                Executors.newFixedThreadPool(threadCount);

        GradeProcessingTask task =
                new GradeProcessingTask(grades);

        Future<Double> result =
                customExecutor.submit(task);

        customExecutor.shutdown();

        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}