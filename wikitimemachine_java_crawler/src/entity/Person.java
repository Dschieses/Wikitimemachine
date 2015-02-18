/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package entity;

import java.util.List;
/**
 * The class person
 * 
 *
 */
public class Person extends Page {
	/**
	 * a list of persons to which this person object has a connection on Wikipedia 
	 */
	private List<Person> linkList;
	/**
	 * a list of categories which are saved on Wikipedia for this person
	 */
	private List<Category> categoryList;
	
	/**
	 * returns a list of persons to which this person object has a connection on Wikipedia 
	 * @return a list object of person objects
	 */
	public List<Person> getLinkList() {
		return linkList;
	}
/**
 * sets the list of persons to which this person has a connection on Wikipedia 
 * @param linkList a list to be set
 */
	public void setLinkList(List<Person> linkList) {
		this.linkList = linkList;
	}
/**
 * The method gets a list of categories which are saved on Wikipedia for this person
 * @return a list of categories
 */
	public List<Category> getCategoryList() {
		return categoryList;
	}
/**
 * sets the list of categories which apply to this person on Wikipedia 
 * @param categoryList a list of categories
 */
	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
}
