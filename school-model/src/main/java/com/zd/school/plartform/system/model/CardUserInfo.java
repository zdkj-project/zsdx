package com.zd.school.plartform.system.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * ClassName:CardUseInfo Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年6月14日 下午1:04:09
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Entity
@Table(name = "CARD_T_USEINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "CARD_ID", length = 36))
public class CardUserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "UP卡流水号")
    @Column(name = "UP_CARD_ID")
    private Long upCardId;

    public Long getUpCardId() {
        return upCardId;
    }

    public void setUpCardId(Long upCardId) {
        this.upCardId = upCardId;
    }
  
    @FieldInfo(name = "物理卡号")
    @Column(name = "FACT_NUMB")
    private Long factNumb;

    public Long getFactNumb() {
        return factNumb;
    }

    public void setFactNumb(Long factNumb) {
        this.factNumb = factNumb;
    }

    @FieldInfo(name = "卡的使用状态")
    @Column(name = "USE_STATE")
    private Integer useState;

    public Integer getUseState() {
        return useState;
    }

    public void setUseState(Integer useState) {
        this.useState = useState;
    }

    @FieldInfo(name = "发卡对象（班级学员或老师）")
    @Column(name = "USER_ID", length = 36, nullable = false)
    private String userId;
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    
    @FieldInfo(name = "印刷卡号")
    @Column(name = "CARD_PRINT_ID", length = 36, nullable = true)
    private String cardPrintId;

	public String getCardPrintId() {
		return cardPrintId;
	}

	public void setCardPrintId(String cardPrintId) {
		this.cardPrintId = cardPrintId;
	}
	
	//1-职工卡、2-合同工、3-学员卡
	@FieldInfo(name = "卡的类型")
    @Column(name = "CARD_TYPE_ID")
    private Integer cardTypeId;

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
    
    @FieldInfo(name = "学员姓名")
    @Formula("(SELECT top 1 a.XM FROM TRAIN_T_CLASSTRAINEE a where a.CLASS_TRAINEE_ID=USER_ID)")
    private String traineeName;

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
  
}