package es.maquina;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * Clase Main que ejecuta el programa
 */
public class Main {

	/**
	 * Log de la clase
	 */
	private static final Logger LOG = Logger.getLogger(Main.class.getName());

	/*
	 * Metodo principal del proyecto simplemente ejecuta un job
	 */
	public static void main(String args[]) {

		// Creamos los parametros del job
		Map<String, JobParameter> mapaParametros = new HashMap<>();
		mapaParametros.put("Hora Actual", new JobParameter(new Date()));
		JobParameters parametrosJob = new JobParameters(mapaParametros);

		// Usamos el try con recursos para que java cierre el context cuando termine (En
		// el log sale reflejado)
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ConfiguracionSpring.xml")) {

			// Recuperamos el lanzador de jobs del contexto
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

			// Recuperamos el job que tenemos configurado en el contexto
			Job job = (Job) context.getBean("campeonResultJob");

			// Lanzamos el job con el lanzador
			JobExecution execution = jobLauncher.run(job, parametrosJob);

			// Dejamos traza para saber que el job ha terminado sin errores
			LOG.log(Level.INFO, "Job Estado De Finalizaci\u00f3n : {0}", execution.getStatus());

		} catch (JobExecutionException exception) {
			// en el caso de que el job falle dejamos traza del error
			LOG.log(Level.WARNING, "Job CampeonResult Fall\u00f3 , Raz\u00f3n:{0}", exception.getMessage());
		}
	}
}
