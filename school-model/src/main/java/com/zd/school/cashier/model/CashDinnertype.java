package com.zd.school.cashier.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * ClassName: CashDinnertype 
 * Function:
 * Reason:
 * Description: 餐类管理(CASH_T_DINNERTYPE)实体类.
 * date: 2017-09-13
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CASH_T_DINNERTYPE")
@AttributeOverride(name = "uuid", column = @Column(name = "DINNERTYPE_ID", length = 36, nullable = false))
public class CashDinnertype extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "餐类名称")
    @Column(name = "DINNERTYPE_NAME", length = 36, nullable = false)
    private String dinnertypeName;
    public void setDinnertypeName(String dinnertypeName) {
        this.dinnertypeName = dinnertypeName;
    }
    public String getDinnertypeName() {
        return dinnertypeName;
    }
        
    @FieldInfo(name = "餐类编号, 固定1-快餐 2-点餐 3-接待围餐")
    @Column(name = "DINNERTYPE_CODE", length = 5, nullable = false)
    private Short dinnertypeCode;
    public void setDinnertypeCode(Short dinnertypeCode) {
        this.dinnertypeCode = dinnertypeCode;
    }
    public Short getDinnertypeCode() {
        return dinnertypeCode;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}