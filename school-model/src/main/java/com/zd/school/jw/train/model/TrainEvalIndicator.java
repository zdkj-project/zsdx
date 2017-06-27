package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * ClassName: TrainEvalIndicator
 * Description: 评价指标实体类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TRAIN_T_EVALINDICATOR")
@AttributeOverride(name = "uuid", column = @Column(name = "INDICATOR_ID", length = 36, nullable = false))
public class TrainEvalIndicator extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "指标名称")
    @Column(name = "INDICATOR_NAME", length = 36, nullable = false)
    private String indicatorName;

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

/*    @FieldInfo(name = "评价标准")
    @Column(name = "INDICATOR_STAND",length = 128,nullable = true)
    private String indicatorStand;

    public String getIndicatorStand() {
        return indicatorStand;
    }

    public void setIndicatorStand(String indicatorStand) {
        this.indicatorStand = indicatorStand;
    }*/

    /**
     * 1-课程（教师）2-班训班整体 3-二者都评价
     */
    @FieldInfo(name = "评估对象")
    @Column(name = "INDICATOR_OBJECT",nullable = false)
    private Integer indicatorObject=1;

    public Integer getIndicatorObject() {
        return indicatorObject;
    }

    public void setIndicatorObject(Integer indicatorObject) {
        this.indicatorObject = indicatorObject;
    }
}
