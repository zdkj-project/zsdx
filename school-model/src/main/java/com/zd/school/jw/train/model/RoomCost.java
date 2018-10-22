package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntegerelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-10-22
 * @time: 9:39
 */
@Entity
@Table(name = "ROOM_COST")
@AttributeOverride(name = "uuid", column = @Column(name = "UUID", length = 36, nullable = false))
public class RoomCost  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 新加入房间类型
     */
    @FieldInfo(name = "团号")
    @Column(name = "TEAM_NO", length = 30, nullable = true)
    private String teamNo;

    @FieldInfo(name = "账号")
    @Column(name = "GUEST_NO", nullable = true)
    private Integer guestNo;


    @FieldInfo(name = "房号")
    @Column(name = "ROOM_NO", length = 30, nullable = true)
    private String RoomNo;

    @FieldInfo(name = "客人姓名")
    @Column(name = "NAME", length = 30, nullable = true)
    private String name;

    @FieldInfo(name = "入住日期")
    @Column(name = "CHECKINDATE", columnDefinition = "datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkinDate;

    @FieldInfo(name = "离店日期")
    @Column(name = "CHECKOUTDATE", columnDefinition = "datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkoutDate;


    @FieldInfo(name = "账号类型")
    @Column(name = "GUESTTYPE", nullable = true)
    private Integer guestType;

    @FieldInfo(name = "入住时间")
    @Column(name = "CHECKINTIME", columnDefinition = "datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkinTime;

    @FieldInfo(name = "房租")
    @Column(name = "RENT", length = 20, nullable = true)
    private BigDecimal rent;

    @FieldInfo(name = "餐娱")
    @Column(name = "MEAL", length = 20, nullable = true)
    private BigDecimal meal;

    @FieldInfo(name = "赔偿")
    @Column(name = "COMPENSATE", length = 20, nullable = true)
    private BigDecimal compensate;

    @FieldInfo(name = "其他消费")
    @Column(name = "OTHERSPEND", length = 20, nullable = true)
    private BigDecimal otherspend;

    @FieldInfo(name = "消费合计")
    @Column(name = "SPENDTOTAL", length = 20, nullable = true)
    private BigDecimal spendtotal;

    @FieldInfo(name = "现金")
    @Column(name = "cash", nullable = true)
    private Integer cash;

    @FieldInfo(name = "押金")
    @Column(name = "DEPOSIT", length = 20, nullable = true)
    private BigDecimal deposit;


    @FieldInfo(name = "信用卡")
    @Column(name = "CREDITCARD", length = 20, nullable = true)
    private Integer creditcard;


    @FieldInfo(name = "挂账")
    @Column(name = "ONACCOUNT", nullable = true)
    private Integer onaccount;


    @FieldInfo(name = "会员卡")
    @Column(name = "MEMBERCARD", nullable = true)
    private Integer membercard;

    @FieldInfo(name = "其他付款")
    @Column(name = "OTHERPAY", length = 20, nullable = true)
    private BigDecimal otherpay;

    @FieldInfo(name = "付款合计")
    @Column(name = "PAYTOTAL", length = 20, nullable = true)
    private BigDecimal paytotal;

    @FieldInfo(name = "消费余额")
    @Column(name = "SURPLUS", length = 20, nullable = true)
    private BigDecimal surplus;

    @FieldInfo(name = "学号")
    @Column(name = "STUDENTNO", length = 40, nullable = true)
    private String studentno;

    @FieldInfo(name = "学员ID")
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;

    @FieldInfo(name = "班级ID")
    @Column(name = "CLASS_ID", length = 36, nullable = false)
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo;
    }

    public Integer getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(Integer guestNo) {
        this.guestNo = guestNo;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Integer getGuestType() {
        return guestType;
    }

    public void setGuestType(Integer guestType) {
        this.guestType = guestType;
    }


    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public BigDecimal getMeal() {
        return meal;
    }

    public void setMeal(BigDecimal meal) {
        this.meal = meal;
    }

    public BigDecimal getCompensate() {
        return compensate;
    }

    public void setCompensate(BigDecimal compensate) {
        this.compensate = compensate;
    }

    public BigDecimal getOtherspend() {
        return otherspend;
    }

    public void setOtherspend(BigDecimal otherspend) {
        this.otherspend = otherspend;
    }

    public BigDecimal getSpendtotal() {
        return spendtotal;
    }

    public void setSpendtotal(BigDecimal spendtotal) {
        this.spendtotal = spendtotal;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Integer getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(Integer creditcard) {
        this.creditcard = creditcard;
    }

    public Integer getOnaccount() {
        return onaccount;
    }

    public void setOnaccount(Integer onaccount) {
        this.onaccount = onaccount;
    }

    public Integer getMembercard() {
        return membercard;
    }

    public void setMembercard(Integer membercard) {
        this.membercard = membercard;
    }

    public BigDecimal getOtherpay() {
        return otherpay;
    }

    public void setOtherpay(BigDecimal otherpay) {
        this.otherpay = otherpay;
    }

    public BigDecimal getPaytotal() {
        return paytotal;
    }

    public void setPaytotal(BigDecimal paytotal) {
        this.paytotal = paytotal;
    }

    public BigDecimal getSurplus() {
        return surplus;
    }

    public void setSurplus(BigDecimal surplus) {
        this.surplus = surplus;
    }

    public String getStudentno() {
        return studentno;
    }

    public void setStudentno(String studentno) {
        this.studentno = studentno;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
