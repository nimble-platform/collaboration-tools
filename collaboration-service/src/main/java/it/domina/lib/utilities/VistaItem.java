package it.domina.lib.utilities;

import it.domina.lib.store.Identificabile;

public interface VistaItem extends Identificabile {

	public int size();
	public String getItem(String key);
	
}
