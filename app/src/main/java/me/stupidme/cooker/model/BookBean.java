package me.stupidme.cooker.model;

/**
 * 该类承载预定信息
 * Created by StupidL on 2017/3/5.
 */

public class BookBean {

    /**
     * 该预定的唯一ID，在服务器成功创建预约的时候，会返回改预约信息，此时可以获取ID，在此之前ID都为空
     */
    private Long bookId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 该预定所选则的电饭锅设备的ID
     */
    private Long cookerId;

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

    public BookBean setBookId(Long id) {
        this.bookId = id;
        return this;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public BookBean setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getCookerId() {
        return cookerId;
    }

    public BookBean setCookerId(Long cookerId) {
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
        return String.format("[useId:%s, bookId:%s, cookerId:%s, cookerName:%s, cookerLocation:%s, " +
                        "cookerStatus:%s, riceWeight:%s, peopleCount:%s, taste:%s, time:%s]",
                userId, bookId, cookerId, cookerName, cookerLocation,
                cookerStatus, riceWeight, peopleCount, taste, time);
    }

}
