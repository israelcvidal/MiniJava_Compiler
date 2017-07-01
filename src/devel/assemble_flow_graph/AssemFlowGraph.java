package devel.assemble_flow_graph;

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
		
		return null;
	}

	public AssemFlowGraph(InstrList instrs){
		
		for(InstrList p=instrs; p!=null; p=p.tail){
		
			Instr instr_a = instrs.head;
			Node node_a = flowGraph.newNode(instr_a);
			
			for( LabelList targets = instr_a.jumps().labels; targets!=null; targets = targets.tail){
				Label label_b = targets.head;
//				Node node_b = flowGraph.newNode(label_b);
				
				flowGraph.addEdge(node_a, node_b);
				
				
				
				
			}
					
		}
	}
	
	@Override
	public TempList def(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TempList use(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMove(Node node) {
		// TODO Auto-generated method stub
		return false;
	}

}
