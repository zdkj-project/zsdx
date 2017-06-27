package com.zd.school.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.school.jw.model.app.SchoolNews;

@Controller
@RequestMapping("/app/SchoolNews")
public class SchoolNewsController{
	
	
	@RequestMapping(value = { "/list" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	private @ResponseBody List<SchoolNews> list(String claiId,
			HttpServletRequest request, HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SchoolNews sn1 = new SchoolNews();
		sn1.setNewsId("1");
		sn1.setNewsTitle("北京地铁1号线停隧道25分钟 女孩险晕倒");
		sn1.setNewsRemark("据@北京地铁，8月18日7:30地铁1号线因信号故障，列车间隔加大，影响部分列车晚点。有网友称，东向西运行的列车停在原地不动近半小时，站台广播和列车广播建议乘客乘坐其它交通工具。车厢内温度上升，还有列车停在隧道中。乘客詹女士反映，她乘1号线前往四惠方向。7点55份，列车停在军博和木樨地之间隧道，广播称，信号灯故障。“车停了25分钟，车厢很挤，闷热。一女孩身体不适差点晕倒。有乘客给她一块糖，情况才好转。” @北京地铁 ，因信号故障1号线所有车站采只出不进。");
		sn1.setCreateDate(sdf.format(new Date()));
		SchoolNews sn2 = new SchoolNews();
		sn2.setNewsId("2");
		sn2.setNewsTitle("快件服务仍遭诟病 全峰登快递投诉“黑榜”榜首2016年08月18日 02:43 北京商报 微博");
		sn2.setNewsRemark("北京商报讯（记者 吴文治 王运）快件延误、丢失等问题一直为消费者所诟病。国家邮政局昨日发布7月邮政业消费者申诉情况通告显示，消费者对于快递服务投诉仍呈同比增长态势。其中，全峰快递以20.11%的申诉率登快递“黑榜”榜首。国家邮政局统计数据显示，消费者对快递服务申诉的主要问题与上月比较，增长的有延误、违规收费、损毁、投递服务、丢失短少。与去年同期相比，增长的有损毁、延误、代收货款，同比分别增长5.2%、4.5%、4%。其中，申诉比较集中的问题是投递服务、延误和丢失短少，占比分别为43.2%、18.9%和18.3%。据了解，今年7月，消费者对42家快递企业进行了有效申诉，全国快递服务有效申诉率为5.13%，其中，高于全国有效申诉率的快递企业有12家。全峰快递、国通、中外运-空运的申诉率位列“黑榜”前三位，而DHL、京东、苏宁易购分别以1.75、1、0.07的低申诉率位列最后三位。");
		sn2.setCreateDate(sdf.format(new Date()));
		SchoolNews sn3 = new SchoolNews();
		sn3.setNewsId("3");
		sn3.setNewsTitle("腾讯二季度总收入增52% 微信朋友圈广告开始发力");
		sn3.setNewsRemark("　腾讯控股（00700，HK）再一次向投资者展示了其强大的赚钱能力。8月17日，公司公布了截至2016年6月30日未经审核的第二季度及中期业绩。2016年第二季度，腾讯实现了356.91亿元（53.82亿美元）的总收入，比去年同期大幅增长52%，上半年总收入为676.86亿元（102.07亿美元），比去年同期增长48%。　　上半年收入盈利双双大增报告显示，2016年上半年，腾讯实现总收入676.86亿元，比去年同期增长48%；经营盈利为人民币277.27亿元，比去年同期增长43%；而腾讯权益持有人应占盈利为人民币199.20亿元，比去年同期增长40%。今年上半年，港股市场一直处于比较低迷的状态，但是作为港股市场的“股王”，腾讯股价却在5月27日创出了历史新高，当日，腾讯大涨4.52%，以每股171.2港元收盘。而腾讯的成交额也非常可观，几乎天天是港股市场的成交额冠军，甚至几倍于成交额排名第二的股票。");
		sn3.setCreateDate(sdf.format(new Date()));
		List<SchoolNews> snList = new ArrayList<SchoolNews>();
		snList.add(sn1);
		snList.add(sn2);
		snList.add(sn3);
		return snList;
	}
	
}
