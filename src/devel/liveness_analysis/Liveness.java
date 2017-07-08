package devel.liveness_analysis;

import java.util.ArrayList;
import java.util.Iterator;

import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.dataflow_analysis.flow_graph.FlowGraph;
import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.NodeList;
import core.dataflow_analysis.reg_alloc.InterferenceGraph;
import core.dataflow_analysis.reg_alloc.MoveList;
import core.instruction_selection.assem.InstrList;

public class Liveness extends InterferenceGraph {
	private java.util.Dictionary<Node, ArrayList<Temp>> liveIn = new java.util.Hashtable<Node, ArrayList<Temp>>();
	private java.util.Dictionary<Node, ArrayList<Temp>> liveOut = new java.util.Hashtable<Node, ArrayList<Temp>>();
	
	public  Liveness(FlowGraph flowGraph) {
		Boolean changed = true;
		
		for(NodeList nodes = flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
			Node node = nodes.head;
			liveIn.put(node, new ArrayList<Temp>());
			liveOut.put(node, new ArrayList<Temp>());
		}
		while(changed){
			for(NodeList nodes = flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
				Node node = nodes.head;
				ArrayList<Temp> in = liveIn.get(node);
				ArrayList<Temp> out = liveOut.get(node);
				
//				in'= in and out'= out
				ArrayList<Temp> in_ = in;
				ArrayList<Temp> out_ = out;
				
				ArrayList<Temp> uses = new ArrayList<Temp>();
				ArrayList<Temp> defs = new ArrayList<Temp>();
				ArrayList<Temp> outMinusDef = new ArrayList<Temp>();
				
//				getting uses from node
				for(TempList temps = node.getUses(); temps!=null; temps = temps.tail){
					uses.add(temps.head);
				}
				
//				getting defs from node
				for(TempList temps = node.getDefs(); temps!=null; temps = temps.tail){
					defs.add(temps.head);
				}
				
//				getting union of In from all successors from node
				ArrayList<Temp> inSucc = new ArrayList<Temp>();
				for(NodeList succs = node.succ(); succs!=null; succs=succs.tail){
					Node succ = succs.head;
					inSucc.addAll(liveIn.get(succ));
				}
				out = inSucc;
				
//				adding use U out - def to in[node]
				in.addAll(uses);
				
//				creating set out-def
				outMinusDef = out;
				for (Temp temp : defs) {
					outMinusDef.remove(temp);
				}
//				adding out-def to in
				in.addAll(outMinusDef);
				
				changed = in.size() > in_.size() || out.size() > out_.size();
				if(changed){
					liveIn.put(node, in);
					liveOut.put(node, out);
				}
			}	
		}
		
		
		
	}
	
	
	@Override
	public Node tnode(Temp temp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Temp gtemp(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoveList moves() {
		// TODO Auto-generated method stub
		return null;
	}

}
