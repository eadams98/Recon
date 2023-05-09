package com.email.recon.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.joda.time.LocalDate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityResult;
import com.email.recon.DTO.ContractorAndTraineeCorrespondenceDTO;
import com.email.recon.DTO.ReportDTO;
import com.email.recon.service.GenericEmailService;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
//import software.amazon.awssdk.core.SdkBytes;
//import software.amazon.awssdk.services.ses.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class GenericEmailServiceImpl implements GenericEmailService {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AmazonSimpleEmailService awsSes;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	// Private Method Start 
	private SendEmailRequest generateSendEmailRequest(String toAddress, String fromAddress, String messageSubject, String messageBody, MimeBodyPart attachment) {
		/*Destination destination = new Destination().withToAddresses("eric051598@gmail.com");
		Content subject = new Content().withData("Test email");
		Content textBody = new Content().withData("Hello from Amazon SES!");
		Body body = new Body().withText(textBody);
		Message message = new Message().withSubject(subject).withBody(body);
		SendEmailRequest request = new SendEmailRequest().withSource("norelpy@datareconreports.com")
		    .withDestination(destination)
		    .withMessage(message);*/
		Destination destination = new Destination().withToAddresses(toAddress);
		Content subject = new Content().withData(messageSubject);
		Content textBody = new Content().withData(messageBody);
		Body body = new Body().withText(textBody);
		Message message = new Message().withSubject(subject).withBody(body);
		
		if (attachment != null) {
			//
		}
		
		SendEmailRequest request = new SendEmailRequest()
			.withSource(fromAddress)
		    .withDestination(destination)
		    .withMessage(message);
		return request;
	}
	// Private Method End

	@Override
	public String sendAssigendTraineeToContractor(ContractorAndTraineeCorrespondenceDTO emailInfo) {
		String contractorMessageSubject = "Trainee " + emailInfo.getTraineeName() + " Added";
		String contractorMessageBody = emailInfo.getTraineeName() + " has been added to your list of trainees. You can contact them at their email address " + emailInfo.getTraineeEmail();
		SendEmailRequest emailForContractor = generateSendEmailRequest(emailInfo.getContractorEmail(), "norelpy@datareconreports.com", contractorMessageSubject, contractorMessageBody, null);
		
		String traineeMessageSubject = "Assigned to Contractor: " + emailInfo.getContractorName();
		String traineeMessageBody = "You have been assigned as a trainee under contractor " + emailInfo.getContractorName() + ". You can contact them at their email address " + emailInfo.getContractorEmail();
		SendEmailRequest emailForTrainee = generateSendEmailRequest(emailInfo.getTraineeEmail(), emailInfo.getContractorEmail(), traineeMessageSubject, traineeMessageBody, null);
		
		try {
			ListVerifiedEmailAddressesResult result = awsSes.listVerifiedEmailAddresses();
		    List<String> verifiedEmailAddresses = result.getVerifiedEmailAddresses();
		    String emailAddress = "eric051598@gmail.com";
		    
		    if (!verifiedEmailAddresses.contains(emailInfo.getContractorEmail()) || !verifiedEmailAddresses.contains(emailInfo.getTraineeEmail())) {
		        System.out.println(emailAddress + " is not verified.");
		        return "One or both emails are not verified";
		    }
		    
		    SendEmailResult resultForContractor = awsSes.sendEmail(emailForContractor);
		    SendEmailResult resultForTrainee = awsSes.sendEmail(emailForTrainee);
		    //System.out.println("Email sent! Message ID: " + result.getMessageId());
		    return "Email(s) sent! Message ID for contractor: " + resultForContractor.getMessageId() + 
		    	", Message ID for Trainee: " + resultForTrainee.getMessageId();
		} catch (AmazonServiceException e) {
		    // Handle the exception
			//logger.error(e.getMessage());
			return e.getMessage();
		} catch (AmazonClientException e) {
		    // Handle the exception
			//logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String verifyEmailAddress(String email) {
		// TODO Auto-generated method stub
		try {
			ListVerifiedEmailAddressesResult result = awsSes.listVerifiedEmailAddresses();
		    List<String> verifiedEmailAddresses = result.getVerifiedEmailAddresses();
		    String emailAddress = "eric051598@gmail.com";
		    
		    if (verifiedEmailAddresses.contains(emailAddress)) {
		        System.out.println(emailAddress + " is already verified.");
		        return "Already verified";
		    }
		    
			//System.out.println("INSIDE VERIFY EMAIL");
			//VerifyEmailIdentityRequest request = new VerifyEmailIdentityRequest().withEmailAddress("eric051598@gmail.com");
			//VerifyEmailIdentityResult result = awsSes.verifyEmailIdentity(request);
			//System.out.println(result.toString());
			return "Email sent to Email address: " ;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
		//String status = result.get
	}
	

	@Override
	public String sendWeeklyReportCreated(ContractorAndTraineeCorrespondenceDTO emailInfo) {
		logger.info(emailInfo.getReportDTO().toString());
		
		// Create a new PDF document
        Document document = new Document();
		String fileName = emailInfo.getTraineeName() + "_report_week_" + emailInfo.getWeekRange()  + ".pdf";
        
		try {
			ReportDTO report = emailInfo.getReportDTO();
			FileOutputStream outputStream = new FileOutputStream(fileName);
	        PdfWriter.getInstance(document, outputStream);
	
	        // Open the document
	        document.open();
	        logger.info("1");
	
	        // Add content to the document
	        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
	        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
	
	        Paragraph title = new Paragraph("Report For The Week Of " + report.getWeekStartDate() + " to " + report.getWeekEndDate() , titleFont);
	        title.setAlignment(Paragraph.ALIGN_CENTER);
	        title.setSpacingAfter(10);
	        document.add(title);
	        logger.info("2");
	
	        Paragraph descriptionHeader = new Paragraph("Description", headerFont);
	        descriptionHeader.setSpacingBefore(10);
	        document.add(descriptionHeader);
	
	        Paragraph description = new Paragraph(report.getDescription(), textFont);
	        description.setSpacingAfter(10);
	        document.add(description);
	
	        if (report.getRebuttal() != null && !report.getRebuttal().isEmpty()) {
	            Paragraph rebuttalHeader = new Paragraph("Rebuttal", headerFont);
	            rebuttalHeader.setSpacingBefore(10);
	            document.add(rebuttalHeader);
	
	            Paragraph rebuttal = new Paragraph(report.getRebuttal(), textFont);
	            rebuttal.setSpacingAfter(10);
	            document.add(rebuttal);
	        }
	
	        Paragraph gradeHeader = new Paragraph("Grade", headerFont);
	        gradeHeader.setSpacingBefore(10);
	        document.add(gradeHeader);
	
	        Paragraph grade = new Paragraph(report.getGrade().toString(), textFont);
	        grade.setSpacingAfter(10);
	        document.add(grade);
	
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, uuuu");
	
	        Paragraph submissionDateHeader = new Paragraph("Submission Date", headerFont);
	        submissionDateHeader.setSpacingBefore(10);
	        document.add(submissionDateHeader);
	
	        //dateFormatter.for
	        //Paragraph submissionDate = new Paragraph(dateFormatter.format(report.getSubmissionDate().atStartOfDay()), textFont);
	        //submissionDate.setSpacingAfter(10);
	        //document.add(submissionDate);
	
	        Paragraph weekStartDateHeader = new Paragraph("Week Start Date", headerFont);
	        weekStartDateHeader.setSpacingBefore(10);
	        document.add(weekStartDateHeader);
	
	        logger.info("3: " + report.getWeekStartDate() + ", Formate: " + dateFormatter.format(report.getWeekStartDate().atStartOfDay()));
	        Paragraph weekStartDate = new Paragraph(dateFormatter.format(report.getWeekStartDate().atStartOfDay()), textFont);
	        weekStartDate.setSpacingAfter(10);
	        document.add(weekStartDate);
	        logger.info("4");
	
	        Paragraph weekEndDateHeader = new Paragraph("Week End Date", headerFont);
	        weekEndDateHeader.setSpacingBefore(10);
	        document.add(weekEndDateHeader);
	
	        Paragraph weekEndDate = new Paragraph(dateFormatter.format(report.getWeekEndDate().atTime(LocalTime.MAX)), textFont);
	        weekEndDate.setSpacingAfter(10);
	        document.add(weekEndDate);
	
	        // Close the document & stream
	        document.close();
	        outputStream.close();
	        
	        //CHECK
	        /*File pdfFile = new File(fileName);
	        if (pdfFile.exists()) {
	            logger.info("PDF file created successfully.");
	        } else {
	            logger.info("Error: PDF file not created.");
	        }*/
	        
		} catch (Exception ex) {
			logger.error("ERROR OCCURED: " + ex.getMessage());
			return ex.getMessage();
		}
		
		String contractorMessageSubject = emailInfo.getTraineeName() + " Weekly Report: " + emailInfo.getWeekRange();
		String contractorMessageBody = "Add a link to the report here or add a pdf of the report";
		SendEmailRequest emailForContractor = generateSendEmailRequest(emailInfo.getContractorEmail(), "norelpy@datareconreports.com", contractorMessageSubject, contractorMessageBody, null);
		
		String traineeMessageSubject = "Weekly Report: " + emailInfo.getWeekRange();
		String traineeMessageBody = "Either add a link to the report here or add a pdf of the report (maybe add a link to rebuttal)";
		SendEmailRequest emailForTrainee = generateSendEmailRequest(emailInfo.getTraineeEmail(), emailInfo.getContractorEmail(), traineeMessageSubject, traineeMessageBody, null);

		try {
			ListVerifiedEmailAddressesResult result = awsSes.listVerifiedEmailAddresses();
		    List<String> verifiedEmailAddresses = result.getVerifiedEmailAddresses();
		    String emailAddress = "eric051598@gmail.com";
		    
		    if (!verifiedEmailAddresses.contains(emailInfo.getContractorEmail()) || !verifiedEmailAddresses.contains(emailInfo.getTraineeEmail())) {
		        System.out.println(emailAddress + " is not verified.");
		        return "One or both emails are not verified";
		    }
		    
		    MimeMessage message = javaMailSender.createMimeMessage();
		    
		    MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
		    
		    File file = new File(fileName);
	        //InputStream inputStream = new FileInputStream(file);
	        //InputStreamSource inputStreamResource = new InputStreamResource(inputStream);
		    DataSource dataSource = new FileDataSource(file);
		    
		    // Add attachment
            helper.addAttachment(fileName, dataSource);
            helper.setTo(Objects.requireNonNull(emailInfo.getTraineeEmail()));
            helper.setText(Objects.requireNonNull(traineeMessageBody));
            helper.setSubject(Objects.requireNonNull(traineeMessageSubject));
            helper.setFrom(Objects.requireNonNull(emailInfo.getContractorEmail()));
            javaMailSender.send(message);
		    
		    /*
		    // create the email attachment
		    MimeBodyPart attachmentPart = new MimeBodyPart();
		    Resource resource = new ClassPathResource(fileName);
		    DataSource dataSource = new FileDataSource(resource.getFile());
		    attachmentPart.setDataHandler(new DataHandler(dataSource));
		    attachmentPart.setFileName(resource.getFilename());
		    
		    // add the attachment to the email body
		    MimeMultipart multipart = new MimeMultipart();
		    multipart.addBodyPart(attachmentPart);
		    MimeBodyPart messageBodyPart = new MimeBodyPart();
		    messageBodyPart.setContent(multipart);
		    
		    SendEmailResult resultForContractor = awsSes.sendEmail(emailForContractor);
		    SendEmailResult resultForTrainee = awsSes.sendEmail(emailForTrainee);
		    //System.out.println("Email sent! Message ID: " + result.getMessageId());
		    return "Email(s) sent! Message ID for contractor: " + resultForContractor.getMessageId() + 
		    	", Message ID for Trainee: " + resultForTrainee.getMessageId();*/
		    
			
		} catch (Exception ex) {
			return ex.getMessage();
		}
		return "ok";
		
	}

	@Override
	public String updateWeeklyReport(ContractorAndTraineeCorrespondenceDTO emailInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
