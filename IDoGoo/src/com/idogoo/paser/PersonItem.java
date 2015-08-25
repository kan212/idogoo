package com.idogoo.paser;


public class PersonItem {

	public static final int COVENANT = 0; // 我约的人
	public static final int ABOUT = 1; // 约我的人
	public static final int MESSAGE = 2;// 我的消息
	public static final int INFORMATION = 3; // 我的资料

	private String[] titleList = { "我约的人", "约我的人", "我的消息", "我的资料" };

	private String title;
	private int mType = -1;

	public PersonItem(int type) {
		if (type >= titleList.length) {
			return;
		}
		this.mType = type;
		this.title = titleList[type];
	}

	public String getTitle() {
		return title;
	}
	
	public int getType() {
		return mType;
	}
	
}
