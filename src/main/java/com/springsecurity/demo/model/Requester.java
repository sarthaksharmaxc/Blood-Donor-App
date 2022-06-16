package com.springsecurity.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

@Entity
public class Requester {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="req_id")
	private long reqid;
	
	private String demailId;
	private String remailId;
		
	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	public long getReqid() {
		return reqid;
	}

	public void setReqid(long reqid) {
		this.reqid = reqid;
	}

	public String getDemailId() {
		return demailId;
	}

	public void setDemailId(String demailId) {
		this.demailId = demailId;
	}

	public String getRemailId() {
		return remailId;
	}

	public void setRemailId(String remailId) {
		this.remailId = remailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Requester [reqid=" + reqid + ", demailId=" + demailId + ", remailId=" + remailId + ", firstName="
				+ firstName + ", lastName=" + lastName + "]";
	}
	
	//@Nullable
//	private boolean isEnabled;
	
	
}
