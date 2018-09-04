package com.zd.school.opu.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.zd.school.opu.*;
import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.zd.core.util.WebServiceUtil;

@Service
public class OpuServiceImpl implements OpuService {

	private static Logger logger = Logger.getLogger(OpuServiceImpl.class);

	private Properties prop;

	public OpuServiceImpl() throws IOException {
		prop = PropertiesLoaderUtils.loadAllProperties("opu.properties");
	}

	/**
	 * 客人账号入账 向酒店系统数据库插入一条账务记录
	 */
	@Override
	public OpuResult accoun(String usercardNo, String room, String amount, String checkcode, String type) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("room", room);
		inMsg.put("amount", amount);
		inMsg.put("checkcode", checkcode);
		inMsg.put("type", type);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 客人入住 根据参数产生一条客人入住信息和事务记录
	 */
	@Override
	public OpuResult checkIn(String username, String usercardNo, String room, String checkinDate, String checkoutDate,
			String price) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("username", username);
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("room", room);
		inMsg.put("checkinDate", checkinDate);
		inMsg.put("checkoutDate", checkoutDate);
		inMsg.put("price", price);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 根据证件号和房间号查询客人余额
	 */
	@Override
	public OpuResult balance(String usercardNo, String room) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("room", room);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 客人退房调用 修改酒店系统客人状态为离店状态
	 */
	@Override
	public OpuResult checkOut(String usercardNo, String room) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("room", room);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 获得已经录入过取卡码，但又取消了预定的预订单
	 */
	@Override
	public OpuResult<CancelOrderResponse> getCancelOrderList() {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult<CancelOrderResponse> result = CancelOrderResponse.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 根据房间类型代码查询该类型的可用房间，参数为空表示查询所有可用房间
	 */
	@Override
	public String[] getRoomList(String roomtype) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("roomtype", roomtype);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			String[] arr = jsonStr.substring(1, jsonStr.length() - 1).replace("\"", "").split(",");
			return arr;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 根据获得的订单生成取卡码，将取卡码录入酒店系统
	 */
	@Override
	public OpuResult insertTakeCardCode(String json) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("json", json);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 门锁对接 向酒店系统添加一条门锁记录
	 */
	@Override
	public OpuResult lock(String cardID, String room, String username, String usercardNo, String checkinDate,
			String checkoutDate) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("cardID", cardID);
		inMsg.put("room", room);
		inMsg.put("username", username);
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("checkinDate", checkinDate);
		inMsg.put("checkoutDate", checkoutDate);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 修改门锁记录的状态为失效状态
	 */
	@Override
	public OpuResult lockTovoid(String lockNo) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("lockNo", lockNo);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 根据 证件号 或 手机号 获取会员等级，以及会员所选房型的原单价
	 */
	@Override
	public OpuResult<MemberInfoResponse> memberInfo(String usercardNo, String phone, String roomType) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardNo", usercardNo);
		inMsg.put("phone", phone);
		inMsg.put("roomType", roomType);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult<MemberInfoResponse> result = MemberInfoResponse.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 为下了预订单的客人办理入住，系统会根据取卡码查询到预订单并改变预定状态
	 */
	public OpuResult orderCheckin(String usercardID) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardID", usercardID);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 获得所有预订单信息
	 */
	@Override
	public OpuResult<QueryOrdersResponse> queryOrders() {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult<QueryOrdersResponse> result = QueryOrdersResponse.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 为下了预订单的客人办理入住，系统会根据取卡码查询到预订单并改变预定状态
	 */
	@Override
	public CreateOrderResponse createOrder(String teamName, int roomNumber, int personNumber, String checkinDate,
			String checkoutDate, String name, String phone, String personsJson) {
		return createOrder(teamName, roomNumber, personNumber, checkinDate, checkoutDate, name, phone, "", "", "",
				personsJson);
	}

	/**
	 * 为下了预订单的客人办理入住，系统会根据取卡码查询到预订单并改变预定状态
	 */
	@Override
	public CreateOrderResponse createOrder(String teamName, int roomNumber, int personNumber, String checkinDate,
			String checkoutDate, String name, String phone, String receptionTag, String guestTag, String accountTag,
			String personsJson) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("teamName", teamName);
		inMsg.put("roomNumber", roomNumber + "");
		inMsg.put("personNumber", personNumber + "");
		inMsg.put("checkinDate", checkinDate);
		inMsg.put("checkoutDate", checkoutDate);
		inMsg.put("name", name);
		inMsg.put("phone", phone);
		inMsg.put("personsJson", personsJson);
		inMsg.put("receptionTag", receptionTag);
		inMsg.put("guestTag", guestTag);
		inMsg.put("accountTag", accountTag);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			CreateOrderResponse result = CreateOrderResponse.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

    /**
     * @return com.zd.school.opu.CreateOrderResponse
     * @description
     * @author yz
     * @date 2018/9/3 22:32
     * @method createOrder
     */
    @Override
    public JsonRootBean new_CreateOrder(String json) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        method = method.substring(0, 1).toUpperCase() + method.substring(1);
        WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");
        try {
            HashMap<String, String> inMsg = new HashMap<String, String>();
            inMsg.put("json", json);
            String jsonStr = ws.sendMessage(inMsg);
			JsonRootBean result = JsonRootBean.fromJson(jsonStr);
            return result;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

	/**
	 * 根据订单号向订单追加入住人信息
	 */
	@Override
	public JsonRootBean addPersonsByOrder(String orderId, int roomNumber, int personNumber, String personsJson) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("orderId", orderId);
		inMsg.put("roomNumber", roomNumber + "");
		inMsg.put("personNumber", personNumber + "");
		inMsg.put("personsJson", personsJson);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			JsonRootBean result = JsonRootBean.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 根据证件号取消预订信息
	 */
	@Override
	public OpuResult cancelReserve(String identitycard) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("identitycard", identitycard);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult result = OpuResult.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

	/**
	 * 根据证件号查询客人信息
	 */
	@Override
	public OpuResult<GetGuestInfoResponse> getGuestInfo(String usercardNo) {
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		// method = method.substring(0, 1).toUpperCase() + method.substring(1);
		WebServiceUtil ws = new WebServiceUtil(prop, method, method + "Response");

		HashMap<String, String> inMsg = new HashMap<String, String>();
		inMsg.put("usercardNo", usercardNo);
		try {
			String jsonStr = ws.sendMessage(inMsg);
			OpuResult<GetGuestInfoResponse> result = GetGuestInfoResponse.fromJson(jsonStr);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return new OpuResult(1, e.getMessage());
		}
	}

}
