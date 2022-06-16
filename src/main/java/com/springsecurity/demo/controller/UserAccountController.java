package com.springsecurity.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springsecurity.demo.model.ConfirmationToken;
import com.springsecurity.demo.model.Donor;
import com.springsecurity.demo.model.Requester;
import com.springsecurity.demo.model.User;
import com.springsecurity.demo.service.DonorService;
import com.springsecurity.demo.service.EmailSenderService;
import com.springsecurity.demo.service.repository.ConfirmationTokenRepository;
import com.springsecurity.demo.service.repository.DonorRepository;
import com.springsecurity.demo.service.repository.ReqRepository;
import com.springsecurity.demo.service.repository.UserRepository;

@Controller
public class UserAccountController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReqRepository reqRepo;

	@Autowired
	private DonorRepository donorRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	private static DonorService donorService;

	// https://stackabuse.com/password-encoding-with-spring-security/
	// to encode our password
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public String sendEmail(@PathVariable(value="email",required=true)String email)
	{
		return sendEmail(email);
	}
	
	// Registration
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView dispyRegistration(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;
	}
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public ModelAndView displayRegistration(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register");
		return modelAndView;
	}
	
	@RequestMapping(value="/contactPlasma", method=RequestMethod.GET)
	public ModelAndView displayContact(ModelAndView modelAndView, Requester reqstr) {
		modelAndView.addObject("reqstr", reqstr);
		modelAndView.setViewName("contactPlasma");
		return modelAndView;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView modelAndView, User user) {
		
		User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
		if(existingUser != null) {
			modelAndView.addObject("message","This email already exists!");
			modelAndView.setViewName("error");
		} else {
			user.setPassword(encoder.encode(user.getPassword()));
			userRepository.save(user);
			//sendEmail(user.getEmailId());
			modelAndView.addObject("emailId", user.getEmailId());
			modelAndView.setViewName("successfulRegisteration");
		}
		
		return modelAndView;
	}
	@RequestMapping(value="/contactPlasma", method=RequestMethod.POST)
	public String requestSend(ModelAndView modelAndView, Requester reqstr) {
		
		
			reqRepo.save(reqstr);
			emailSenderService.sendEmail(reqstr.getDemailId(),reqstr.getRemailId());
			modelAndView.addObject("message","Request has been sent!!");
			
		
		return "redirect:/view-donor";
	}
	/*
	// Confirm registration
	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(token != null)
		{
			User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			user.setEnabled(true);
			userRepository.save(user);
			modelAndView.setViewName("accountVerified");
		}
		else
		{
			modelAndView.addObject("message","The link is invalid or broken!");
			modelAndView.setViewName("error");
		}
		
		return modelAndView;
	}	*/

	// Login
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView displayLogin(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}
	

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView loginUser(ModelAndView modelAndView, User user) {
		
		User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
		if(existingUser != null) {
			// use encoder.matches to compare raw password with encrypted password

			if (encoder.matches(user.getPassword(), existingUser.getPassword())){
				// successfully logged in
				modelAndView.addObject("message", "You Have Successfully Logged into Blood Donor Application!");
				modelAndView.setViewName("successLogin");
			} else {
				// wrong password
				modelAndView.addObject("message", "Incorrect password. Try again.");
				modelAndView.setViewName("login");
			}
		} else {	
			modelAndView.addObject("message", "The email provided does not exist!");
			modelAndView.setViewName("login");

		}
		
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public ModelAndView displayLogout(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	//Donor handler
	@RequestMapping(value="/add-donor", method=RequestMethod.GET)
	public ModelAndView adddDonor(ModelAndView modelAndView, Donor donor) {
		modelAndView.addObject("donor", donor);
		modelAndView.setViewName("addDonor");
		return modelAndView;
	}
	
	@RequestMapping(value="/add-donor", method=RequestMethod.POST)
	public ModelAndView registerDonor(ModelAndView modelAndView, Donor donor) {
		
		Donor existingdonor = donorRepository.findByEmailIgnoreCase(donor.getEmail());
		if(existingdonor != null) {
			modelAndView.addObject("message","Donor with this email already exists!");
			modelAndView.setViewName("error");
		} else {
			//user.setPassword(encoder.encode(user.getPassword()));
			donorRepository.save(donor);
			
			modelAndView.addObject("message","Donor Added Suceesfully");
			
			modelAndView.setViewName("successLogin");
		}
		
		return modelAndView;
	}
	
	@GetMapping("/view-donor")
	public ModelAndView viewdDonor(ModelAndView modelAndView, Donor donor) {
		//donorRepository.findAll();
		modelAndView.addObject("donors", donorRepository.findAll());
		modelAndView.setViewName("viewDonor");
		return modelAndView;
	}
	
	/**
	 * Display the forgot password page and form
	 */
	@RequestMapping(value="/forgot-password", method=RequestMethod.GET)
	public ModelAndView displayResetPassword(ModelAndView modelAndView, User user) {
		modelAndView.addObject("user", user);
		modelAndView.setViewName("forgotPassword");
		return modelAndView;
	}

	/**
	 * Receive email of the user, create token and send it via email to the user
	 */
	@RequestMapping(value="/forgot-password", method=RequestMethod.POST)
	public ModelAndView forgotUserPassword(ModelAndView modelAndView, User user) {
		User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
		if(existingUser != null) {
			// create token
			ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
			
			// save it
			confirmationTokenRepository.save(confirmationToken);
			
			// create the email
			sendEmail(user.getEmailId());

			modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
			modelAndView.setViewName("successForgotPassword");

		} else {	
			modelAndView.addObject("message", "This email does not exist!");
			modelAndView.setViewName("error");
		}
		
		return modelAndView;
	}


	@RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
	{
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(token != null) {
			User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			//user.setEnabled(true);
			userRepository.save(user);
			modelAndView.addObject("user", user);
			modelAndView.addObject("emailId", user.getEmailId());
			modelAndView.setViewName("resetPassword");
		} else {
			modelAndView.addObject("message", "The link is invalid or broken!");
			modelAndView.setViewName("error");
		}
		
		return modelAndView;
	}	

	/**
	 * Receive the token from the link sent via email and display form to reset password
	 */
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ModelAndView resetUserPassword(ModelAndView modelAndView, User user) {
		// ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(user.getEmailId() != null) {
			// use email to find user
			User tokenUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
			//tokenUser.setEnabled(true);
			tokenUser.setPassword(encoder.encode(user.getPassword()));
			System.out.println(tokenUser.getPassword());
			userRepository.save(tokenUser);
			modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
			modelAndView.setViewName("successResetPassword");
		} else {
			modelAndView.addObject("message","The link is invalid or broken!");
			modelAndView.setViewName("error");
		}
		
		return modelAndView;
	}


	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ConfirmationTokenRepository getConfirmationTokenRepository() {
		return confirmationTokenRepository;
	}

	public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
		this.confirmationTokenRepository = confirmationTokenRepository;
	}

	public EmailSenderService getEmailSenderService() {
		return emailSenderService;
	}

	public void setEmailSenderService(EmailSenderService emailSenderService) {
		this.emailSenderService = emailSenderService;
	}
	
}
