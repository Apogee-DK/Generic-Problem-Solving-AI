package Core;
/*
 * Every heuristic will need to implement this interface, which accepts an initial state and a goal object
 */
public interface Heuristics<T>{
	public int determineScore(char[][] puzzle, T goal);
}
