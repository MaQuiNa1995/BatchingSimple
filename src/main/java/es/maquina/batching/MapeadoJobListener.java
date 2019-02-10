package es.maquina.batching;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Clase usada para monitorizar la previa y posterior ejecucion del job a traves
 * de los metodos de la super clase
 * 
 * @link org.springframework.batch.core.JobExecutionListener
 * 
 * @author MaQuiNa1995
 *
 */
public class MapeadoJobListener implements JobExecutionListener {

	/**
	 * Log generico de la clase
	 */
	private static final Logger LOG = Logger.getLogger(MapeadoJobListener.class.getName());

	// Inyectamos la lista creada por el contexto de spring
	// usamos el @Resource porque el autowired no lo inyecta bien para entender este
	// comportamiento mira este enlace
	// https://readlearncode.com/spring-4/insights-from-stackoverflow-most-voted-for-spring-4-questions/#3
	@Resource(name = "valoresRecuperados")
	private List<String> valoresRecuperados;

	/*
	 * Atributo usado para grabar la hora a la que se va a empezar el job
	 */
	private Date horaInicio;

	/**
	 * Metodo ejecutado antes de la ejecucion del job
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {

		// Como se puede ver esta es la primera clase que usa este bean no se le hace el
		// "New" para que siga gestionado por spring es un error muy comun hacer el new
		// de un bean inyectado por spring si haces esto perdera la sincronizacion y los
		// cambios que hagas en esta clse por ejemplo no se veran reflejados cuando lo
		// inyectes en otra

		// Metemos 2 valores de prueba para ver que realmente en todas las clases donde
		// lo use esta sincronizado
		valoresRecuperados.add("prueba");
		valoresRecuperados.add("prueba2");

		// hacemos un impresion en pantalla para ver que tiene estas 2 string que hemos
		// metido en las lineas anteriores
		valoresRecuperados.forEach(System.out::println);

		// Dejamos constancia en el log de la hora a la que empieza el job e
		// inicializamos la variable asi podemos recuperarla mas adelante
		horaInicio = new Date();
		LOG.log(Level.INFO, "Campeon Job Empieza a Las: {0}", horaInicio);
	}

	/**
	 * Metodo ejecutado despues de la finalizacion del job
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {

		// hacemos un volcado en pantalla de los resultados de la lista para ver que
		// tenemos los valores de las 2 clases donde hemos metido datos al array
		valoresRecuperados.forEach(System.out::println);

		// Creamos el objeto new date para recoger la hora actual
		Date horaTerminado = new Date();

		// Dejamos traza para saber a que hora se acabo el job
		LOG.log(Level.INFO, "El Job Termino a Las: {0}", horaTerminado);
		LOG.log(Level.INFO, "Tiempo Transcurrido: {0} Segundo/s", calcularTiempo(horaTerminado));

		// recuperamos el estado del job
		BatchStatus codigoEjecucion = jobExecution.getStatus();

		// Si esta completado dejamos traza de que ha acabado
		if (codigoEjecucion.equals(BatchStatus.COMPLETED)) {

			LOG.info("Job Completado");

			// si fallo dejamos traza de las excepciones que han ocurrido
		} else if (codigoEjecucion.equals(BatchStatus.FAILED)) {

			LOG.warning("El Job Fallo Con Las Siguientes Excepciones: ");

			List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();

			exceptionList.stream().forEach((Throwable th) -> {
				LOG.warning(th.getMessage());
			});
		}
	}

	/**
	 * Metodo usado para calcular los milisegundos transcurridos entre el inicio y
	 * final de la ejecucion
	 * 
	 * @param fin fecha de fin del job
	 * @return tiempo en milisegundos
	 */
	private double calcularTiempo(Date fin) {

		return (fin.getTime() - horaInicio.getTime()) / 1000D;
	}

}
