package devel.semantic_analysis;

import java.util.HashMap;
import java.util.Map;

public class Symbol {
	private static Map<String,Symbol> dict = new HashMap<String,Symbol>();
	private String name;
	
	private Symbol(String n) {
		name=n;
	}

	public static Symbol symbol(String n) {
		String u = n.intern();
		Symbol s = null;
		
		if (!dict.containsKey(u)) {
			s = new Symbol(u);
			dict.put(u,s);
		}
		
		return s;
	}
	
	public String toString() {
		return name;
	}

}