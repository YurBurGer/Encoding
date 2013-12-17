package codingThreads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import main.TableRow;

public class HaffmanThread extends Thread{
	private List<TableRow> spt=new ArrayList<>();
	private List<TableRow> res=new ArrayList<>();
	private boolean isOne=false;
	public HaffmanThread(List<TableRow> spt) {
		super();
		this.setDaemon(true);
		this.spt.addAll(spt);
	}
	public List<TableRow> getRes() {
		return res;
	}
	public void run(){		
		TreeNode tr=MakeTree(spt);
		if(!isOne)
			TreeTrav(tr, "");
		else
			TreeTrav(tr, "0");
		Collections.sort(res,new ResultComparator());
	}
	private TreeNode MakeTree(List<TableRow> l){
		if(l.size()>1){
			LinkedList<TreeNode> tree=new LinkedList<>();
			for(TableRow t:l)
				tree.add(new TreeNode(null,null,t));
			Collections.sort(tree);
			while(tree.size()>1){
				TreeNode left=tree.poll();
				TreeNode right=tree.poll();								
				tree.add(new TreeNode(left,right,left.getP()+right.getP()));
				Collections.sort(tree);
			}
			return tree.peek();
		}
		else{
			isOne=true;
			return new TreeNode(null,null,l.get(0));
		}			
	}
	private void TreeTrav(TreeNode t,String s){
		if(t!=null){
			if((t.left!=null)||(t.right!=null)){
				TreeTrav(t.left,s.concat("0"));
				TreeTrav(t.right,s.concat("1"));
			}
			TableRow tr=t.getElem();
			if(tr!=null){
				tr.setCode(s);
				res.add(tr);
			}
		}
	}
}
class TreeNode implements Comparable<TreeNode>{
	TreeNode left=null,right=null;
	Integer p;
	String code="";
	TableRow elem=null;
	public TableRow getElem() {
		return elem;
	}
	public TreeNode(TreeNode left, TreeNode right, TableRow elem) {
		super();
		this.left = left;
		this.right = right;
		this.p = elem.getP();
		this.elem=elem;
	}
	public TreeNode(TreeNode left, TreeNode right, Integer p) {
		super();
		this.left = left;
		this.right = right;
		this.p = p;
	}
	public TreeNode getLeft() {
		return left;
	}
	public TreeNode getRight() {
		return right;
	}
	public Integer getP() {
		return p;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public int compareTo(TreeNode o) {
		return p.compareTo(o.p);
	}	
}