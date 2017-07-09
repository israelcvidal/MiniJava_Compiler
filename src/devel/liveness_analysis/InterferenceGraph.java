package devel.liveness_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InterferenceGraph<Temp>{

	private HashMap<Temp,ArrayList<Temp>> interferences;
	private HashMap<Temp,ArrayList<Temp>> moves;
	private int significativeDegree;
	
	public InterferenceGraph(int degree){
		interferences = new HashMap<>();
		moves = new HashMap<>();
		significativeDegree = degree;
	}
	
	public void addInterference(Temp a, Temp b){
		if(!interferences.containsKey(a)){
			interferences.put(a, new ArrayList<Temp>());
		}
		ArrayList<Temp> l = interferences.get(a);
		l.add(b);
		interferences.put(a,l);
		
		if(!interferences.containsKey(b)){
			interferences.put(b, new ArrayList<Temp>());
		}
		l = interferences.get(b);
		l.add(a);
		interferences.put(b,l);
	}
	
	public void addMove(Temp a, Temp b){
		if(!moves.containsKey(a)){
			moves.put(a, new ArrayList<Temp>());
		}
		ArrayList<Temp> l = moves.get(a);
		l.add(b);
		moves.put(a,l);
		
		if(!moves.containsKey(b)){
			moves.put(b, new ArrayList<Temp>());
		}
		l = moves.get(b);
		l.add(a);
		moves.put(b,l);
	}
	
	public ArrayList<Temp> getInterferences(Temp a){
		return interferences.getOrDefault(a, new ArrayList<Temp>());
	}
	
	public ArrayList<Temp> getMoves(Temp a){
		return moves.getOrDefault(a, new ArrayList<Temp>());
	}
	
	public int degreeOf(Temp a){
		return interferences.get(a).size();
	}
	
	public boolean hasInterference(Temp a, Temp b){
		return interferences.get(a).contains(b);
	}
	
	public boolean hasMove(Temp a, Temp b){
		return moves.get(a).contains(b);
	}

	public boolean George(Temp a, Temp b){
		ArrayList<Temp> neighs = interferences.get(a);
		int cont = 0;
		
		for(int i=0; i<neighs.size(); i++){
			//the temp is neighbor of b
			Temp n = neighs.get(i);
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
			Temp n = neighs.get(i);
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
	
	public boolean Briggs(Temp a, Temp b){
		
		HashSet<Temp> newNeighs = new HashSet<>();
		for(Temp n : interferences.get(a))
			newNeighs.add(n);
		
		for(Temp n : interferences.get(a))
			newNeighs.add(n);
		
		int significantNeigh = 0;
		
		for(Temp n : newNeighs){
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

	public void removeNode(Temp a){
		interferences.remove(a);
		for(Temp k : interferences.keySet())
			interferences.get(k).remove(a);
		
		moves.remove(a);
		for(Temp k : moves.keySet())
			moves.get(k).remove(a);
		
	}
}
