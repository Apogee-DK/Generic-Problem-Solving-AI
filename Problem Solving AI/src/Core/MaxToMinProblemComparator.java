package Core;
import java.util.Comparator;

public class MaxToMinProblemComparator implements Comparator<Problem>{
	// class which will allow us to compare between two problems by using the heuristic value
	@Override
	public int compare(Problem p1, Problem p2){
		int currentValue = p1.getHeuristicValue();
		int compValue = p2.getHeuristicValue();		

		if(currentValue < compValue){
			return 1;
		}
		else if (currentValue > compValue){
			return -1;
		}
		else{
			return 0;
		}		
	}	
}
