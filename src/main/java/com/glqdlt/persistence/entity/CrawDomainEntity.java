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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "craw_domain")
@Entity
public class CrawDomainEntity {

	@Id
	@GeneratedValue
	private int crawNo;
	private String url;
	private String dataName;
	private Integer dataTag;
	private String siteName;
	private Integer siteTag;
	private Integer crawType;
	private Integer lastBoardNo;
}
