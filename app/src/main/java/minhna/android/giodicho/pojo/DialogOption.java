package minhna.android.giodicho.pojo;

public class DialogOption {
	private String icon;
	private String title;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public DialogOption(String icon, String title) {
		super();
		this.icon = icon;
		this.title = title;
	}
	
}
