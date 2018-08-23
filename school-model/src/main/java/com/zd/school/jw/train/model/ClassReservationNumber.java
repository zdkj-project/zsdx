package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "CLASS_RESERVATION_NUMBER")
@AttributeOverride(name = "uuid", column = @Column(name = "numid", length = 36, nullable = false))
public class ClassReservationNumber extends BaseEntity  implements Serializable {

    @FieldInfo(name = "班级ID")
    @Column(name = "classid", length = 36, nullable = false)
    private String classid;

    @FieldInfo(name = "预定单号")
    @Column(name = "reservationid", length = 256, nullable = false)
    private String reservationid;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getReservationid() {
        return reservationid;
    }

    public void setReservationid(String reservationid) {
        this.reservationid = reservationid;
    }

    public ClassReservationNumber() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ClassReservationNumber(String uuid) {
        super(uuid);
        // TODO Auto-generated constructor stub
    }
}
