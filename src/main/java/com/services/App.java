package com.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

/**
 * The program is supposed to read the html template and its corresponding Js file
 * from Amazon S3 bucket and render it with data at runtime. It uses the handlebars mustache 
 * java integration to render the template in the server side.
 * 
 * Kindly refer to the ReadMe doc for further details.
 *
 */
public class App {
	/**
	 * Intialize the AWS credentials with the access-key and the secret-key of Aws account
	 */
	private static  AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials("AWS_ACCESS_KEY",
		      "AWS_SECRET_KEY");
	
	/**
	 * The method builds the Amazon S3 client using the initialized Aws_Credentials
	 * and the Amazon S3 endpoint.
	 * The Amazon S3 client is used for all the interaction with Amazon S3 storage system
	 * 
	 * @return AmazonS3 client
	 */
	private static AmazonS3 getS3Client() {
	    AmazonS3 s3Client = new AmazonS3Client(AWS_CREDENTIALS);
	    s3Client.setEndpoint("AMAZON_S3_ENDPOINT");
	    return s3Client;
	  }
	
	
	/**
	 * The entire code is written inside the main method for simplification.
	 * 
	 * It renders the template content, populates with the runtime data using Handle Bars Mustache
	 * java integration
	 * 
	 * @param args
	 */
    public static void main( String[] args )
    {
    	/**
    	 * the html file name which is supposed to be read from amazon s3
    	 */
    	String htmlFileName = "HtmlFile.html";
    	
    	/**
    	 * The js file which is supposed to be read from amazon s3
    	 */
		String jsFileName = "JsFile.js";
		
		/**
		 * initializing a temp file which will be used to write the js file
		 * content after reading from amazon s3 bucket
		 */
		File tempJsFile = null;
		
		/**
		 * initializing and populating the map of data which will be used in
		 * the html template
		 */
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", "Spartan");
		dataMap.put("age", 25);
		dataMap.put("gender", "M");
		
		
		try{
			/**
			 * Initializing the GetObjectRequest of Amazon S3. It is used to read files stored
			 * in amazon s3 bucket. It is initialized with the Aws_Bucket_Name, in which the file
			 * is stored, and the file name which we want to read
			 */
	    	GetObjectRequest htmlReq = new GetObjectRequest("AWS_BUCKET_NAME", htmlFileName);
	    	GetObjectRequest jsReq = new GetObjectRequest("AWS_BUCKET_NAME", jsFileName);
	    	
	    	/**
	    	 * Use the Amazon S3 client and the GetObjectRequest to fetch the files and
	    	 * hold it in the S3Object container.
	    	 */
	    	S3Object htmlObj = getS3Client().getObject(htmlReq);
	    	S3Object jsObj = getS3Client().getObject(jsReq);
	    	
	    	/**
	    	 * creating a temp file in memory for writing the js file content
	    	 * which will be later used to render the html template.
	    	 * 
	    	 * The Amazon S3Object does not directly converts to a File, nor
	    	 * does it has any built-in function to do so. Hence we need to use
	    	 * the IOUtils of common.io for writing the input Stream to a file.
	    	 * We can do the same using the conventional manual style but IOUtils 
	    	 * provide the built-in function for it, thus lessening our work.
	    	 */
	    	tempJsFile = File.createTempFile("temp", ".js");
	    	FileOutputStream out = new FileOutputStream(tempJsFile);
	        IOUtils.copy(jsObj.getObjectContent(), out);
	        out.close();
	        
	        /**
	         * HandleBars is the java version of handlebars.js
	         * It helps us to render html templates with dynamic data
	         * and some logic. Handlebars is an extension of Mustache.js
	         * which are basically logicless templates. Although, handlebars 
	         * extends all the properties of Mustache it also heps us
	         * with some added functionalities. We can add some custom logic
	         * in the form of java scripts which will be used in rendering 
	         * the html templates. We just need to register the script file
	         * with our HandleBar instance and it will do the magic. I say it as magic
	         * because all this is done from the server side, without any interaction
	         * with browser etc.
	         */
	    	Handlebars handleBar = new Handlebars();
			handleBar.registerHelpers(tempJsFile);
			
			/**
			 * Once we register the helper with our HandleBar instance, we can call the
			 * handleBar.compileInLine() function and pass the html content as parameter.
			 * 
			 * We can even send an html file directly in the parameter for rendering but for
			 * that we will have to use the handleBar.compile() function.
			 */
			Template template = handleBar.compileInline(IOUtils.toString(htmlObj.getObjectContent()));
			
			/**
			 * Once we have got the template, we use the template.apply() function for populating 
			 * the template with our values and return the html string.
			 */
			String mail = template.apply(dataMap);
			
			/**
			 * The html string is printed in the console here. But we can take this
			 * and use it elsewhere, as triggering an email with html content in the 
			 * body etc.
			 */
			System.out.println("The Mail Template with data rendered is:"+mail);
			
		}catch(IOException ioEx){
			System.out.println("IO exception caught"+ioEx.getMessage());
		} catch (Exception ex) {
			System.out.println("Exception while registering handlebar helper"+ ex.getMessage());
		}
		
    }
}
