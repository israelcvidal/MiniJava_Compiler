package devel.IR_translation;

import core.activation_records.frame.Frame;
import core.activation_records.temp.Label;
import core.translation_to_IR.tree.Stm;

/**
 * The class implements a function body's fragment for a given frame which is depending of architecture.
 * @author daniel
 *
 */

public class ProcFrag extends Frag {
	private Frame frame;
	private Stm body;
	
	public ProcFrag(Frame frame, Stm body) {
		this.frame = frame;
		this.body = body;
	}

	public Frame getFrame() {
		return frame;
	}
	
	public Stm getBody() {
		return body;
	}
	
	public Label getLabel() {
		return frame.name;
	}
	
	public int wordSize() {
		return frame.wordSize();
	}

}
