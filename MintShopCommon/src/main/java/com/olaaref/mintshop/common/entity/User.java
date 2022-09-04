package com.olaaref.mintshop.common.entity;

import com.olaaref.mintshop.common.Constants;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
public class User extends IdBasedEntity {
	@Column(name = "email", length = 128, nullable = false, unique = true)
	private String email;

	@Column(name = "password", length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "image")
	private String image;

	@Transient
	private String imagePath;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(String email, String password, String firstName, String lastName, boolean enabled, String image,
			Set<Role> roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = enabled;
		this.image = image;
		this.roles = roles;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImagePath() {
		if (this.id == null || this.image == null)
			return "/img/no-image.gif";
		return Constants.GCP_Base_URI + "/user-photos/" + this.id + "/" + this.image;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Transient
	public String getFullName() {
		return this.firstName + " " + this.firstName;
	}

	public String toString() {
		return "User [id=" + this.id + ", email=" + this.email + ", password=" + this.password + ", firstName="
				+ this.firstName + ", lastName=" + this.lastName + ", enabled=" + this.enabled + "]";
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = this.roles.iterator();
		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName))
				return true;
		}
		return false;
	}
}
