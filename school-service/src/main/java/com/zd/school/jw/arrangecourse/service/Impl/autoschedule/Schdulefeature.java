package com.zd.school.jw.arrangecourse.service.Impl.autoschedule;

import java.util.HashMap;
import java.util.Map;

import com.zd.school.jw.arrangecourse.model.AcsTimecourselimit;

public class Schdulefeature {
	/**总共要排多少节课**/
	int allnum=0;
	/**总共已经排多少节课**/
	int paiednum=0;
	/**总共剩余未拍课**/
	int nupainum=0;
	
	/**当天已经排多少节课**/
	int paidenum_day=0;
//	/**当天未拍多少节课**/
//	byte nupainum_day=0;
	
	
	/**当天最多排**/
	private	double paimaxnum_day=0;
	/**平均每天排多少节**/
	double avgpainum=0;
	
	/**限制表数据**/
	Map<Integer,AcsTimecourselimit> schudle=new HashMap<Integer,AcsTimecourselimit>();
	
	public double getPaiMaxNum_day(int week){
		paimaxnum_day= AcsUtils.div(String.valueOf(nupainum), String.valueOf(week), 2);
		return paimaxnum_day;
	}
	public double getAvgPaiNum(int week){
		avgpainum= AcsUtils.div(String.valueOf(allnum), String.valueOf(week), 2);
		return avgpainum;
	}
}
