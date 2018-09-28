package maindir;

public class User {
    private String password;
    private String Username;

    public User(String username, String password) {
        this.password = password;
        Username = username;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof User)) return false;
        User otherUser = (User) other;

        return otherUser.getUsername().equals(Username) && otherUser.getPassword().equals(password);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Username: " + getUsername() + " and password: " + getPassword();
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return Username;
    }
}

