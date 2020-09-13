
public class vEBtreePriorityQueue<T> {

	public vEBtreePriorityQueue() {
		
	}
	
	public T vEBsucc(T x) {
		if (this.equals(== 2)) {
			if (x == 0 && this.max == 1) return 1;
			else return null;
		}
		else if (this.min != null && x < this.min) return min;
		else {
			T max_low = vEBmax(this.cluster[high(x)]);
			if (max_low != null && low(x) < max_low) 
				T offset = vEBsucc(this.cluster[high(x)], low(x));
			else {
				T[] succ_cluster = vEBsucc(this.summary, high(x));
				if (succ_cluser == null) return null;
				else {
					T offset = vEBmin(this.cluster[succ.cluster]);
					return index(succ.cluster, offset);
				}
					
			}
		}
	}
	
	public boolean vEBmember(T x) {
		if (x == this.min || x == this.max) return true;
		else if (this.u == 2) return false;
		else return vEBmember(V.cluster[high(x)], low(x));
	}
	
	public void vEBinsert(T x) {
		if (this.min ==null) {
			this.min = x;
			this.max = x;
		}
		else {
			if (x < this.min) {
				T temp = this.min;
				this.min = x;
				x = temp;
			}
			if (this.u > 2) {
				if (vEB(this.cluster[high(x)]) == null) {
					vEBinsert(this.summary, high(x));
					this.cluster[high(x)].min = low(x);
					this.cluster[high(x)].max = low(x);
				}
				else vEBinsert(this.cluster[high(x)], low(x));
			}
			if (x > this.max) this.max = x;
		}
	}
}
