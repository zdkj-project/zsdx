package com.zd.school.opu;

/**
 * 酒店接口
 */
public interface OpuService {

    /**
     * 客人账号入账 向酒店系统数据库插入一条账务记录
     *
     * @param usercardNo 客人证件号
     * @param room       房间号
     * @param amount     入账金额
     * @param checkcode  校验码，由 房间号+证件号+入账金额得到的字符串,经过MD5加密变成的32位小写MD5字符串
     * @param type       入账类型，0银行卡信用卡，1微信，2支付宝，3退款
     * @return 见实体类说明
     */
    public OpuResult accoun(String usercardNo, String room, String amount, String checkcode, String type);

    /**
     * 客人入住，为客人办理入住
     *
     * @param username     客人姓名
     * @param usercardNo   客人证件号
     * @param room         房间号
     * @param checkinDate  入住日期，格式yyyy-MM-dd 例：2017-12-05
     * @param checkoutDate 离店日期，格式yyyy-MM-dd 例：2017-12-08
     * @param price        房租
     * @return 见实体类说明
     */
    public OpuResult checkIn(String username, String usercardNo, String room, String checkinDate, String checkoutDate,
                             String price);

    /**
     * 根据证件号和房间号查询客人余额
     *
     * @param usercardNo 客人证件号
     * @param room       房间号
     * @return 见实体类说明
     */
    public OpuResult balance(String usercardNo, String room);

    /**
     * 客人退房调用 修改酒店系统客人状态为离店状态
     *
     * @param usercardNo 客人证件号
     * @param room       房间号
     * @return 见实体类说明
     */
    public OpuResult checkOut(String usercardNo, String room);

    /**
     * 获得已经录入过取卡码，但又取消了预定的预订单
     *
     * @return 见实体类说明
     */
    public OpuResult<CancelOrderResponse> getCancelOrderList();

    /**
     * 根据房间类型代码查询该类型的可用房间，参数为空表示查询所有可用房间
     *
     * @param roomtype 房间类型代码，传空值表示查询所有可用房间号
     * @return 房号
     */
    public String[] getRoomList(String roomtype);

    /**
     * @param json [ {"tzxh",90285,"code","12345678"},
     *             {"tzxh",90286,"code","87654321"} ] (json字符串)
     *             （tzxh：同住序号，code：取卡码）
     * @return 见实体类说明
     */
    public OpuResult insertTakeCardCode(String json);

    /**
     * 门锁对接 向酒店系统添加一条门锁记录
     *
     * @param cardID       门卡物理卡号
     * @param room         房间号
     * @param username     客人姓名
     * @param usercardNo   客人证件号
     * @param checkinDate  入住日期，格式yyyy-MM-dd 例：2017-12-05
     * @param checkoutDate 离店日期，格式yyyy-MM-dd 例：2017-12-08
     * @return 见实体类说明
     */
    public OpuResult lock(String cardID, String room, String username, String usercardNo, String checkinDate,
                          String checkoutDate);

    /**
     * 修改门锁记录的状态为失效状态
     *
     * @param lockNo 门锁记录8位16进制序号
     * @return 见实体类说明
     */
    public OpuResult lockTovoid(String lockNo);

    /**
     * 根据 证件号 或 手机号 获取会员等级，以及会员所选房型的原单价
     *
     * @param usercardNo 客人证件号
     * @param phone      手机号
     * @param roomType   房间类型，用于查询该房型房价，如此参数为空则返回房型价为0
     * @return 见实体类说明
     */
    public OpuResult<MemberInfoResponse> memberInfo(String usercardNo, String phone, String roomType);

    /**
     * 预订单入住
     *
     * @param usercardID 身份证
     * @return 见实体类说明
     */
    public OpuResult orderCheckin(String usercardID);

    /**
     * 获得所有预订单信息
     *
     * @return 见实体类说明
     */
    public OpuResult<QueryOrdersResponse> queryOrders();

