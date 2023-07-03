package com.idea.recon.controllers;

import java.io.File;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.idea.recon.config.JwtTokenUtil;

@CrossOrigin
@RestController
@RequestMapping(value = "/bucket")
public class BucketController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AmazonS3 s3Client;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PostMapping(value = "/picture")
	ResponseEntity<String> postPicture(@RequestParam("profileImage") MultipartFile file, @RequestHeader (name="Authorization") String token) {
		token = token.split(" ")[1];
		
		File tempFile;
		try {
			logger.info("START S3 STORE");
			String bucketName = "my-java-aws-recon-bucket";
			String fileName = jwtTokenUtil.getUsernameFromToken(token).replace(".", "");
			/*if (isEmail(fileName))
				fileName = removeFileExtension(fileName);*/
			tempFile = File.createTempFile("temp", null);
			file.transferTo(tempFile);
			//File fileObj = (File) file.getResource();
			//File fileObj = new File("/Users/repoleved/Desktop/IMG_0030.jpg");
			s3Client.putObject(bucketName, fileName, tempFile);
			return new ResponseEntity<>(s3Client.getUrl(bucketName, fileName).toString(), HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
		}
		
		//logger.info("SUCCESS");
		//return new ResponseEntity<>("failed", HttpStatus.CREATED);
	}
	
	@GetMapping(value="/picture")
	ResponseEntity<String> getPicture(@RequestHeader (name="Authorization") String token) {	
		try {
			token = token.split(" ")[1];
			String bucketName = "my-java-aws-recon-bucket";
			String fileName = jwtTokenUtil.getUsernameFromToken(token).replace(".", "");
			/*if (isEmail(fileName))
				fileName = removeFileExtension(fileName);*/
			//File fileObj = new File("/Users/repoleved/Developer/GIT Clones/Recon/reconV2/src/main/java/com/idea/recon/controllers/ContractorController.java");
			//S3Object s3Obj = s3Client.getObject(bucketName, fileName+"lfkadsjfa");
			boolean doesPhotoExist = s3Client.doesObjectExist(bucketName, fileName);
			logger.info(doesPhotoExist+"");
			if (doesPhotoExist) {
				S3Object s = s3Client.getObject(bucketName, fileName);
				URL url = s3Client.getUrl(bucketName, fileName);
				return new ResponseEntity<>(url.toString(), HttpStatus.CREATED);
				 
			}
				
			//s3Obj.getKey()
		} catch (AmazonS3Exception ex) {
			logger.info(ex.getMessage());
			logger.info(ex.getClass().toString());
			logger.info(ex.getErrorCode());
			logger.info(ex.getStatusCode()+"");
			logger.info(ex.toString());
			
			return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			logger.info(ex.getClass().toString());
			return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
	
	private boolean isEmail(String username) {
	    // Add your email validation logic here
	    // For simplicity, let's assume that any string containing '@' is considered an email
	    return username.contains("@");
	}

	private String removeFileExtension(String fileName) {
	    int dotIndex = fileName.lastIndexOf(".");
	    if (dotIndex > 0) {
	        return fileName.substring(0, dotIndex);
	    }
	    return fileName;
	}
}
