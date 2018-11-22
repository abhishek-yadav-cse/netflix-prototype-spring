package com.sjsu.cmpe275.netflix.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import org.json.JSONObject;

import com.sjsu.cmpe275.netflix.model.Subscription;
import com.sjsu.cmpe275.netflix.repository.SubscriptionRepository;

@RestController
@RequestMapping(value = "/subscription")
public class Customer_details {
	
	//get_user,	subscribe
	
	@Autowired
	SubscriptionRepository repository;

	//User_Admin,	subscription
	
	@RequestMapping(value = "/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubscriptionDetails(@PathVariable("email") String email) {
        return getSubscription(email);
    }
	
	private ResponseEntity<?> getSubscription(String email) {
		
		Subscription subscription = new Subscription();
		
		subscription.setEmail(email);
		subscription.setSubscriptionStartDate(repository.getSubscriptionStartDate(email));
		subscription.setSubscriptionEndDate(repository.getSubscriptionEndDate(email));
            
        return new ResponseEntity<>(subscription, HttpStatus.OK);

    }
	
//	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> updateSubscriptionDetails(HttpSession httpSession) {
//		ResponseEntity responseEntity = new ResponseEntity(null, HttpStatus.NOT_FOUND);
//		
//		try {
//			System.out.println(httpSession.getAttribute("email"));
//			if (httpSession.getAttribute("email") != null) {
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.put("email", httpSession.getAttribute("email"));
//				httpSession.getAttribute("email");
////				if(repository.getSubscriptionStartDate(email) == null) {
////					repository.updateSubscriptionStartDate();
////				}
//				System.out.println(jsonObject);
////				responseEntity = new ResponseEntity(user, HttpStatus.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return responseEntity;
//    }
//	
//	private ResponseEntity<?> updateSubscription(String email) {
//			
//			Subscription subscription = new Subscription();
//			
//			subscription.setEmail(email);
//			subscription.setSubscriptionStartDate(repository.getSubscriptionStartDate(email));
//			subscription.setSubscriptionEndDate(repository.getSubscriptionEndDate(email));
//	            
//	        return new ResponseEntity<>(subscription, HttpStatus.OK);
//
//    }


}