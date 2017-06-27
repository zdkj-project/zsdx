package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import org.hibernate.annotations.Formula;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by luoyibo on 2017-06-19.
 */
@Entity
@Table(name = "TRAIN_T_INDICATORSTAND")
@AttributeOverride(name = "uuid", column = @Column(name = "STAND_ID", length = 36, nullable = false))
public class TrainIndicatorStand  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "指标ID")
    @Column(name = "INDICATOR_ID", length = 36, nullable = false)
    private String indicatorId;

    public String getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }

    @FieldInfo(name = "评价标准")
    @Column(name = "INDICATOR_STAND",length = 256,nullable = false)
    private String indicatorStand;

    public String getIndicatorStand() {
        return indicatorStand;
    }

    public void setIndicatorStand(String indicatorStand) {
        this.indicatorStand = indicatorStand;
    }

    @FieldInfo(name = "指标名称")
    @Formula("(SELECT a.INDICATOR_NAME FROM dbo.TRAIN_T_EVALINDICATOR a WHERE a.INDICATOR_ID=INDICATOR_ID)")
    private String indicatorName;

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    @FieldInfo(name = "指标评估对象")
    @Formula("(SELECT a.INDICATOR_OBJECT FROM dbo.TRAIN_T_EVALINDICATOR a WHERE a.INDICATOR_ID=INDICATOR_ID)")
    private Integer indicatorObject;

    public Integer getIndicatorObject() {
        return indicatorObject;
    }

    public void setIndicatorObject(Integer indicatorObject) {
        this.indicatorObject = indicatorObject;
    }
}
