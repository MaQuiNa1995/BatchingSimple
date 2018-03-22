package es.cic.cmunoz.batching;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import es.cic.cmunoz.domain.Mapeado;

@Component
public class MapeadoRowMapper implements RowMapper<Mapeado> {

    @Value("${sql.select}")
    String[] tablas;

    @Override
    public Mapeado mapRow(ResultSet rs, int rowNum) throws SQLException {

        String[] camposRecuperar = tablas;

        Map<String, String> mapaCampos = new HashMap<>();

        for (int i = 0; i < camposRecuperar.length; i++) {
            mapaCampos.put(camposRecuperar[i], rs.getString(camposRecuperar[i]));
        }

        Mapeado mapeadoObjeto = new Mapeado();

        mapeadoObjeto.setMapaCadenas(mapaCampos);

        return mapeadoObjeto;
    }

}
