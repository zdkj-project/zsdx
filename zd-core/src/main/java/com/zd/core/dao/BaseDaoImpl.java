package com.zd.core.dao;

import com.zd.core.constant.StatuVeriable;
import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.ExtSortModel;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.DateUtil;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

public class BaseDaoImpl<E> implements BaseDao<E> {
    // private final Logger logger = Logger.getLogger(entityClass);
    // protected final Logger log = Logger.getLogger(BaseDao.class);
    // private static Map<String, Method> MAP_METHOD = new HashMap();
    private SessionFactory sessionFactory;

    protected Class<E> entityClass;

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Resource(name = "sessionFactory")
    public void setSF(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public BaseDaoImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 持久化对象实体
     *
     * @param entity 对象实体
     */
    @Override
    public void persist(E entity) {
        getSession().save(entity);
    }

    /**
     * 根据多个id参数删除对象
     *
     * @param id 多个id，以英文逗号隔开
     * @return 返回true或者false
     */
    @Override
    public boolean deleteByPK(Serializable... id) {
        boolean result = false;
        if ((id != null) && (id.length > 0)) {
            for (int i = 0; i < id.length; ++i) {
                Object entity = get(id[i]);
                if (entity != null) {
                    getSession().delete(entity);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 删除对象实体
     *
     * @param entity 对象实体
     */
    @Override
    public void delete(E entity) {
        getSession().delete(entity);
    }

    /**
     * 以HQL的方式，根据单个属性删除对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     */
    @Override
    public void deleteByProperties(String propName, Object propValue) {
        deleteByProperties(new String[]{propName}, new Object[]{propValue});
    }

    /**
     * 以HQL的方式，根据多个属性删除对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     */
    @Override
    public void deleteByProperties(String[] propName, Object[] propValue) {
        if ((propName != null) && (propName.length > 0) && (propValue != null) && (propValue.length > 0)
                && (propValue.length == propName.length)) {
            StringBuffer sb = new StringBuffer("delete from " + this.entityClass.getName() + " o where 1=1 ");
            appendQL(sb, propName, propValue);
            Query query = getSession().createQuery(sb.toString());
            setParameter(query, propName, propValue);
            query.executeUpdate();
        }
    }

    /**
     * 根据给定的Detached对象标识符更新对象实体
     *
     * @param entity 对象实体
     */
    @Override
    public void update(E entity) {
        getSession().update(entity);
    }

    /**
     * 根据单个属性条件更新对象实体多个属性
     *
     * @param conditionName  WHERE子句条件的属性数组名称
     * @param conditionValue WHERE子句条件的属性数组值
     * @param propertyName   UPDATE子句属性名称
     * @param propertyValue  UPDATE子句属性值
     */
    @Override
    public void updateByProperties(String[] conditionName, Object[] conditionValue, String propertyName,
                                   Object propertyValue) {
        updateByProperties(conditionName, conditionValue, new String[]{propertyName},
                new Object[]{propertyValue});
    }

    /**
     * 根据多个属性条件更新对象实体单个属性
     *
     * @param conditionName  WHERE子句条件的属性名称
     * @param conditionValue WHERE子句条件的属性值
     * @param propertyName   UPDATE子句属性数组名称
     * @param propertyValue  UPDATE子句属性数组值
     */
    @Override
    public void updateByProperties(String conditionName, Object conditionValue, String[] propertyName,
                                   Object[] propertyValue) {
        updateByProperties(new String[]{conditionName}, new Object[]{conditionValue}, propertyName,
                propertyValue);
    }

    /**
     * 根据单个属性条件更新对象实体单个属性
     *
     * @param conditionName  WHERE子句条件的属性名称
     * @param conditionValue WHERE子句条件的属性值
     * @param propertyName   UPDATE子句属性名称
     * @param propertyValue  UPDATE子句属性值
     */
    @Override
    public void updateByProperties(String conditionName, Object conditionValue, String propertyName,
                                   Object propertyValue) {
        updateByProperties(new String[]{conditionName}, new Object[]{conditionValue},
                new String[]{propertyName}, new Object[]{propertyValue});
    }

    /**
     * 根据多个属性条件更新对象实体多个属性
     *
     * @param conditionName  WHERE子句条件的属性数组名称
     * @param conditionValue WHERE子句条件的属性数组值
     * @param propertyName   UPDATE子句属性数组名称
     * @param propertyValue  UPDATE子句属性数组值
     */
    @Override
    public void updateByProperties(String[] conditionName, Object[] conditionValue, String[] propertyName,
                                   Object[] propertyValue) {
        if ((propertyName != null) && (propertyName.length > 0) && (propertyValue != null) && (propertyValue.length > 0)
                && (propertyName.length == propertyValue.length) && (conditionValue != null)
                && (conditionValue.length > 0)) {
            StringBuffer sb = new StringBuffer();
            sb.append("update " + this.entityClass.getName() + " o set ");
            for (int i = 0; i < propertyName.length; ++i) {
                sb.append(propertyName[i] + " = :p_" + propertyName[i] + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" where 1=1 ");
            appendQL(sb, conditionName, conditionValue);
            Query query = getSession().createQuery(sb.toString());
            for (int i = 0; i < propertyName.length; ++i) {
                query.setParameter("p_" + propertyName[i], propertyValue[i]);
            }
            setParameter(query, conditionName, conditionValue);
            query.executeUpdate();
        } else {
            throw new IllegalArgumentException("Method updateByProperties in BaseDao argument is illegal!");
        }
    }

    /**
     * 先删除再插入去更新对象实体
     *
     * @param entity 待更新的对象实体
     * @param oldId  已存在的对象实体主键
     */
    @Override
    public void update(E entity, Serializable oldId) {
        deleteByPK(new Serializable[]{oldId});
        persist(entity);
    }

    /**
     * 合并给定的对象实体状态到当前的持久化上下文
     *
     * @param entity 给定的对象实体
     * @return 返回对象实体
     */
    @Override
    @SuppressWarnings("unchecked")
    public E merge(E entity) {
        return (E) getSession().merge(entity);
    }

    /**
     * 根据ID立即加载持久化对象实体
     *
     * @param id ID值
     * @return 返回对象实体
     */
    @Override
    public E get(Serializable id) {
        return getSession().get(this.entityClass, id);
    }

    /**
     * 根据ID延迟加载持久化对象实体
     *
     * @param id ID值
     * @return 返回对象实体
     */
    @Override
    public E load(Serializable id) {
        return getSession().load(this.entityClass, id);
    }

    /**
     * 根据属性数组获取单个对象实体
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @return 返回对象实体
     */
    @Override
    public E getByProerties(String[] propName, Object[] propValue) {
        return getByProerties(propName, propValue, null);
    }

    /**
     * 根据属性获取单个对象实体
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @return 返回对象实体
     */
    @Override
    public E getByProerties(String propName, Object propValue) {
        return getByProerties(new String[]{propName}, new Object[]{propValue});
    }

    /**
     * 根据属性和排序条件获取单个对象实体
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @return 返回对象实体
     */
    @Override
    public E getByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
        return getByProerties(new String[]{propName}, new Object[]{propValue}, sortedCondition);
    }

    /**
     * 根据属性数组和排序条件获取单个对象实体
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @return 返回对象实体
     */
    @Override
    public E getByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
        if ((propName != null) && (propName.length > 0) && (propValue != null) && (propValue.length > 0)
                && (propValue.length == propName.length)) {
            StringBuffer sb = new StringBuffer("select o from " + this.entityClass.getName() + " o where 1=1 ");
            appendQL(sb, propName, propValue);
            if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
                sb.append(" order by ");
                for (Map.Entry e : sortedCondition.entrySet()) {
                    sb.append(((String) e.getKey()) + " " + ((String) e.getValue()) + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            Query query = getSession().createQuery(sb.toString());
            setParameter(query, propName, propValue);
            List list = query.list();
            if ((list != null) && (list.size() > 0))
                return (E) list.get(0);
        }
        return null;
    }

    /**
     * 根据属性和排序条件获取对象实体列表
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition) {
        return queryByProerties(propName, propValue, sortedCondition, null);
    }

    /**
     * 根据属性和要返回的记录数目获取对象实体列表
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @param top       要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String[] propName, Object[] propValue, Integer top) {
        return queryByProerties(propName, propValue, null, top);
    }

    /**
     * 根据属性获取对象实体列表
     *
     * @param propName  属性数组名称
     * @param propValue 属性数组值
     * @return
     */
    @Override
    public List<E> queryByProerties(String[] propName, Object[] propValue) {
        return queryByProerties(propName, propValue, null, null);
    }

    /**
     * 根据属性、排序条件和要返回的记录数目获取对象实体列表
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition,
                                    Integer top) {
        return queryByProerties(new String[]{propName}, new Object[]{propValue}, sortedCondition, top);
    }

    /**
     * 根据属性和排序条件获取对象实体列表
     *
     * @param propName        属性名称
     * @param propValue       属性值
     * @param sortedCondition 排序条件
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String propName, Object propValue, Map<String, String> sortedCondition) {
        return queryByProerties(new String[]{propName}, new Object[]{propValue}, sortedCondition, null);
    }

    /**
     * 根据属性和要返回的记录数目获取对象实体列表
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @param top       要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String propName, Object propValue, Integer top) {
        return queryByProerties(new String[]{propName}, new Object[]{propValue}, null, top);
    }

    /**
     * 根据属性获取对象实体列表
     *
     * @param propName  属性名称
     * @param propValue 属性值
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String propName, Object propValue) {
        return queryByProerties(new String[]{propName}, new Object[]{propValue}, null, null);
    }

    /**
     * 根据属性、排序条件和要返回的记录数目获取对象实体列表
     *
     * @param propName        属性数组名称
     * @param propValue       属性数组值
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    public List<E> queryByProerties(String[] propName, Object[] propValue, Map<String, String> sortedCondition,
                                    Integer top) {
        if ((propName != null) && (propValue != null) && (propValue.length == propName.length)) {
            StringBuffer sb = new StringBuffer("select o from " + this.entityClass.getName() + " o where 1=1 ");
            appendQL(sb, propName, propValue);
            if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
                sb.append(" order by ");
                for (Map.Entry e : sortedCondition.entrySet()) {
                    sb.append(((String) e.getKey()) + " " + ((String) e.getValue()) + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            Query query = getSession().createQuery(sb.toString());
            setParameter(query, propName, propValue);
            if (top != null) {
                query.setFirstResult(0);
                query.setMaxResults(top.intValue());
            }
            return query.list();
        }
        return null;
    }

    /**
     * 彻底清除会话
     */
    @Override
    public void clear() {
        getSession().clear();
    }

    /**
     * 从会话缓存中删除此对象实体
     *
     * @param entity 待删除的对象实体
     */
    @Override
    public void evict(E entity) {
        getSession().evict(entity);
    }

    /**
     * 查询出对象实体的所有数目
     *
     * @return 返回对象实体所有数目
     */
    @Override
    public Long countAll() {
        return ((Long) getSession().createQuery("select count(*) from " + this.entityClass.getName()).uniqueResult());
    }

    /**
     * 查询出所有的对象实体列表
     *
     * @return 返回对象实体列表
     */
    @Override
    public List<E> doQueryAll() {
        return doQueryAll(null, null);
    }

    /**
     * 根据要返回的记录数目查询出对象实体列表
     *
     * @param top 要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    public List<E> doQueryAll(Integer top) {
        return doQueryAll(null, top);
    }

    /**
     * 根据排序条件和要返回的记录数目查询出对象实体列表
     *
     * @param sortedCondition 排序条件
     * @param top             要返回的记录数目
     * @return 返回对象实体列表
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<E> doQueryAll(Map<String, String> sortedCondition, Integer top) {
        Criteria criteria = getSession().createCriteria(this.entityClass);
        if ((sortedCondition != null) && (sortedCondition.size() > 0)) {
            for (Iterator it = sortedCondition.keySet().iterator(); it.hasNext(); ) {
                String pm = (String) it.next();
                if ("DESC".equals(sortedCondition.get(pm)))
                    criteria.addOrder(Order.desc(pm));
                else if ("ASC".equals(sortedCondition.get(pm))) {
                    criteria.addOrder(Order.asc(pm));
                }
            }
        }
        if (top != null) {
            criteria.setMaxResults(top.intValue());
            criteria.setFirstResult(0);
        }
        return criteria.list();
    }

    /**
     * 根据HQL查询实体列表
     *
     * @param sql 查询语句
     * @return
     */
    @Override
    public List<E> doQuery(String hql) {
        return doQuery(hql, 0, 0);
    }

    /**
     * 根据HQL查询实体列表
     *
     * @param hql   查询语句
     * @param start 返回记录起始位置
     * @param limit 返回最大记录数
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<E> doQuery(String hql, Integer start, Integer limit) {
        Query query = getSession().createQuery(hql);
        if (limit > 0) {
            query.setFirstResult(start);
            query.setMaxResults(limit);

        }
        return query.list();
    }

    /**
     * doQuery:根据HQL查询实体列表.
     *
     * @param hql   查询语句
     * @param start 返回记录起始位置
     * @param limit 返回最大记录数
     * @param objs  集合参数
     * @return List<E>
     * @throws @since JDK 1.8
     * @author luoyibo
     */
    @Override
    public List<E> doQuery(String hql, Integer start, Integer limit, String propName, Object[] objs) {
        Query query = this.getSession().createQuery(hql);
        if (StringUtils.isNotEmpty(propName))
            query.setParameterList(propName, objs);
        if (limit > 0) {
            query.setFirstResult(start);
            query.setMaxResults(limit);

        }
        return query.list();
    }

    /**
     * 根据HQL语句返回对象实体数目
     *
     * @param hql 执行的HQL语句
     * @return
     */
    @Override
    public Integer getCount(String hql) {
        Integer c = 0;
        Query query = getSession().createQuery(hql);
        Object count = query.uniqueResult();
        if (null != count && StringUtils.isInteger(count.toString())) {
            c = Integer.parseInt(count.toString());
        }
        return c;
    }

    @Override
    public Integer getCount(String hql, String propName, Object[] objs) {
        Integer c = 0;
        Query query = getSession().createQuery(hql);
        if (StringUtils.isNotEmpty(propName))
            query.setParameterList(propName, objs);
        Object count = query.uniqueResult();
        if (null != count && StringUtils.isInteger(count.toString())) {
            c = Integer.parseInt(count.toString());
        }
        return c;
    }

    /**
     * 执行HQL语句并返回受影响的记录的条数
     *
     * @param hql 要执行的HQL语句
     * @return 受影响的记录数
     */
    @Override
    public Integer executeHql(String hql) {
        Query query = getSession().createQuery(hql);

        return query.executeUpdate();
    }

    /**
     * 判断字段的值是否存在 如果是插入id赋值-1或者new Guid,如果是修改id赋值 要修改项的值
     *
     * @param fieldName  要判断的字段
     * @param fieldValue 要判断的字段的值
     * @param id         实体的标识
     * @param where      附加查询条件
     * @return
     */
    @Override
    public boolean IsFieldExist(String fieldName, String fieldValue, String id, String where) {
        String pkName = ModelUtil.getClassPkName(entityClass);
        StringBuffer hql = new StringBuffer("select count(*) from ");
        hql.append(entityClass.getSimpleName());
        hql.append(" as o where o." + fieldName);
        hql.append(" = '" + fieldValue + "' ");
        hql.append(" and o." + pkName + " <>'" + id + "'");
        if (!StringUtils.isEmpty(where)) {
            hql.append(" and " + where);
        }
        Query query = getSession().createQuery(hql.toString());
        return (Long) query.uniqueResult() > 0;
    }

    /**
     * 判断字段的值是否存在 如果是插入id赋值-1或者new Guid,如果是修改id赋值 要修改项的值
     *
     * @param fieldName  要判断的字段
     * @param fieldValue 要判断的字段的值
     * @param id         实体的标识
     * @return
     */
    @Override
    public boolean IsFieldExist(String fieldName, String fieldValue, String id) {
        return IsFieldExist(fieldName, fieldValue, id, null);
    }

    /**
     * 逻辑删除或还原指定的记录
     *
     * @param ids      要处理的记录的ID,多个ID使用","间隔
     * @param isDelete 处理标记
     * @return
     */
    @Override
    public boolean logicDelOrRestore(String ids, String isDelete) {
        String doIds = "'" + ids.replace(",", "','") + "'";
        String pkName = ModelUtil.getClassPkName(entityClass);
        String entityName = entityClass.getSimpleName();
        String updateTime = DateUtil.formatDateTime(new Date());
        String hql = "UPDATE " + entityName + " SET isDelete='" + isDelete + "', updateTime= CONVERT(datetime,'"
                + updateTime + "') WHERE " + pkName + " IN (" + doIds + ")";

        return executeHql(hql) > 0;
    }

    /**
     * 根据SQL查询实体列表
     *
     * @param sql 查询语句
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<E> doQuerySql(String sql) {
        Query query = getSession().createSQLQuery(sql);
        return query.list();
    }

    private void appendQL(StringBuffer sb, String[] propName, Object[] propValue) {
        for (int i = 0; i < propName.length; ++i) {
            String name = propName[i];
            Object value = propValue[i];
            if ((value instanceof Object[]) || (value instanceof Collection)) {
                Object[] arraySerializable = (Object[]) value;
                if ((arraySerializable != null) && (arraySerializable.length > 0)) {
                    sb.append(" and o." + name + " in (:" + name.replace(".", "") + ")");
                }
            } else if (value == null) {
                sb.append(" and o." + name + " is null ");
            } else {
                sb.append(" and o." + name + "=:" + name.replace(".", ""));
            }
        }
    }

    private void setParameter(Query query, String[] propName, Object[] propValue) {
        for (int i = 0; i < propName.length; ++i) {
            String name = propName[i];
            Object value = propValue[i];
            if (value != null)
                if (value instanceof Object[])
                    query.setParameterList(name.replace(".", ""), (Object[]) value);
                else if (value instanceof Collection)
                    query.setParameterList(name.replace(".", ""), (Collection) value);
                else
                    query.setParameter(name.replace(".", ""), value);

        }
    }

    /**
     * 根据SQL查询对象集合
     *
     * @param sql 查询语句
     * @return
     */
    @Override
    public List<Object[]> ObjectQuerySql(String sql) {
        Query query = getSession().createSQLQuery(sql);
        return query.list();
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public QueryResult<E> doPaginationQuery(Integer start, Integer limit, String sort, String filter,
                                            boolean isDelete) {
        Criteria criteria = getSession().createCriteria(this.entityClass);
        // 如果isDlete 为true 则为只列出未删除的数据
        if (isDelete) {
            //criteria.add(Restrictions.eq("isDelete", Integer.parseInt(StatuVeriable.ISNOTDELETE)));
        	//修改为，不等于1
        	criteria.add(Restrictions.ne("isDelete", Integer.parseInt(StatuVeriable.ISDELETE)));
        }
        // 设置 了过滤条件，需要组装这些过滤条件
        if (StringUtils.isNotEmpty(filter)) {
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
                            criteria.add(Restrictions.like(field, "%" + value + "%"));
                        } else {
                            criteria.add(GetComparison(comparison, field, value));
                        }
                        break;
                    case "boolean":
                        criteria.add(Restrictions.eq(field, Boolean.parseBoolean(value)));
                        break;
                    case "short":
                        criteria.add(GetComparison(comparison, field, Short.parseShort(value)));
                        break;
                    case "numeric":
                        criteria.add(GetComparison(comparison, field, Integer.parseInt(value)));
                        break;
                    case "float":
                        criteria.add(GetComparison(comparison, field, Float.parseFloat(value)));
                        break;
                    case "date":
                        criteria.add(GetComparison(comparison, field, DateUtil.getDate(value)));
                        break;
                    case "time":
                        criteria.add(GetComparison(comparison, field, DateUtil.getTime(value)));
                        break;
                    default:// 不在其中，则表示为实体类型
                        try {
                            String hqlTemp = "from " + type + " g where g." + field + " = ?";
                            Query query = getSession().createQuery(hqlTemp);
                            query.setParameter(0, value);
                            Object o = query.uniqueResult();
                            char c = type.charAt(0);
                            if (c <= 97) {
                                c = (char) (c + 32);
                                type = c + type.substring(1);
                            }
                            criteria.add(Restrictions.eq(type, o));
                        } catch (Exception e) {
                            System.out.println("实体参数转换出错了！");
                        }
                }
            }
        }
        try {
            QueryResult<E> qr = new QueryResult<E>();
            criteria.setProjection(Projections.rowCount());
            qr.setTotalCount(Long.valueOf(((Number) criteria.uniqueResult()).longValue()));
            if (qr.getTotalCount().longValue() > 0L) {
                // 构建排序条件
                if (StringUtils.isNotEmpty(sort)) {
                    List<ExtSortModel> listSorts = (List<ExtSortModel>) JsonBuilder.getInstance().fromJsonArray(sort,
                            ExtSortModel.class);
                    // for(int i=listSorts.size()-1;i>=0;i--){
                    for (ExtSortModel extSortModel : listSorts) {
                        // ExtSortModel extSortModel=listSorts.get(i);
                        String direction = extSortModel.getDirection().toUpperCase();
                        String property = extSortModel.getProperty();
                        if ("DESC".equals(direction)) {
                            criteria.addOrder(Order.desc(property));
                        } else if ("ASC".equals(direction)) {
                            criteria.addOrder(Order.asc(property));
                        }
                    }
                }
                criteria.setProjection(null);
                criteria.setMaxResults(limit);
                criteria.setFirstResult(start);
                qr.setResultList(criteria.list());
            } else {
                qr.setResultList(new ArrayList());
            }
            return qr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <ICriterion> Criterion GetComparison(String comparison, String field, Object value) {
        ICriterion res;
        switch (comparison) {
            case "<": // 小于
                res = (ICriterion) Restrictions.lt(field, value);
                break;
            case ">": // 大于
                res = (ICriterion) Restrictions.gt(field, value);
                break;
            case "=":// 等于
                res = (ICriterion) Restrictions.eq(field, value);
                break;
            case "!=":// 不等于
                res = (ICriterion) Restrictions.ne(field, value);
                break;
            case "<=": // 小于等于
                res = (ICriterion) Restrictions.le(field, value);
                break;
            case ">=": // 大于等于
                res = (ICriterion) Restrictions.ge(field, value);
                break;
            case "is null": // 为空
                res = (ICriterion) Restrictions.isNull(field);
                break;
            case "is not null": // 非空
                res = (ICriterion) Restrictions.isNotNull(field);
                break;
            case "in": // 包含
                Object[] setValue = value.toString().split(",");
                res = (ICriterion) Restrictions.in(field, setValue);
                break;
            case "not in": // 不包含
                Object[] setValue2 = value.toString().split(",");
                res = (ICriterion) Restrictions.not(Restrictions.in(field, setValue2));
                break;              
            default: // 默认为等于
                res = (ICriterion) Restrictions.eq(field, value);
                // (ICriterion) Restrictions.in
                break;
        }
        return (Criterion) res;
    }

    /**
     * 根据HQL查询对象统计信息集合与记录总数
     *
     * @param start   分页开始从多少条记录开始，初始为0
     * @param limit   每页多少条数据,为0则不分也
     * @param sort    排序信息
     * @param filter  过滤信息
     * @param groupBy 分组信息
     * @param hql     查询的基本语句，如：select new
     *                com.zd.school.good.model.good.GoodSignupCount(g.empName,count(
     *                g.actTime),sum(g.actTime),sum(g.rewardTime)) FROM GoodSignup g
     * @return 对象数据集
     */
    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public <T> QueryResult<T> doQueryCountToHql(Integer start, Integer limit, String sort, String filter, String hql,
                                                String groupBy, String orderBy) {
        // String hql = "select new
        // com.zd.school.good.model.good.GoodSignupCount(g.empName,count(g.actTime),sum(g.actTime),sum(g.rewardTime))
        // FROM GoodSignup g GROUP BY g.empName";
        StringBuffer hqlStringBuffer = new StringBuffer();
        hqlStringBuffer.append(hql);
        // hqlStringBuffer.append(" where 1=1 ");
        try {
            // 设置 了过滤条件，需要组装这些过滤条件
            List<ExtDataFilter> listFilters = null;
            if (StringUtils.isNotEmpty(filter)) {
                listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                        ExtDataFilter.class);
            }
            if (listFilters != null) {
                for (ExtDataFilter extDataFilter : listFilters) {
                    String type = extDataFilter.getType();
                    String field = extDataFilter.getField();
                    // String value = extDataFilter.getValue();
                    String comparison = extDataFilter.getComparison();
                    switch (type) {
                        case "string":
                            if (StringUtils.isEmpty(comparison)) {
                                hqlStringBuffer.append(" and g." + field + " like ?");
                            } else {
                                hqlStringBuffer.append(" and g." + field + " = ?");
                            }
                            break;
                        case "boolean":
                            hqlStringBuffer.append(" and g." + field + " = ?");
                            break;
                        case "numeric":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "float":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "date":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "time":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "list":// list的设置参数方式只能使用具名方式
                            hqlStringBuffer.append(" and g." + field + " in :lists");
                            break;
                        default:// 不在其中，则表示为实体类型
                            char c = type.charAt(0);
                            if (c <= 97) {
                                c = (char) (c + 32);
                                type = c + type.substring(1);
                            }
                            hqlStringBuffer.append(" and g." + type + " = ?");
                        /*
						 * try{ String hqlTemp="from "+type+" g where g."
						 * +field+"="+value; Object o =
						 * getSession().createQuery(hqlTemp).uniqueResult();
						 * char c = type.charAt(0); if(c>=97){ c=(char) (c-32);
						 * type=c+type.substring(1); } hqlStringBuffer.append(
						 * " and g."+type+"="+o); }catch(Exception e){
						 * System.out.println("实体参数转换出错了！"); }
						 */
                    }
                }
            }

            // 是否设置了分组条件
            if (StringUtils.isNotEmpty(groupBy)) {
                hqlStringBuffer.append(" " + groupBy);

            }

            // 是否设置了排序条件字符串
            if (StringUtils.isNotEmpty(orderBy)) {
                hqlStringBuffer.append(" " + orderBy);
                sort = ""; // 强行设置为空，防止下面的代码执行
            }

            // 构建排序条件
            if (StringUtils.isNotEmpty(sort)) {
                List<ExtSortModel> listSorts = (List<ExtSortModel>) JsonBuilder.getInstance().fromJsonArray(sort,
                        ExtSortModel.class);
                // hqlStringBuffer.append(" order by ");
                int temp = 0;
                for (int i = listSorts.size() - 1; i >= 0; i--) {
                    // for (ExtSortModel extSortModel : listSorts) {
                    ExtSortModel extSortModel = listSorts.get(i);
                    String direction = extSortModel.getDirection().toUpperCase();
                    String property = extSortModel.getProperty();
                    if (StringUtils.isNotEmpty(groupBy)) {
                        if (!groupBy.contains(property)) // 如果group中没包含orderby的属性，则排除,
                            // 否则语句出错
                            break;
                    }
                    if ("DESC".equals(direction)) {
                        if (temp == 0)
                            hqlStringBuffer.append(" order by  g." + property + " desc,");
                        else
                            hqlStringBuffer.append(" g." + property + " desc,");
                    } else if ("ASC".equals(direction)) {
                        if (temp == 0)
                            hqlStringBuffer.append(" order by  g." + property + " asc,");
                        else
                            hqlStringBuffer.append(" g." + property + " asc,");
                    }
                    temp++;
                }

                if (temp > 0)
                    hqlStringBuffer.deleteCharAt(hqlStringBuffer.length() - 1);
            }

            //System.out.println(hqlStringBuffer.toString());
            Query query = getSession().createQuery(hqlStringBuffer.toString());

            // 设置了过滤条件，在读数据之前需要给hql赋值
            if (listFilters != null) {
                int temp = 0;
                for (ExtDataFilter extDataFilter : listFilters) {
                    String type = extDataFilter.getType();
                    String field = extDataFilter.getField();
                    String value = extDataFilter.getValue();
                    // String comparison = extDataFilter.getComparison();

                    switch (type) {
                        case "string":
                            String comparison = extDataFilter.getComparison();
                            if (StringUtils.isEmpty(comparison) || comparison == "like") {
                                query.setParameter(temp, "%" + value + "%");
                            } else {
                                query.setParameter(temp, value);
                            }
                            break;
                        case "boolean":
                            query.setParameter(temp, Boolean.parseBoolean(value));
                            break;
                        case "numeric":
                            query.setParameter(temp, Integer.parseInt(value));
                            break;
                        case "float":
                            query.setParameter(temp, Float.parseFloat(value));
                            break;
                        case "date":
                            query.setParameter(temp, DateUtil.getDate(value));
                            break;
                        case "time":
                            query.setParameter(temp, DateUtil.getTime(value));
                            break;
                        case "list": // 如果为list的话
                            String values[] = value.split(",");
                            // List<String> list = Arrays.asList(s);
                            List<String> valuelist = new ArrayList<>();
                            for (String s : values) {
                                valuelist.add(s.trim());
                            }
                            query.setParameterList("lists", valuelist);
                            temp--;
                            break;
                        // query.setParameterList(temp,value);
                        default: // 不在其中，则表示为实体类型
                            try {
                                String hqlTemp = "from " + type + " g where g." + field + "=" + value;
                                Object o = getSession().createQuery(hqlTemp).uniqueResult();
                                query.setParameter(temp, o);
                            } catch (Exception e) {
                                System.out.println("实体参数转换出错了！");
                            }
                    }
                    temp++;
                }
            }

            QueryResult<T> qr = new QueryResult<T>();

            ScrollableResults scrollableResults = query.scroll(ScrollMode.SCROLL_SENSITIVE);
            scrollableResults.last();
            // rowNumber从0开始，为空时则为-1,因此计算totalCount时应+1
            int totalCount = scrollableResults.getRowNumber() + 1;

            qr.setTotalCount((long) totalCount);

            if (totalCount > 0) {
                if (limit > 0) {
                    query.setMaxResults(limit);
                    query.setFirstResult(start);
                }
                qr.setResultList(query.list());
            } else {
                qr.setResultList(new ArrayList());
            }
            return qr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 根据HQL查询对象统计信息集合与记录总数
     *
     * @param start   分页开始从多少条记录开始，初始为0
     * @param limit   每页多少条数据,为0则不分也
     * @param sort    排序信息
     * @param filter  过滤信息
     * @param groupBy 分组信息
     * @param hql     查询的基本语句，如：select new
     *                com.zd.school.good.model.good.GoodSignupCount(g.empName,count(
     *                g.actTime),sum(g.actTime),sum(g.rewardTime)) FROM GoodSignup g
     * @return 对象数据集
     */
    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public <T> QueryResult<T> doQueryCountToHql(Integer start, Integer limit, String sort, String filter, String hql,
                                                String groupBy, String orderBy, String where) {
        // String hql = "select new
        // com.zd.school.good.model.good.GoodSignupCount(g.empName,count(g.actTime),sum(g.actTime),sum(g.rewardTime))
        // FROM GoodSignup g GROUP BY g.empName";
        StringBuffer hqlStringBuffer = new StringBuffer();
        hqlStringBuffer.append(hql);
        hqlStringBuffer.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(where)) {
            hqlStringBuffer.append(where);
        }
        try {
            // 设置 了过滤条件，需要组装这些过滤条件
            List<ExtDataFilter> listFilters = null;
            if (StringUtils.isNotEmpty(filter)) {
                listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                        ExtDataFilter.class);
            }
            if (listFilters != null) {
                for (ExtDataFilter extDataFilter : listFilters) {
                    String type = extDataFilter.getType();
                    String field = extDataFilter.getField();
                    // String value = extDataFilter.getValue();
                    String comparison = extDataFilter.getComparison();
                    switch (type) {
                        case "string":
                            if (StringUtils.isEmpty(comparison)) {
                                hqlStringBuffer.append(" and g." + field + " like ?");
                            } else {
                                hqlStringBuffer.append(" and g." + field + " = ?");
                            }
                            break;
                        case "boolean":
                            hqlStringBuffer.append(" and g." + field + " = ?");
                            break;
                        case "numeric":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "float":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "date":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "time":
                            hqlStringBuffer.append(" and g." + GetComparisonString(comparison, field));
                            break;
                        case "list":// list的设置参数方式只能使用具名方式
                            hqlStringBuffer.append(" and g." + field + " in :lists");
                            break;
                        default:// 不在其中，则表示为实体类型
                            char c = type.charAt(0);
                            if (c <= 97) {
                                c = (char) (c + 32);
                                type = c + type.substring(1);
                            }
                            hqlStringBuffer.append(" and g." + type + " = ?");
                        /*
                         * try{ String hqlTemp="from "+type+" g where g."
                         * +field+"="+value; Object o =
                         * getSession().createQuery(hqlTemp).uniqueResult();
                         * char c = type.charAt(0); if(c>=97){ c=(char) (c-32);
                         * type=c+type.substring(1); } hqlStringBuffer.append(
                         * " and g."+type+"="+o); }catch(Exception e){
                         * System.out.println("实体参数转换出错了！"); }
                         */
                    }
                }
            }

            // 是否设置了分组条件
            if (StringUtils.isNotEmpty(groupBy)) {
                hqlStringBuffer.append(" " + groupBy);

            }

            // 是否设置了排序条件字符串
            if (StringUtils.isNotEmpty(orderBy)) {
                hqlStringBuffer.append(" " + orderBy);
                sort = ""; // 强行设置为空，防止下面的代码执行
            }

            // 构建排序条件
            if (StringUtils.isNotEmpty(sort)) {
                List<ExtSortModel> listSorts = (List<ExtSortModel>) JsonBuilder.getInstance().fromJsonArray(sort,
                        ExtSortModel.class);
                // hqlStringBuffer.append(" order by ");
                int temp = 0;
                for (int i = listSorts.size() - 1; i >= 0; i--) {
                    // for (ExtSortModel extSortModel : listSorts) {
                    ExtSortModel extSortModel = listSorts.get(i);
                    String direction = extSortModel.getDirection();
                    String property = extSortModel.getProperty();
                    if (StringUtils.isNotEmpty(groupBy)) {
                        if (!groupBy.contains(property)) // 如果group中没包含orderby的属性，则排除,
                            // 否则语句出错
                            break;
                    }
                    if ("DESC".equals(direction)) {
                        if (temp == 0)
                            hqlStringBuffer.append(" order by  g." + property + " desc,");
                        else
                            hqlStringBuffer.append(" g." + property + " desc,");
                    } else if ("ASC".equals(direction)) {
                        if (temp == 0)
                            hqlStringBuffer.append(" order by  g." + property + " asc,");
                        else
                            hqlStringBuffer.append(" g." + property + " asc,");
                    }
                    temp++;
                }

                if (temp > 0)
                    hqlStringBuffer.deleteCharAt(hqlStringBuffer.length() - 1);
            }

            //System.out.println(hqlStringBuffer.toString());
            Query query = getSession().createQuery(hqlStringBuffer.toString());

            // 设置了过滤条件，在读数据之前需要给hql赋值
            if (listFilters != null) {
                int temp = 0;
                for (ExtDataFilter extDataFilter : listFilters) {
                    String type = extDataFilter.getType();
                    String field = extDataFilter.getField();
                    String value = extDataFilter.getValue();
                    // String comparison = extDataFilter.getComparison();

                    switch (type) {
                        case "string":
                            String comparison = extDataFilter.getComparison();
                            if (StringUtils.isEmpty(comparison) || comparison == "like") {
                                query.setParameter(temp, "%" + value + "%");
                            } else {
                                query.setParameter(temp, value);
                            }
                            break;
                        case "boolean":
                            query.setParameter(temp, Boolean.parseBoolean(value));
                            break;
                        case "numeric":
                            query.setParameter(temp, Integer.parseInt(value));
                            break;
                        case "float":
                            query.setParameter(temp, Float.parseFloat(value));
                            break;
                        case "date":
                            query.setParameter(temp, DateUtil.getDate(value));
                            break;
                        case "time":
                            query.setParameter(temp, DateUtil.getTime(value));
                            break;
                        case "list": // 如果为list的话
                            String values[] = value.split(",");
                            // List<String> list = Arrays.asList(s);
                            List<String> valuelist = new ArrayList<>();
                            for (String s : values) {
                                valuelist.add(s.trim());
                            }
                            query.setParameterList("lists", valuelist);
                            temp--;
                            break;
                        // query.setParameterList(temp,value);
                        default: // 不在其中，则表示为实体类型
                            try {
                                String hqlTemp = "from " + type + " g where g." + field + "=" + value;
                                Object o = getSession().createQuery(hqlTemp).uniqueResult();
                                query.setParameter(temp, o);
                            } catch (Exception e) {
                                System.out.println("实体参数转换出错了！");
                            }
                    }
                    temp++;
                }
            }

            QueryResult<T> qr = new QueryResult<T>();

            ScrollableResults scrollableResults = query.scroll(ScrollMode.SCROLL_SENSITIVE);
            scrollableResults.last();
            // rowNumber从0开始，为空时则为-1,因此计算totalCount时应+1
            int totalCount = scrollableResults.getRowNumber() + 1;

            qr.setTotalCount((long) totalCount);

            if (totalCount > 0) {
                if (limit > 0) {
                    query.setMaxResults(limit);
                    query.setFirstResult(start);
                }
                qr.setResultList(query.list());
            } else {
                qr.setResultList(new ArrayList());
            }
            return qr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    private String GetComparisonString(String comparison, String field) {
        String res = "";
        switch (comparison) {
            case "<": // 小于
                res = field + "< ?";
                break;
            case ">": // 大于
                res = field + " > ?";
                break;
            case "=":// 等于
                res = field + " = ?";
                break;
            case "<=": // 小于等于
                res = field + " <= ?";
                break;
            case ">=": // 大于等于
                res = field + " >= ?";
                break;
            default: // 默认为等于
                res = field + " = ?";
                break;
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getForValue(String hql, Object... args) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i, args[i]);
        }

        T value = (T) query.uniqueResult();
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getForValues(String hql, Object... args) {
        // TODO Auto-generated method stub
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i, args[i]);
        }

        List<T> values = query.list();
        return values;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> List<T> doQuerySqlObject(String sql, Class<T> clz) {

        // TODO Auto-generated method stub
        Query query = getSession().createSQLQuery(sql);
        // List<ordercount> orderlist =
        // query.setResultTransformer(Transformers.aliasToBean(ordercount.class)).list();
        // List<T> values = query.list();
        List<T> values = query.setResultTransformer(Transformers.aliasToBean(clz)).list();

        return values;
    }

    @Override
    public Integer executeSql(String sql) {

        Query query = getSession().createSQLQuery(sql);

        return query.executeUpdate();
    }

    @Override
    public Integer getCountSql(String sql) {
        Integer c = 0;
        Query query = getSession().createSQLQuery(sql);
        Object count = query.uniqueResult();
        if (null != count && StringUtils.isInteger(count.toString())) {
            c = Integer.parseInt(count.toString());
        }
        return c;
    }

    @Override
    public List<E> doQuery(String hql, String propName, Object[] objs) {
        return doQuery(hql, 0, 0, propName, objs);
    }

    @Override
    public QueryResult<E> doQueryResult(String hql, Integer start, Integer limit) {
        Query query = this.getSession().createQuery(hql);
        QueryResult<E> qr = new QueryResult<E>();
        qr.setTotalCount((long) query.list().size());
        if (limit > 0) {
            query.setFirstResult(start);
            query.setMaxResults(limit);

        }
        qr.setResultList(query.list());
        return qr;
    }

    @Override
    public <T> List<T> doQuerySqlObject(String sql) {
        Query query = getSession().createSQLQuery(sql);

        List<T> values = query.list();
        return values;
    }

    @Override
    public <T> T getForValueToSql(String sql) {
        // TODO Auto-generated method stub
        Query query = getSession().createSQLQuery(sql);
        T value = (T) query.uniqueResult();
        return value;
    }

    @Override
    public List<Map<String, Object>> getForValuesToSql(String sql) {
        // TODO Auto-generated method stub
        Query query = getSession().createSQLQuery(sql);
        query.setResultTransformer(new ResultTransformer() {

            @Override
            public Object transformTuple(Object[] values, String[] columns) {
                Map<String, Object> map = new LinkedHashMap<String, Object>(1);
                int i = 0;
                for (String column : columns) {
                    map.put(column, values[i++]);
                }
                return map;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        });

        List<Map<String, Object>> value = query.list();
        return value;
    }

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
    @Override
    public <T> QueryResult<T> doQueryResultSqlObject(String sql, Integer start, Integer limit, Class<T> clz) {
        Query query = this.getSession().createSQLQuery(sql);
        QueryResult<T> qr = new QueryResult<T>();
        qr.setTotalCount((long) query.list().size());
        if (limit > 0) {
            query.setFirstResult(start);
            query.setMaxResults(limit);

        }
        qr.setResultList(query.setResultTransformer(Transformers.aliasToBean(clz)).list());
        return qr;
    }
    
    
    @Override
	@SuppressWarnings({ "unchecked" })
	public <T> List<T> queryEntityBySql(String sql, Class<T> clz) {
		Query query = getSession().createSQLQuery(sql);
		if (clz != null)
			query.setResultTransformer(Transformers.aliasToBean(clz));

		List<T> values = query.list();
		return values;
	}
    
    /**
	 * 修正分页的统计总数方式，推荐使用count(*)来查询总数
	 */
	@Override
	public QueryResult<E> queryPageResult(Integer start, Integer limit, String hql, String countHql) {
		Query query = this.getSession().createQuery(hql);
		QueryResult<E> qr = new QueryResult<E>();

		// 如果存在countHql，就使用count(*)查询
		if (StringUtils.isNotEmpty(countHql)) {
			Query query2 = this.getSession().createQuery(countHql);
			
			Object count = query2.uniqueResult();
			if (null != count) 
				qr.setTotalCount(Long.valueOf(count.toString()));
			else
				qr.setTotalCount((long) 0);
			
		} else {
			qr.setTotalCount((long) query.list().size());
		}

		if (limit > 0) {
			query.setFirstResult(start);
			query.setMaxResults(limit);

		}
		qr.setResultList(query.list());
		return qr;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public QueryResult<E> queryPageResult(Integer start, Integer limit, String sort, String filter, boolean isDelete) {
		Criteria criteria = getSession().createCriteria(this.entityClass);
		// 如果isDlete 为true 则为只列出未删除的数据
		if (isDelete) {
			criteria.add(Restrictions.eq("isDelete", Integer.parseInt(StatuVeriable.ISNOTDELETE)));
		}
		// 设置 了过滤条件，需要组装这些过滤条件
		if (StringUtils.isNotEmpty(filter)) {
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
						criteria.add(Restrictions.like(field, "%" + value + "%"));
					} else {
						criteria.add(GetComparison(comparison, field, value));
					}
					break;
				case "boolean":
					criteria.add(Restrictions.eq(field, Boolean.parseBoolean(value)));
					break;
				case "short":
					criteria.add(GetComparison(comparison, field, Short.parseShort(value)));
					break;
				case "numeric":
					criteria.add(GetComparison(comparison, field, Integer.parseInt(value)));
					break;
				case "float":
					criteria.add(GetComparison(comparison, field, Float.parseFloat(value)));
					break;
				case "date":
					criteria.add(GetComparison(comparison, field, DateUtil.getDate(value)));
					break;
				case "time":
					criteria.add(GetComparison(comparison, field, DateUtil.getTime(value)));
					break;
				default:// 不在其中，则表示为实体类型
					try {
						String hqlTemp = "from " + type + " g where g." + field + " = ?";
						Query query = getSession().createQuery(hqlTemp);
						query.setParameter(0, value);
						Object o = query.uniqueResult();
						char c = type.charAt(0);
						if (c <= 97) {
							c = (char) (c + 32);
							type = c + type.substring(1);
						}
						criteria.add(Restrictions.eq(type, o));
					} catch (Exception e) {
						System.out.println("实体参数转换出错了！");
					}
				}
			}
		}
		try {
			QueryResult<E> qr = new QueryResult<E>();
			criteria.setProjection(Projections.rowCount());
			qr.setTotalCount(Long.valueOf(((Number) criteria.uniqueResult()).longValue()));
			if (qr.getTotalCount().longValue() > 0L) {
				// 构建排序条件
				if (StringUtils.isNotEmpty(sort)) {
					List<ExtSortModel> listSorts = (List<ExtSortModel>) JsonBuilder.getInstance().fromJsonArray(sort,
							ExtSortModel.class);
					// for(int i=listSorts.size()-1;i>=0;i--){
					for (ExtSortModel extSortModel : listSorts) {
						// ExtSortModel extSortModel=listSorts.get(i);
						String direction = extSortModel.getDirection().toUpperCase();
						String property = extSortModel.getProperty();
						if ("DESC".equals(direction)) {
							criteria.addOrder(Order.desc(property));
						} else if ("ASC".equals(direction)) {
							criteria.addOrder(Order.asc(property));
						}
					}
				}
				criteria.setProjection(null);
				if (limit <= 0)
					criteria.setMaxResults(Integer.MAX_VALUE);
				else
					criteria.setMaxResults(limit);

				criteria.setFirstResult(start);
				qr.setResultList(criteria.list());
			} else {
				qr.setResultList(new ArrayList());
			}
			return qr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryEntityByHql(String hql, Object... args) {
		Query query = getSession().createQuery(hql);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		List<T> values = query.list();
		return values;
	}
	
	/**
	 * hql语句查询实体列表
	 *
	 * @param hql
	 *            查询语句
	 * @return
	 */
	@Override
	public List<E> queryByHql(String hql) {
		return queryByHql(hql, 0, 0);
	}
	
	/**
	 * 根据HQL查询实体列表
	 *
	 * @param hql
	 *            查询语句
	 * @param start
	 *            返回记录起始位置
	 * @param limit
	 *            返回最大记录数
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<E> queryByHql(String hql, Integer start, Integer limit) {
		Query query = getSession().createQuery(hql);
		if (limit > 0) {
			query.setFirstResult(start);
			query.setMaxResults(limit);

		}
		return query.list();
	}
	
	/**
	 * 逻辑删除或还原指定的记录
	 *
	 * @param ids
	 *            要处理的记录的ID,多个ID使用","间隔
	 * @param isDelete
	 *            处理标记
	 * @return
	 */
	@Override
	public boolean logicDelOrRestore(String ids, String isDelete, String operator) {
		String doIds = "'" + ids.replace(",", "','") + "'";
		String pkName = ModelUtil.getClassPkName(entityClass);
		String entityName = entityClass.getSimpleName();
		String updateTime = DateUtil.formatDateTime(new Date());
		String hql = "UPDATE " + entityName + " SET isDelete='" + isDelete + "', updateTime= CONVERT(datetime,'"
				+ updateTime + "'),updateUser='" + operator + "' WHERE " + pkName + " IN (" + doIds + ")";

		return getExecuteCountByHql(hql) > 0;
	}
	
	/**
	 * 执行HQL语句并返回受影响的记录的条数
	 *
	 * @param hql
	 *            要执行的HQL语句
	 * @return 受影响的记录数
	 */
	@Override
	public Integer getExecuteCountByHql(String hql) {
		Query query = getSession().createQuery(hql);

		return query.executeUpdate();
	}
	
	/**
	 * 带分页的sql查询并转换成实体类;修正totalCount值的获取方式
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @param start
	 *            起始页
	 * @param limit
	 *            每页记录数
	 * @param clz
	 *            要转换成的实体类
	 * @param <T>
	 *            实体类的泛型参数
	 * @return 返回转换后的结果
	 */
	@Override
	public <T> QueryResult<T> queryPageResultBySql(String sql,Integer start, Integer limit,
			Class<T> clz, String countSql) {
		Query query = this.getSession().createSQLQuery(sql);
		QueryResult<T> qr = new QueryResult<T>();

		if (StringUtils.isNotEmpty(countSql)) {
			Query query2 = this.getSession().createSQLQuery(countSql);
			
			Object count = query2.uniqueResult();
			if (null != count) 
				qr.setTotalCount(Long.valueOf(count.toString()));
			else
				qr.setTotalCount((long) 0);
			
			//qr.setTotalCount(Long.parseLong(query2.uniqueResult().toString()));
			
		} else {
			qr.setTotalCount((long) query.list().size());
		}

		if (limit > 0) {
			query.setFirstResult(start);
			query.setMaxResults(limit);

		}
		if (clz != null) {
			qr.setResultList(query.setResultTransformer(Transformers.aliasToBean(clz)).list());
		} else {
			// 转为hashmap
			query.setResultTransformer(new ResultTransformer() {

				@Override
				public Object transformTuple(Object[] values, String[] columns) {
					Map<String, Object> map = new LinkedHashMap<String, Object>(1);
					int i = 0;
					for (String column : columns) {
						map.put(column, values[i++]);
					}
					return map;
				}

				@Override
				public List transformList(List list) {
					return list;
				}
			});
			qr.setResultList(query.list());
		}
		return qr;
	}


	@Override
	@SuppressWarnings("unchecked")
	public <T> T getEntityByHql(String hql, Object... args) {
		Query query = getSession().createQuery(hql);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		T value = (T) query.uniqueResult();
		return value;
	}
	
	@Override
	public Integer getQueryCountBySql(String sql) {
		Integer c = 0;
		Query query = getSession().createSQLQuery(sql);
		Object count = query.uniqueResult();
		if (null != count && StringUtils.isInteger(count.toString())) {
			c = Integer.parseInt(count.toString());
		}
		return c;
	}
}

