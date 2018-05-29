/**
 * 日期功能类
 */
Ext.define("core.util.DateUtil", {
    /* 日期格式化成字符串 */
    formatDate: function(date) {
        var year = date.getYear() + 1900;
        var month = date.getMonth() + 1;
        if (month < 10) {
            month = "0" + month;
        }
        var day = date.getDate();
        if (day < 10) {
            day = "0" + day;
        }
        var str = year + "-" + month + "-" + day;
        return str;
    },

    /* 日期格式化成字符串 */
    formatDateStr: function(dateStr) {
        var date ;
        var reg = new RegExp("^[0-9]*$");    
            
        if(reg.test(dateStr)){                
            if(dateStr.length!=0)
                date=dateStr;
            else
                return "";
        }else{
            //若格式为Y-m-d，则要将他转换为Y/m/d，因为部门浏览器不识别横杠格式
            date = dateStr.replace(new RegExp(/-/gm),"/");
        }
    
        return  Ext.Date.format(new Date(date), 'Y年m月d日');
    },
        

    /**
     * 根据格式的字符串转成date对象并返回格式为Y-m-s
     * 
     * @param {}
     *            dateStr
     */
    toDate: function(dateStr) {
        var str = (dateStr + "").split("-");
        var year = str[0] * 1;
        var month = str[1] * 1;
        var day = str[2] * 1;
        var date = new Date();
        date.setYear(year * 1);
        date.setMonth(month * 1 - 1);
        date.setDate(day * 1);
        return date;
    },
    /**
     * 根据格式的字符串转成date对象并返回格式为Y-m-s
     * 
     * @param {}
     *            dateStr
     */
    toTime: function(dateStr) {
        var ddStr = dateStr.split(" ");
        var str = (ddStr[0] + "").split("-");
        var timeStr = (ddStr[1] + "").split(":");
        var year = str[0] * 1;
        var month = str[1] * 1;
        var day = str[2] * 1;
        var hour = timeStr[0] * 1;
        var minutie = timeStr[1] * 1;
        var secords = timeStr[2] * 1;
        var date = new Date();
        date.setYear(year * 1);
        date.setMonth(month * 1 - 1);
        date.setDate(day * 1);
        date.setHours(hour);
        date.setMinutes(minutie);
        date.setSeconds(secords);
        return date;
    },

    /**
     * [GetDateDiff 获取两个日期之间的时间差]
     * @param {[type]} startTime [日期一]
     * @param {[type]} endTime   [日期二]
     * @param {[type]} diffType  [返回精度，为分、小时、天]
     */
    GetDateDiff: function(startTime, endTime, diffType) {
        //将计算间隔类性字符转换为小写
        diffType = diffType.toLowerCase();
        var sTime = new Date(startTime); //开始时间
        var eTime = new Date(endTime); //结束时间
        //作为除数的数字
        var divNum = 1;
        switch (diffType) {
            case "s":
                divNum = 1000;
                break;
            case "m":
                divNum = 1000 * 60;
                break;
            case "h":
                divNum = 1000 * 3600;
                break;
            case "d":
                divNum = 1000 * 3600 * 24;
                break;
            default:
                break;
        }
        return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
    }
});
