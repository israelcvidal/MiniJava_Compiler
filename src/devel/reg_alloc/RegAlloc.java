package devel.reg_alloc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import core.activation_records.mips.MipsFrame;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.InstrList;
import core.instruction_selection.assem.MOVE_ASSEM;
import devel.assemble_flow_graph.AssemFlowGraph;
import devel.liveness_analysis.Liveness;

public class RegAlloc {
	public static InstrList instList;
	public static HashMap<String,Integer> Alloc(Liveness graph){
		int k = graph.getNumReg();
		Liveness workingGraph = new Liveness(graph);
		List<String> preColNodes = new LinkedList<String>();
		HashMap<String,Integer> colorMap = new HashMap<>();
		//HashMap<String,Boolean> freezedNodes = new HashMap<>();
		
		//stack of simplified nodes (without freeze)
		Stack<String> simplifyStack = new Stack<>();

		//set of real spill temps
		HashSet<String> spillStack = new HashSet<>();
		
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
				
				//spill!
				if(neighColors.size()==k){
					System.out.println("Real spill occured");
					//we add the spill to the stack as a temp
					spillStack.add(t);
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
		
		if(spillStack.isEmpty()){
				
			//printing collors
			System.out.println("Colored temps: ("+colorMap.size()+")");
			for(String t : colorMap.keySet())
				System.out.println(t+" - "+colorMap.get(t));			
		}
		else{
			colorMap = rewriteCode(spillStack);
		}
		return colorMap;
	}
	
	public static HashMap<String,Integer> rewriteCode(HashSet<String> spills){
		Instr instr = instList.head;
		ArrayList<Instr> newList = new ArrayList<>();
		
		HashMap<String,Integer> newTemps = new HashMap<>();
		
		while(instr!=null){
			TempList use = instr.use();
			while(use!=null){
				//if use a spilled node, we should add a memory load to a new temp
				if(spills.contains(use.head.toString())){
					//add a load instruction on the newList
					Temp aux = new Temp();
					Instr newInstr = new MOVE_ASSEM("sw "+aux+", 0('"+use.head+")",aux, use.head);
					newList.add(newInstr);
					
					//we must replace the old used temp for the new one created
					instr.use().replace(use.head, aux);
				}
				
				use = use.tail;
			}
			//after check all the used temps, getting from memory and replacing the spill ones,
			//we add the instruction to the list;
			newList.add(instr);
			
			TempList def = instr.def();
			while(def!=null){
				//if use a spilled node, we should add a memory load to a new temp
				if(spills.contains(def.head.toString())){
					//we just create a new temp to get a memory position
					Temp aux = new Temp();
					//add a save instruction on the newList
					Instr newInstr = new MOVE_ASSEM("lw "+aux+", 0('"+def.head+")",aux, def.head);
					newList.add(newInstr);
				}
				
				def = def.tail;
			}
			//after check all the defined temps, getting from memory and replacing the spill ones,
			//we add the instruction to the list;
			newList.add(instr);
		}
		
		InstrList newInstList = null;
		
		//now we re-build the instructions list on the head/tail format
		for(int i=newList.size()-1; i<=0; i--){
			newInstList = new InstrList(newList.get(i), newInstList);
		}
		
		return RegAlloc.Alloc(new Liveness(
				new AssemFlowGraph(
						newInstList)));
	}
}
