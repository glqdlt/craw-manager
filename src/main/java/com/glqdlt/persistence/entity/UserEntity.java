package com.glqdlt.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue
	private Integer no;

	private String id;
	private String name;
	private String n_name;
	private String email;
	private Integer role;
	private boolean listen_mail;
	private String password;

}
