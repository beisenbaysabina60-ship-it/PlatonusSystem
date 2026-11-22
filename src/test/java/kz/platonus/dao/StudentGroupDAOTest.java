package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.StudentGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentGroupDAOTest {

    private StudentGroupDAO groupDAO;
    private StudentGroup group;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        groupDAO = new StudentGroupDAO();

        group = new StudentGroup(
                9701,
                "TEST-9701",
                "Computer Science"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String query = "DELETE FROM student_groups WHERE id = 9701";

            try (PreparedStatement statement =
                         connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void testAddGroup() {
        groupDAO.addGroup(group);

        StudentGroup found = groupDAO.findById(9701);

        assertNotNull(found);
        assertEquals("TEST-9701", found.getGroupName());
    }

    @Test
    void testFindById() {
        groupDAO.addGroup(group);

        StudentGroup found = groupDAO.findById(9701);

        assertNotNull(found);
        assertEquals("Computer Science", found.getSpecialty());
    }

    @Test
    void testFindAll() {
        groupDAO.addGroup(group);

        List<StudentGroup> groups = groupDAO.findAll();

        assertFalse(groups.isEmpty());
    }

    @Test
    void testUpdateGroup() {
        groupDAO.addGroup(group);

        StudentGroup updatedGroup = new StudentGroup(
                9701,
                "UPDATED-9701",
                "Information Technology"
        );

        groupDAO.updateGroup(updatedGroup);

        StudentGroup found = groupDAO.findById(9701);

        assertNotNull(found);
        assertEquals("UPDATED-9701", found.getGroupName());
        assertEquals("Information Technology", found.getSpecialty());
    }

    @Test
    void testDeleteGroup() {
        groupDAO.addGroup(group);

        groupDAO.deleteGroup(9701);

        assertNull(groupDAO.findById(9701));
    }
}