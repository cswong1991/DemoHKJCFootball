package com.cswongwd.hkjcfootball.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/active-match")
@CrossOrigin(origins = "https://hkjcfootball.cswongwd.com")
public class ActiveMatchController {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@GetMapping("/")
	public List<JsonObject> getIndex() {
		return stringRedisTemplate.opsForList().range("active_matches", 0, -1).stream()
				.map(e1 -> stringRedisTemplate.opsForHash().entries(e1.toString()))
				.map(e1 -> JsonParser.parseString(e1.get("matchInfo").toString()).getAsJsonObject())
				.collect(Collectors.toList());
	}

	@GetMapping("/{match_id}")
	public Map<String, Object> getMatchDetail(@PathVariable(value = "match_id") String match_id) {
		Map<Object, Object> match_data = stringRedisTemplate.opsForHash().entries(match_id);
		if (match_data.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		JsonObject match_info = JsonParser.parseString(match_data.get("matchInfo").toString()).getAsJsonObject();
		List<JsonObject> odds_data = match_data.keySet().stream().filter(e1 -> e1.toString().contains("oddsData"))
				.map(e1 -> JsonParser.parseString(match_data.get(e1).toString()).getAsJsonObject())
				.collect(Collectors.toList());
		odds_data.sort((JsonObject a, JsonObject b) -> a.get("id").getAsInt() - b.get("id").getAsInt());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("matchInfo", match_info);
		result.put("oddsData", odds_data);
		return result;
	}

	@GetMapping("/{match_id}/{after_oddsdataID}")
	public Map<String, Object> getOddsData(@PathVariable(value = "match_id") String match_id,
			@PathVariable(value = "after_oddsdataID") String after_oddsdataID) {
		Integer afterID_int = Integer.parseInt(after_oddsdataID);
		Map<Object, Object> match_data = stringRedisTemplate.opsForHash().entries(match_id);
		if (match_data.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		List<JsonObject> odds_data = match_data.keySet().stream()
				.filter(e1 -> e1.toString().contains("oddsData")
						&& Integer.parseInt(e1.toString().replace("oddsData", "")) > afterID_int)
				.map(e1 -> JsonParser.parseString(match_data.get(e1).toString()).getAsJsonObject())
				.collect(Collectors.toList());
		odds_data.sort((JsonObject a, JsonObject b) -> a.get("id").getAsInt() - b.get("id").getAsInt());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("oddsData", odds_data);
		return result;
	}
}
