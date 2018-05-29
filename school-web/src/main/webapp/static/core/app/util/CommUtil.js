/**
 * 通用的一些工具类
 * @author luoyibo
 * date: 2016年4月23日 上午10:03:09 
 */
Ext.define("core.util.CommUtil", {
    /**
     * [SetCookie 保存cookie]
     * @param {[type]} name  [Cookie的名字]
     * @param {[type]} value [Cookie的值]
     */
    SetCookie: function(name, value) {
        var Days = 3000; // 此 cookie 将被保存 30 天
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    },

    /**
     * [getCookie 获取cookie]
     * @param  {[type]} name [要获取cookie的名字]
     * @return {[type]}      [获取到的cookie的值]
     */
    getCookie: function(name) { // 取cookies函数
        var arr = document.cookie
            .match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
        if (arr != null)
            return unescape(arr[2]);
        return null;

    },
    // 移除cookie
    /**
     * [delCookie 移除cookie]
     * @param  {[type]} name [要移除的cookie的名字]
     * @return {[type]}      [无返回值]
     */
    delCookie: function(name) { // 删除cookie
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null)
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }
});
