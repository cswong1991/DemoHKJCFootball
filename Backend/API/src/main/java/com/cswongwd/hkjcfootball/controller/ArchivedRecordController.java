package com.cswongwd.hkjcfootball.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cswongwd.hkjcfootball.model.ArchivedRecordModel;
import com.cswongwd.hkjcfootball.repository.ArchivedRecordRepository;

@RestController
@RequestMapping("/archived-record")
public class ArchivedRecordController {
	@Autowired
	ArchivedRecordRepository archivedRecordRepository;

	@GetMapping("/{match_id}")
	public ArchivedRecordModel getArchivedRecord(@PathVariable(value = "match_id") String match_id) {
		return archivedRecordRepository.findOneByMatchID(match_id);
	}
}
