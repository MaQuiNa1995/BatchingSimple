package es.cic.cmunoz.batching;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MapeadoJobListener implements JobExecutionListener {

    private static final Logger LOG = Logger.getLogger(MapeadoJobListener.class.getName());

    private DateTime horaInicio;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        horaInicio = new DateTime();
        LOG.log(Level.INFO, "Campeon Job Empieza a Las: {0}", horaInicio);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        DateTime horaTerminado;

        horaTerminado = new DateTime();
        LOG.log(Level.INFO, "Campeon Job Termino a Las: {0}", horaTerminado);
        LOG.log(Level.INFO, "Tiempo Empleado: {0} Segundos", calcularTiempo(horaInicio, horaTerminado) / 1000f);

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            LOG.info("Campeon Job Completado");

        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {

            LOG.warning("Campeon Job Fallo Con Las Siguientes Excepciones: ");

            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();

            exceptionList.stream().forEach((Throwable th) -> {
                LOG.warning(th.getLocalizedMessage());
            });

//            exceptionList.stream().forEach((th) -> {
//                LOG.warning(th.getLocalizedMessage());
//            });
        }
    }

    private long calcularTiempo(DateTime inicio, DateTime fin) {

        long fechaAhora = fin.getMillis() - inicio.getMillis();

        return fechaAhora;
    }

}
