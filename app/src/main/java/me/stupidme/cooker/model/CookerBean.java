package me.stupidme.cooker.model;

/**
 * Created by StupidL on 2017/3/5.
 */

public class CookerBean {

    private String name;
    private String location;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "name: " + name + " location: " + location + " status: " + status;
    }
}
