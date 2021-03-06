/*************************************************************************
 *
 *  Pace University
 *  Fall 2020
 *  Advanced Data Structures and Computational Geometry
 *
 *  Course: CS 398F
 *  Author: Jack Bonnelycke
 *  Collaborators: none
 *  References: Introduction to Algorithms by Cormen et al.
 *
 *  Assignment: #1
 *  Problem: Compare the run times of Priority Queues implemented with Binary Heaps and Van Emde Boas Trees for
 *  		 construction and m operations on both.
 *  Description: In this assignment, I implemented two types of priority queue, one with a binary heap and one with
 *  			 a Van Emde Boas tree. In both I implemented the constructor and primary functions: insert(), 
 *  			 extractMax(), and increaseKey(). Once these were both implemented, I hypothesized construction and
 *  			 m operation running times, tested it with n insertions and m operations, and lastly analyzed the
 *  			 results.
 *
 *  Input: Universe size u, items to insert n, and m operations.
 *  Output: The run times for construction and n insertions on both data structures and for the same m operations on
 *  		both data structures.
 *
 *  Visible data fields:
 *  none
 *
 *  Visible methods:
 *  public vEBtreePriorityQueue(Integer u) {
 *  public vEBtreePriorityQueue(Integer u, Integer[] list) {
 *  public Integer ExtractMax() {
 *  public void Insert(Integer priority, Integer value) {
 *  public void IncreaseKey(Integer priority, Integer value) {
 *  public Integer vEBtreeMaximum() {
 *  public Integer vEBtreeMinimum() {
 *
 *   Remarks
 *   -------
 *
 *   1. Insertion is O(lgn) so inserting n times is O(nlgn).
 *   2. All three operations are O(lgn) in the binary heap implementation of priority queue. If we do m of these
 *   	operations then that's O(mlgn), which then amortized over those m operations is O(lgn)
 *   3. Insertion is O(lglgu), so n times will be O(nlglgu).
 *   4. As above, all three operations are the same, O(lglgu), so m times is O(mlglgu) amortized over the m 
 *   	operations is O(lglgu).
 *   
 *   	(for these first two tables, u = 1048576, while in the third I played around with having a tight u)
 *   
 *   6. construction time |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *        binary heap     |  5088400   |  6052200   |  9179200   |  21180600  | 57974200
 *         vEB tree       |  198816700 |  183842600 |  217866500 |  362887000 | 2334545300
 *
 *      m >= n operations |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *        binary heap     |   507500   |  1454100   |  7303100   |  101359000 | 6046404900
 *         vEB tree       |  140452600 |  13024100  |  14027100  |  148561300 | 1203503900
 *         
 *      tight-bound u     |  n = 10^2  |  n = 10^3  |  n = 10^4  |  n = 10^5  | n = 10^6
 *      				  |  u = 256   |  u = 1024	|  u = 16384 | u = 262144 | u = 1048576
 *      construction	  |  3827800   |  5675300   |  18420700  |  177800000 | 981366200
 *      m >= n operations |  1814300   |  2570600   |  15440500  |  130834300 | 1127774900
 *      
 *   7. Binary heap construction: The run times observed make sense. This is because we have
 *   	lgn which is pretty close to constant, times n. So mostly in the result we will see
 *      the effects of n. And looking at the numbers, it generally does that. Not quite though,
 *      and I think the reason is that O(lgn) for insertions is a pretty loose bound since 
 *      half the items are at the bottom of the "tree" that a binary heap uses. A pretty close
 *      relation can be seen at n = 10^4 to n = 10^5 which hops from 9179200 to 21180600. I
 *      should also note there could be unexpected overhead from how I implemented both of 
 *      the data structures to keep track of priority and value, as well as achieving random
 *      order.
 *      
 *      VEB construction: In this construction, the run times make sense again. The lglgu
 *      isn't really seen, and we just get the effects of n. This is seen best between 
 *      n = 10^5 and n = 10^6 when the number of digits increases by one from 362887000 to
 *      2334545300. While not part of the big-O, it doesn't help adding the base u time from
 *      making the empty tree before adding anything in.
 *      
 *      Binary Heap operations: Again, we see mostly linear run times. From n = 10^5 to 
 *      n = 10^6 we see a good example, with an extra digit added when going from 101359000
 *      to 6036404900.
 *      
 *      VEB operations: From 10^4 to 10^5 for n, we see linear run times in the numbers
 *      18420700 and 177800000. 
 *      
 *      I know question 7 is not asking our opinion, but I just found it fascinating that
 *      such an exciting data structure didn't perform very well in practice. I am certain
 *      there is a lot of overhead going on, but even when I have a tighter-bound u, it
 *      barely performs better than the binary heap. The most noticeable different is at
 *      n = 10^6, so maybe it just needs a larger number of operations to really feel the
 *      difference.
 *
 *************************************************************************/

import java.util.ArrayList;

public class vEBtreePriorityQueue {

	private Integer u;
	private Integer min, max;
	private vEBtreePriorityQueue summary;
	private ArrayList<vEBtreePriorityQueue> cluster;

	private Integer[] values;

	/**
	 * The main constructor that initializes everything including the array for the values,
	 * the u, the summary, and then the cluster and all of its recursive veb trees.
	 * @param u the universe size
	 */
	public vEBtreePriorityQueue(Integer u) {
		if (values == null) {
			values = new Integer[u];
		}

		this.u = u;

		// if u is 2, then this is all that needs to be initialized since it's a leaf
		if (u <= 2) {
			return;
		}

		summary = new vEBtreePriorityQueue((int) (highSqRt(u) + 0));

		Integer numTrees = (int) (highSqRt(u) + 0), size = (int) (lowSqRt(u) + 0);
		cluster = new ArrayList<vEBtreePriorityQueue>();
		for (Integer i = 0; i < numTrees; i++) { // make a cluster of size highsqrt(u) that has size lowsqrt(u) veb trees
			cluster.add(i, new vEBtreePriorityQueue(size));
		}
	}

