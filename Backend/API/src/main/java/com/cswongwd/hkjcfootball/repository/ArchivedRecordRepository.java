package com.cswongwd.hkjcfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cswongwd.hkjcfootball.model.ArchivedRecordModel;

public interface ArchivedRecordRepository extends JpaRepository<ArchivedRecordModel, Integer> {

	ArchivedRecordModel findOneByMatchID(String match_id);
}
