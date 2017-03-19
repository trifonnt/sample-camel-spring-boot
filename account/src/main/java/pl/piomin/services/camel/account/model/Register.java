package pl.piomin.services.camel.account.model;

import java.util.ArrayList;
import java.util.List;

public class Register {

	private String id;
	private String name;
	private String address;
	private int port;
	private List<String> tags = new ArrayList<String>();


	public Register() {

	}

	public Register(String id, String name, String address, int port, List<String> tags) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.port = port;
		if (tags != null) {
			this.tags = tags;
		}
	}

	public String getId() {
		return id;
	}
	public void setId(String iD) {
		id = iD;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public void addTag(String tag) {
		tags.add(tag);
	}
}