package codingThreads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dataClasses.ResultComparator;
import dataClasses.TableRow;

public class ShennonThread extends Thread{
	private List<TableRow> spt=new ArrayList<>();
	public ShennonThread(List<TableRow> spt) {
		super();
		this.setDaemon(true);
		this.spt.addAll(spt);
	}
	public List<TableRow> getRes() {
		return spt;
	}
	public void run(){
		Code(spt);
		Collections.sort(spt, new ResultComparator());		
	}
	private List<TableRow> Code(List<TableRow> l){
		if(l.size()>1){
			int psum=0;
			for(TableRow t:l)
				psum+=t.getP();
			int sum=0,mind=psum,mini=-1;
			for(int i=0;i<l.size()-1;i++){
				sum+=l.get(i).getP();
				int d=Math.abs(psum-2*sum);
				if(d<mind){
					mini=i;
					mind=d;
				}
			}			
			List<TableRow> l1=new ArrayList<>();
			l1.addAll(l.subList(0, mini+1));								
			List<TableRow> l2=new ArrayList<>();
			l2.addAll(l.subList(mini+1, l.size()));
			for(TableRow t:l1)
				t.setCode(t.getCode().concat("0"));
			for(TableRow t:l2)
				t.setCode(t.getCode().concat("1"));
			List<TableRow> res = new ArrayList<>();
			res.addAll(Code(l1));	
			res.addAll(Code(l2));			
			return res;
		}		
		if(l.get(0).getCode().equals(""))
			l.get(0).setCode(l.get(0).getCode().concat("0"));
		return l;
	}
}