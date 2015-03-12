package dz.data;

public class Record {
	private String src_path;
	private String id;
	private String createDate;
	private String deadline;
	public String getSrc_path() {
		return src_path;
	}
	
	public Record(){}
	public Record(String id, String srcPath, String createDate, String deadline){
		this.setId(id);
		this.setSrc_path(srcPath);
		this.setCreateDate(createDate);
		this.setDeadline(deadline);
	}
	public void setSrc_path(String src_path) {
		this.src_path = src_path;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	

}
