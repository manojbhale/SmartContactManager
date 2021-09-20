package com.bns.entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USER_TAB")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends AbstractTimestampEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@NotBlank(message = "First Name Shouldn't Be Empty")
	@Size(min = 3, max = 20, message = "First Name should be 3 to 20 characters")
	@Pattern(regexp = "^[A-Za-z]+$",message = "please insert only alphabet letters")
	private String firstName;

	@NotBlank(message = "Last Name Shouldn't Be Empty")
	@Size(min = 3, max = 20, message = "Last Name should be 3 to 20 characters")
	@Pattern(regexp = "^[A-Za-z]+$",message = "please insert only alphabet letters")
	private String lastName;

	@NotBlank(message = "Email Shouldn't Be Empty")
	//@Email
	@Pattern(regexp="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email id is invalid")
	@Column(unique = true, nullable = false)
	private String email;

	private String password;

	private String role;

	private Boolean enabled;

	private String imageUrl;

	@NotBlank(message = "Mobile Number Shouldn't Be Empty")
	@Size(min = 10, max = 10, message = "Mobile Number should be 10 digit")
	@Pattern(regexp="(^$|[0-9]{10})",message = "please insert only numbers")
	private String mobileNo;

	@Size(min = 10, max = 10, message = "Mobile Number should be 10 digit")
	@Pattern(regexp="(^$|[0-9]{10})",message = "please insert only numbers")
	private String alternateMobileNo;

	@Column(length = 500)
	@Size(min = 10, max = 500, message = "Write About Atleast 10 to 500 characters")
	private String about;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user",orphanRemoval = true)
	private List<Contact> constacts = new ArrayList<Contact>();

}
