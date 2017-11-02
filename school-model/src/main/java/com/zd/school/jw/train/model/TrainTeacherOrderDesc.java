
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
 * ClassName: TrainClassorder 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教师订餐信息(TRAIN_T_CLASSORDER)实体类.
 * date: 2017-10-30
 *
 * @author  zzk 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "TRAIN_T_TEACHERORDER_DESC")
@AttributeOverride(name = "uuid", column = @Column(name = "ORDER_DESC_ID", length = 36, nullable = false))
public class TrainTeacherOrderDesc extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "订餐说明")
    @Column(name = "ORDER_DESC", length = 2048, nullable = true)
    private String orderDesc;
    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }
    public String getOrderDesc() {
        return orderDesc;
    }
        
}