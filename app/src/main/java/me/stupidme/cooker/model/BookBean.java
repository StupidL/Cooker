package me.stupidme.cooker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 该类承载预定信息
 * Created by StupidL on 2017/3/5.
 */

public class BookBean extends RealmObject {

    /**
     * 该预定的唯一ID，在服务器成功创建预约的时候，会返回改预约信息，此时可以获取ID，在此之前ID都为空
     */
    @PrimaryKey
    private long bookId;

    /**
     * 该预定所选则的电饭锅设备的ID
     */
    private long cookerId;

    /**
     * 该预约所选定的电饭锅的名称
     */
    private String cookerName;

    /**
     * 改预约所选定的电饭锅的名称
     */
    private String cookerLocation;

    /**
     * 该预约所选顶的电饭锅的状态，服务器成功创建预约时，状态要更新为<code>booking</code>
     */
    private String cookerStatus;

    /**
     * 该预约设置的大米的重量
     */
    private float riceWeight;

    /**
     * 该预约设置的吃饭人数
     */
    private int peopleCount;

    /**
     * 该预约设置的预计吃饭时间
     */
    private long time;

    /**
     * 该预约设置的米饭口感
     */
    private String taste;

    public BookBean setBookId(long id) {
        this.bookId = id;
        return this;
    }

    public long getBookId() {
        return bookId;
    }

    public long getCookerId() {
        return cookerId;
    }

    public BookBean setCookerId(long cookerId) {
        this.cookerId = cookerId;
        return this;
    }

    public String getCookerName() {
        return cookerName;
    }

    public BookBean setCookerName(String cookerName) {
        this.cookerName = cookerName;
        return this;
    }

    public float getRiceWeight() {
        return riceWeight;
    }

    public BookBean setRiceWeight(float riceWeight) {
        this.riceWeight = riceWeight;
        return this;
    }

    public String getCookerLocation() {
        return cookerLocation;
    }

    public BookBean setCookerLocation(String cookerLocation) {
        this.cookerLocation = cookerLocation;
        return this;
    }

    public String getCookerStatus() {
        return cookerStatus;
    }

    public BookBean setCookerStatus(String cookerStatus) {
        this.cookerStatus = cookerStatus;
        return this;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public BookBean setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
        return this;
    }

    public long getTime() {
        return time;
    }

    public BookBean setTime(long time) {
        this.time = time;
        return this;
    }

    public String getTaste() {
        return taste;
    }

    public BookBean setTaste(String taste) {
        this.taste = taste;
        return this;
    }

    @Override
    public String toString() {
        return "bookId: " + bookId
                + "cookerId: " + cookerId + "\n "
                + "cookerName: " + cookerName + "\n"
                + "cookerLocation: " + cookerLocation + "\n"
                + "riceWeight: " + riceWeight + "\n"
                + "peopleCount: " + peopleCount + "\n"
                + "taste: " + taste + "\n"
                + "time: " + time + "\n"
                + "cookerStatus: " + cookerStatus + "\n";
    }
}
