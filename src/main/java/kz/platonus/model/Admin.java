package kz.platonus.model;

/**
 * Represents an administrator in the Platonus system.
 */
public class Admin extends User {

    private String adminLevel;

    public Admin(int id, String fullName, String email,
                 String adminLevel) {

        super(id, fullName, email);

        if (adminLevel == null || adminLevel.isBlank()) {
            throw new IllegalArgumentException(
                    "Admin level cannot be empty");
        }

        this.adminLevel = adminLevel;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        if (adminLevel == null || adminLevel.isBlank()) {
            throw new IllegalArgumentException(
                    "Admin level cannot be empty");
        }

        this.adminLevel = adminLevel;
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }

    @Override
    public String getInfo() {
        return super.getInfo()
                + ", Admin Level: " + adminLevel;
    }
}