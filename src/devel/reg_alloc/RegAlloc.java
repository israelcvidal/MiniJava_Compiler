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
		Stack<String> simplifyStack = new Stack<>();
		Stack<String> spillStack = new Stack<>();
		Stack<String> freezeStack = new Stack<>();
		boolean change = true;
		
		//trying to make changes on the graph
		while(change){
			change = false;
			for(String n1 : workingGraph.getNodes()){
				//simplify
				if(workingGraph.canSimplify(n1)){
					workingGraph.removeNode(n1);
					simplifyStack.add(n1);
					change = true;
				}
				else{
					for(String n2 : workingGraph.getMoves(n1)){
						//merging
						if((workingGraph.Briggs(n1, n2) || workingGraph.George(n1, n2)) && !workingGraph.hasInterference(n1, n2)){
							workingGraph.merge(n1, n2);
							change = true;
						}
						else{
							//freeze
							if(workingGraph.canFreeze(n1)){
								workingGraph.removeNode(n1);
								freezeStack.add(n1);
								change = true;
							}
							//spill
							else{
								workingGraph.removeNode(n1);
								spillStack.add(n1);
							}
						}
					}
				}
			}
		}
		
		//coloring temps from simplify stack
		while(!simplifyStack.isEmpty()){
			String temps = simplifyStack.pop();
			
			//In case of merged nodes, analyse each temp
			for(String t : temps.split(",")){
				HashSet<Integer> neighColors = new HashSet<>();
				
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colors.containsKey(n))
						neighColors.add(colors.get(t));
				}
				
				for(int i = 0; i<k;i++){
					if(!neighColors.contains(i)){
						colors.put(new Temp(
								Integer.parseInt(t.replace("t", ""))), i);
						break;
					}
				}
			}
		}
		
		//coloring temps from freeze stack
		while(!freezeStack.isEmpty()){
			String temps = simplifyStack.pop();
			
			//In case of merged nodes, analyse each temp
			for(String t : temps.split(",")){
				HashSet<Integer> neighColors = new HashSet<>();
				
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colors.containsKey(n))
						neighColors.add(colors.get(t));
				}
				
				if(neighColors.size()==k){
					//TODO SPILL
				}
				
				else{
					for(int i = 0; i<k;i++){
						if(!neighColors.contains(i)){
							colors.put(new Temp(
									Integer.parseInt(t.replace("t", ""))), i);
							break;
						}
					}
				}
			}
		}
		
		return colors;
	}
}
