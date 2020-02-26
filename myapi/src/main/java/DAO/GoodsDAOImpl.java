package DAO;
import Entity.Goods;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

public class GoodsDAOImpl implements GoodsDAO {

    public Goods findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Goods.class, id);
    }

    public void save(Goods goods) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(goods);
        tx1.commit();
        session.close();
    }

    public void update(Goods goods) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(goods);
        tx1.commit();
        session.close();
    }

    public void delete(Goods goods) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(goods);
        tx1.commit();
        session.close();
    }

    public List<Goods> findAll() {
        List<Goods> goods = (List<Goods>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Goods").list();
        return goods;
    }

    public Goods findByWarehouseAndCode(String warehouse, long code){
        String sql = "select * FROM schema.goods WHERE warehouse_name = '" + warehouse + "' AND code = '" + code + "';";
        //System.out.println(sql);
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createSQLQuery(sql).addEntity(Goods.class);

        List<Goods> goods = ((NativeQuery) query).list();
        Goods good = goods.get(0);
       //System.out.println("Айди записи запроса bywarehouse= " + good.getId());

        return good;
    }

    public List<Goods> findByCode(long code){
        String sql = "select * FROM schema.goods WHERE code = '" + code + "'";
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createSQLQuery(sql).addEntity(Goods.class);

        List<Goods> goods = ((NativeQuery) query).list();
       // System.out.println("МАССИВ СПИСКА BY CODE:");
       // System.out.println("goodsByCode.size = " + goods.size());
        return goods;
    }

    public List<Goods> findByWarehouse(String warehouse){
        String sql = "select * FROM schema.goods WHERE warehouse_name = '" + warehouse + "'";
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createSQLQuery(sql).addEntity(Goods.class);

        List<Goods> goods = ((NativeQuery) query).list();
        // System.out.println("МАССИВ СПИСКА BY CODE:");
        // System.out.println("goodsByCode.size = " + goods.size());
        return goods;
    }

    public List<Goods> findByName(String name){
        String sql = "select * FROM schema.goods WHERE name = '" + name + "'";
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createSQLQuery(sql).addEntity(Goods.class);

        List<Goods> goods = ((NativeQuery) query).list();
        // System.out.println("МАССИВ СПИСКА BY CODE:");
        // System.out.println("goodsByCode.size = " + goods.size());
        return goods;
    }
}