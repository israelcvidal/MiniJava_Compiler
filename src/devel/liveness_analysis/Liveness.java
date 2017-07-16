package devel.liveness_analysis;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.dataflow_analysis.flow_graph.FlowGraph;
import core.dataflow_analysis.graph.Node;
import core.dataflow_analysis.graph.NodeList;
import core.dataflow_analysis.reg_alloc.InterferenceGraph;
import core.dataflow_analysis.reg_alloc.MoveList;

public class Liveness extends InterferenceGraph {
	private java.util.Dictionary<Node, HashSet<Temp>> liveIn = new java.util.Hashtable<Node, HashSet<Temp>>();
	private java.util.Dictionary<Node, HashSet<Temp>> liveOut = new java.util.Hashtable<Node, HashSet<Temp>>();
	private FlowGraph flowGraph;
	private HashMap<String, HashSet<String>> interferences;
	private HashMap<String, HashSet<String>> moves;
	private int significativeDegree = 32;
	
	public  Liveness(FlowGraph flowGraph) {
		this.flowGraph = flowGraph;
		
		interferences = new HashMap<>();
		moves = new HashMap<>();
		
		Boolean changed = true;
		
		for(NodeList nodes = this.flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
			Node node = nodes.head;
			liveIn.put(node, new HashSet<Temp>());
			liveOut.put(node, new HashSet<Temp>());
		}
		while(changed){
			changed = false;
			
			for(NodeList nodes = this.flowGraph.nodes(); nodes!=null; nodes=nodes.tail){
				Node node = nodes.head;
				HashSet<Temp> in = liveIn.get(node);
				HashSet<Temp> out = liveOut.get(node);
				
//				in'= in and out'= out
				int in_ = in.size();
				int out_ = out.size();
				
				HashSet<Temp> uses = new HashSet<Temp>();
				HashSet<Temp> defs = new HashSet<Temp>();
				HashSet<Temp> outMinusDef = new HashSet<Temp>();
				
//				getting uses from node
				for(TempList temps = node.getUses(); temps!=null; temps = temps.tail){
					uses.add(temps.head);
				}
				
//				getting defs from node
				for(TempList temps = node.getDefs(); temps!=null; temps = temps.tail){
					defs.add(temps.head);
				}
				
//				getting union of In from all successors from node
				HashSet<Temp> inSucc = new HashSet<Temp>();
				for(NodeList succs = node.succ(); succs!=null; succs=succs.tail){
					Node succ = succs.head;
					inSucc.addAll(liveIn.get(succ));
				}
				out.addAll(inSucc);
				
//				adding use U out - def to in[node]
				in.addAll(uses);
				
//				creating set out-def
				for(Temp temp: out){
					if(!defs.contains(temp))
						outMinusDef.add(temp);
				}
				
//				adding out-def to in
				in.addAll(outMinusDef);
				
				liveIn.put(node, in);
				liveOut.put(node, out);
				
				changed = changed || in.size() > in_ || out.size() > out_;
			}	
		}
//		Printing LiveIn 
//		Enumeration<Node> keys = liveIn.keys();
//		while(keys.hasMoreElements()){
//			Node key = keys.nextElement();
//			System.out.println(key + ":" + liveIn.get(key));
//		}
		
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
	
	public Liveness(Liveness graph){
		this.liveIn = graph.getLiveIn();
		this.liveOut = graph.getLiveOut();
		this.flowGraph = graph.getFlowGraph();
		
		this.interferences = new HashMap<>();
		for(Map.Entry<String, HashSet<String>> entry : graph.getInterferences().entrySet())
			this.interferences.put(entry.getKey(), entry.getValue());
		
		this.moves = new HashMap<>();
		for(Map.Entry<String, HashSet<String>> entry : graph.getMoves().entrySet())
			this.moves.put(entry.getKey(), entry.getValue());
		
		this.significativeDegree = graph.getSignificativeDegree();
	}
	
	public java.util.Dictionary<Node, HashSet<Temp>> getLiveIn() {
		return liveIn;
	}

	public java.util.Dictionary<Node, HashSet<Temp>> getLiveOut() {
		return liveOut;
	}

	public FlowGraph getFlowGraph() {
		return flowGraph;
	}

	public HashMap<String, HashSet<String>> getInterferences() {
		return interferences;
	}

	public HashMap<String, HashSet<String>> getMoves() {
		return moves;
	}

	public int getSignificativeDegree() {
		return significativeDegree;
	}
	
	public void addInterference(Temp a, Temp b){
		addInterference(a.toString(), b.toString());
	}
	
	public void addInterference(String a, String b){
		HashSet<String> t;
		
		if(!interferences.containsKey(a)){
			interferences.put(a, new HashSet<String>());
		}
		interferences.get(a).add(b);
		if(!interferences.containsKey(b)){
			interferences.put(b, new HashSet<String>());
		}
		interferences.get(b).add(a);
		
//		if((a.equals("t0") && b.equals("t1")) ||(a.equals("t1") && b.equals("t0"))){
//			System.out.println(interferences.get(a));
//			System.out.println(interferences.get(b));
//		}
	}
	
	public void addMove(Temp a, Temp b){
		addMove(a.toString(), b.toString());
	}
	
	public void addMove(String a, String b){		
		if(!moves.containsKey(a)){
			moves.put(a, new HashSet<String>());
		}
		HashSet<String> l = moves.get(a);
		l.add(b);
		moves.put(a,l);
		
		if(!moves.containsKey(b)){
			moves.put(b, new HashSet<String>());
		}
		l = moves.get(b);
		l.add(a);
		moves.put(b,l);
	}
	
	public HashSet<String> getInterferences(String a){
		return interferences.getOrDefault(a, new HashSet<String>());
	}
	
	public HashSet<String> getMoves(String a){
		return moves.getOrDefault(a, new HashSet<String>());
	}
	
	public int degreeOf(String a){
		if(!interferences.containsKey(a))
			return 0;
		return interferences.get(a).size();
	}
	
	public boolean hasInterference(String a, String b){
		if(!interferences.containsKey(a))
			return false;
		
		return interferences.get(a).contains(b);
	}
	
	public boolean hasMove(String a, String b){
		if(!moves.containsKey(a))
			return false;
		return moves.get(a).contains(b);
	}
	
	public boolean hasMove(String a){
		if(!moves.containsKey(a))
			return false;
		
		return moves.get(a).size()>0;
	}
	
	public boolean George(String a, String b){
		HashSet<String> neighs = interferences.getOrDefault(a, new HashSet<>());
		int cont = 0;
		
		for(String n: neighs){
			//the temp is neighbor of b
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
		
		neighs = interferences.getOrDefault(b, new HashSet<>());
		cont = 0;
		for(String n: neighs){
			//the edge is neighbor of b
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
		//set of unique neighbors of a and b
		HashSet<String> newNeighs = new HashSet<>();
		
		//getting the neighbors of node a
		if(interferences.containsKey(a)){
			for(String n : interferences.get(a))
				newNeighs.add(n);
		}
		else{
			//System.out.println(a);
		}
			
		//getting the neighbors of node b that aren't neighbors of a
		if(interferences.containsKey(a)){
			for(String n : interferences.get(b))
				newNeighs.add(n);
		}
		else{
			//System.out.println(b);
		}
		
		//counter of significative neighbors
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
		return degreeOf(a)<significativeDegree && (moves.getOrDefault(a, new HashSet<>()).size()==0);
	}
	
	public boolean canFreeze(String a){
		if(!moves.containsKey(a))
			return degreeOf(a)<significativeDegree;
		
		return moves.get(a).size()>0 && degreeOf(a)<significativeDegree;
	}
	
	public int getNumReg(){
		return significativeDegree;
	}
	
	public ArrayList<String> getNodes(){
		ArrayList<String> list = new ArrayList<>(interferences.keySet());
		for (String n : moves.keySet())
			if(!list.contains(n))
				list.add(n);
		return list;
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
//		System.out.println("Trying to remove node "+a);
//		System.out.println(interferences.containsKey(a));
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

	public void freeze(String a) {
		HashSet<String> neighs = moves.remove(a);
		for(String n : neighs){
			moves.get(n).remove(a);
		}
	}
	
	public void save(){
		try {
			Writer w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("interferenceGraph.csv")));
			
			HashMap<String,HashSet<String>> edges = new HashMap<>();
			
			for(Map.Entry<String, HashSet<String>> entry : interferences.entrySet()){
				String n1 = entry.getKey();
				
				for(String n2 : entry.getValue()){
					w.write(n1+","+n2+"\n");
				}
			}
			w.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
