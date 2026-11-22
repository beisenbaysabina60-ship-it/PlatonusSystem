package kz.platonus.concurrency;

import kz.platonus.model.Grade;

import java.util.List;
import java.util.concurrent.Callable;

public class GradeProcessingTask implements Callable<Double> {

    private final List<Grade> grades;

    public GradeProcessingTask(List<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public Double call() {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double total = 0;

        for (Grade grade : grades) {
            total += grade.getScore();
        }

        return total / grades.size();
    }
}