	/**
	 * Calls the main constructor, then also inserts all of the items found in list
	 * @param u the universe size
	 * @param list the array of items
	 */
	public vEBtreePriorityQueue(Integer u, Integer[] list) {
		this(u);
		for (int i = 0; i < list.length; i++) { // adds all of the items in list
			if (list[i] != null) { // checks to make sure those items actually need to be added
				Insert(list[i], i);
			}
		}
	}

	/**
	 * Gets the max by deleting that priority in the veb and returning it
	 * @return the maximum
	 */
	public Integer ExtractMax() {
		Integer max = vEBtreeMaximum();
		vEBdelete(max);
		values[max] = null;
		return max;
	}

	/**
	 * Inserts an item by putting the value in the array of values and inserting
	 * priority into the veb
	 * @param priority
	 * @param value
	 */
	public void Insert(Integer priority, Integer value) {
		values[priority] = value;
		vEBinsert(priority);
	}

	/**
	 * Increases the priority to priority and the value to value
	 * @param priority
	 * @param value
	 */
	public void IncreaseKey(Integer priority, Integer value) {
		// checks to make sure the item to increase exists.
		if (values[priority] == null) {
			return;
		}
		vEBdelete(priority);
		values[priority] = value;
		vEBinsert(priority);
	}

	/**
	 * Typical member method that checks if an item exists
	 * @param x
	 * @return true if the item exists, false otherwise
	 */
	public boolean vEBmember(Integer x) {
		if (x == min || x == max) // does the initial check on the most definite values, min and max
			return true;
		else if (u == 2) // if its not min or max and its a leaf, it isn't there
			return false;
		else // o.w. check the item's corresponding cluster.
			return cluster.get(high(x)).vEBmember(low(x));
	}

	/**
	 * inserts item x into the veb tree
	 * @param x item to insert
	 */
	private void vEBinsert(Integer x) {
		if (min == null) { // nothing is in the veb tree, so just set min and max
			vebEmptyTreeInsert(x);
		} else {
			if (x < this.min) { // if the item is smaller than the min, exchange 
								// them and now attempt to insert the old min
				Integer temp = this.min;
				this.min = x;
				x = temp;
			}
			if (u > 2) {
				if (cluster.get(high(x)).vEBtreeMinimum() == null) { // if the item's cluster is null
					summary.vEBinsert(high(x));
					cluster.get(high(x)).vebEmptyTreeInsert(low(x));
				} else
					cluster.get(high(x)).vEBinsert(low(x));
			}
			if (x > max) // makes the item max if it's greater than max
				max = x;
		}
	}

	/**
	 * Adds one item to an empty tree by setting min and max to that item
	 * @param x item to insert
	 */
	private void vebEmptyTreeInsert(Integer x) {
		min = x;
		max = x;
	}

	/**
	 * deletes item x from the veb
	 * @param x item to be deleted
	 */
	private void vEBdelete(Integer x) {
		if (min == max) { // vebEmptyTreeInsert but delete
			min = null;
			max = null;
		} else if (u == 2) { // if a leaf, then set min and max to the remaining element
			if (x == 0)
				min = 1;
			else
				min = 0;
			max = min;
		} else { // not a leaf and not a veb with one item (a cluster)
			if (x == min) { // when x is min and there needs to be a new min in that cluster
				Integer first_cluster = summary.vEBtreeMinimum();
				x = index(first_cluster, cluster.get(first_cluster).vEBtreeMinimum());
				min = x;
			}
			cluster.get(high(x)).vEBdelete(low(x));
			if (cluster.get(high(x)).vEBtreeMinimum() == null) { // if x's cluster is empty
				summary.vEBdelete(high(x));
				if (x == max) { // if the item we're deleting is the max
					Integer summary_max = summary.vEBtreeMaximum();
					if (summary_max == null) // if min is the only element remaining
						max = min;
					else { // o.w. set max to the maximum element in the highest nonempty cluster
						max = index(summary_max, cluster.get(summary_max).vEBtreeMaximum());
					}
				}
			} else if (x == max) // in case x's cluster still has items
				max = index(high(x), cluster.get(high(x)).vEBtreeMaximum());
		}
	}
	
	/**
	 * @return the minimum from the tree
	 */
	public Integer vEBtreeMinimum() {
		return min;
	}

	/**
	 * @return the maximum from the tree
	 */
	public Integer vEBtreeMaximum() {
		return max;
	}

	/**
	 * @param x
	 * @return the most significant bits
	 */
	private Integer high(Integer x) {
		return (int) (Math.floor(x / lowSqRt(u)));
	}

	/**
	 * @param x
	 * @return the least significant bits
	 */
	private Integer low(Integer x) {
		return (int) (x % lowSqRt(u));
	}

	/**
	 * @param x
	 * @param y
	 * @return the combination of x and y
	 */
	private Integer index(Integer x, Integer y) {
		return (int) (x * lowSqRt(u) + y);
	}

	/**
	 * @param x
	 * @return 2^((lgu)/2) but with a ceiling around the exponent
	 */
	private static Double highSqRt(Integer x) {
		return Math.pow(2, Math.ceil(log2(x) / 2));
	}

	/**
	 * @param x
	 * @return 2^((lgu)/2) but with a floor around the exponent
	 */
	private static Double lowSqRt(Integer x) {
		return Math.pow(2, Math.floor(log2(x) / 2));
	}

	/**
	 * @param x
	 * @return log base 2 of x
	 */
	private static Double log2(Integer x) {
		return (Math.log(x) / Math.log(2));
	}
}
