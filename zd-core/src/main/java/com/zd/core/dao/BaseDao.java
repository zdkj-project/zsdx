package com.zd.core.dao;

import com.zd.core.model.extjs.QueryResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

/**
 * @param <E>
 * @ClassName: BaseDao
 * @Description: 数据访问接口基类
 * @author: luoyibo
 * @date: 2016年3月9日 下午1:06:16
 */
public interface BaseDao<E> {

	public Session getSession();
    /**
     * 持久化对象实体
     *
     * @param entity 对象实体
     */
    public void persist(E entity);

    /**
     * 根据多个id参数删除对象
     *
     * @param id 多个id，以英文逗号隔开
     * @return 返回true或者false
     */
    public boolean deleteByPK(Serializable... id);

    /**
     * 删除对象实体
     *
     * @param entity 对象实体
     */
    public void delete(E entity);

    /**
     * 以HQL的方式，根据单个属性删除对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     */
    public void deleteByProperties(String propName, Object propValue);

    /**
     * 以HQL的方式，根据多个属性删除对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     */
    public void deleteByProperties(String[] propName, Object[] propValue);

    /**
     * 根据给定的Detached对象标识符更新对象实体
     *
     * @param entity 对象实体
     */
    public void update(E entity);

    /**
     * 根据多个属性条件更新对象实体多个属性
     *
     * @param conditionName  WHERE子句条件的属性数组名称
     * @param conditionValue WHERE子句条件的属性数组值
     * @param propertyName   UPDATE子句属性数组名称
     * @param propertyValue  UPDATE子句属性数组值
     */
    public void updateByProperties(String[] conditionName, Object[] conditionValue, String[] propertyName,
                                   Object[] propertyValue);

    /**
     * 根据单个属性条件更新对象实体多个属性
     *
     * @param conditionName  WHERE子句条件的属性数组名称
     * @param conditionValue WHERE子句条件的属性数组值
     * @param propertyName   UPDATE子句属性名称
     * @param propertyValue  UPDATE子句属性值
     */
    public void updateByProperties(String[] conditionName, Object[] conditionValue, String propertyName,
                                   Object propertyValue);

    /**
     * 根据多个属性条件更新对象实体单个属性
     *
     * @param conditionName  WHERE子句条件的属性名称
     * @param conditionValue WHERE子句条件的属性值
     * @param propertyName   UPDATE子句属性数组名称
     * @param propertyValue  UPDATE子句属性数组值
     */
    public void updateByProperties(String conditionName, Object conditionValue, String[] propertyName,
                                   Object[] propertyValue);

    /**
     * 根据单个属性条件更新对象实体单个属性
     *
     * @param conditionName  WHERE子句条件的属性名称
     * @param conditionValue WHERE子句条件的属性值
     * @param propertyName   UPDATE子句属性名称
     * @param propertyValue  UPDATE子句属性值
     */
    public void updateByProperties(String conditionName, Object conditionValue, String propertyName,
                                   Object propertyValue);

    /**
     * 先删除再插入去更新对象实体
     *
     * @param entity 待更新的对象实体
     * @param oldId  已存在的对象实体主键
     */
    public void update(E entity, Serializable oldId);

    /**
     * 合并给定的对象实体状态到当前的持久化上下文
     *
     * @param entity 给定的对象实体
     * @return 返回对象实体
     */
    public E merge(E entity);

    /**
     * 根据ID立即加载持久化对象实体
     *
     * @param id ID值
     * @return 返回对象实体
     */
    public E get(Serializable id);

    /**
     * 根据ID延迟加载持久化对象实体
     *
     * @param id ID值
     * @return 返回对象实体
     */
    public E load(Serializable id);

    /**
     * 根据属性数组获取单个对象实体
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @return 返回对象实体
     */
    public E getByProerties(String[] propName, Object[] propValue);

