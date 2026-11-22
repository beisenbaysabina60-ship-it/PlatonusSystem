package kz.platonus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentGroupTest {

    @Test
    void shouldCreateGroupSuccessfully() {
        StudentGroup group = new StudentGroup(1, "IT-2301", "Information Technology");

        assertEquals(1, group.getId());
        assertEquals("IT-2301", group.getGroupName());
        assertEquals("Information Technology", group.getSpecialty());
    }

    @Test
    void shouldRejectInvalidGroupId() {
        assertThrows(IllegalArgumentException.class,
                () -> new StudentGroup(0, "IT-2301", "IT"));
    }

    @Test
    void shouldRejectEmptyGroupName() {
        assertThrows(IllegalArgumentException.class,
                () -> new StudentGroup(1, "", "IT"));
    }

    @Test
    void shouldUpdateGroupName() {
        StudentGroup group = new StudentGroup(1, "IT-2301", "IT");

        group.setGroupName("IT-2302");

        assertEquals("IT-2302", group.getGroupName());
    }

    @Test
    void shouldUpdateSpecialty() {
        StudentGroup group = new StudentGroup(1, "IT-2301", "IT");

        group.setSpecialty("Computer Science");

        assertEquals("Computer Science", group.getSpecialty());
    }
}