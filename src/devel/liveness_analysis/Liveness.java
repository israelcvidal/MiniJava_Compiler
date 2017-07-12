package devel.liveness_analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.dataflow_analysis.flow_graph.FlowGraph;
import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.NodeList;
import core.dataflow_analysis.reg_alloc.InterferenceGraph;
import core.dataflow_analysis.reg_alloc.MoveList;

public class Liveness extends InterferenceGraph {
	private java.util.Dictionary<Node, ArrayList<Temp>> liveIn = new java.util.Hashtable<Node, ArrayList<Temp>>();
	private java.util.Dictionary<Node, ArrayList<Temp>> liveOut = new java.util.Hashtable<Node, ArrayList<Temp>>();
	private FlowGraph flowGraph;
	private HashMap<String,ArrayList<String>> interferences;
	private HashMap<String,ArrayList<String>> moves;
	private int significativeDegree;
	
	public  Liveness(FlowGraph flowGraph) {
		this.flowGraph = flowGraph;
		
		interferences = new HashMap<>();
		moves = new HashMap<>();
		
		Boolean changed = true;
		
		for(NodeList nodes = this.flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
			Node node = nodes.head;
			liveIn.put(node, new ArrayList<Temp>());
			liveOut.put(node, new ArrayList<Temp>());
		}
		int count = 0;
		while(changed){
			count++;
			changed = false;
			
			for(NodeList nodes = this.flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
				Node node = nodes.head;
				ArrayList<Temp> in = liveIn.get(node);
				ArrayList<Temp> out = liveOut.get(node);
				
//				in'= in and out'= out
				ArrayList<Temp> in_ = new ArrayList<Temp>();
				ArrayList<Temp> out_ = new ArrayList<Temp>();
				in_.addAll(in);
				out_.addAll(out);
				
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
				
				liveIn.put(node, in);
				liveOut.put(node, out);
				
				changed = changed || in.size() > in_.size() || out.size() > out_.size();

				
			}	
		}
//		Enumeration<Node> keys = liveIn.keys();
//		while(keys.hasMoreElements()){
//			Node key = keys.nextElement();
//			System.out.println(key + ":" + liveIn.get(key));
//		}
		
		System.out.println(count);
		
		//Building interference graph
		
		//getting interferences from liveIn
		Enumeration<Node> nodes = liveIn.keys();
		while(nodes.hasMoreElements()){
			Node n = nodes.nextElement();
			
			for(Temp t1 : liveIn.get(n)){
				for(Temp t2 : liveIn.get(n)){
					if(t1!=null && t2 != null){
						if(!t1.toString().equals(t2.toString())){
							addInterference(t1, t2);
							//check if is a Move instruction
							if(this.flowGraph.isMove(n)){
								//adding move edge
								addMove(t1, t2);
							}
						}
					}
				}
			}
		}
		//getting interferences from liveOut
		nodes = liveOut.keys();
		while(nodes.hasMoreElements()){
			Node n = nodes.nextElement();
			
			for(Temp t1 : liveOut.get(n)){
				for(Temp t2 : liveOut.get(n)){
					if(t1!=null && t2 != null){
						if(!t1.toString().equals(t2.toString())){
							addInterference(t1, t2);
							//check if is a Move instruction
							if(this.flowGraph.isMove(n)){
								//adding move edge
								addMove(t1, t2);
							}
						}
					}
				}
			}
		}
	}

	public void addInterference(Temp a, Temp b){
		addInterference(a.toString(), b.toString());
	}
	
	public void addInterference(String a, String b){
		if(!interferences.containsKey(a)){
			interferences.put(a, new ArrayList<String>());
		}
		ArrayList<String> l = interferences.get(a);
		l.add(b);
		interferences.put(a,l);
		
		if(!interferences.containsKey(b)){
			interferences.put(b, new ArrayList<String>());
		}
		l = interferences.get(b);
		l.add(a);
		interferences.put(b,l);
	}
	
	public void addMove(Temp a, Temp b){
		addMove(a.toString(), b.toString());
	}
	
