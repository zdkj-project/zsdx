package com.zd.school.jw.train.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: TrainCreditsrule 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学分计算规则(TRAIN_T_CREDITSRULE)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_CREDITSRULE")
@AttributeOverride(name = "uuid", column = @Column(name = "RULE_ID", length = 36, nullable = false))
public class TrainCreditsrule extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "规则名称")
    @Column(name = "RULE_NAME", length = 36, nullable = false)
    private String ruleName;
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getRuleName() {
        return ruleName;
    }
        
    //不需要了，默认为课程的学分
//    @FieldInfo(name = "正常出勤学分")
//    @Column(name = "NORMAL_CREDITS", length = 5, nullable = false)
//    private Short normalCredits;
//    public void setNormalCredits(Short normalCredits) {
//        this.normalCredits = normalCredits;
//    }
//    public Short getNormalCredits() {
//        return normalCredits;
//    }
        
    @FieldInfo(name = "迟到扣除学分")
    @Column(name = "LATE_CREDITS", length = 5, nullable = false)
    private Short lateCredits;
    public void setLateCredits(Short lateCredits) {
        this.lateCredits = lateCredits;
    }
    public Short getLateCredits() {
        return lateCredits;
    }
        
    @FieldInfo(name = "早退扣除学分")
    @Column(name = "EARLY_CREDITS", length = 5, nullable = false)
    private Short earlyCredits;
    public void setEarlyCredits(Short earlyCredits) {
        this.earlyCredits = earlyCredits;
    }
    public Short getEarlyCredits() {
        return earlyCredits;
    }
        
    @FieldInfo(name = "规则说明")
    @Column(name = "RULE_DESC", length = 255, nullable = true)
    private String ruleDesc;
    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }
    public String getRuleDesc() {
        return ruleDesc;
    }
        
    @FieldInfo(name = "启用标识 0-不启用 1-启用")
    @Column(name = "START_USING", length = 5, nullable = false)
    private Short startUsing;
    public void setStartUsing(Short startUsing) {
        this.startUsing = startUsing;
    }
    public Short getStartUsing() {
        return startUsing;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}