package me.stupidme.cooker.model;

/**
 * Created by StupidL on 2017/3/5.
 */

public class BookBean {

    // 电饭锅设备id
    private int deviceId;

    // 电饭锅设备名称
    private String deviceName;

    // 电饭锅工作时米的重量
    private float riceWeight;

    // 电饭锅所在的地点
    private String devicePlace;

    // 电饭锅的状态
    private String deviceStatus;

    // 该预定设置的人数
    private int peopleCount;

    // 该预定设置的时间
    private String time;

    // 该预定设置的口感
    private String taste;


    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public float getRiceWeight() {
        return riceWeight;
    }

    public void setRiceWeight(float riceWeight) {
        this.riceWeight = riceWeight;
    }

    public String getDevicePlace() {
        return devicePlace;
    }

    public void setDevicePlace(String devicePlace) {
        this.devicePlace = devicePlace;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    @Override
    public String toString() {
        return "id: " + deviceId + "\n"
                + "name: " + deviceName + "\n"
                + "place: " + devicePlace + "\n"
                + "weight: " + riceWeight + "\n"
                + "count: " + peopleCount + "\n"
                + "taste: " + taste + "\n"
                + "time: " + time + "\n"
                + "status: " + deviceStatus + "\n";
    }
}
