package com.sjsu.cmpe275.netflix.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subscription")
public class Subscription {
	
	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="subscription_start_date")
	private Date subscriptionStartDate;
	
	@Column(name="subscription_end_date")
	private Date subscriptionEndDate;
	
	public Subscription() {
		
	}
	
	public Subscription(String email, Date subscriptionStartDate, Date subscriptionEndDate) {
		super();
		this.email = email;
		this.subscriptionStartDate = subscriptionStartDate;
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

}
