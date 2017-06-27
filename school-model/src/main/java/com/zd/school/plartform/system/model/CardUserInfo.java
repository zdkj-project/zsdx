package com.zd.school.plartform.system.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.plartform.system.model.SysUser;

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

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.8
     */
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

    @FieldInfo(name = "发卡对象")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private SysUser userInfo;

    public SysUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SysUser userInfo) {
        this.userInfo = userInfo;
    }

}