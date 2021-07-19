package com.wu.qcc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.wu.qcc.deciders.BatchFlowDecider;



@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({ "com.wu.qcc.*" })
public class MamLoaderSpringBatchApplication {

	private static final Logger log = LogManager.getLogger(MamLoaderSpringBatchApplication.class);
//	private static final String BILLER_EXTN = "GTW";
	private static ConfigurableApplicationContext ctx1;
	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) throws Exception {
		synchronized (MamLoaderSpringBatchApplication.class) {
			ctx1 = SpringApplication.run(MamLoaderSpringBatchApplication.class, args);
		}
		try {

			String biller = args[0];
			Optional<String> dataFile=Optional.empty();
			Optional<String> dataFile1 = Optional.empty();
			if(args.length>1)
			 dataFile= Optional.ofNullable(args[1]);
			if(args.length>2)
			 dataFile1= Optional.ofNullable(args[2]);
			if(dataFile.isPresent())
			{
				log.info("First File:-"+dataFile.get());
			}
			if(dataFile1.isPresent())
			{
				log.info("Second File:-"+dataFile1.get());
			}
			
			log.info("Biller Name:-"+biller);
			
			String fileName = biller ;
			

			ctx = new FileSystemXmlApplicationContext(new String[] { "classpath*:" + biller + "_MAM_JOB_CONFIG.xml" },
					ctx1);


//			String clientId = getClienId(biller);
			
		
			JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
			Job job = (Job) ctx.getBean(biller + "_MAM_JOB");
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addDate("system", new Date());
			jobBuilder.addString("clientId", biller);
			jobBuilder.addString("billerPrefix", biller);
			jobBuilder.addString("dataFile", dataFile.get());
			JobParameters jobParameters = jobBuilder.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			System.out.println("Completion Status : " + execution.getStatus());
			
//			Path fileTobeDeleted = Paths.get(configVariablesUtil.getTempLocation() + fileName);
//			Files.delete(fileTobeDeleted);
			
			ctx.close();
			ctx1.close();

		} catch (Exception e) {

			e.printStackTrace();

			throw e;

		}
		System.out.println("Done..");
	}

}
