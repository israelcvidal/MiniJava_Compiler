package devel.liveness_analysis;

import core.activation_records.temp.TempList;
import core.dataflow_analysis.flow_graph.FlowGraph;
import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.NodeList;

public class LivenessGraph extends FlowGraph{

	@Override
	public TempList def(Node node) {
		// TODO Auto-generated method stub
		return node.getDefs();
	}

	@Override
	public TempList use(Node node) {
		// TODO Auto-generated method stub
		return node.getUses();
	}

	@Override
	public boolean isMove(Node node) {
		// TODO Auto-generated method stub
		return node.getMoves().labels.head!=null;
	}

}
