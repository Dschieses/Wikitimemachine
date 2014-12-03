package entity;

import java.util.List;

public class Page {
	private String title;
	private int ns;
	private int pageid;
	private List<Page> linkList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public int getPageid() {
		return pageid;
	}

	public void setPageid(int pageid) {
		this.pageid = pageid;
	}

	public List<Page> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Page> linkList) {
		this.linkList = linkList;
	}
}
