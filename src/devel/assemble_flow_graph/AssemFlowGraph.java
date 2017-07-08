package devel.assemble_flow_graph;

import java.util.ArrayList;
import java.util.HashMap;

import core.activation_records.temp.Label;
import core.activation_records.temp.LabelList;
import core.activation_records.temp.TempList;
import core.dataflow_analysis.flow_graph.FlowGraph;
import core.dataflow_analysis.graph.Graph;
import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.NodeList;
import core.instruction_selection.assem.*;

public class AssemFlowGraph extends FlowGraph{
	private FlowGraph flowGraph = null;
	
	public Instr instr(Node n){
		
		return n.getInstruction();
	}

	public AssemFlowGraph(InstrList instrs){
		HashMap<Label, Node> labelMap = new HashMap<>();

		for(InstrList p=instrs; p!=null; p=p.tail){
			
			Instr instr = instrs.head;
			Node node = flowGraph.newNode(instr);
			
						
//			if the instruction is an label, add the map label=>node to the hashMap
			if(instr.getClass() == LABEL.class)
				labelMap.put(((LABEL)instr).label, node);
		}
		
//		Now we hash the map of all label instructions to its nodes. The we add the 
		for(InstrList p=instrs; p!=null; p=p.tail){
			
			Instr instr_a = instrs.head;
			Instr instr_b = null;
			Node node_a = null;
			Node node_b = null;

			if(instrs.tail!= null)
				instr_b = instrs.tail.head;
				
//			Getting node from instr_a
			for(NodeList nodes = flowGraph.nodes(); nodes!= null && node_a == null; nodes=nodes.tail){
				if(instr(nodes.head) == instr_a)
					node_a = nodes.head;
			}
			
//			Getting node from instr_b
			for(NodeList nodes = flowGraph.nodes(); nodes!= null && node_b == null; nodes=nodes.tail){
				if(instr(nodes.head) == instr_b)
					node_b = nodes.head;
			}
			
//			Adding edge to next instruction in list
			if(node_b != null)
				flowGraph.addEdge(node_a, node_b);
			
			node_b = null;
//			Adding edge to the label instr_a jumps to 
			for( LabelList targets = instr_a.jumps().labels; targets!=null; targets = targets.tail){
				Label label_b = targets.head;
				node_b = labelMap.get(label_b);
				if(node_b != null)
					flowGraph.addEdge(node_a, node_b);
			}
		}	
	}
	
	public TempList def(Node node) {
		return node.getDefs();
	}

	public TempList use(Node node) {
		return node.getUses();
	}

	public boolean isMove(Node node) {
		return this.instr(node).getClass() == MOVE.class;
	}

}
