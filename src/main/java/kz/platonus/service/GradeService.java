package kz.platonus.service;

import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.util.List;

public class GradeService {

    private final GradeDAO gradeDAO;

    public GradeService() {
        this.gradeDAO = new GradeDAO();
    }

    public GradeService(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    // Баға қосу
    public void addGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException(
                    "Grade cannot be null"
            );
        }

        if (grade.getScore() < 0 || grade.getScore() > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100"
            );
        }

        gradeDAO.addGrade(grade);
    }

    // Бағаны жаңарту
    public void updateGrade(int gradeId, double score) {
        Grade grade = gradeDAO.findById(gradeId);

        if (grade == null) {
            throw new IllegalArgumentException(
                    "Grade not found: " + gradeId
            );
        }

        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100"
            );
        }

        grade.setScore(score);
        gradeDAO.updateGrade(grade);
    }

    // Студенттің барлық бағаларын алу
    public List<Grade> getStudentGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId);
    }

    // Курстың бағаларын алу
    public List<Grade> getCourseGrades(int courseId) {
        return gradeDAO.findByCourseId(courseId);
    }

    // Орташа балл есептеу
    public double calculateAverageGrade(int studentId) {
        List<Grade> grades = gradeDAO.findByStudentId(studentId);

        if (grades.isEmpty()) {
            return 0.0;
        }

        double average = grades.stream()
                .mapToDouble(Grade::getScore)
                .average()
                .orElse(0.0);

        return average;
    }

    // Бағалар санын анықтау
    public int countGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId).size();
    }

    // Ең жоғары бағаны анықтау
    public double getHighestGrade(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .mapToDouble(Grade::getScore)
                .max()
                .orElse(0.0);
    }

    // Ең төменгі бағаны анықтау
    public double getLowestGrade(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .mapToDouble(Grade::getScore)
                .min()
                .orElse(0.0);
    }
}