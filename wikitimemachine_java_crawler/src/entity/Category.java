/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package entity;
/**
 * 
 * The class category
 *
 */
public class Category {
	/**
	 * The category title
	 */
	private String title;
/**
 * A constructor for a new Category class
 * @param title a title
 */
	public Category(String title) {
		setTitle(title);
	}
/**
 * returns actual category title
 * @return a category title 
 */
	public String getTitle() {
		return title;
	}
	/**
	 * sets new title
	 * @param title new title 
	 */
	public void setTitle(String title) {
		this.title = title;
	}
/**
 * gets ns
 * @return ns value
 */
	public int getNs() {
		return ns;
	}
/**
 * Sets ns
 * @param ns the new ns
 */
	public void setNs(int ns) {
		this.ns = ns;
	}

	/**
	 * The ns.
	 */
	private int ns;

}
