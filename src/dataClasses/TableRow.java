package dataClasses;

public class TableRow implements Comparable<TableRow>{
	private Integer p;
	private String s;
	private String code="";
	public TableRow(Integer p, String s) {
		super();
		this.p = p;
		this.s = s;
	}
	public Integer getP() {
		return p;
	}
	public String getS() {
		return s;
	}	
	public String getCode() {
		return code;
	}	
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public int compareTo(TableRow o) {
		if(p.compareTo(o.p)!=0)
			return -1*p.compareTo(o.p);
		else
			return s.compareTo(o.s);
	}	
}