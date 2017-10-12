package Challenge;

public class subContRange implements Comparable<subContRange>{
	/* start index of the subRange */
	public int end;	
	/* end index of the subRange */
	public int start;
	/* length of the subRange */
	public int length;
	public Trend trend;
	public subContRange(int s, int d, Trend t){
		start = s;
		end = d;
		length = d - s + 1;
		trend = t;
	}
	
	@Override
	public int compareTo(subContRange sr){
		return this.start - sr.start;
	}
}