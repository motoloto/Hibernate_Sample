package com.dept.model;

/*
 Hibernate is providing a factory.getCurrentSession() method for retrieving the current session. A
 new session is opened for the first time of calling this method, and closed when the transaction is
 finished, no matter commit or rollback. But what does it mean by the ��current session��? We need to
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
	/* �`�N:
       A. �ثe�u�O��B����Hibernate���򥻥\��
       B. �ثe�|�ݤ��XHibernate���¤O�Ҧb
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
			
			// ���R�����u
			Query query1 = session.createQuery(DELETE_EMPs);
			query1.setParameter(0, deptno);
			updateCount_EMPs = query1.executeUpdate();			
			
//			�i�R�������ɥi�ĥΤU����ؤ覡�ܤ@:�j
			
			// �A�R������-1 (by HQL)
			Query query2 = session.createQuery(DELETE_DEPT);
			query2.setParameter(0, deptno);
			query2.executeUpdate();
			
			// �A�R������-2 (��  by session - cascade)
//			DeptVO deptVO = (DeptVO) session.get(DeptVO.class, deptno);
//			session.delete(deptVO);
			
			session.getTransaction().commit();			
			System.out.println("�R�������s��" + deptno + "��,�@�����u" + updateCount_EMPs
					+ "�H�P�ɳQ�R��");
			
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

		// �s�W
//		DeptVO deptVO1 = new DeptVO();
//		deptVO1.setDname("�s�y��");
//		deptVO1.setLoc("���ꦿ��");
//		dao.insert(deptVO1);

		// �ק�
//		DeptVO deptVO2 = new DeptVO();
//		deptVO2.setDeptno(10);
//		deptVO2.setDname("�]�ȳ�2");
//		deptVO2.setLoc("�O�W�x�_2");
//		dao.update(deptVO2);

		// �R��
//		dao.delete(30);

		// �d��
//		DeptVO deptVO3 = dao.findByPrimaryKey(10);
//		System.out.print(deptVO3.getDeptno() + ",");
//		System.out.print(deptVO3.getDname() + ",");
//		System.out.println(deptVO3.getLoc());
//		System.out.println("---------------------");

		// �d�߳���
		List<DeptVO> list = dao.getAll();
		for (DeptVO aDept : list) {
			System.out.print(aDept.getDeptno() + ",");
			System.out.print(aDept.getDname() + ",");
			System.out.print(aDept.getLoc());
			System.out.println();
		}
		
		// �d�߬Y���������u
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
