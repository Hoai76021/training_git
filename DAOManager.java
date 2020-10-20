package DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.query.NativeQuery;

public class DAOManager<T> {//Generic Class. List<T> ArrayList<T> Set<T> 
	
	private Class<T> type;
	
	public DAOManager(Class<T> type) {
		this.type = type;
	}

	public void exit() {
	}

	public boolean create(T object) {
		
		Transaction transaction = null;
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			transaction = session.beginTransaction();
			session.save(object);
			
			transaction.commit();
			
			session.close();
			
		} catch(Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<T> getList(String... order) {  //String[]
		
		List<T> list = new ArrayList<T>();
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			Criteria criteria = session.createCriteria(type); 
			
			if(order.length > 0) {
				criteria.addOrder(Order.asc(order[0]));
			}
			list = criteria.list(); 
			
			session.close();
			
		} catch(Exception e) {
		
			e.printStackTrace();
		}

		return list;
	}
	

	public T findById(Object primaryKey) {
		
		T obj = null;
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			obj = session.find(type, primaryKey);
			
			session.close();
			
		} catch(Exception e) {
		
			e.printStackTrace();
		}

		return obj;
	}

	public boolean update(T object) {
		
		Transaction transaction = null;
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			transaction = session.beginTransaction();
			
			session.update(object);
			
			transaction.commit();
			
			session.close();
			
		} catch(Exception e) {
			
			if(transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean delete(T object) {
		
		Transaction transaction = null;
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			transaction = session.beginTransaction();
			
			session.delete(object);
			
			transaction.commit();
			
			session.close();
			
		} catch(Exception e) {
			
			if(transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean executeQuery(String query) { 
		
		Transaction transaction = null;
		
		try (Session session = HibernateManager.getSessionFactory().openSession()) {
			
			transaction = session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			NativeQuery<T> q = session.createSQLQuery(query);
			int rs = q.executeUpdate();
			
			transaction.commit();
			
			session.close();
			
			return true;
			
		} catch(Exception e) {
			
			if(transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			return false;
		}
	}
}
