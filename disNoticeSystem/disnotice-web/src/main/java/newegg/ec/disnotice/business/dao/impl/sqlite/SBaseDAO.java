package newegg.ec.disnotice.business.dao.impl.sqlite;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.Id;

import newegg.ec.disnotice.business.dao.util.DefineParameter;
import newegg.ec.disnotice.business.dao.util.DefineParameter.CompareOption;
import newegg.ec.disnotice.business.dao.util.LockControl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author William Zhu
 */
public abstract class SBaseDAO<T> {

    static Log log = LogFactory.getLog(SBaseDAO.class);
    /**
     * hibernate init conf
     */
    private final static String xmlFileName = "hibernate-disnotice.cfg.xml";

    protected static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration();
        configuration.configure(xmlFileName);
        Properties properties = configuration.getProperties();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    protected SBaseDAO(String tableName) {

    }

    public SBaseDAO() {

    }

    public static SessionFactory getSesssion() {
        return sessionFactory;
    }


    /**
     * execute a sql independency of hibernate
     *
     * @param sql
     */
    protected void executeSql(String sql) {
        log.info("execute sql:" + sql);
        Session session = null;
        try {
            LockControl.lockWriteTransaction();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("execute sql failed sql=" + sql + e);
        } finally {
            if (null != session) {
                session.close();
            }
            LockControl.unlockWriteTransaction();
        }

    }

    /**
     * delete an entity
     */
    protected void executeHql(String hql) {
        log.info("execute hql:" + hql);
        Session session = null;
        try {
            LockControl.lockWriteTransaction();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery(hql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("execute hql failed hql=" + hql + e);
        } finally {
            if (null != session) {
                session.close();
            }
            LockControl.unlockWriteTransaction();
        }
    }

    /**
     * delete an entity
     *
     * @param dao
     */
    protected void delete(T dao) {
        Session session = null;
        try {
            LockControl.lockWriteTransaction();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.getTransaction().setTimeout(DefineParameter.sqliteTimeout);
            session.delete(dao);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e + "delete dto is " + dao + " false");
        } finally {
            if (null != session) {
                session.close();
            }
            LockControl.unlockWriteTransaction();
        }

    }

