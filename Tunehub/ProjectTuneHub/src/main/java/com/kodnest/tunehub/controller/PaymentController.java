package com.kodnest.tunehub.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.serviceimpl.UserServiceImpl;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserServiceImpl userService;

	@GetMapping("/pay")
	public String pay() {
		return "pay";
	}

	@SuppressWarnings("finally")
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {

		int  amount  = 5;
		Order order=null;
		try {
			RazorpayClient razorpay=new RazorpayClient("rzp_test_5bEqt0kMJenGw3", "YOXa6RIbiNANBaQ4GjmNq5jl");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = razorpay.orders.create(orderRequest);

			//			String mail =  (String) session.getAttribute("email");
			//			User user = userService.getUser(mail);
			//			user.setIspreimium(true);
			//			userService.updateUser(user);

		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		finally {
			return order.toString();
		}
	}

	//	 @PostMapping("/verify")
	//	    @ResponseBody
	//	    public ResponseEntity<String> verifyPayment(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String signature) {
	//	        // Your payment verification logic here...
	//	        boolean isValidPayment = true; // Example: Assuming payment is valid
	//
	//	        if (isValidPayment) {
	//	            return ResponseEntity.ok().body("{\"message\": \"Payment successful\"}");
	//	        } else {
	//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Payment failed\"}");
	//	        }
	//	    }

	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId,
			@RequestParam String signature) {
		try {
			// Initialize Razorpay client with your API key and secret
			@SuppressWarnings("unused")
			RazorpayClient razorpayClient = new RazorpayClient("rzp_test_5bEqt0kMJenGw3", 
					"YOXa6RIbiNANBaQ4GjmNq5jl");
			// Create a signature verification data string
			String verificationData = orderId + "|" + paymentId;

			// Use Razorpay's utility function to verify the signature
			boolean isValidSignature = Utils.verifySignature(verificationData, signature, 
					"YOXa6RIbiNANBaQ4GjmNq5jl");

			return isValidSignature;
		} catch (RazorpayException e) {
			e.printStackTrace();
			return false;
		}
	}

	@GetMapping("/payment-success")
	public String paymentSuccess(HttpSession session) {

		String mail =  (String) session.getAttribute("email");
		User user = userService.getUser(mail);
		user.setIspremium(true);
		userService.updateUser(user);
		return "customerhome";
	}

	@GetMapping("/payment-failure")
	public String paymentFailure() {
		return "customerhome";
	}
}