    /**
     * 为下了预订单的客人办理入住，系统会根据取卡码查询到预订单并改变预定状态
     *
     * @param teamName     团名
     * @param roomNumber   预定房间数
     * @param personNumber 人数
     * @param checkinDate  入住日期（格式：2018-02-27）
     * @param checkoutDate 离店日期（格式：2018-02-27）
     * @param name         联系人姓名
     * @param phone        联系人电话
     * @param personsJson  人员名单json字符串，isCheckin0表示住宿，1表示不住宿,格式如下： [
     *                     {"name":"name1","usercardNo":"123456789123456789","sex":"男","checkinDate":"2018-03-15","checkoutDate":"2018-04-08","isCheckin":0},
     *                     {"name":"name2","usercardNo":"123456789123456788","sex":"男","checkinDate":"2018-03-15","checkoutDate":"2018-04-08","isCheckin":0}
     *                     ]
     * @return 见实体类说明
     */
    public CreateOrderResponse createOrder(String teamName, int roomNumber, int personNumber, String checkinDate,
                                           String checkoutDate, String name, String phone, String personsJson);

    public CreateOrderResponse new_CreateOrder(String json);

    public CreateOrderResponse CreateOrder_Not_Row_Room(String json);

    /**
     * 为下了预订单的客人办理入住，系统会根据取卡码查询到预订单并改变预定状态
     *
     * @param teamName     团名
     * @param roomNumber   预定房间数
     * @param personNumber 人数
     * @param checkinDate  入住日期（格式：2018-02-27）
     * @param checkoutDate 离店日期（格式：2018-02-27）
     * @param name         联系人姓名
     * @param phone        联系人电话
     * @param receptionTag 接待备注
     * @param guestTag     客人备注
     * @param accountTag   账务备注
     * @param personsJson  人员名单json字符串，isCheckin0表示住宿，1表示不住宿,格式如下： [
     *                     {"name":"name1","usercardNo":"123456789123456789","sex":"男","checkinDate":"2018-03-15","checkoutDate":"2018-04-08","isCheckin":0},
     *                     {"name":"name2","usercardNo":"123456789123456788","sex":"男","checkinDate":"2018-03-15","checkoutDate":"2018-04-08","isCheckin":0}
     *                     ]
     * @return 见实体类说明
     */
    public CreateOrderResponse createOrder(String teamName, int roomNumber, int personNumber, String checkinDate,
                                           String checkoutDate, String name, String phone, String receptionTag, String guestTag, String accountTag,
                                           String personsJson);

    /**
     * 根据订单号向订单追加入住人信息
     *
     * @param orderId      订单号
     * @param roomNumber   追加房数
     * @param personNumber 追加人数
     * @param personsJson  追加入住人员名单json格式如下 [{ "name":"name3",
     *                     "usercardNo":"12345678912345678", "sex":"男",
     *                     "checkinDate":"2018-03-15", "checkoutDate":"2018-04-08" }]
     * @return { "rspMsg":"成功", "rspCode":0 }
     */
    public CreateOrderResponse addPersonsByOrder(String orderId, int roomNumber, int personNumber, String personsJson);

    /**
     * 根据证件号取消预订信息
     *
     * @param identitycard 证件号
     * @return { "rspMsg":"成功", "rspCode":0 }
     */
    public OpuResult cancelReserve(String identitycard);

    /**
     * 根据证件号查询客人信息
     *
     * @param usercardNo 证件号
     * @return 见实体类说明
     */
    public OpuResult<GetGuestInfoResponse> getGuestInfo(String usercardNo);

    /**
     * @param orderID 订单号
     * @return com.zd.school.opu.CreateOrderResponse
     * @description 取消预订单
     * @author yz
     * @date 2018/9/12 11:41
     * @method Order_Cancel
     */
    public CreateOrderResponse Order_Cancel(String orderID);
}
