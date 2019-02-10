package es.maquina.batching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import es.maquina.domain.Mapeado;

/**
 * Clase usada para transformar cada resultado que nos llega a un mapa para
 * poder leer los resultados (esto no haria falta si supieramos exactamente como
 * nos vienen los datos pero asi podemos recuperar y mapear cualquier numero de
 * resultados y tipos) en su dia era requisito del programa
 * 
 * @author MaQuiNa1995
 *
 */
@Component
public class MapeadoRowMapper implements RowMapper<Mapeado> {

	/**
	 * Log generico de la clase
	 */
	private static final Logger LOG = Logger.getLogger(MapeadoRowMapper.class.getName());

	/*
	 * Recuperamos el valor que le hemos dado en el properties
	 */
	@Value("${sql.select}")
	private String[] tablas;

	// Inyectamos la lista creada por el contexto de spring
	// usamos el @Resource porque el autowired no lo inyecta bien para entender este
	// comportamiento mira este enlace
	// https://readlearncode.com/spring-4/insights-from-stackoverflow-most-voted-for-spring-4-questions/#3
	@Resource(name = "valoresRecuperados")
	private List<String> valoresRecuperados;

	/**
	 * Metodo ejecutado por cada registro
	 */
	@Override
	public Mapeado mapRow(ResultSet rs, int rowNum) {

		// Creamos el mapa para guardar los resultados
		Map<String, String> mapaCampos = new HashMap<>();

		// Recorremos todos los resultados de la query
		for (int i = 0; i < tablas.length; i++) {

			// inicializamos la variable para guardar los valores que recojamos
			String resultadoQuery = "";
			try {
				// Guardamos su valor
				resultadoQuery = rs.getString(tablas[i]);
			} catch (SQLException exception) {
				// Dejamos constancia en el log de la excepcion que ha pasado
				LOG.log(Level.WARNING, "Se ha producido la siguiente excepcion:" + exception.getMessage());
			}

			// verificamos que no este vacio por si dio previamente un error
			if (!resultadoQuery.isEmpty()) {
				// guardamos el valor en el mapa
				mapaCampos.put(tablas[i], resultadoQuery);
				// Metemos los resultados a la lista inyectada por spring (no sive realmente
				// para nada en este proyecto pero si que vale para enseñar a grosso como
				// inyectar y compartir un bean de una lista en 2 clases o mas )

				// Mas tarde veremos todos los valores de la lista y comprobaremos que hemos
				// usado el bean en 2 clases distintas y hemos podido escribir y leer los
				// valores que hemos metido en ambas clases juntos
				valoresRecuperados.add(resultadoQuery);
			}
		}
		// Inicializamos y seteamos el mapa a muestra entidad pra ser leida mas tarde
		// por el writter
		Mapeado mapeadoObjeto = new Mapeado();
		mapeadoObjeto.setMapaCadenas(mapaCampos);

		// Retornamos la entidad
		return mapeadoObjeto;
	}

}
