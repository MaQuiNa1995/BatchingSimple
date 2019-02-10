package es.maquina.domain;

import java.util.HashMap;
import java.util.Map;

public class Mapeado {

	private Map<String, String> mapaCadenas = new HashMap<>();

	public Map<String, String> getMapaCadenas() {
		return mapaCadenas;
	}

	public void setMapaCadenas(Map<String, String> mapaCadenas) {
		this.mapaCadenas = mapaCadenas;
	}

}
