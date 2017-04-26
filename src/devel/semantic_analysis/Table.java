package devel.semantic_analysis;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The class implement a environment whose bindings are <identifier (symbol), T>.
 * @author daniel
 *
 * @param <T> Binding's second attribute.
 */

public class Table<T> {
	private Map<Symbol, T> bindings = new LinkedHashMap<>();
	
	public void put(Symbol key, T value) throws SemanticErrorException {
		if (bindings.containsKey(key))
			throw new SemanticErrorException(key.toString()+" already defined!");
		
		bindings.put(key, value);
	}
	
	public T get(Symbol key) {
		return bindings.get(key);
	}
	
	public Enumeration<Symbol> keys() {
		return Collections.enumeration(bindings.keySet());
	}
}