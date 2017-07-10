package devel.reg_alloc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import core.activation_records.temp.Temp;
import devel.liveness_analysis.Liveness;

public class RegAlloc {
	public static HashMap<Temp,Integer> Alloc(Liveness graph){
		int k = graph.getNumReg();
		Liveness workingGraph = graph;
		HashMap<Temp,Integer> colors = new HashMap<>();
		Stack<Temp> stack = new Stack<>();
		boolean change = true;
		
		//trying to make changes on the graph
		while(change){
			change = false;
			for(Temp t1 : workingGraph.getNodes()){
				//simplify
				if(workingGraph.canSimplify(t1)){
					workingGraph.removeNode(t1);
					stack.add(t1);
					change = true;
				}
				else{
					for(Temp t2 : workingGraph.getMoves(t1)){
						//merging
						if((workingGraph.Briggs(t1, t2) || workingGraph.George(t1, t2)) && !workingGraph.hasInterference(t1, t2)){
							workingGraph.merge(t1, t2);
							change = true;
						}
						else{
							//freeze
							if(workingGraph.canFreeze(t1)){
								workingGraph.removeNode(t1);
								stack.add(t1);
								change = true;
							}
							//spill
							else{
								workingGraph.removeNode(t1);
								t1.spillTemp = true;
								stack.add(t1);
							}
						}
					}
				}
			}
		}
		
		while(!stack.isEmpty()){
			Temp t = stack.pop();
			
			HashSet<Integer> neighColors = new HashSet<>();
			
			//checking wich colors are beeing used
			for(Temp n : graph.getInterferences(t)){
				if(colors.containsKey(n))
					neighColors.add(colors.get(t));
			}
			
			if(neighColors.size()==graph.getNumReg()){
				//TODO SPILL
			}
			else{
				for(int i = 0; i<graph.getNumReg();i++){
					if(!neighColors.contains(i)){
						colors.put(t, i);
						break;
					}
				}
			}
		}
		
		return colors;
	}
}
