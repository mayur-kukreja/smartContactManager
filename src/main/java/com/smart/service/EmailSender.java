package com.smart.service;

import org.springframework.stereotype.Service;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
@Service
public class EmailSender {

    public  boolean sendEmail(String to, String subject, String body) {

        // Set the properties for the email server
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com"); // Replace with your SMTP server
        properties.put("mail.smtp.port", "465"); // Replace with the appropriate port
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mayurkukreja26@gmail.com", "mayur2612");
                // Replace with your email and password
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender address
            message.setFrom(new InternetAddress("mayurkukreja26@gmail.com")); // Replace with your email

            // Set the recipient address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set the email subject
            message.setSubject(subject);

            // Set the email body
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }

    
}

