package core.dataflow_analysis.reg_alloc;

import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.Graph;
import core.activation_records.temp.Temp;

abstract public class InterferenceGraph extends Graph {
   abstract public Node tnode(Temp temp);
   abstract public Temp gtemp(Node node);
   abstract public MoveList moves();
   public int spillCost(Node node) {return 1;}
}
