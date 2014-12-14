package entity;

import java.util.List;

public class Person extends Page {
	private List<Person> linkList;
	private List<Category> categoryList;
	
	public List<Person> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Person> linkList) {
		this.linkList = linkList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
}
