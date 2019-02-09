package es.cic.cmunoz;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) {

	JobLauncher jobLauncher;
	Job job;

	try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ConfiguracionSpring.xml")) {

	    jobLauncher = (JobLauncher) context.getBean("jobLauncher");
	    job = (Job) context.getBean("campeonResultJob");

	    JobExecution execution = jobLauncher.run(job, new JobParameters());
	    LOG.log(Level.INFO, "Job Estado De Finalizaci\u00f3n : {0}", execution.getStatus());

	} catch (JobExecutionException exception) {
	    LOG.log(Level.WARNING, "Job CampeonResult Fall\u00f3 , Raz\u00f3n:{0}", exception.getMessage());
	}
    }
}
