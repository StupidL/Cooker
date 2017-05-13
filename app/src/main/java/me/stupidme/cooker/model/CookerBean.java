package me.stupidme.cooker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 该类用来承载电饭锅设备信息。
 * Created by StupidL on 2017/3/5.
 */

public class CookerBean extends RealmObject {

    /**
     * 该电饭锅的设备ID，唯一的。
     * 该ID值在服务器增加该设备成功的时候会返回，在此之前，设备无ID。若服务器添加失败，则该设备创建失败。
     */
    @PrimaryKey
    private Long cookerId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 该电饭锅设备的名字，不保证唯一。
     */
    private String cookerName;

    /**
     * 该电饭锅的所在位置，不保证唯一。
     */
    private String cookerLocation;

    /**
     * 该电饭锅的当前状态，可能是<code>booking</code>或者<code>free</code>两种状态之一。
     */
    private String cookerStatus;

    public CookerBean() {
    }

    public CookerBean(CookerBean cookerBean) {
        this.cookerId = cookerBean.getCookerId();
        this.userId = cookerBean.getUserId();
        this.cookerName = cookerBean.getCookerName();
        this.cookerLocation = cookerBean.getCookerLocation();
        this.cookerStatus = cookerBean.getCookerStatus();
    }

    /**
     * 获取设备ID值
     *
     * @return 设备ID值
     */
    public Long getCookerId() {
        return cookerId;
    }

    /**
     * 设置该电饭锅设备的ID值，在服务器创建该设备成功并且返回的时候调用。
     *
     * @param cookerId 设备的唯一ID值
     * @return 自身引用，支持链式调用
     */
    public CookerBean setCookerId(Long cookerId) {
        this.cookerId = cookerId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CookerBean setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 获取该电饭锅设备的名字
     *
     * @return 设备名字
     */
    public String getCookerName() {
        return cookerName;
    }

    /**
     * 设置该电饭锅设备的名字
     *
     * @param cookerName 设备名字
     * @return 自身引用，支持链式调用
     */
    public CookerBean setCookerName(String cookerName) {
        this.cookerName = cookerName;
        return this;
    }

    /**
     * 获取电饭锅设备的位置
     *
     * @return 设备位置
     */
    public String getCookerLocation() {
        return cookerLocation;
    }

    /**
     * 设置电饭锅设备的位置
     *
     * @param cookerLocation 设备位置
     * @return 自身引用，支持链式调用
     */
    public CookerBean setCookerLocation(String cookerLocation) {
        this.cookerLocation = cookerLocation;
        return this;
    }

    /**
     * 获取电饭锅设备的状态
     *
     * @return 设备状态
     */
    public String getCookerStatus() {
        return cookerStatus;
    }

    /**
     * 设置电饭锅设备的状态
     *
     * @param cookerStatus 状态
     * @return 自身引用，支持链式调用
     */
    public CookerBean setCookerStatus(String cookerStatus) {
        this.cookerStatus = cookerStatus;
        return this;
    }

    @Override
    public String toString() {
        return String.format("[userId:%s, cookerId:%s, cookerName:%s, cookerLocation:%s, cookerStatus:%s]",
                userId, cookerId, cookerName, cookerLocation, cookerStatus);
    }

}
