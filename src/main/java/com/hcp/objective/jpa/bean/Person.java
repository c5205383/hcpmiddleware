package com.hcp.objective.jpa.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class holding information on a person.
 */
@Entity
@Table(name = "T_PERSON")
@NamedQuery(name = "AllPersons", query = "select p from Person p")
public class Person implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 819136598000488042L;
	@Id
    @GeneratedValue
    private Long id;
	@Column(name="NAME")
    private String name;
    @Column(name="EMAIL")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}