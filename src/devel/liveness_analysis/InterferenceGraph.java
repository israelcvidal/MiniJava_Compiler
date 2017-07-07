package devel.liveness_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InterferenceGraph<T>{

	private HashMap<T,ArrayList<T>> interferences;
	private HashMap<T,ArrayList<T>> moves;
	private int significativeDegree;
	
	public InterferenceGraph(int degree){
		interferences = new HashMap<>();
		moves = new HashMap<>();
		significativeDegree = degree;
	}
	
	public void addInterference(T a, T b){
		if(!interferences.containsKey(a)){
			interferences.put(a, new ArrayList<T>());
		}
		ArrayList<T> l = interferences.get(a);
		l.add(b);
		interferences.put(a,l);
		
		if(!interferences.containsKey(b)){
			interferences.put(b, new ArrayList<T>());
		}
		l = interferences.get(b);
		l.add(a);
		interferences.put(b,l);
	}
	
	public void addMove(T a, T b){
		if(!moves.containsKey(a)){
			moves.put(a, new ArrayList<T>());
		}
		ArrayList<T> l = moves.get(a);
		l.add(b);
		moves.put(a,l);
		
		if(!moves.containsKey(b)){
			moves.put(b, new ArrayList<T>());
		}
		l = moves.get(b);
		l.add(a);
		moves.put(b,l);
	}
	
	public ArrayList<T> getInterferences(T a){
		return interferences.getOrDefault(a, new ArrayList<T>());
	}
	
	public ArrayList<T> getMoves(T a){
		return moves.getOrDefault(a, new ArrayList<T>());
	}
	
	public int degreeOf(T a){
		return interferences.get(a).size();
	}
	
	public boolean hasInterference(T a, T b){
		return interferences.get(a).contains(b);
	}
	
	public boolean hasMove(T a, T b){
		return moves.get(a).contains(b);
	}

	public boolean George(T a, T b){
		ArrayList<T> neighs = interferences.get(a);
		int cont = 0;
		
		for(int i=0; i<neighs.size(); i++){
			//the temp is neighbor of b
			T n = neighs.get(i);
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
			T n = neighs.get(i);
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
	
	public boolean Briggs(T a, T b){
		
		HashSet<T> newNeighs = new HashSet<>();
		for(T n : interferences.get(a))
			newNeighs.add(n);
		
		for(T n : interferences.get(a))
			newNeighs.add(n);
		
		int significantNeigh = 0;
		
		for(T n : newNeighs){
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

	public void removeNode(T a){
		interferences.remove(a);
		for(T k : interferences.keySet())
			interferences.get(k).remove(a);
		
		moves.remove(a);
		for(T k : moves.keySet())
			moves.get(k).remove(a);
		
	}
}
