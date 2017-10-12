package Challenge;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Test {
	public static class subRange implements Comparable<subRange>{
		public int start;	// start index of the subRange
		public int end;	// end index of the endRange
		public int length;
		public subRange(int s, int d){
			start = s;
			end = d;
			length = d - s + 1;
		}
		@Override
		public int compareTo(subRange sr){
			return this.start - sr.start;
		}
	}

	public static Queue<subRange> queueInc = new PriorityQueue<subRange>();
    public static Queue<subRange> queueDec = new PriorityQueue<subRange>();

    public static void findLength(int[] nums) {
        if (nums == null || nums.length == 0) {
        	return ;
        }
        int n = nums.length;
        int[] dpInc = new int[n];
        int[] dpDec = new int[n];    
        dpInc[0] = 1; dpDec[0] = 1;
        int startInc = 0; int endInc = 0; int startDec = 0; int endDec = 0;
        boolean StateInc = false;	// whether the last element is in an increasing subArray
        boolean StateDec = false;	// whether the last element is in an decreasing subArray
        for (int i = 1; i < n; i++) {
        	/* take care of the last subArray
        	 * including the situation if it is end of two same elements
        	*/
        	if (i == n-1){
        		if (StateInc && !StateDec) { queueInc.add(new subRange(startInc, i)); }
        		if (!StateInc && StateDec) { queueDec.add(new subRange(startDec, i)); }
        	}
            if (nums[i] > nums[i - 1]) {
            	StateInc = true;
                dpInc[i] = dpInc[i - 1] + 1;
                dpDec[i] = 1;
                if (dpDec[i - 1] != 1) {  
                	endDec = i -1;
                	queueDec.add(new subRange(startDec, endDec));
                	StateDec = false;             	
                	startInc = i - 1;
                }
            }
            else if (nums[i] < nums[i - 1]){
            	StateDec = true;
            	dpDec[i] = dpDec[i - 1] + 1;
                dpInc[i] = 1;
                if (dpInc[i - 1] != 1){
                	endInc = i - 1;
                	queueInc.add(new subRange(startInc, endInc));
                	StateInc = false;
                	startDec = i - 1;
                }                
            }
            /* the following element is the same as the previous one */
            else {
            	if (StateInc){
                	endInc = i - 1;
                	queueInc.add(new subRange(startInc, endInc));
                	StateInc = false;
            	}
            	if (StateDec){
                	endDec = i -1;
                	queueDec.add(new subRange(startDec, endDec));
                	StateDec = false; /* No decreasing trend any more */
            	}
            	/* Since they are the same , so there is no trend anymore 
            	 * Just reInitalize dpInc, dpDec, startInc, startDec as the beginning of the array
            	 * */
            	dpInc[i] = 1; dpDec[i] = 1;
            	startInc = i; startDec = i;
            }
        }        
        System.out.println("check it");
    }
    public static int sumWithoutOne(int largestNum) {
    	int sum = 0;
    	do {
    		sum += --largestNum;
    	} while (largestNum > 0);
    	return sum;
    }
	public static void main(String[] args) {
		System.out.println(sumWithoutOne(4));
		int arr[] =  {18, 19, 20, 21, 15, 15, 16, 18};
		int N = arr.length;
		int K = 5;
		findLength(arr);
		// for (int i = 0; i < N - K + 1; i++){
		// 	int[] sub = Arrays.copyOfRange(arr, i, i+K-1);

		// 	//int[] answer = findLengthOfLCIS(sub);
		// 	//System.out.println(answer[0]+" "+answer[1]);
		// }

	}

}
