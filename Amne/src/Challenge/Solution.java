package Challenge;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Solution {
	public static int[] result;
	public static Queue<subRange> queueInc = new PriorityQueue<subRange>();
    public static Queue<subRange> queueDec = new PriorityQueue<subRange>();
	public static HashMap<Integer, Integer> sumSubArray= new HashMap<Integer, Integer>();
	public static subRange first; 
	public static subRange last; 
	public static LinkedList<subRange> windowMiddle = new LinkedList<subRange>();
	public static int N,K;

	/* Find all subRanges and store them into queueInc and queueDec */
    public static void findLength(int[] nums) {
        if (nums == null || nums.length == 0) {
        	return ;
        }
        int n = nums.length;
        int[] dpInc = new int[n];
        int[] dpDec = new int[n];    
        dpInc[0] = 1; dpDec[0] = 1;
        int startInc = 0; int endInc = 0; int startDec = 0; int endDec = 0;
        boolean StateInc = false;	// whether the last element is in an increasing subRange
        boolean StateDec = false;	// whether the last element is in an decreasing subRange
        for (int i = 1; i < n; i++) {
        	/* take care of the last subRange
        	 * including the situation if it is end of two same elements
        	*/
        	if (i == n-1 && nums[i] != nums[i - 1]){
        		if (nums[i] > nums[i - 1] || StateInc && !StateDec) { queueInc.add(new subRange(startInc, i, true)); }
        		if (nums[i] < nums[i - 1] || !StateInc && StateDec) { queueDec.add(new subRange(startDec, i, false)); }
        	}
            if (nums[i] > nums[i - 1]) {
            	StateInc = true;
                dpInc[i] = dpInc[i - 1] + 1;
                dpDec[i] = 1;
                if (dpDec[i - 1] != 1) {  
                	endDec = i -1;
                	queueDec.add(new subRange(startDec, endDec, false));
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
                	queueInc.add(new subRange(startInc, endInc, true));
                	StateInc = false;
                	startDec = i - 1;
                }                
            }
            /* the following element is the same as the previous one */
            else {
            	if (StateInc){
                	endInc = i - 1;
                	queueInc.add(new subRange(startInc, endInc, true));
                	StateInc = false;
            	}
            	if (StateDec){
                	endDec = i -1;
                	queueDec.add(new subRange(startDec, endDec, false));
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

    /* Return the sum of all subRanges of one Array */
    public static int sumWithoutOne(int largestNum) {
    	if (sumSubArray.containsKey(largestNum)) {return sumSubArray.get(largestNum);}
    	int sum = 0;
    	do {
    		sum += --largestNum;
    	} while (largestNum > 0);
    	sumSubArray.put(largestNum, sum);
    	return sum;
    }

    /* Return and remove the next subRange from either queueInc or queueDec */
    public static subRange getNextEle(){
		subRange ele;
		if (queueInc.size() > 0 && queueDec.size() >0){
			if (queueInc.peek().start < queueDec.peek().start ){
				ele = queueInc.poll();
			} else {// add elements from Decrease queue	
				ele = queueDec.poll();				
			}			
		} else if (queueInc.size() > 0){
			ele = queueInc.poll();
		} else {
			ele = queueDec.poll();
		}
		int l = ele.length;
		if (!sumSubArray.containsKey(l)){
			// put <key, value> to the hashMap
			sumSubArray.put(l, sumWithoutOne(l));
		}
		return ele;	
    }

    /* Return but do not remove the next subRange from either queueInc or queueDec */
    public static subRange peekNextEle(){
		subRange ele;
		if (queueInc.size() > 0 && queueDec.size() >0){
			if (queueInc.peek().start < queueDec.peek().start ){
				ele = queueInc.peek();
			} else {// add elements from Decrease queue	
				ele = queueDec.peek();				
			}			
		} else if (queueInc.size() > 0){
			ele = queueInc.peek();
		} else {
			ele = queueDec.peek();
		}
		int l = ele.length;
		if (!sumSubArray.containsKey(l)){
			// put <key, value> to the hashMap
			sumSubArray.put(l, sumWithoutOne(l));
		}
		return ele;	
    }

    /* Update the value of result[index] based on we want to add or delete a subRange from the window */
    public static void updateResult(subRange sr, int index, boolean add){
    	int l = sr.length;
		if (!sumSubArray.containsKey(l)){
			// put <key, value> to the hashMap
			sumSubArray.put(l, sumWithoutOne(l));
		}
		int pre = 1;
		if (!sr.increase){ pre = -1; }
    	/* add here means we want to add value to windowMiddle or minus value to it */
		if (sr.increase){
			result[index] += pre * sumSubArray.get(sr.length);			
		}
		else {
			result[index] -= pre * sumSubArray.get(sr.length);
		}
    }

    /* Set the result[0] based on window [0,K-1], and return pair<FirstValue, LastValue> */
    public static int[] setFirstWindow(int K){
    	/* If all the elements from the input data are the same */
    	if (queueInc.size() + queueDec.size() == 0){
    		return new int[]{0 , 0};
    	}
    	/* There is at least one trend */
    	/* if in the first window all the elements are the same. which means
    	 * first, windowMiddle and last are all null
		if () 
    	/* Set first element */
		first = peekNextEle();

		int preFirst = 1;
		if (!first.increase) { preFirst = -1;}

		if (first.start >= K - 1){
			first = null;
			windowMiddle = null;
			last = null;
			result[0] = 0;
			return new int[]{0 , 0};
		}
		first = getNextEle();

		// update the result of first window 
		// updateResult(first, 0, true);
		
		last = null;
		/* The whole window could only contains one element */

		/* There are three conditions:
		 * 1. normal condition: within the window, there are other subRange filled to windowMiddle even last
		 * 2. the rest elements are all the same so that there is to subRange following
		 * 3. the nearest subRange is out of window 
		 */
		if (queueInc.size() + queueDec.size()>0){
			subRange ele = getNextEle();
			int firstIndex ;
			firstIndex = ele.start;
			while (firstIndex < K - 1) { // at most index is K - 2
				ele = getNextEle();
				// updateResult(ele, 0, true);
				windowMiddle.addLast(ele);	// add the element to windowMiddle
				if (ele.end >= K - 1) {						
					break;
				}	// find the last element
				ele = peekNextEle();
				firstIndex = ele.start;					
			}
			if (windowMiddle.size() > 0){
				last = windowMiddle.pollLast();	// poll out the last in windowMiddle to last
			}
		}			
		/* Now we have given value to first, maybe last and windowMiddle as well, 
		 * now we need to calculate from result[0] 
		 */

		/* The end of first element equals to or exceeds the window 
		 * the first element may start at 0 or some index less than K - 1
		 */
		if (first.end >= K - 1){
			int l = K -1 - first.start + 1;
			if (!sumSubArray.containsKey(l)){
				// put <key, value> to the hashMap
				sumSubArray.put(l, sumWithoutOne(l));
			}
			
			result[0] += preFirst * sumSubArray.get(l);

			return new int[]{preFirst * sumSubArray.get(l), 0};	
		} 
		
		/* Add first and check windwoMiddle and last later */
		updateResult(first, 0, true);		

		/* If there is any element in windowMiddle */
		if (windowMiddle.size() > 0){
			for (int i = 0; i < windowMiddle.size(); i++) {
				updateResult(windowMiddle.pollFirst(), 0, true);
			}
		}
		// There is only one element in first within the window and no in windwoMiddle and last
		if (last == null) { return new int[]{sumSubArray.get(first.length), 0}; }

		int preLast = 1;
		if (!last.increase) { preLast = -1;}
		// if (last != null) 
		int l = K-1 - last.start + 1;
		if (!sumSubArray.containsKey(l)){
			// put <key, value> to the hashMap
			sumSubArray.put(l, sumWithoutOne(l));
		}		
		result[0] += preLast * sumSubArray.get(l);

		return new int[]{preLast * sumSubArray.get(l), sumSubArray.get(l)};
    }

    /* Update last subRange with the movement of window */
    public static void updateLast(int i){
		if (queueInc.size() + queueDec.size() > 0){
			if (last != null){
				if (last.end < i + K -1 || last.end == N - 1) {				
					// add elements from two queues
					subRange ele = peekNextEle();
					if (ele.start < i + K- 1 ){
						ele = getNextEle();
						windowMiddle.addLast(last);
						// Update result[i]
						updateResult(last, i, true);
						// in case there is no element in windowMiddle
						last = ele;							
					}
										
				}
			} else { // last is null
				subRange ele = peekNextEle();
				if (ele.start < i + K- 1 ){
					ele = getNextEle();
					last = ele;
				}
			}
		}
    }

    /* Update first subRange with the movement of window */
    public static void updateFirst(int i){
		if (first != null) {
			if (first.end <= i){
				if (windowMiddle.size() > 0){					
					//Update windowMiddle by remove an old element
					first = windowMiddle.pollFirst();
					// Update result[i]  
					updateResult(first, i, false);					
				} else {
					// two cases:
					// 1. last is not null
					// 2. last is null 
					first = last;
					last = null;
				}					
			}				
		} else if (last != null){ // last is not null, first is null
			first = last;
			last = null;				
		}
    }

    /* Return updatedFirstValue based on updated first subRange */
    public static int getUpdatedFirstValue(int i){

    	if (first == null) { return 0; }
    	int pre = 1;
    	if (!first.increase) {pre = -1;}
    	if (first.start < i){
    		if (first.end > i + K -1){	// case 4
    			return pre * sumWithoutOne(K);
    		} else {					// case 1
    			return pre * sumWithoutOne(first.end - i +1);
    		}
    	} else {
    		if (first.end > i - K +1){// case 3
    			return pre * sumWithoutOne(i - K + 1 - first.start + 1);
    		} else {	// case 2
    			return pre * sumWithoutOne(first.length);
    		}
    	}
    }

    /* Return updatedLastValue based on updated last subRange */
    public static int getUpdatedLastValue(int i){

    	if (last == null) { return 0;}
    	int pre = 1;
    	if (!last.increase) {pre = -1;}
    	if (last.end > i + K - 1) {	// case 2
    		return pre * sumWithoutOne(i + K - 1 - last.start + 1);
    	} else {					// case 1
    		return pre * sumWithoutOne(last.length);
    	}
    }
	public static int[] findPattern(int n, int k, int[] price){
		N = n; K = k;
		findLength(price);
		result = new int [N - K + 1];
		/* Set first window */
		int[] pair = setFirstWindow(K);
		/* Initialize first value and last value */
		int FirstValue = pair[0];
		int LastValue = pair[1];
		for (int i = first.start + 1; i < N - K + 1; i++){
			/* window start index is i, window end index is i + K -1 
			 * If there is only one element, apply it to first subRange
			 * If there is only two elements, apply one to first subRange and next to last subRange
			 * otherwise, apply the first to first, the last to last, and the rest to windowMiddle
			*/

		 	/* Update last if necessary */
			updateLast(i);

			/* Update first if necessary */
			updateFirst(i);

			/* Calculate result[i] based on result[i - 1]
			 * Keep the sum of elements in windowMiddle
			 * Just update the sum of first and last
			 */
			int updatedFirstValue = getUpdatedFirstValue(i); 
			int updatedLastValue = getUpdatedLastValue(i);				
			result[i] += result[i - 1] + updatedFirstValue - FirstValue + updatedLastValue - LastValue;
			FirstValue = updatedFirstValue;
			LastValue = updatedLastValue;			
		}

		return result;
	}
	public static void main(String[] args) throws IOException {
		// we assume that the input.txt is located in the same directory of this Solution.java file
		InputStream inputstream = new FileInputStream("./src/Challenge/inputChengxi2.txt");
		@SuppressWarnings("resource")
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputstream));
		if(reader.ready()){
			String[] line = reader.readLine().split(" ");
			int N = Integer.valueOf(line[0]);
			int K = Integer.valueOf(line[1]);
			line = reader.readLine().split(" ");
			int i = 0;
			int[] price = new int[line.length];
			while (i < line.length){
				price[i] = Integer.valueOf(line[i]);
				System.out.println(line[i++]);
			}
			int[] result = findPattern(N, K, price);
			OutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Challenge/output.txt"));
			for (int j = 0; j < result.length; j++){
				output.write((String.valueOf(result[j])+" ").getBytes());
			}
			output.close();
		}		
	}
}