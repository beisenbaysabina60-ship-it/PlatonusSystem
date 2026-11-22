package kz.platonus.repository;

import kz.platonus.dao.StudentDAO;
import kz.platonus.model.Student;

import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

public class StudentRepository implements GenericRepository<Student, Integer> {

    private final StudentDAO studentDAO;

    public StudentRepository() {
        this.studentDAO = new StudentDAO();
    }

    public StudentRepository(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void save(Student student) {
        try {
            studentDAO.addStudent(student);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving student", e);
        }
    }

    @Override
    public Optional<Student> findById(Integer id) {
        try {
            return Optional.ofNullable(studentDAO.findById(id));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding student", e);
        }
    }

    @Override
    public List<Student> findAll() {
        try {
            return studentDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving students", e);
        }
    }

    @Override
    public void update(Student student) {
        try {
            studentDAO.updateStudent(student);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating student", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            studentDAO.deleteStudent(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student", e);
        }
    }

    @Override
    public long count() {
        try {
            return studentDAO.findAll().size();
        } catch (SQLException e) {
            throw new RuntimeException("Error counting students", e);
        }
    }
}