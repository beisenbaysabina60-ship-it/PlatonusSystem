package kz.platonus.export;

import kz.platonus.dao.GradeDAO;
import kz.platonus.model.Grade;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GradeJSONExporter {

    private final GradeDAO gradeDAO;

    public GradeJSONExporter() {
        this.gradeDAO = new GradeDAO();
    }

    public GradeJSONExporter(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    public void exportAllGrades(String filePath) {
        List<Grade> grades = gradeDAO.findAll();
        writeJSON(grades, filePath);
    }

    public void exportStudentGrades(int studentId, String filePath) {
        List<Grade> grades = gradeDAO.findByStudentId(studentId);
        writeJSON(grades, filePath);
    }

    private void writeJSON(List<Grade> grades, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("[");
            writer.newLine();

            for (int i = 0; i < grades.size(); i++) {
                Grade grade = grades.get(i);

                writer.write("  {");
                writer.newLine();

                writer.write("    \"id\": " + grade.getId() + ",");
                writer.newLine();

                writer.write("    \"student\": \"" +
                        escapeJSON(grade.getStudent().getFullName()) + "\",");
                writer.newLine();

                writer.write("    \"course\": \"" +
                        escapeJSON(grade.getCourse().getCourseName()) + "\",");
                writer.newLine();

                writer.write("    \"score\": " + grade.getScore() + ",");
                writer.newLine();

                writer.write("    \"letterGrade\": \"" +
                        escapeJSON(grade.getLetterGrade()) + "\"");
                writer.newLine();

                writer.write("  }");

                if (i < grades.size() - 1) {
                    writer.write(",");
                }

                writer.newLine();
            }

            writer.write("]");
        } catch (IOException e) {
            throw new RuntimeException("Error exporting grades to JSON", e);
        }
    }

    private String escapeJSON(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}