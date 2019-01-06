package minhna.android.giodicho.pojo;

public class Item {
	private long listId;
	private long _id;
	private String title;
	private int quantity;
	private String description;
	private boolean isDone;
	private String color;

	public Item(){}
	
	public Item(long listId, long _id, String title, int quantity,
			String description, boolean isDone, String color) {
		this.listId = listId;
		this._id = _id;
		this.title = title;
		this.quantity = quantity;
		this.description = description;
		this.isDone = isDone;
		this.color = color;
	}

	public Item(String title, int quantity,boolean isDone) {
		this.title = title;
		this.quantity = quantity;
		this.isDone = isDone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
