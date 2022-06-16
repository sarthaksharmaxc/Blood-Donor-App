package com.springsecurity.demo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.springsecurity.demo.model.ConfirmationToken;
import com.springsecurity.demo.model.User;
import com.springsecurity.demo.service.repository.ReqRepository;
@Service
public class EmailSenderService {

	private ReqRepository reqRepo;
	@Value("${app.sendgrid.templateId}")
	private String templateId;
	@Autowired
	SendGrid sendGrid;
	public String sendEmail(String email,String rmail)
	{
		
		Email from = new Email("ahirejyoti13@gmail.com");
        String subject = "New blood Request";
        Email to = new Email(email);
        Content content = new Content("text/plain", "You have New blood request!! please reply to on "+"'"+rmail+"'"+" Please Contact As soon as possible");
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = this.sendGrid.api(request);
            sendGrid.api(request);
           	
 		   
 		   if(response!=null)
 		   {
 			   System.out.println("Response code from sendgrid"+response.getBody());
 		   }

            // ...
        } catch (IOException ex) {
        	ex.printStackTrace();
			return "error in sending";
        }
    	
		
	
		return "mail has been sent pls check";
	}
	
	public Mail prepareMail(String email) {
		
		Mail mail=new Mail();
		
		Email fromEmail=new Email();
		fromEmail.setEmail("ahirejyoti13@gmail.com");
		
		Email to=new Email();
		to.setEmail(email);
		Personalization personalization=new Personalization();
		personalization.addTo(to);
		mail.setTemplateId(templateId);
	    return mail;			
	}
}

