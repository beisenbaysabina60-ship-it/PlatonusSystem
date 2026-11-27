package kz.platonus.service;

import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class GradeStatisticsService {

    private final GradeDAO gradeDAO;

    public GradeStatisticsService() {
        this.gradeDAO = new GradeDAO();
    }

    public GradeStatisticsService(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    // 90 және одан жоғары бағаларды алу
    public List<Grade> getExcellentGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .filter(grade -> grade.getScore() >= 90)
                .collect(Collectors.toList());
    }

    // 50-ден төмен бағаларды алу
    public List<Grade> getFailedGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .filter(grade -> grade.getScore() < 50)
                .collect(Collectors.toList());
    }

    // Бағаларды өсу ретімен сорттау
    public List<Grade> getGradesSortedAscending(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .sorted((g1, g2) ->
                        Double.compare(g1.getScore(), g2.getScore()))
                .collect(Collectors.toList());
    }

    // Бағаларды кему ретімен сорттау
    public List<Grade> getGradesSortedDescending(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .sorted((g1, g2) ->
                        Double.compare(g2.getScore(), g1.getScore()))
                .collect(Collectors.toList());
    }

    // Stream API: map — бағаларды әріптік бағаға түрлендіру.
    public List<String> getLetterGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId).stream()
                .map(Grade::getLetterGrade)
                .collect(Collectors.toList());
    }

    // Set коллекциясы: қайталанбайтын әріптік бағаларды алу.
    public Set<String> getDistinctLetterGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId).stream()
                .map(Grade::getLetterGrade)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // Stream API: groupingBy — бағаларды әріптік баға бойынша топтау.
    public Map<String, List<Grade>> groupGradesByLetter(int studentId) {
        return gradeDAO.findByStudentId(studentId).stream()
                .collect(Collectors.groupingBy(Grade::getLetterGrade));
    }

    // Орташа балл
    public double calculateAverage(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .mapToDouble(Grade::getScore)
                .average()
                .orElse(0.0);
    }

    // Бағалардың жалпы қосындысы
    public double calculateTotal(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .mapToDouble(Grade::getScore)
                .sum();
    }

    // Бағалар саны
    public long countGrades(int studentId) {
        return gradeDAO.findByStudentId(studentId)
                .stream()
                .count();
    }
}