/**
* The Solution program implements a solution to the challenge from Amne.Inc
*
* @author  Chengxi Zhu
* @version 1.0
* @since   2017-10-12 
*/
package Challenge;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
//import java.util.Random;

public class Solution {
	public static int[] dp;
	public static Queue<SubContRange> queueInc = new PriorityQueue<SubContRange>();
    public static Queue<SubContRange> queueDec = new PriorityQueue<SubContRange>();
    public static Queue<SubContRange> queueRem = new PriorityQueue<SubContRange>();
    public static LinkedList<Trend> trendList = new LinkedList<Trend>();
	public static HashMap<Integer, Integer> sumSubArray= new HashMap<Integer, Integer>();
	public static SubContRange first; 
	public static SubContRange last; 
	public static LinkedList<SubContRange> windowMiddle = new LinkedList<SubContRange>();
	public static int N,K;

	/* Find all subContRanges and store them into queueInc or queueDec or queueRem */
    public static void findLength(int[] nums) {
        if (nums == null || nums.length == 0) {
        	return ;
        }
        int n = nums.length;
        int[] dpInc = new int[n];
        int[] dpDec = new int[n];    
        int[] dpRem = new int[n];
        dpInc[0] = 1; dpDec[0] = 1;dpRem[0] = 1;
        int startInc = 0; int endInc = 0; int startDec = 0; int endDec = 0;int startRem = 0; int endRem = 0;
        Trend current = null;
        for (int i = 1; i < n; i++) {
        	/* take care of the last subContRange
        	 * including the situation the last two elements have a same/different trend with the previous one
        	 * don't miss them
        	*/
        	if (i == n-1 ){
        		if (nums[i] > nums[i - 1]){
        			if (current == Trend.Inc){ 
        				queueInc.add(new SubContRange(startInc, i, Trend.Inc)); trendList.addLast(Trend.Inc);
        			} else {
                        if (current == Trend.Rem){
                            queueRem.add(new SubContRange(startRem, i-1, Trend.Rem)); trendList.addLast(Trend.Rem);
                        } else if (current == Trend.Dec){
                            queueDec.add(new SubContRange(startDec, i-1, Trend.Dec)); trendList.addLast(Trend.Dec);
                        }
        				queueInc.add(new SubContRange(i - 1, i, Trend.Inc)); trendList.addLast(Trend.Inc);
        			}
        		}
        		if (nums[i] < nums[i - 1] ) {
                    if (current == Trend.Dec){
                        queueDec.add(new SubContRange(startDec, i, Trend.Dec)); trendList.addLast(Trend.Dec);
                    }else{
                        if (current == Trend.Rem){
                            queueRem.add(new SubContRange(startRem, i-1, Trend.Rem)); trendList.addLast(Trend.Rem);
                        } else if (current == Trend.Inc){
                            queueInc.add(new SubContRange(startInc, i-1, Trend.Inc)); trendList.addLast(Trend.Inc);
                        }
                        queueDec.add(new SubContRange(i - 1, i, Trend.Dec)); trendList.addLast(Trend.Dec);
                    }                    
                }
        		if (nums[i] == nums[i - 1] ) {
                    if (current == Trend.Rem){                    
                    queueRem.add(new SubContRange(startRem, i, Trend.Rem)); trendList.addLast(Trend.Rem);
                    } else {
                        if (current == Trend.Inc){
                            queueInc.add(new SubContRange(startInc, i-1, Trend.Inc)); trendList.addLast(Trend.Inc);
                        } else if(current == Trend.Dec){
                            queueDec.add(new SubContRange(startDec, i-1, Trend.Dec)); trendList.addLast(Trend.Dec);
                        }
                        queueRem.add(new SubContRange(i - 1, i, Trend.Rem)); trendList.addLast(Trend.Rem);
                    }
                }
        	}
            if (nums[i] > nums[i - 1]) {
            	current = Trend.Inc;
                dpInc[i] = dpInc[i - 1] + 1;
                dpDec[i] = 1;
                dpRem[i] = 1;
                if (dpDec[i - 1] != 1) {  
                	endDec = i - 1;                	
                	queueDec.add(new SubContRange(startDec, endDec, Trend.Dec));
                    trendList.addLast(Trend.Dec);
                	startInc = i - 1;   
                }
                if (dpRem[i - 1] != 1) {
                	endRem = i - 1;					
					queueRem.add(new SubContRange(startRem, endRem, Trend.Rem));
                    trendList.addLast(Trend.Rem);
					startInc = i - 1;   
                }            	                	          	            	            
            }
            else if (nums[i] < nums[i - 1]){
            	current = Trend.Dec;
            	dpDec[i] = dpDec[i - 1] + 1;
                dpInc[i] = 1;
                dpRem[i] = 1;
                if (dpInc[i - 1] != 1){
                	endInc = i - 1;                	
                	queueInc.add(new SubContRange(startInc, endInc, Trend.Inc));
                    trendList.addLast(Trend.Inc);
                	startDec = i - 1;
                }
                if (dpRem[i - 1] != 1) {	
                	endRem = i - 1;					
					queueRem.add(new SubContRange(startRem, endRem, Trend.Rem)); 
                    trendList.addLast(Trend.Rem);
					startDec = i - 1;
                }                                	              
            }
            /* the following element is the same as the previous one */
            else {
            	current = Trend.Rem;
            	dpRem[i] = dpRem[i - 1] + 1;
                dpInc[i] = 1;
                dpDec[i] = 1;
                if (dpInc[i - 1] != 1){
                	endInc = i - 1;                	
                	queueInc.add(new SubContRange(startInc, endInc, Trend.Inc));
                    trendList.addLast(Trend.Inc);
                	startRem = i - 1;  
                }                
                if (dpDec[i - 1] != 1) {  
                	endDec = i - 1;                	
                	queueDec.add(new SubContRange(startDec, endDec, Trend.Dec));
                    trendList.addLast(Trend.Dec);
                	startRem = i - 1;  
                }                             
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

    /* Return and remove the next subRange from among queueInc , queueDec and queueRem */
    public static SubContRange getNextEle(){
        Trend t = trendList.getFirst();
        SubContRange sc;
        switch(t){
            case Inc: sc = queueInc.poll(); break;
            case Dec: sc = queueDec.poll(); break;
            case Rem: sc = queueRem.poll(); break;
            default: sc = null;
        }
        trendList.remove(t);
        return sc;
    }

    /* Return but hold the next subRange from among queueInc , queueDec and queueRem */
    public static SubContRange peekNextEle(){
        Trend t = trendList.getFirst();
        SubContRange sc;
        switch(t){
            case Inc: sc = queueInc.peek(); break;
            case Dec: sc = queueDec.peek(); break;
            case Rem: sc = queueRem.peek(); break;
            default: sc = null;
        } 
        return sc;
    }

    public static void updateResult(SubContRange sr, int actualRange, int dpIndex, boolean add){
        if (sr.trend == Trend.Rem) return;
        if (!sumSubArray.containsKey(actualRange)){
            // put <key, value> to the hashMap
            sumSubArray.put(actualRange, sumWithoutOne(actualRange));
        }
        int pre = 1;
        if (sr.trend == Trend.Dec) pre = -1;
        if (add){
            dp[dpIndex] += pre * sumWithoutOne(actualRange);
        } else {
            dp[dpIndex] -= pre * sumWithoutOne(actualRange);
        }
    }

    public static void updateResult(SubContRange sr, int dpIndex, boolean add){
        if (sr.trend == Trend.Rem) return;
        int actualRange = sr.length;
        if (!sumSubArray.containsKey(actualRange)){
            // put <key, value> to the hashMap
            sumSubArray.put(actualRange, sumWithoutOne(actualRange));
        }
        int pre = 1;
        if (sr.trend == Trend.Dec) pre = -1;
        if (add){
            dp[dpIndex] += pre * sumWithoutOne(actualRange);
        } else {
            dp[dpIndex] -= pre * sumWithoutOne(actualRange);
        }
    }

    public static int[] setFirstWindow(int K){
        last = null;
        first = getNextEle();

        /* case 1: only one first element and no last and windowMiddle */
        if (first.end >= K - 1) { 
            updateResult(first, K, 0, true); 
            return new int[]{getUpdatedValue(K, true), 0};
        }

        /* case 2 & 3: one first element,one last element and no windowMiddle or a windowMiddle with several elements */
        // overall there is at least one element (which we have pooled)
        //assuming all elements are the same. so we need to check here)
        updateResult(first, first.length, 0, true);
        SubContRange ele = peekNextEle();
        int firstIndex = ele.start;
        while (firstIndex < K - 1) { // at most index is K - 2
            ele = getNextEle();
            windowMiddle.addLast(ele);  // add the element to windowMiddle
            updateResult(ele, 0, true); 
            if (ele.end >= K - 1) {                     
                break;
            }   // find the last element
            ele = peekNextEle();
            firstIndex = ele.start;                 
        }
        if (windowMiddle.size() > 0){
            last = windowMiddle.pollLast(); // poll out the last in windowMiddle to last
            // delete the original last value which may exceeds the window
            updateResult(last, 0, false); 
            // add the actualRange
            updateResult(last, (K - 1 - last.start + 1), 0, true);            
        }
        return new int[]{getUpdatedValue(first.length, true), getUpdatedValue(K - 1 - last.start + 1, false)};
    }

    public static void updateLast(int i){
        /* case 1: only one first element and no last and windowMiddle */
        if (last == null) {
            /* If the new window should include a first and a last */
            if (first.end < i + K -1) { 
            	last = getNextEle(); 
            }
            return;
        }
        /* case 2 & 3: one first element,one last element and no windowMiddle or a windowMiddle with several elements */
        if (last.end < i + K -1) {
            windowMiddle.addLast(last);
            updateResult(last, i, true);
            last = getNextEle(); 
        }
    }

    public static void updateFirst(int i){
        /* Keep same */        
        if (first.end > i ) return;
        /* case 2 : one first element,one last element and a windowMiddle with several elements */
        if (windowMiddle.size() > 0){ 
            first = windowMiddle.pollFirst(); 
            updateResult(first, i, false);
            return; 
        }
        /* case 3 : one first element,one last element and no windowMiddle */
        first = last; last = null; // make last to first
    }

    public static int getUpdatedValue(int actualRange, boolean fst){
        SubContRange sr;
        if (fst) { sr = first; } else { sr = last; }
        if (sr.trend == Trend.Rem) return 0;
        int pre = 1;
        if (sr.trend == Trend.Dec) pre = -1;
        if (!sumSubArray.containsKey(actualRange)){
            // put <key, value> to the hashMap
            sumSubArray.put(actualRange, sumWithoutOne(actualRange));
        }
        return (pre * sumSubArray.get(actualRange));
    }

    public static int getUpdatedValue(int i, int K, boolean fst){
        /* Determine it is first or last */
        SubContRange sr;
        int actualRange;
        if (fst) { 
            sr = first;
            if (sr.trend == Trend.Rem) {return 0;}
            if (sr.end >= i + K - 1 ) {
                actualRange = K;
            } else {
                actualRange = sr.end - i + 1;
            }
        } else { 
            sr = last;
            if ( sr == null) {return 0;}
            if (sr.trend == Trend.Rem) {return 0;}
            actualRange = i + K -1 - sr.start + 1;
        }
        int pre = 1;
        if (sr.trend == Trend.Dec) pre = -1;
        if (!sumSubArray.containsKey(actualRange)){
            // put <key, value> to the hashMap
            sumSubArray.put(actualRange, sumWithoutOne(actualRange));
        }
        return (pre * sumSubArray.get(actualRange));
    }

	public static int[] findPattern(int n, int k, int[] price){
		N = n; K = k;
		findLength(price);
        dp = new int [N - K + 1];
        /* Set the first window */
        int[] pair = setFirstWindow(K);
        int firstValue = pair[0]; 
        int lastValue = pair[1];

        // Runtime rt = Runtime.getRuntime();
        // long prevTotal = 0;
        // long prevFree = rt.freeMemory();
        
        for (int i = 1; i < N - K + 1; i++){            
            updateLast(i);
            updateFirst(i);
            int updatedFirstValue = getUpdatedValue(i, K, true);
            int updateLastValue = getUpdatedValue(i, K, false);
            dp[i] += dp[i-1] + updatedFirstValue + updateLastValue - firstValue - lastValue;
            firstValue = updatedFirstValue;
            lastValue = updateLastValue;            
            
            // long total = rt.totalMemory();
            // long free = rt.freeMemory();
            // if (total != prevTotal || free != prevFree) {
            //     System.out.println(
            //         String.format("#%s, Total: %s, Free: %s, Diff: %s",
            //             i, 
            //             total,
            //             free,
            //             prevFree - free));
            //     prevTotal = total;
            //     prevFree = free;
            // }
        }
		return dp;
		
	}
    public static void main(String[] args) throws IOException {
        // Random rand = new Random();
        // OutputStream testFile = new BufferedOutputStream(new FileOutputStream("./src/Challenge/intputTest.txt"));
        // for (int j = 0; j < 200000; j++){
        // 	testFile.write((String.valueOf((rand.nextInt(1000000) + 1))+" ").getBytes());
        // }
        // testFile.close();
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
            int[] dp = findPattern(N, K, price);
            OutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Challenge/output.txt"));
            for (int j = 0; j < dp.length; j++){
                output.write((String.valueOf(dp[j])+" ").getBytes());
                output.write(String.format("%n").getBytes());
            }
            output.close();
        }       
    }
}