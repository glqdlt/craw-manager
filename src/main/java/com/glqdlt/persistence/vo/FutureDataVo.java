package com.glqdlt.persistence.vo;

import java.util.List;
import java.util.concurrent.Future;

import com.glqdlt.persistence.entity.CrawRawDataEntity;
import com.glqdlt.persistence.entity.CrawDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FutureDataVo {

	private Future<List<CrawRawDataEntity>> future; 
	private CrawDomainEntity crawDomainEntity;

}
