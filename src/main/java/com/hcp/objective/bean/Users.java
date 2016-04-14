package com.hcp.objective.bean;

import java.util.ArrayList;
import java.util.List;

public class Users {
	private List<User> users;
	
	public Users(){
		users=new ArrayList<User>();
	}

	public void addUser(User user){
		users.add(user);
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
