package com.idea.recon.controllers;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;

@CrossOrigin
@RestController
@RequestMapping(value = "/bucket")
public class BucketController {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AmazonS3 s3Client;
	
	@PostMapping(value = "/picture")
	ResponseEntity<String> postPicture() {
		try {
			String bucketName = "my-java-aws-recon-bucket";
			String fileName = "TEST_FILE";
			File fileObj = new File("/Users/repoleved/Developer/GIT Clones/Recon/reconV2/src/main/java/com/idea/recon/controllers/ContractorController.java");
			s3Client.putObject(bucketName, fileName, fileObj);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}
	
	@GetMapping(value="/picture")
	ResponseEntity<String> getPicture() {
		try {
			String bucketName = "my-java-aws-recon-bucket";
			String fileName = "TEST_FILE";
			//File fileObj = new File("/Users/repoleved/Developer/GIT Clones/Recon/reconV2/src/main/java/com/idea/recon/controllers/ContractorController.java");
			//S3Object s3Obj = s3Client.getObject(bucketName, fileName+"lfkadsjfa");
			logger.info(s3Client.doesObjectExist(bucketName, fileName)+"");
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
}