    /**
     * 根据属性数组和排序条件获取单个对象实体
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @return 返回对象实体
     */
    public E getByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition);

    /**
     * 根据属性获取单个对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @return 返回对象实体
     */
    public E getByProerties(String propName, Object propValue);

    /**
     * 根据属性和排序条件获取单个对象实体
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @return 返回对象实体
     */
    public E getByProerties(String propName, Object propValue, Map<String, String> sortedCondition);

    /**
     * 根据属性、排序条件和要返回的记录数目获取对象实体列表
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition,
                                    Integer top);

    /**
     * 根据属性和排序条件获取对象实体列表
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition);

    /**
     * 根据属性和要返回的记录数目获取对象实体列表
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @param top       要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String[] propName, Object[] propValue, Integer top);

    /**
     * 根据属性获取对象实体列表
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @return
     */
    public List<E> queryByProerties(String[] propName, Object[] propValue);

    /**
     * 根据属性、排序条件和要返回的记录数目获取对象实体列表
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition,
                                    Integer top);

    /**
     * 根据属性和排序条件获取对象实体列表
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition);

    /**
     * 根据属性和要返回的记录数目获取对象实体列表
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @param top       要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String propName, Object propValue, Integer top);

    /**
     * 根据属性获取对象实体列表
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @return 返回对象实体列表
     */
    public List<E> queryByProerties(String propName, Object propValue);

    /**
     * 彻底清除会话
     */
    public void clear();

    /**
     * 从会话缓存中删除此对象实体
     *
     * @param entity 待删除的对象实体
     */
    public void evict(E entity);

    /**
     * 查询出对象实体的所有数目
     *
     * @return 返回对象实体所有数目
     */
    public Long countAll();

    /**
     * 查询出所有的对象实体列表
     *
     * @return 返回对象实体列表
     */
    public List<E> doQueryAll();

    /**
     * 根据排序条件和要返回的记录数目查询出对象实体列表
     *
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> doQueryAll(Map<String, String> sortedCondition, Integer top);

    /**
     * 根据要返回的记录数目查询出对象实体列表
     *
     * @param top 要返回的记录数目
     * @return 返回对象实体列表
     */
    public List<E> doQueryAll(Integer top);

    /**
     * 根据HQL查询实体列表
     *
     * @param hql 查询语句
     * @return
     */
    public List<E> doQuery(String hql);

    /**
     * 根据HQL查询实体列表
     *
     * @param hql   查询语句
     * @param start 返回记录起始位置
     * @param limit 返回最大记录数
     * @return
     */
    public List<E> doQuery(String hql, Integer start, Integer limit);

    /**
     * doQuery:根据HQL查询实体列表.
     *
     * @param hql      查询语句
     * @param start    返回记录起始位置
     * @param limit    返回最大记录数
     * @param propName 参数名
     * @param objs     参数集合
     * @return List<E>
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public List<E> doQuery(String hql, Integer start, Integer limit, String propName, Object[] objs);

    /**
     * 根据HQL语句返回对象实体数目
     *
     * @param hql 执行的HQL语句
     * @return
     */
    public Integer getCount(String hql);

    public Integer getCount(String hql, String propName, Object[] objs);

    /**
     * 执行HQL语句并返回受影响的记录的条数
     *
     * @param hql 要执行的HQL语句
     * @return 受影响的记录数
     */
    public Integer executeHql(String hql);

    /**
     * 判断字段的值是否存在 如果是插入id赋值-1或者new Guid,如果是修改id赋值 要修改项的值
     *
     * @param fieldName  要判断的字段
     * @param fieldValue 要判断的字段的值
     * @param id         实体的标识
     * @param where      附加查询条件
     * @return
     */
    public boolean IsFieldExist(String fieldName, String fieldValue, String id, String where);

    /**
     * 判断字段的值是否存在 如果是插入id赋值-1或者new Guid,如果是修改id赋值 要修改项的值
     *
     * @param fieldName  要判断的字段
     * @param fieldValue 要判断的字段的值
     * @param id         实体的标识
     * @return
     */
    public boolean IsFieldExist(String fieldName, String fieldValue, String id);

    /**
     * 逻辑删除或还原指定的记录
     *
     * @param ids      要处理的记录的ID,多个ID使用","间隔
     * @param isDelete 处理标记
     * @return
     */
    public boolean logicDelOrRestore(String ids, String isDelete);

    /**
     * 根据SQL查询实体列表
     *
     * @param sql 查询语句
     * @return
     */
    public List<E> doQuerySql(String sql);

    /**
     * 根据SQL查询对象集合
     *
     * @param sql 查询语句
     * @return
     */
    public List<Object[]> ObjectQuerySql(String sql);

    public QueryResult<E> doPaginationQuery(Integer start, Integer limit, String sort, String filter, boolean isDelete);

    public <T> QueryResult<T> doQueryCountToHql(Integer start, Integer limit, String sort, String filter, String hql,
                                                String groupBy, String orderBy);

    public <T> QueryResult<T> doQueryCountToHql(Integer start, Integer limit, String sort, String filter, String hql,
                                                String groupBy, String orderBy, String where);

    public <T> T getForValue(String hql, Object... args);

    public <T> List<T> getForValues(String hql, Object... args);

    public <T> List<T> doQuerySqlObject(String sql, Class<T> clz);

    public <T> List<T> doQuerySqlObject(String sql);

    public Integer executeSql(String sql);


    public Integer getCountSql(String sql);

    /**
     * doQuery:执行指定的hql语句，并带有参数
     *
     * @param hql
     * @param propName 参数名
     * @param objs     参数值，数组格式
     * @return List<E>
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    public List<E> doQuery(String hql, String propName, Object[] objs);

    public QueryResult<E> doQueryResult(String hql, Integer start, Integer limit);

    public <T> T getForValueToSql(String sql);

    public List<Map<String, Object>> getForValuesToSql(String sql);

    /**
     * 带分页的sql查询并转换成实体类
     *
     * @param sql   查询的sql语句
     * @param start 起始页
     * @param limit 每页记录数
     * @param clz   要转换成的实体类
     * @param <T>   实体类的泛型参数
     * @return 返回转换后的结果
     */
    public <T> QueryResult<T> doQueryResultSqlObject(String sql, Integer start, Integer limit, Class<T> clz);
    
    /**
     * 根据HQL查询实体列表
     *
     * @param hql   查询语句
     * @param start 返回记录起始位置
     * @param limit 返回最大记录数
     * @return
     */
    public <T> List<T> queryEntityBySql(String sql, Class<T> clz);
    

    /**
     * 修正totalCount的获取方式，以免造成数据量过多时的内存爆炸的风险
     * @param start
     * @param limit
     * @param hql
     * @param countHql
     * @return
     */
    public QueryResult<E> queryPageResult(Integer start, Integer limit, String hql, String countHql);
    
    /**
     * 查询数据并以指定的格式返回，带翻页功能
     * @param start 起始页
     * @param limit 每页记录数
     * @param sort 排列方式
     * @param filter 过滤方式
     * @param isDelete 记录的删除标记
     * @return
     */
    public QueryResult<E> queryPageResult(Integer start, Integer limit, String sort, String filter, boolean isDelete);
    
    /**
     * 执行HQL语句，返回实体类清单
     * @param hql SQL语句
     * @param args 参数列表
     * @param <T>
     * @return
     */
    public <T> List<T> queryEntityByHql(String hql, Object... args);
    
    /**
     * 根据HQL查询实体列表
     *
     * @param hql 查询语句
     * @return
     */
    public List<E> queryByHql(String hql);
    
    /**
     * 根据HQL查询实体列表
     *
     * @param hql   查询语句
     * @param start 返回记录起始位置
     * @param limit 返回最大记录数
     * @return
     */
    public List<E> queryByHql(String hql, Integer start, Integer limit);
}