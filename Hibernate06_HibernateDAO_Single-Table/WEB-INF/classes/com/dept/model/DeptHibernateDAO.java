package com.dept.model;

/*
 Hibernate is providing a factory.getCurrentSession() method for retrieving the current session. A
 new session is opened for the first time of calling this method, and closed when the transaction is
 finished, no matter commit or rollback. But what does it mean by the “current session”? We need to
 tell Hibernate that it should be the session bound with the current thread.

 <hibernate-configuration>
 <session-factory>
 ...
 <property name="current_session_context_class">thread</property>
 ...
 </session-factory>
 </hibernate-configuration>

 */


import org.hibernate.*;
import hibernate.util.HibernateUtil;
import java.util.*;
import com.emp.model.EmpVO;

public class DeptHibernateDAO implements DeptDAO_interface {
	/* 注意:
       A. 目前只是初步測試Hibernate的基本功能
       B. 目前尚看不出Hibernate的威力所在
    */
	private static final String GET_ALL_STMT = "from DeptVO order by deptno";
	private static final String DELETE_EMPs = "delete from EmpVO where deptno = ?";
	private static final String DELETE_DEPT = "delete from DeptVO where deptno = ?";	
	private static final String GET_Emps_ByDeptno_STMT = "from EmpVO as empvo where empvo.deptno=? order by empvo.empno";

	@Override
	public void insert(DeptVO deptVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(deptVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(DeptVO deptVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(deptVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Integer deptno) {
		int updateCount_EMPs = 0;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			
			// 先刪除員工
			Query query1 = session.createQuery(DELETE_EMPs);
			query1.setParameter(0, deptno);
			updateCount_EMPs = query1.executeUpdate();			
			
//			【刪除部門時可採用下面兩種方式擇一:】
			
			// 再刪除部門-1 (by HQL)
			Query query2 = session.createQuery(DELETE_DEPT);
			query2.setParameter(0, deptno);
			query2.executeUpdate();
			
			// 再刪除部門-2 (或  by session - cascade)
//			DeptVO deptVO = (DeptVO) session.get(DeptVO.class, deptno);
//			session.delete(deptVO);
			
			session.getTransaction().commit();			
			System.out.println("刪除部門編號" + deptno + "時,共有員工" + updateCount_EMPs
					+ "人同時被刪除");
			
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public DeptVO findByPrimaryKey(Integer deptno) {
		DeptVO deptVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			deptVO = (DeptVO) session.get(DeptVO.class, deptno);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return deptVO;
	}

	@Override
	public List<DeptVO> getAll() {
		List<DeptVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_STMT);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	
	@Override
	public Set<EmpVO> getEmpsByDeptno(Integer deptno) {		
		Set<EmpVO> set = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_Emps_ByDeptno_STMT);
			query.setParameter(0, deptno);
			List list = query.list();
			set = new HashSet<EmpVO>(list);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return set;
	}

	public static void main(String[] args) {

		DeptHibernateDAO dao = new DeptHibernateDAO();

		// 新增
//		DeptVO deptVO1 = new DeptVO();
//		deptVO1.setDname("製造部");
//		deptVO1.setLoc("中國江西");
//		dao.insert(deptVO1);

		// 修改
//		DeptVO deptVO2 = new DeptVO();
//		deptVO2.setDeptno(10);
//		deptVO2.setDname("財務部2");
//		deptVO2.setLoc("臺灣台北2");
//		dao.update(deptVO2);

		// 刪除
//		dao.delete(30);

		// 查詢
//		DeptVO deptVO3 = dao.findByPrimaryKey(10);
//		System.out.print(deptVO3.getDeptno() + ",");
//		System.out.print(deptVO3.getDname() + ",");
//		System.out.println(deptVO3.getLoc());
//		System.out.println("---------------------");

		// 查詢部門
		List<DeptVO> list = dao.getAll();
		for (DeptVO aDept : list) {
			System.out.print(aDept.getDeptno() + ",");
			System.out.print(aDept.getDname() + ",");
			System.out.print(aDept.getLoc());
			System.out.println();
		}
		
		// 查詢某部門的員工
//		Set<EmpVO> set = dao.getEmpsByDeptno(10);
//		for (EmpVO aEmp : set) {
//			System.out.print(aEmp.getEmpno() + ",");
//			System.out.print(aEmp.getEname() + ",");
//			System.out.print(aEmp.getJob() + ",");
//			System.out.print(aEmp.getHiredate() + ",");
//			System.out.print(aEmp.getSal() + ",");
//			System.out.print(aEmp.getComm() + ",");
//			System.out.print(aEmp.getDeptno());
//			System.out.println();
//		}
	}
}
