package kz.platonus.model;

/**
 * Represents a student group in the university.
 */
public class StudentGroup {
    private final int id;
    private String groupName;
    private String specialty;

    public StudentGroup(int id, String groupName, String specialty) {
        if (id <= 0) {
            throw new IllegalArgumentException("Group ID must be positive");
        }
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty cannot be empty");
        }

        this.id = id;
        this.groupName = groupName;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setGroupName(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        this.groupName = groupName;
    }

    public void setSpecialty(String specialty) {
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty cannot be empty");
        }
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return "StudentGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}