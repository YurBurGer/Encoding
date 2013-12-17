package codingThreads;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dataClasses.Interval;
import dataClasses.TableRow;

public class ArythmeticsThread extends Thread {
	private ArrayList<Interval> bord=new ArrayList<>();
	private String mess;
	private Interval res;
	public ArythmeticsThread(List<TableRow> inp, String m) {
		super();
		res=new Interval();
		int l=0,r=0;
		for(TableRow t:inp){
			r+=t.getP();
			bord.add(new Interval(l,r,t));
			l=r;		
		}
		Collections.sort(bord);
		this.mess=m.substring(0);
	}
	public Interval getRes() {
		return res;
	}
	public void run(){
		res.setLeft(0);
		res.setRight(1);
		for(char c:mess.toCharArray()){
			int ind=find(c,0,bord.size()-1);
			BigDecimal rlow=bord.get(ind).getLeft(),rhigh=bord.get(ind).getRight();
			BigDecimal l=res.getLeft(),r=res.getRight();
			BigDecimal low=l.add((r.subtract(l)).multiply(rlow));
			BigDecimal high=l.add((r.subtract(l)).multiply(rhigh));
			res.setLeft(low);
			res.setRight(high);			
		}
	}
	private int find(Character c,int l,int r){
		if(l!=r){
			int mid=(r+l)/2;
			if(c.compareTo(bord.get(mid).getS().charAt(0))>0)
				return find(c,mid+1,r);
			else if(c.compareTo(bord.get(mid).getS().charAt(0))<0)
				return find(c,l,mid-1);
			else
				return mid;			
		}
		else
			return l;
	}
}