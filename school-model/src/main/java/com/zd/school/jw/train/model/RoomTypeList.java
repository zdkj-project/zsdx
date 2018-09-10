package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 房型列表
 * @author: yz
 * @date: 2018-09-06
 * @time: 16:48
 */
@Entity
@Table(name = "ROOM_TYPELIST")
@AttributeOverride(name = "uuid", column = @Column(name = "UUID", length = 36, nullable = false))
public class RoomTypeList extends BaseEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 新加入房间类型
     */
    @FieldInfo(name = "房间类型")
    @Column(name = "ROOM_TYPE", length = 20, nullable = true)
    private String roomType;

    @FieldInfo(name = "房型代码")
    @Column(name = "ROOM_CODE", length = 20, nullable = true)
    private String roomCode;

    @FieldInfo(name = "可住人数")
    @Column(name = "COHABIT_NUMBER", length = 20, nullable = true)
    private String cohabitNumber;

    @FieldInfo(name = "房间单价")
    @Column(name = "ROOM_PRICE", length = 20, nullable = true)
    private String roomPrice;

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getCohabitNumber() {
        return cohabitNumber;
    }

    public void setCohabitNumber(String cohabitNumber) {
        this.cohabitNumber = cohabitNumber;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public RoomTypeList(){

    };
    public RoomTypeList(String roomType, String roomCode, String cohabitNumber, String roomPrice) {
        this.roomType = roomType;
        this.roomCode = roomCode;
        this.cohabitNumber = cohabitNumber;
        this.roomPrice = roomPrice;
    }
}