	public void addMove(String a, String b){
		if(!moves.containsKey(a)){
			moves.put(a, new ArrayList<String>());
		}
		ArrayList<String> l = moves.get(a);
		l.add(b);
		moves.put(a,l);
		
		if(!moves.containsKey(b)){
			moves.put(b, new ArrayList<String>());
		}
		l = moves.get(b);
		l.add(a);
		moves.put(b,l);
	}
	
	public ArrayList<String> getInterferences(String a){
		return interferences.getOrDefault(a, new ArrayList<String>());
	}
	
	public ArrayList<String> getMoves(String a){
		return moves.getOrDefault(a, new ArrayList<String>());
	}
	
	public int degreeOf(String a){
		return interferences.get(a).size();
	}
	
	public boolean hasInterference(String a, String b){
		return interferences.get(a).contains(b);
	}
	
	public boolean hasMove(Temp a, Temp b){
		return moves.get(a).contains(b);
	}
	
	public boolean George(String a, String b){
		ArrayList<String> neighs = interferences.get(a);
		int cont = 0;
		
		for(int i=0; i<neighs.size(); i++){
			//the temp is neighbor of b
			String n = neighs.get(i);
			if(hasInterference(b, n))
				cont++;
			else
				//the temp hasn't significative degree
				if(interferences.get(n).size()<significativeDegree){
					cont++;
				}
		}
		if(cont==neighs.size()){
			return true;
		}
		
		neighs = interferences.get(b);
		cont = 0;
		for(int i=0; i<neighs.size(); i++){
			//the edge is neighbor of b
			String n = neighs.get(i);
			if(hasInterference(a, n))
				cont++;
			else
				//the temp hasn't significative degree
				if(interferences.get(n).size()<significativeDegree){
					cont++;
				}
		}
		if(cont==neighs.size()){
			return true;
		}
		
		
		return false;
	}
	
	public boolean Briggs(String a, String b){
		
		HashSet<String> newNeighs = new HashSet<>();
		for(String n : interferences.get(a))
			newNeighs.add(n);
		
		for(String n : interferences.get(a))
			newNeighs.add(n);
		
		int significantNeigh = 0;
		
		for(String n : newNeighs){
			//check if has significative degree
			if(degreeOf(n)>=significativeDegree){
				//if has interference with both nodes, the degree should be decreased by one
				if(hasInterference(a, n) && hasInterference(b, n)){
					//if still significative
					if(degreeOf(n)-1>=significativeDegree){
						significantNeigh++;
					}
				}
			}
		}
		
		if(significantNeigh<significativeDegree)
			return true;
		
		return false;
	}
	
	public boolean canSimplify(String a){
		return degreeOf(a)<significativeDegree && (!moves.containsKey(a) || moves.get(a).size()==0);
	}
	
	public boolean canFreeze(String a){
		return moves.get(a).size()>0 && degreeOf(a)<significativeDegree;
	}
	
	public int getNumReg(){
		return significativeDegree;
	}
	
	public ArrayList<String> getNodes(){
		return new ArrayList<>(interferences.keySet());
	}
	
	public void merge(String a, String b){
		//TODO join 2 temps into 1 node
		HashSet<String> neighs = new HashSet<>();
		HashSet<String> moves = new HashSet<>();
		
		//getting the unique interferences
		for(String n : getInterferences(a))
			neighs.add(n);
		
		for(String n : getInterferences(b))
			neighs.add(n);
		
		//getting the unique moves
		for(String n : getMoves(a))
			moves.add(n);
		
		for(String n : getMoves(b))
			moves.add(n);
		
		//removing the nodes from the graph
		removeNode(a);
		removeNode(b);
		
		//new node
		String newNode = a+","+b;
		
		//adding the new node's interferences
		for(String n : neighs)
			addInterference(newNode,n);
		
		//adding the new moves
		for(String n: moves)
			if(!n.equals(a) && !n.equals(b))
				addMove(newNode, n);
	}
	
	public void removeNode(String a){
		interferences.remove(a);
		for(String k : interferences.keySet())
			interferences.get(k).remove(a);
		
		moves.remove(a);
		for(String k : moves.keySet())
			moves.get(k).remove(a);
		
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
