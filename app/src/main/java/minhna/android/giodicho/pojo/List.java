package minhna.android.giodicho.pojo;

public class List {
	long _id;
	private String title;
	private int meetQuantity;
	private int quantity;
	private boolean isDone;
	private String color;
	
	public List(){};
	
	public List(long _id, String title, int meetQuantity, int quantity,
			boolean isDone, String color) {
		super();
		this._id = _id;
		this.title = title;
		this.meetQuantity = meetQuantity;
		this.quantity = quantity;
		this.isDone = isDone;
		this.color = color;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDone() {
		return isDone;
	}


	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public int getMeetQuantity() {
		return meetQuantity;
	}

	public void setMeetQuantity(int meetQuantity) {
		this.meetQuantity = meetQuantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
