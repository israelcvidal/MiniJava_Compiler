package core.dataflow_analysis.graph;

import core.activation_records.temp.TempList;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.Targets;

public class Node {

    Graph mygraph;
    @SuppressWarnings("unused")
	private Node(){}
    int mykey;
    private Instr instruction;
    
    public Instr getInstruction(){
    	return this.instruction;
    }
    public Node(Graph g, Instr instruction) {
		mygraph=g;
		this.instruction = instruction;
		mykey= g.nodecount++;
		NodeList p = new NodeList(this, null);
		if (g.mylast==null)
		   g.mynodes=g.mylast=p;
		else g.mylast = g.mylast.tail = p;
	}
    
    public TempList getUses(){
    	return instruction.use();
    }
    
    public TempList getDefs(){
    	return instruction.def();
    }
    
    public Targets getMoves(){
    	return instruction.jumps();
    }

    NodeList succs = null;
    NodeList preds = null;
    
    public NodeList succ() {return succs;}
    
    public NodeList pred() {return preds;}
    
    NodeList cat(NodeList a, NodeList b) {
    	if (a==null) return b;
    	else return new NodeList(a.head, cat(a.tail,b));
    }
    public NodeList adj() {return cat(succ(), pred());}

    int len(NodeList l) {
		int i=0;
		for(NodeList p=l; p!=null; p=p.tail) 
			i++;
		return i;
    }

    public int inDegree() {return len(pred());}
    
    public int outDegree() {return len(succ());}
    
    public int degree() {return inDegree()+outDegree();} 

    public boolean goesTo(Node n) {
    	return Graph.inList(n, succ());
    }

    public boolean comesFrom(Node n) {
    	return Graph.inList(n, pred());
    }

    public boolean adj(Node n) {
    	return goesTo(n) || comesFrom(n);
    }

    public String toString() {return String.valueOf(mykey);}

}
