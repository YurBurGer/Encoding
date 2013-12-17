package codingThreads;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import main.TableRow;

public class BlockThread extends Thread{
	List<TableRow> Shennon=new LinkedList<>();
	List<TableRow> Haffman=new LinkedList<>();
	List<TableRow> inp=new LinkedList<>();
	private int blockSize=2;
	public List<TableRow> getShennon() {
		return Shennon;
	}
	public List<TableRow> getHaffman() {
		return Haffman;
	}
	public BlockThread(List<TableRow> inp, int blockSize) {
		super();
		this.setDaemon(true);
		this.inp.clear();
		this.Shennon.clear();
		this.Haffman.clear();
		this.inp.addAll(inp);
		this.blockSize = blockSize;
	}	
	public BlockThread(List<TableRow> inp) {
		super();
		this.inp.clear();
		this.Shennon.clear();
		this.Haffman.clear();
		this.inp.addAll(inp);
	}
	public void run(){
		List<TableRow> blocks=Collections.synchronizedList(makeBlocks(inp,blockSize));
		ShennonThread sht=new ShennonThread(blocks);
		HaffmanThread ht=new HaffmanThread(blocks);
		sht.start();
		ht.start();
		try {
			sht.join();
			ht.join();
			Shennon.addAll(sht.getRes());
			Haffman.addAll(ht.getRes());
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	private LinkedList<TableRow> makeBlocks(List<TableRow> l,int b) {
		if(b==2){
			LinkedList<TableRow> result=new LinkedList<>();
			for(TableRow t1:l)
				for(TableRow t2:l)
					result.add(new TableRow(t1.getP()*t2.getP(), t1.getS().concat(t2.getS())));
			Collections.sort(result);
			return result;
		}
		else{
			LinkedList<TableRow> res=makeBlocks(l,b-1);
			return makeBlocks(res,2);
		}
	}
}
