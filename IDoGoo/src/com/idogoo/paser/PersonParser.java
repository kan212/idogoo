package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class PersonParser extends BaseParser{

	private List<PersonItem> list = new ArrayList<PersonItem>();
	
	@Override
	public void parse(String json) {
		super.parse(json);
		
	}
	
	public class PersonItem{
		
		private String title;
		
		public PersonItem(JSONObject obj) {
			
		}
		
		public String getTitle() {
			return title;
		}
		
	}
	
}
