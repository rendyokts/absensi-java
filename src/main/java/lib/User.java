package lib;

public class User {
    private int id;
    private String nrp;
    private String username;
    private String name;
    private String email;
    private String noHp;
    private String role;

    // Constructor to initialize a User object
    public User(int id, String nrp, String username, String name, String email, String noHp, String role) {
        this.id = id;
        this.nrp = nrp;
        this.username = username;
        this.name = name;
        this.email = email;
        this.noHp = noHp;
        this.role = role;
    }

    // Getters and setters for the fields
    public int getId() {
        return id;
    }

    public String getNrp() {
        return nrp;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getRole() {
        return role;
    }
}
