package com.dept.model;

import java.util.*;
import com.emp.model.EmpVO;

public class DeptVO  implements java.io.Serializable{
	private Integer deptno;
	private String dname;
	private String loc;
	private Set<EmpVO> emps = new HashSet<EmpVO>();
	
	public Integer getDeptno() {
		return deptno;
	}
	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public Set<EmpVO> getEmps() {
		return emps;
	}
	public void setEmps(Set<EmpVO> emps) {
		this.emps = emps;
	}
	
	
}
