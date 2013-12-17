package dataClasses;

import java.math.BigDecimal;

public class Interval implements Comparable<Interval>{
	private BigDecimal left,right;
	private String s;
	public final int SCALECONST=5;
	public Interval() {
		super();
	}
	public Interval(int left, int right, TableRow t) {
		super();
		this.left = new BigDecimal((double)left/100.0);
		this.left= this.left.setScale(SCALECONST,BigDecimal.ROUND_HALF_UP);
		this.right = new BigDecimal((double)right/100.0);
		this.right = this.right.setScale(SCALECONST,BigDecimal.ROUND_HALF_UP);
		this.s = t.getS();
	}
	public BigDecimal getLeft() {
		return left;
	}
	public void setLeft(BigDecimal left) {
		this.left = left.setScale(SCALECONST,BigDecimal.ROUND_HALF_UP);
	}
	public void setLeft(int left) {
		this.left = new BigDecimal(left);
	}
	public BigDecimal getRight() {
		return right;
	}
	public void setRight(BigDecimal right) {
		this.right = right.setScale(SCALECONST,BigDecimal.ROUND_HALF_UP);
	}
	public void setRight(int right) {
		this.right = new BigDecimal(right);
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	@Override
	public int compareTo(Interval o) {
		return s.compareTo(o.s);
	}	
}