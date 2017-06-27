package com.zd.core.util;

import com.zd.core.constant.CharConvertType;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.ExtSortModel;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author luoyibo
 */
public class StringUtils {

    /**
     * 判断字符串是否是整数
     *
     * @param number
     * @return
     */
    public static boolean isInteger(String number) {
        boolean isNumber = false;
        if (StringUtils.isNotEmpty(number)) {
            isNumber = number.matches("^([1-9]\\d*)|(0)$");
        }
        return isNumber;
    }

    /**
     * 判断字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        boolean isNotEmpty = false;
        if (str != null && !str.trim().equals("") && !str.trim().equalsIgnoreCase("NULL")) {
            isNotEmpty = true;
        }
        return isNotEmpty;
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /**
     * 将数组转成SQL认识的字符串 123,432,2312 id in('123','432','2312')
     *
     * @param ids
     * @return
     */
    public static String fromArrayToStr(String[] ids) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            str.append("'" + ids[i] + "',");
        }
        if (ids.length > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    /**
     * 去掉指定字符串的最后一位
     *
     * @param string 要处理的字符串
     * @return 处理后的字符串
     */
    public static String trimLast(String string) {
        return string.substring(0, string.length() - 1);
    }

    /**
     * addString:将指定的基础字符串补充指定的字符，以达到指定的长度.
     *
     * @param base 要补充的基础字符串
     * @param add  填充的字符
     * @param len  填充后的总长度,如果填充后的总长度小于基础字符串的长度则不进行处理
     * @param pos  填充的位置，L向左填充 R向右填充
     * @return 填充后的字符串
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public static String addString(String base, String add, Integer len, String pos) {
        StringBuffer sBuffer = new StringBuffer();
        String reString = base;
        Integer addLen = len - base.length();
        if (addLen > 0) {
            for (int i = 0; i < addLen; i++) {
                sBuffer.append(add);
            }
            if (pos.toUpperCase() == "L")
                reString = sBuffer.toString() + reString;
            else
                reString = reString + sBuffer.toString();
        }
        return reString;
    }

    @SuppressWarnings("unchecked")
    public static String convertFilterToSql(String filter) {
        String whereSql = "";
        StringBuffer sbHql = new StringBuffer();
        try {
            List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                    ExtDataFilter.class);
            for (ExtDataFilter extDataFilter : listFilters) {
                String type = extDataFilter.getType();
                String field = extDataFilter.getField();
                String value = extDataFilter.getValue();
                String comparison = extDataFilter.getComparison();
                switch (type) {
                    case "string":
                        if (StringUtils.isEmpty(comparison)) {
                            sbHql.append(" and o." + field + " like '%" + value + "%'");
                        } else {
                            sbHql.append(" and o." + field + " " + comparison + " '" + value + "'");
                        }
                        break;
                    case "boolean":
                        sbHql.append(" and o." + field + " " + comparison + " " + Boolean.parseBoolean(value) + " ");
                        break;
                    case "numeric":
                        sbHql.append(" and o." + field + " " + comparison + " " + Integer.parseInt(value) + " ");
                        break;
                    case "float":
                        sbHql.append(" and o." + field + " " + comparison + " " + Float.parseFloat(value) + " ");
                        break;
                    case "date":
                        sbHql.append(" and o." + field + " " + comparison + " '" + DateUtil.getDate(value) + "' ");
                        break;
                    case "time":
                        sbHql.append(" and o." + field + " " + comparison + " '" + DateUtil.getTime(value) + "' ");
                        // criteria.add(GetComparison(comparison, field,
                        // DateUtil.getTime(value)));
                        break;
                    default:
                        sbHql.append(" and o." + field + " " + comparison + " " + value + " ");
                        break;
                }
            }
            whereSql = sbHql.toString();
        } catch (Exception e) {
            whereSql = "";
        }

        return whereSql;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ExtDataFilter> convertFilterToMap(String filter) {
        Map<String, ExtDataFilter> filterMap = new HashMap<String, ExtDataFilter>();
        try {
            List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                    ExtDataFilter.class);
            for (ExtDataFilter extDataFilter : listFilters) {
                String type = extDataFilter.getType();
                String field = extDataFilter.getField();
                String value = extDataFilter.getValue();
                String comparison = extDataFilter.getComparison();
                filterMap.put(field, extDataFilter);
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return filterMap;
    }

    public static String convertSortToSql(String sort) {
        String orderSql = "";
        StringBuffer sbHql = new StringBuffer();
        if (sort.length() > 0) {
            List<ExtSortModel> listSorts = (List<ExtSortModel>) JsonBuilder.getInstance().fromJsonArray(sort,
                    ExtSortModel.class);
            // for(int i=listSorts.size()-1;i>=0;i--){
            for (ExtSortModel extSortModel : listSorts) {
                // ExtSortModel extSortModel=listSorts.get(i);
                String direction = extSortModel.getDirection();
                String property = extSortModel.getProperty();
                if ("DESC".equals(direction)) {
                    sbHql.append(" o." + property + " DESC,");
                } else if ("ASC".equals(direction)) {
                    sbHql.append(" o." + property + " ASC,");
                }
            }
            if (sbHql.length() > 0) {
                sbHql.deleteCharAt(sbHql.length() - 1);
                orderSql = sbHql.toString();
            }
        }
        return orderSql;
    }

    public static boolean isBlank(String ip) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 将指定的中文转换成拼音，并返回首字母
     * @param wenZi 要转换的文字
     * @param convertType 转换的类型 LOWERCASE-全小写 UPPERCASE-全大写 FIRSTUPPER-首字母大写
     * @param returnType 返回的类型 RETURNFIRST-仅返回首字母 否则返回全部字符
     * @return
     */
    public static String ConvertWenziToPingYing(String wenZi, String convertType, String returnType) {
        Integer wenziLenth = wenZi.length();
        String[] pingYing = new String[wenziLenth];
        StringBuilder sb = new StringBuilder();
        try {
            switch (convertType) {
                case CharConvertType.LOWERCASE:
                    for (Integer i = 0; i < wenziLenth; i++) {
                        pingYing[i] = PinyinUtil.toPinYin(wenZi.substring(i, i + 1));
                    }
                    break;
                case CharConvertType.UPPERCASE:
                    for (Integer i = 0; i < wenziLenth; i++) {
                        pingYing[i] = PinyinUtil.toPinYin(wenZi.substring(i, i + 1), "", PinyinUtil.Type.UPPERCASE);
                    }
                    break;
                case CharConvertType.FIRSTUPPER:
                    for (Integer i = 0; i < wenziLenth; i++) {
                        pingYing[i] = PinyinUtil.toPinYin(wenZi.substring(i, i + 1), "", PinyinUtil.Type.FIRSTUPPER);
                    }
                    break;
            }
            if (CharConvertType.RETURNFIRST.equals(returnType)) {
                for (Integer i = 0; i < wenziLenth; i++) {
                    sb.append(pingYing[i].substring(0, 1));
                }
            } else {
                for (Integer i = 0; i < wenziLenth; i++) {
                    sb.append(pingYing[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return sb.toString();
    }

    public static String HtmlToText(String htmlStr){
        String text ="";
        if(isNotEmpty(htmlStr)) {
            Document doc = Jsoup.parse(htmlStr);
            text = doc.text();
            // remove extra white space
            StringBuilder builder = new StringBuilder(text);
            int index = 0;
            while (builder.length() > index) {
                char tmp = builder.charAt(index);
                if (Character.isSpaceChar(tmp) || Character.isWhitespace(tmp)) {
                    builder.setCharAt(index, ' ');
                }
                index++;
            }
            text = builder.toString().replaceAll(" +", " ").trim();
        } else
            text = "";
        return text;
    }
}
