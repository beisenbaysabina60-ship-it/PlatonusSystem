package kz.platonus.dao;

import kz.platonus.database.DatabaseConnection;
import kz.platonus.database.DatabaseInitializer;
import kz.platonus.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminDAOTest {

    private AdminDAO adminDAO;
    private Admin admin;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseInitializer.initialize();
        cleanTestData();

        adminDAO = new AdminDAO();

        admin = new Admin(
                9601,
                "Test Admin",
                "admin9601@example.com",
                "Super Admin"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        cleanTestData();
    }

    private void cleanTestData() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            String[] queries = {
                    "DELETE FROM admins WHERE id = 9601",
                    "DELETE FROM users WHERE id = 9601"
            };

            for (String query : queries) {
                try (PreparedStatement statement =
                             connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        }
    }

    @Test
    void testAddAdmin() {
        adminDAO.addAdmin(admin);

        Admin found = adminDAO.findById(9601);

        assertNotNull(found);
        assertEquals("Test Admin", found.getFullName());
    }

    @Test
    void testFindById() {
        adminDAO.addAdmin(admin);

        Admin found = adminDAO.findById(9601);

        assertNotNull(found);
        assertEquals("Super Admin", found.getAdminLevel());
    }

    @Test
    void testFindAll() {
        adminDAO.addAdmin(admin);

        List<Admin> admins = adminDAO.findAll();

        assertFalse(admins.isEmpty());
    }

    @Test
    void testUpdateAdmin() {
        adminDAO.addAdmin(admin);

        Admin updatedAdmin = new Admin(
                9601,
                "Updated Admin",
                "admin9601@example.com",
                "System Administrator"
        );

        adminDAO.updateAdmin(updatedAdmin);

        Admin found = adminDAO.findById(9601);

        assertNotNull(found);
        assertEquals("Updated Admin", found.getFullName());
        assertEquals("System Administrator", found.getAdminLevel());
    }

    @Test
    void testDeleteAdmin() {
        adminDAO.addAdmin(admin);

        adminDAO.deleteAdmin(9601);

        assertNull(adminDAO.findById(9601));
    }
}