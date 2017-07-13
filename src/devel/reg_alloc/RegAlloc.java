package devel.reg_alloc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import core.activation_records.temp.Temp;
import devel.liveness_analysis.Liveness;

public class RegAlloc {
	public static HashMap<String,Integer> Alloc(Liveness graph){
		int k = graph.getNumReg();
		Liveness workingGraph = new Liveness(graph);
		HashMap<String,Integer> colors = new HashMap<>();
		HashMap<String,Boolean> freezedNodes = new HashMap<>();
		
		Stack<String> simplifyStack = new Stack<>();
		Stack<String> spillStack = new Stack<>();
		Stack<String> freezeStack = new Stack<>();
		boolean change = true;
		graph.save();
		System.out.println(workingGraph.getNodes().size()+" nodes to analyse");
		//trying to make changes on the graph
		while(change){
			change = false;
			//System.out.println("NEW ITERATION ------------------------------");
			for(String n1 : workingGraph.getNodes()){
				//System.out.println("Analizing node "+n1);
				//simplify
				if(workingGraph.canSimplify(n1)){
					workingGraph.removeNode(n1);
					if(freezedNodes.containsKey(n1))
						freezeStack.add(n1);
					else
						simplifyStack.add(n1);
					
					change = true;
				}
				else{
					for(String n2 : workingGraph.getMoves(n1)){
						//merging
						if((workingGraph.Briggs(n1, n2) || workingGraph.George(n1, n2)) 
								&& !workingGraph.hasInterference(n1, n2) && workingGraph.hasMove(n1, n2)){
							workingGraph.merge(n1, n2);
							System.out.println("Merged "+n1+" whith "+n2);
							change = true;
						}
						
					}
				}
				
				//if we don't merge or simplify, we need to freeze or add as a potential spill
				if(!change){
					int min = Integer.MAX_VALUE;
					String node = null;
					for(String n : workingGraph.getNodes()){
						int degree = workingGraph.degreeOf(n);
						if(degree<min && workingGraph.hasMove(n)){
							min = degree;
							node = n;
						}
					}
					//if there is a node to freeze
					if(node!=null){
						//remove all the moves from taht node
						workingGraph.freeze(node);
						freezedNodes.put(node, true);
						change = true;
					}
					//spill
					else{
						int max = 0;
						for(String n : workingGraph.getNodes()){
							int degree = workingGraph.degreeOf(n);
							if(degree>max){
								max = degree;
								node = n;
							}
						}
						
						System.out.println("adding potencial spill");
						workingGraph.removeNode(node);
						spillStack.add(node);
						change = true;
					}
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
				
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colors.containsKey(n))
						neighColors.add(colors.get(t));
				}
				System.out.println("Temp "+t+" has "+neighColors.size()+" neighbors colored and "+graph.getInterferences(t).size()+" neighbors");
				for(int i = 0; i<k;i++){
					if(!neighColors.contains(i)){
						System.out.println("Color choosed to "+t+" : "+i);
						colors.put(t, i);
						break;
					}
				}
			}
		}
		
		//coloring temps from freeze stack
		System.out.println(freezeStack.size()+" freezed nodes to color");
		while(!freezeStack.isEmpty()){
			String temps = freezeStack.pop();
			
			//In case of merged nodes, analyse each temp
			for(String t : temps.split(",")){
				HashSet<Integer> neighColors = new HashSet<>();
				
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colors.containsKey(n))
						neighColors.add(colors.get(t));
				}
				System.out.println("Temp "+t+" has "+neighColors.size()+" neighbors coloredand "+graph.getInterferences(t).size()+" neighbors");
				if(neighColors.size()==k){
					//TODO SPILL
					System.out.println("Spill on freeze"); 
				}
				
				else{
					for(int i = 0; i<k;i++){
						if(!neighColors.contains(i)){
							colors.put(t, i);
							break;
						}
					}
				}
			}
		}
		
		//coloring temps from freeze stack
		System.out.println(spillStack.size()+" potential spill nodes to color");
		while(!spillStack.isEmpty()){
			String temps = spillStack.pop();
			
			//In case of merged nodes, analyse each temp
			for(String t : temps.split(",")){
				HashSet<Integer> neighColors = new HashSet<>();
				
				//checking wich colors are beeing used
				for(String n : graph.getInterferences(t)){
					if(colors.containsKey(n))
						neighColors.add(colors.get(t));
				}
				System.out.println("Temp "+t+" has "+neighColors.size()+" neighbors colored and "+graph.getInterferences(t).size()+" neighbors");
				if(neighColors.size()==k){
					//TODO SPILL
					System.out.println("Spill on spill stack"); 
				}
				
				else{
					//find the smaller color that is free
					for(int i = 0; i<k;i++){
						if(!neighColors.contains(i)){
							colors.put(t, i);
							break;
						}
					}
				}
			}
		}
		
		//printing collors
		System.out.println("Colors: "+colors.size());
		
		for(String t : colors.keySet())
			System.out.println(t+" - "+colors.get(t));
		
//		System.out.println(graph.hasInterference("t3", "t37"));
//		System.out.println(graph.getNodes().size());
//		graph.save();
		return colors;
	}
}
