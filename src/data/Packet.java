package data;

import data.elements.Header;


public abstract class Packet {

	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
}
