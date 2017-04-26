package devel.semantic_analysis;

/**
 * The class represents a semantic error.
 * @author daniel
 *
 */

public class SemanticErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public SemanticErrorException(String error) {
		super(error);
	}

}
