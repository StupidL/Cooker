package me.stupidme.cooker.model;

/**
 * Created by StupidL on 2017/3/8.
 */

public class UserBean {
    private String name;
    private String password;

    public UserBean(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "name: " + name + " \tpassword: " + password;
    }
}
