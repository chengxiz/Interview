package Challenge;

public class subRange implements Comparable<subRange>{
	/* start index of the subRange */
	public int end;	
	/* end index of the subRange */
	public int start;
	/* length of the subRange */
	public int length;
	public boolean increase;
	public subRange(int s, int d, boolean in){
		start = s;
		end = d;
		length = d - s + 1;
		increase = in;
	}
	
	@Override
	public int compareTo(subRange sr){
		return this.start - sr.start;
	}
}