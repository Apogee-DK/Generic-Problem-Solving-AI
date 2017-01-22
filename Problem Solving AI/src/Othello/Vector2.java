package Othello;
import java.util.HashMap;

// Vector class which holds the positions of the chips on the Othello board
public class Vector2 {
	public int x;
	public int y;
	HashMap<Integer, Vector2> adjacentVectors; // the adjacent non-empty tiles of an empty tile
	
	public Vector2(int x, int y){
		this.x = x;
		this.y = y;
		adjacentVectors = new HashMap<Integer, Vector2>();
	}
	
	// add the adjacent tile to the hashmap
	public void addAdjacentVector(Vector2 v){
		adjacentVectors.put(v.hashCode(), v);
	}
	
	// remove the adjacent tile to the hashmap
	public void removeAdjacentVector(Vector2 v){
		adjacentVectors.remove(v.hashCode());
	}
	
	// return all the adjacent tiles
	public HashMap<Integer, Vector2> getAdjacentVectors(){
		return adjacentVectors;		
	}
	
	// copy the data of the vector
	public Vector2 clone(){
		Vector2 v = new Vector2(this.x, this.y);		
		
		for(Vector2 vector : adjacentVectors.values()){
			v.adjacentVectors.put(vector.hashCode(), vector.clone());
		}
		
		return v; 
	}
	
	// Hash code used for the hashmap
	@Override
	public int hashCode() {	
	    return (x*8) + y;
	}
	
	// Equals required to compare to vectors
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		
		Vector2 vector = (Vector2) obj;
		
		return (vector.x == this.x && vector.y == this.y)? true : false;		
	}
	
	// Displaying the vector in x, y coordinates
	@Override
	public String toString(){
		return "( " + x + ", " + y + " )";	
	}
}
