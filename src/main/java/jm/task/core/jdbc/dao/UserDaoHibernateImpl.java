package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Transaction transaction;
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (id INTEGER NOT NULL PRIMARY KEY " +
                    "auto_increment, name VARCHAR(45), lastName VARCHAR(45), age INTEGER(3))").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.HibernateUtil.getSessionFactory().openSession();
        User user = new User(name, lastName, age);
        transaction = session.beginTransaction();
        Long userId = (Long) session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user1 = session.get(User.class, id);
            session.delete(user1);
            session.getTransaction().commit(); //
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        Query query = session.createQuery(cq);
        List<User> userList = query.getResultList();
        session.getTransaction().commit();
        session.close();


        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("truncate table users").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
