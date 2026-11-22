package kz.platonus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void studentShouldHaveStudentRole() {
        Student student = new Student(
                1, "Aruzhan", "aruzhan@mail.com",
                "ST001", "IT-23"
        );

        assertEquals(UserRole.STUDENT, student.getRole());
    }

    @Test
    void teacherShouldHaveTeacherRole() {
        Teacher teacher = new Teacher(
                2, "Ali", "ali@mail.com",
                "Computer Science", "Master"
        );

        assertEquals(UserRole.TEACHER, teacher.getRole());
    }

    @Test
    void adminShouldHaveAdminRole() {
        Admin admin = new Admin(
                3, "Dana", "dana@mail.com", "Super Admin"
        );

        assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    void studentShouldStoreStudentNumberAndGroup() {
        Student student = new Student(
                1, "Aruzhan", "aruzhan@mail.com",
                "ST001", "IT-23"
        );

        assertEquals("ST001", student.getStudentNumber());
        assertEquals("IT-23", student.getGroupName());
    }

    @Test
    void studentShouldRejectEmptyStudentNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student(1, "Aruzhan", "a@mail.com",
                        "", "IT-23")
        );
    }

    @Test
    void teacherShouldStoreDepartment() {
        Teacher teacher = new Teacher(
                2, "Ali", "ali@mail.com",
                "Computer Science", "Master"
        );

        assertEquals("Computer Science", teacher.getDepartment());
    }

    @Test
    void adminShouldStoreAdminLevel() {
        Admin admin = new Admin(
                3, "Dana", "dana@mail.com", "Super Admin"
        );

        assertEquals("Super Admin", admin.getAdminLevel());
    }

    @Test
    void userShouldRejectInvalidId() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student(0, "Aruzhan", "a@mail.com",
                        "ST001", "IT-23")
        );
    }

    @Test
    void userShouldRejectEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student(1, "", "a@mail.com",
                        "ST001", "IT-23")
        );
    }

    @Test
    void userShouldRejectEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student(1, "Aruzhan", "",
                        "ST001", "IT-23")
        );
    }

    @Test
    void userEqualityShouldBeBasedOnId() {
        Student student1 = new Student(
                1, "Aruzhan", "a@mail.com", "ST001", "IT-23"
        );

        Student student2 = new Student(
                1, "Another Name", "another@mail.com", "ST002", "IT-24"
        );

        assertEquals(student1, student2);
    }

    @Test
    void studentInfoShouldContainStudentDetails() {
        Student student = new Student(
                1, "Aruzhan", "aruzhan@mail.com",
                "ST001", "IT-23"
        );

        String info = student.getInfo();

        assertTrue(info.contains("Aruzhan"));
        assertTrue(info.contains("ST001"));
        assertTrue(info.contains("IT-23"));
    }
}