    /**
     * get object by ids{id1,id2...}
     *
     * @param ids
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "finally"})
    protected Set<T> getByIds(String[] ids, Class<T> clazz) {
        Session session = null;
        Set<T> sets = new TreeSet<T>();
        try {
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(clazz);
            Disjunction or = Restrictions.disjunction();
            for (String id : ids) {
                or.add(Restrictions.idEq(id));
            }
            // add empty or restriction when ids is empty
            if(ids.length==0){
                or.add(Restrictions.idEq(""));
            }
            crit.add(or);
            List<T> getList = crit.list();
            for (T dto : getList) {
                parseToTake(dto);
                sets.add(dto);
            }
        } catch (Exception e) {
            log.error(e + "getByIds from " + clazz.getName() + " false");
        } finally {
            if (null != session) {
                session.close();
            }
            return sets;
        }
    }

    /**
     * get object by id
     *
     * @param id
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "finally"})
    protected T getById(String id, Class<T> clazz) {
        T dto = null;
        Session session = null;
        try {
            log.info("getById from " + clazz.getName() + " id=" + id);
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(clazz);
            crit.add(Restrictions.idEq(id));
            List<T> getList = crit.list();
            if (null != getList && getList.size() == 1) {
                dto = getList.get(0);
                parseToTake(dto);
            }
        } catch (Exception e) {
            log.error(e + "getById from " + clazz.getName() + " id=" + id);
        } finally {
            if (null != session) {
                session.close();
            }
            return dto;
        }
    }

    /**
     * get all object from clazz.entity.tableName
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "finally"})
    protected Set<T> getAll(Class<T> clazz) {

        Set<T> sets = new TreeSet<T>();
        Session session = null;
        try {
            log.info("getAll from " + clazz.getName());
            session = sessionFactory.openSession();
            List<T> dtos = session.createQuery("from " + clazz.getName()).list();
            for (T dto : dtos) {
                parseToTake(dto);
                sets.add(dto);
                log.debug("get a dto is : " + dto.toString());
            }

        } catch (Exception e) {
            log.error(e + "getAll from " + clazz.getName() + "false");
        } finally {
            if (null != session) {
                session.close();
            }
            return sets;
        }

    }

    /**
     * get object by specified column with given value
     *
     * @param columnName
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "finally"})
    protected Set<T> getByColumnValue(String columnName, Object key, Class<T> clazz) {
        Set<T> sets = new TreeSet<T>();
        Session session = null;
        try {
            log.info("getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " begin");
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(clazz);
            crit.add(Restrictions.eq(columnName, key));
            List<T> getList = crit.list();
            for (T dto : getList) {
                parseToTake(dto);
                log.info("get a dto is : " + dto.toString());
                sets.add(dto);
            }
            log.info("getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " end");
        } catch (Exception e) {
            log.error(e + "getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " false");
        } finally {
            if (null != session) {
                session.close();
            }
            return sets;
        }
    }

    /**
     * get object by specified column with given value
     *
     * @param columnName
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "finally"})
    protected Set<T> getByColumnValue(String columnName, Object key, Class<T> clazz, int numbs) {
        Set<T> sets = new TreeSet<T>();
        Session session = null;
        try {
            log.info("getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " begin");
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(clazz);
            crit.add(Restrictions.eq(columnName, key));
            crit.setMaxResults(numbs);
            List<T> getList = crit.list();
            for (T dto : getList) {
                parseToTake(dto);
                log.info("get a dto is : " + dto.toString());
                sets.add(dto);
            }
            log.info("getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " end");
        } catch (Exception e) {
            log.error(e + "getByColumnValue from " + clazz.getName() + " columnName=" + columnName + " Object" + key + " false");
        } finally {
            if (null != session) {
                session.close();
            }
            return sets;
        }
    }

    protected boolean existColumnValue(String columnName, Object key, Class<T> clazz) {
        Set<T> returnResult = getByColumnValue(columnName, key, clazz, 1);
        if (null != returnResult && returnResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get table Name from clazz
     *
     * @param clazz
     * @return
     */
    protected String getTableName(Class<T> clazz) {
        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);
        return entityAnnotation.name();
    }

    /**
     * get @id name from clazz
     *
     * @param clazz
     * @return
     */
    protected String getPrimaryID(Class<T> clazz) {
        // ClassMetadata meta = sessionFactory.getClassMetadata(clazz);
        // return meta.getIdentifierPropertyName();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (null != id) {
                return field.getName();
            }
        }
        return null;
    }

    /**
     * parse object when get object call
     *
     * @param obj
     */
    protected abstract void parseToTake(T obj);

    /**
     * parse object when saveTableAsDTO call
     *
     * @param obj
     */
    protected abstract void parseToSave(T obj);

    /**
     * delete row from table(clazz.entity.tableName) where @id=key
     *
     * @param clazz
     * @param key
     */
    protected void delTableByKey(Class<T> clazz, String key) throws Exception {
        Session session = null;
        try {
            LockControl.lockWriteTransaction();
            log.info("delTableByKey from " + clazz.getName() + "key=" + key + " begin");
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.getTransaction().setTimeout(DefineParameter.sqliteTimeout);
            String hql = "Delete from " + clazz.getName() + " where " + getPrimaryID(clazz) + " = '" + key + "'";
            session.createQuery(hql).executeUpdate();
            session.getTransaction().commit();
            log.info("delTableByKey from " + clazz.getName() + "key=" + key + " end");
        } catch (Exception e) {
            log.error(e + "delTableByKey from " + clazz.getName() + "key=" + key + " false");
            throw e;
        } finally {
            if (null != session) {
                session.close();
            }
            LockControl.unlockWriteTransaction();
        }

    }

    /**
     * change from CompareOption to CompareOp
     *
     * @param compareOption
     * @return
     */
    protected SimpleExpression getSimpleExpressionForScanTableByColumnValue(String column, Object obj, CompareOption compareOption) {

        if (compareOption == null) {
            return Restrictions.eq(column, obj);
        }
        switch (compareOption) {
            case LESS:
                return Restrictions.lt(column, obj);
            case LESS_OR_EQUAL:
                return Restrictions.le(column, obj);
            case EQUAL:
                return Restrictions.eq(column, obj);
            case NOT_EQUAL:
                return Restrictions.ne(column, obj);
            case GREATER_OR_EQUAL:
                return Restrictions.ge(column, obj);
            case GREATER:
                return Restrictions.gt(column, obj);
            default:
                throw new RuntimeException("Unknown Compare op " + compareOption.name());
        }
    }

    /**
     * @param clazz
     * @param column
     * @param key
     * @param compareOption
     * @return scan results
     */
    @SuppressWarnings({"finally", "unchecked"})
    protected Set<T> scanTableByColumnValue(Class<T> clazz, String column, Object key, CompareOption compareOption) {

        Session session = null;
        Set<T> sets = new TreeSet<T>();
        try {
            log.info("scanTableByColumnValue from " + clazz.getName() + "column=" + column + " key=" + key + " begin");
            session = sessionFactory.openSession();
            Criteria crit = session.createCriteria(clazz);
            crit.add(getSimpleExpressionForScanTableByColumnValue(column, key, compareOption));
            List<T> dtos = crit.list();

            for (T dto : dtos) {
                parseToTake(dto);
                sets.add(dto);
                log.debug("get a dto is : " + dto.toString());
            }
            log.info("scanTableByColumnValue from " + clazz.getName() + "column=" + column + " key=" + key + " end");
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (null != session) {
                session.close();
            }
            return sets;
        }
    }

    /**
     * sava an entity
     *
     * @param obj
     */
    protected void saveTableAsDTO(T obj) {

        Session session = null;
        try {
            LockControl.lockWriteTransaction();
            parseToSave(obj);
            log.info("Insert sqlite table " + obj.getClass().getName() + " begin");
            log.debug("obj is :" + obj);
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.getTransaction().setTimeout(DefineParameter.sqliteTimeout);
            session.saveOrUpdate(obj);
            session.getTransaction().commit();
            log.info("Insert sqlite table " + obj.getClass().getName() + " end");
        } catch (Exception e) {
            log.error(e + "Insert sqlite table " + obj.getClass().getName() + " false");
        } finally {
            if (null != session) {
                session.close();
            }
            LockControl.unlockWriteTransaction();
        }
    }
}
