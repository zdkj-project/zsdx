package com.zd.core.constant;

/**
 * 
 * @ClassName: StatuVeriable
 * @Description: 常规状态变量
 * @author: luoyibo
 * @date: 2016年3月7日 下午8:46:24
 *
 */

public class StatuVeriable {
    /** 记录已删除的标志 */
    public static final String ISDELETE = "1";

    /** 记录未删除的标志 */
    public static final String ISNOTDELETE = "0";

    /** 报名中 */
    public static final String ISSGIGN = "0";

    /** 报名结束 */
    public static final String SGIGNEND = "1";

    /** 进行中 */
    public static final String ISDOING = "2";

    /** 已取消 */
    public static final String ISCANCEL = "3";

    /** 已结束 */
    public static final String ISEND = "9";
}
