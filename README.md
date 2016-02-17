# handlebars-amazonS3


Handlebars.js is an extension to the Mustache templating language created by Chris Wanstrath. Handlebars.js and Mustache are both logicless templating languages that keep the view and the code separated like we all know they should be.

Checkout the official Handlebars docs site at http://www.handlebarsjs.com and the live demo at http://tryhandlebarsjs.com/.

In this small project example I have tried to show how we can integrate amazon s3 and handlebars to render any template in the server side.

In any modern interactive application we do keep some email templates which is to be sent to users/customers at different intervals. Say suppose, a user creates an account and we need to send him/her a welcome email. Now, the content of this email remains same except that the user details, such as name, email id, phone number or login details varies. In such scenarios using handlebars and mustache we can build any such template and use it.

In this example, I am fetching the templates from amazon s3 bucket since storing such templates in amazon s3 bucket is the standard approach followed by most of the companies. The handlebars.js has its java implementation which helps us in registering the helpers as well as rendering the templates from a java api.

You need to have the following in your pom.xml ;

The aws java sdk for integrating with Amazon S3 Bucket

<dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk</artifactId>       
        <version>1.9.3</version>
 </dependency>
 
The handlebars java dependency
 
 <dependency>
        <groupId>com.github.jknack</groupId>
        <artifactId>handlebars</artifactId>
        <version>4.0.4</version>
 </dependency>
 
 
The commons.io for basic IO operations
 
 <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.4</version>
 </dependency>
 
 