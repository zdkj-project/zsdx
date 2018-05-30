package com.zd.school.control.device.service;

import com.zd.core.service.BaseService;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.model.TLVModel;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 设备表
 * @author hucy
 *
 */
public interface PtTermService extends BaseService<PtTerm>{

	public PtTerm doAddEntity(PtTerm entity, SysUser currentUser);
	
	void batchUpdate(int termTypeID, String areaType, String areaType2, String[] strings, Object[] objects);

	public PtTerm doUpdateEntity(PtTerm entity, SysUser currentUser);

	public void doUpdatHighParamToIds(TLVModel tlvs, String termIds, String xm);

	public void doUpdateHighParam(TLVModel tlvs, String xm);

	public void doBatchUpdateHighParam(TLVModel tlvs, String termTypeID, String areaType, String xm);

	public void doUpdateBaseParam(TLVModel tlvs, String notes, String xm);

	public void doBatchUpdateBaseParam(TLVModel tlvs, String termTypeID, String notes, String areaType, String xm);

	public void doSetPtTerm(String roomId, String uuid, SysUser currentUser);
	


}
