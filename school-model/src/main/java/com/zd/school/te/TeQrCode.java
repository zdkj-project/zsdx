package com.zd.school.te;

import com.zd.core.annotation.FieldInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 扫码开门
 * @author: yz
 * @date: 2018-09-12
 * @time: 16:09
 */
@Entity
@Table(name = "TE_QRCODE")
@AttributeOverride(name = "qrCode", column = @Column(name = "QR_CODE", length = 36, nullable = false))
public class TeQrCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @FieldInfo(name = "二维码序列号")
    @Column(name = "QR_CODE", length = 36, nullable = false)
    private String qrCode;

    @FieldInfo(name = "设备ID")
    @Column(name = "TREAM_ID", length = 36, nullable = true)
    private String treamID;

    @FieldInfo(name = "房间ID")
    @Column(name = "ROOM_ID", length = 36, nullable = true)
    private String roomID;

    @FieldInfo(name = "设备类型")
    @Column(name = "TREAM_TYPE", length = 10, nullable = true)
    private Integer treamType;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTreamID() {
        return treamID;
    }

    public void setTreamID(String treamID) {
        this.treamID = treamID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Integer getTreamType() {
        return treamType;
    }

    public void setTreamType(Integer treamType) {
        this.treamType = treamType;
    }
}
