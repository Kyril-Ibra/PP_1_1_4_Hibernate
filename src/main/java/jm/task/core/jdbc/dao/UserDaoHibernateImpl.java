package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction txn = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            txn = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users (id INTEGER NOT NULL PRIMARY KEY " +
                    "auto_increment, name VARCHAR(45), lastName VARCHAR(45), age INTEGER(3))").executeUpdate();
            txn.commit();
        } catch (HibernateException e) {
            if (txn != null) {
                txn.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction txn = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            txn = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            txn.commit();
        } catch (HibernateException e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction txn = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            User user = new User(name, lastName, age);
            txn = session.beginTransaction();
            Long userId = (Long) session.save(user);
            txn.commit();
        } catch (Exception e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction txn = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            txn = session.beginTransaction();
            User user1 = session.get(User.class, id);
            session.delete(user1);
        } catch (HibernateException e) {
            e.printStackTrace();
            if (txn != null) {
                txn.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction txn = null;
        List<User> users = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            txn = session.beginTransaction();
            users = session.createQuery("from User").list();
            txn.commit();
        } catch (Exception e) {
            if (txn != null)
                txn.rollback();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction txn = null;
        try (Session session = Util.HibernateUtil.getSessionFactory().openSession()) {
            txn = session.beginTransaction();
            session.createSQLQuery("truncate table users").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        }
    }
}
