package DAO;

import Entity.DocumentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class DocumentDAOImpl implements DocumentDAO {

    @Override
    public DocumentEntity findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(DocumentEntity.class, id);
    }

    @Override
    public void save(DocumentEntity document) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(document);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(DocumentEntity document) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(document);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(DocumentEntity document) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(document);
        tx1.commit();
        session.close();
    }
    @Override
    public List<DocumentEntity> findAll() {
        List<DocumentEntity> document = (List<DocumentEntity>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From DocumentEntity").list();
        return document;
    }

}
