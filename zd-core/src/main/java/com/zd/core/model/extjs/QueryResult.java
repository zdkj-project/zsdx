/**
 * Project Name:zd-core
 * File Name:QueryResult.java
 * Package Name:com.zd.core.model.extjs
 * Date:2016年6月3日上午11:24:33
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.model.extjs;

import java.util.List;

/**
 * ClassName:QueryResult Function: TODO ADD FUNCTION. Reason: TODO ADD REASON.
 * Date: 2016年6月3日 上午11:24:33
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class QueryResult<E> {
    private List<E> resultList;
    private Long totalCount;

    public List<E> getResultList() {
        return resultList;
    }

    public void setResultList(List<E> resultList) {
        this.resultList = resultList;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
