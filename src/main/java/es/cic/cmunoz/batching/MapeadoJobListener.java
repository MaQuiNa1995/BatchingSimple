package es.cic.cmunoz.batching;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MapeadoJobListener implements JobExecutionListener {

    private static final Logger LOG = Logger.getLogger(MapeadoJobListener.class.getName());

    private Date horaInicio;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        horaInicio = new Date();
        LOG.log(Level.INFO, "Campeon Job Empieza a Las: {0}", horaInicio);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        Date horaTerminado = new Date();
        
        LOG.log(Level.INFO, "Campeon Job Termino a Las: {0}", horaTerminado);
        LOG.log(Level.INFO, "Tiempo Empleado: {0} Segundos", calcularTiempo(horaInicio, horaTerminado) / 1000f);

        BatchStatus codigoEjecucion = jobExecution.getStatus();
        
        if (codigoEjecucion == BatchStatus.COMPLETED) {

            LOG.info("Campeon Job Completado");

        } else if (codigoEjecucion == BatchStatus.FAILED) {

            LOG.warning("Campeon Job Fallo Con Las Siguientes Excepciones: ");

            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();

            exceptionList.stream().forEach((Throwable th) -> {
                LOG.warning(th.getLocalizedMessage());
            });
        }
    }

    private long calcularTiempo(Date inicio, Date fin) {

        long milisTranscurridos = fin.getTime() - inicio.getTime();

        return milisTranscurridos;
    }

}
