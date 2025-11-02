package models;

public class User {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    
    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserInfo() {
        return "ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + email;
    }
}
