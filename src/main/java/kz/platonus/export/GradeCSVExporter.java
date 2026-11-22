package kz.platonus.export;

import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GradeCSVExporter {

    private final GradeDAO gradeDAO;

    public GradeCSVExporter() {
        this.gradeDAO = new GradeDAO();
    }

    public GradeCSVExporter(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    // Барлық бағаларды CSV файлға экспорттау
    public void exportAllGrades(String filePath) {
        List<Grade> grades = gradeDAO.findAll();

        writeCSV(grades, filePath);
    }

    // Белгілі студенттің бағаларын экспорттау
    public void exportStudentGrades(int studentId, String filePath) {
        List<Grade> grades = gradeDAO.findByStudentId(studentId);

        writeCSV(grades, filePath);
    }

    // CSV файлға жазу
    private void writeCSV(List<Grade> grades, String filePath) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath))) {

            // CSV тақырыптары
            writer.write("ID,Student,Course,Score,Letter Grade");
            writer.newLine();

            for (Grade grade : grades) {

                writer.write(
                        grade.getId() + "," +
                        escapeCSV(grade.getStudent().getFullName()) + "," +
                        escapeCSV(grade.getCourse().getCourseName()) + "," +
                        grade.getScore() + "," +
                        grade.getLetterGrade()
                );

                writer.newLine();
            }

            System.out.println("CSV file exported successfully: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error exporting CSV file", e);
        }
    }

    // CSV мәтінін қауіпсіз форматтау
    private String escapeCSV(String value) {

        if (value == null) {
            return "";
        }

        if (value.contains(",") ||
                value.contains("\"") ||
                value.contains("\n")) {

            value = value.replace("\"", "\"\"");

            return "\"" + value + "\"";
        }

        return value;
    }
}