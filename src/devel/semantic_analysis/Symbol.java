package devel.semantic_analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * The class implements a manner more optimized to deal with identifier. 
 * @author daniel
 *
 */

public class Symbol {
	private static Map<String,Symbol> dict = new HashMap<String,Symbol>();
	private String name;
	
	private Symbol(String n) {
		name=n;
	}

	public static Symbol symbol(String n) {
		String u = n.intern();
		Symbol s = dict.get(u);
		
		if (s==null) {
			s = new Symbol(u);
			dict.put(u,s);
		}
		
		return s;
	}
	
	public String toString() {
		return name;
	}

}