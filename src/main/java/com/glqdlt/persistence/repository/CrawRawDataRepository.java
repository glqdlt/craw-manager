package com.glqdlt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glqdlt.persistence.entity.CrawRawDataEntity;

import java.lang.Integer;
import java.util.List;

public interface CrawRawDataRepository extends JpaRepository<CrawRawDataEntity, Integer> {
	List<CrawRawDataEntity> findByCrawNo(Integer CrawNo);
}
