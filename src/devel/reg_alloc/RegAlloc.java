package devel.reg_alloc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import core.activation_records.mips.MipsFrame;
import devel.liveness_analysis.Liveness;

public class RegAlloc {
	public static HashMap<String,Integer> Alloc(Liveness graph){
		int k = graph.getNumReg();
		Liveness workingGraph = new Liveness(graph);
		List<String> preColNodes = new LinkedList<String>();
		HashMap<String,Integer> colorMap = new HashMap<>();
		//HashMap<String,Boolean> freezedNodes = new HashMap<>();
		
		//stack of simplified nodes (without freeze)
		Stack<String> simplifyStack = new Stack<>();
		//stack of potential spill nodes
//		Stack<String> spillStack = new Stack<>();
//		//stack of simplified nodes (with freeze)
//		Stack<String> freezeStack = new Stack<>();
		
		//pre-colorred nodes
		for (int i = 0; i < MipsFrame.registers().length; i++) {
			//System.out.println("Pre-coloring registor "+MipsFrame.registers()[i].toString()+" with color "+i);
			colorMap.put(MipsFrame.registers()[i].toString(), i);
			preColNodes.add(MipsFrame.registers()[i].toString());
		}
		
		boolean change = true;
		
//		graph.save();
		//System.out.println("Nodes to analyze: "+workingGraph.getNodes());
		
		//trying to make changes on the graph
		while(change) {
//			System.out.println("Still "+workingGraph.getNodes().size()+" nodes to analyze...");
			//we assume that there isn't going to have any changes
			change = false;
			if(workingGraph.getNodes().size()==0)
				break;
			
			//we check if the graph has only pre-colored nodes
			boolean onlyPreColored = true;
			for(String n : workingGraph.getNodes()){
				//in case of merged nodes, we must check each temp on the node
				boolean hasAPreColored = false;
				for(String t : n.split(",")){
					if(preColNodes.contains(t)){
						hasAPreColored = true;
					}
				}
				if(!hasAPreColored){
					onlyPreColored = false;
					break;
				}
			}
			if(onlyPreColored)
				break;
			//for each node on the graph...
			for(String n1 : workingGraph.getNodes()){
				//System.out.println("Analysing node "+n1);
				//we try to simplify
				if(!preColNodes.contains(n1) && workingGraph.canSimplify(n1)){
					//System.out.println("Simplifying node "+n1);
					//remove it from the graph
					workingGraph.removeNode(n1);
					//put it on the simplifiedStack
					simplifyStack.push(n1);
					change = true;
					
				} else {
					if(!preColNodes.contains(n1))
//						System.out.println("Can't simplify node "+n1+". It has degree "+workingGraph.degreeOf(n1)+
//								" (max = "+workingGraph.getNumReg()+") and "+workingGraph.getMoves(n1).size()+" moves");
					//we try too merge with one of it's neighbors
					for(String n2 : workingGraph.getMoves(n1)) {
						//check briggs and george
						if((workingGraph.Briggs(n1, n2) || workingGraph.George(n1, n2)) 
								&& !workingGraph.hasInterference(n1, n2) && workingGraph.hasMove(n1, n2)){
							//if possible, we merge
							workingGraph.merge(n1, n2);
							System.out.println("Merged "+n1+" whith "+n2);
							change = true;
						}
						
					}
				}
			}
				
			//if we don't merge or simplify, we need to freeze or add as a potential spill
			if(!change){
				//System.out.println("Couldn't merge or simplify... time to call the calvalary!");
				//we try to get the "freezeable" node with the lowest degree
				int min = Integer.MAX_VALUE;
				String node = null;
				for(String n : workingGraph.getNodes()){
//						System.out.println("Checking if node "+n+" is freazeable. It has "+
//								workingGraph.getMoves(n).size()+" moves and "+
//								workingGraph.getInterferences(n).size()+" interferences");
					int degree = workingGraph.degreeOf(n);
					if(degree<min && workingGraph.hasMove(n)){
						min = degree;
						node = n;
					}
				}
				//if there is a node to freeze
				if(node!=null){
					//System.out.println("We have a freezeable node: "+node+" with moves "+workingGraph.getMoves(node));
					//remove all the moves from that node
					workingGraph.freeze(node);
//						freezedNodes.put(node, true);
					change = true;
				}
				//else, we have a potential spill
				else{
					//System.out.println("Ok, time to fuck this shit.... SPILL!!!!");
					//we will remove the node with highest degree, doesn't matter if is move-related or not
					int max = 0;
					for(String n : workingGraph.getNodes()){
						int degree = workingGraph.degreeOf(n);
						if(!preColNodes.contains(n) && degree>max) {
							max = degree;
							node = n;
						}
					}
					//System.out.println("adding potencial spill for " + node);
					//remove the node from the graph
					workingGraph.removeNode(node);
					//add to the spillStack
					simplifyStack.push(node);
					//spillStack.push(node);
					change = true;
				}
			}
		}
		
		//coloring temps from simplify stack
		System.out.println(simplifyStack.size()+" simplified nodes to color");
		while(!simplifyStack.isEmpty()){
			String temps = simplifyStack.pop();
			//In case of merged nodes, analyse each temp
			for(String t : temps.split(",")){
				HashSet<Integer> neighColors = new HashSet<>();
				//System.out.println(colorMap.size()+" colored nodes");
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colorMap.containsKey(n)){
						//System.out.println("Neighbor "+n+" already colored with color "+colorMap.get(n));
						neighColors.add(colorMap.get(n));
					}
				}
				//System.out.println("Temp "+t+" has neighbors colored "+neighColors+" and neighbors "+graph.getInterferences(t));
				for(int i = 0; i<k;i++){
					if(!neighColors.contains(i)){
						//System.out.println("Color choosed to "+t+" : "+i);
						colorMap.put(t, i);
						break;
					}
				}
			}
		}
				
		//printing collors
		System.out.println("Colored temps: ("+colorMap.size()+")");
		for(String t : colorMap.keySet())
			System.out.println(t+" - "+colorMap.get(t));
		
//		graph.save();
		return colorMap;
	}
}
