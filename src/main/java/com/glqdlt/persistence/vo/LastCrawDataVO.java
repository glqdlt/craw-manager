package com.glqdlt.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class LastCrawDataVO {

	private int siteTag;
	private int dataTag;
	private int lastCrawNo;
	private String lastCrawHash;
	private String url;
	private String value;

}
