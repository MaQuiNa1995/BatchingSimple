package es.cic.cmunoz.batching;

import org.springframework.batch.item.ItemProcessor;

import es.cic.cmunoz.domain.Mapeado;

public class MapeadoItemProcessor implements ItemProcessor<Mapeado, Mapeado> {

	@Override
	public Mapeado process(Mapeado mapeado) {

		// Lo dejamos vacio porque no queremos hacer nada con el contenido
		// for (String objetoSacado : mapeado.getMapaCadenas().values()) {
		// LOG.info(objetoSacado);
		// }

		return mapeado;
	}
}
