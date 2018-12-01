package com.sjsu.cmpe275.netflix.controller;

import java.sql.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.cmpe275.netflix.repository.MoviesRepository;
import com.sjsu.cmpe275.netflix.repository.UserActivityRepository;
import com.sjsu.cmpe275.netflix.repository.SubscriptionRepository;
import com.sjsu.cmpe275.netflix.repository.PayPerViewRepository;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
@RequestMapping(value = "/movies/play")
public class PlayController {
	
	@Autowired
	MoviesRepository moviesRepository;
	
	@Autowired
	UserActivityRepository userActivityRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	PayPerViewRepository payPerViewRepository;

	@RequestMapping(value = "/{email}/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> playMovie(@PathVariable("email") String email, @PathVariable("title") String title) {
		HttpStatus status = HttpStatus.OK;
		Map<String, String> responseMap = new HashMap<>();
//		String email = "ab@gmail.com";
		String availability = moviesRepository.getMovieAvailabilityByTitle(title);
		if(availability.equals("Free")) {
			
			// update user_activity
			userActivityRepository.insertUserActivity(email, title, Date.valueOf(java.time.LocalDate.now()), availability);
			responseMap.put("movieType", "Free");
			responseMap.put("message", "Awesome! This is a free movie.");
			
		} else if(availability.equals("SubscriptionOnly")) {
			
			// check if user is subscribed or not
			// if yes, update user_activity; if no, return 404 with message "please subscribe to view"
			responseMap.put("movieType", "Subscription Only");
			Date subscriptionEndDate = subscriptionRepository.getSubscriptionEndDate(email);
			if(subscriptionEndDate != null && subscriptionEndDate.after(Date.valueOf(java.time.LocalDate.now()))) {
				userActivityRepository.insertUserActivity(email, title, Date.valueOf(java.time.LocalDate.now()), availability);
				responseMap.put("message", "Awesome! You have an active subscription.");
			} else {
				responseMap.put("message", "Access Denied! You need to subscribe for playing this movie.");
				status = HttpStatus.NOT_FOUND;
			}
			
		} else if(availability.equals("PayPerView")) {
			
			// check if user has paid for payPerView or not
			// if yes, update user_activity and payPerView Table; if no, return 404 with message "please pay to view"
			responseMap.put("movieType", "Pay Per View");
			String payPerViewStatus = payPerViewRepository.getPayPerViewStatus(email);
			if(payPerViewStatus != null && payPerViewStatus.equals("subscribed")) {
				userActivityRepository.insertUserActivity(email, title, Date.valueOf(java.time.LocalDate.now()), availability);
				payPerViewRepository.updatePayPerViewStatus(email, "unsubscribed");
				responseMap.put("message", "Awesome! You have already paid for this movie as Pay Per View.");
			} else {
				responseMap.put("message", "Access Denied! You need to pay for playing movie as Pay Per View.");
				status = HttpStatus.NOT_FOUND;
			}
			
		} else if(availability.equals("Paid")) {
			
			// check if user is subscribed or not
			// if yes, update user_activity
			responseMap.put("movieType", "Paid");
			Date subscriptionEndDate = subscriptionRepository.getSubscriptionEndDate(email);
			if(subscriptionEndDate != null && subscriptionEndDate.after(Date.valueOf(java.time.LocalDate.now()))) {
				userActivityRepository.insertUserActivity(email, title, Date.valueOf(java.time.LocalDate.now()), availability);
				responseMap.put("message", "Awesome! You have an active subscription.");
			} else {				// if no, check if user has paid for payPerView or not
				
				// if yes, update user_activity and payPerView Table; if no, return 404 with message "please pay to view"
				
				String payPerViewStatus = payPerViewRepository.getPayPerViewStatus(email);
				if(payPerViewStatus != null && payPerViewStatus.equals("subscribed")) {
					userActivityRepository.insertUserActivity(email, title, Date.valueOf(java.time.LocalDate.now()), availability);
					payPerViewRepository.updatePayPerViewStatus(email, "unsubscribed");
					responseMap.put("message", "Awesome! You have already paid for this movie as pPay Per View.");
				} else {
					responseMap.put("message", "Access Denied! You need to subscribe or pay for playing movie as Pay Per View.");
					status = HttpStatus.NOT_FOUND;
				}
				
			}
			
		}
		
		return new ResponseEntity(responseMap, null, status);
	}


//	FOR TOTAL_UNIQUE_ACTIVE_USER
	@RequestMapping(value = "/totalUniqueActiveUser/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> forTotalUniqueActiveUser(@PathVariable("date") Date date) {
		HttpStatus status = HttpStatus.OK;
		List uniqueActiveUserList = userActivityRepository.getTotalUniqueActiveUser(date);
		return new ResponseEntity(uniqueActiveUserList, null, status);
	}

}
