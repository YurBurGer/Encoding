package codingThreads;

import java.util.Comparator;

import main.TableRow;

class ResultComparator implements Comparator<TableRow>{
	@Override
	public int compare(TableRow o1, TableRow o2) {
		Integer l1=o1.getCode().length(),l2=o2.getCode().length();
		if(l1.compareTo(l2)!=0)						
			return l1.compareTo(l2);
		else
			return o1.getCode().compareTo(o2.getCode());			
	}	
}