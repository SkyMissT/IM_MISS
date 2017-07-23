package com.miss.entity;

public class ChartMessage {
	
	
	
	public ChartMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChartMessage(int id, int userone, int usertwo, String message, String time, int said) {
		super();
		this.id = id;
		this.userone = userone;
		this.usertwo = usertwo;
		this.message = message;
		this.time = time;
		this.said = said;
	}
	
	private int id;
	private int userone;
	private int usertwo;
	private String message;
	private String time;
	private int said;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserone() {
		return userone;
	}
	public void setUserone(int userone) {
		this.userone = userone;
	}
	public int getUsertwo() {
		return usertwo;
	}
	public void setUsertwo(int usertwo) {
		this.usertwo = usertwo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public int getSaid() {
		return said;
	}
	public void setSaid(int said) {
		this.said = said;
	}
	@Override
	public String toString() {
		return "ChartMessage [id=" + id + ", userone=" + userone + ", usertwo=" + usertwo + ", message=" + message
				+ ", time=" + time + ", said=" + said + "]";
	}
	
	

}
