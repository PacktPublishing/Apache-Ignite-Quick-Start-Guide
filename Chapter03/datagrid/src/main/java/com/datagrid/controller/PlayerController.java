package com.datagrid.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.datagrid.entity.Player;
import com.datagrid.repository.PlayerRepository;

@RestController
@RequestMapping("/rest")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;

	@GetMapping("/players")
	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	@PostMapping("/player")
	public Player createPlayer(@Valid @RequestBody Player Player) {
		return playerRepository.save(Player);
	}

	@GetMapping("/players/{id}")
	public Player getPlayerById(@PathVariable(value = "id") Integer id) {
		return playerRepository.findById(id).orElse(null);
	}

	@GetMapping("/players/name/{name}")
	public List<Player> getPlayerLikeName(@PathVariable(value = "name") String name) {
		return playerRepository.findByName(name);
	}
	
	@PutMapping("/players/{id}")
	public Player updatePlayer(@PathVariable(value = "id") Integer playerId, @Valid @RequestBody Player details) {

		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new RuntimeException("Player" + playerId + "  not found"));

		player.setClub(details.getClub());
		player.setName(details.getName());
		player.setWages(details.getWages());

		Player updatedPlayer = playerRepository.save(player);
		return updatedPlayer;
	}

	@DeleteMapping("/players/{id}")
	public ResponseEntity<?> deletePlayer(@PathVariable(value = "id") Integer id) {
		Player Player = playerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Player:" + id + "  not found"));

		playerRepository.delete(Player);

		return ResponseEntity.ok().build();
	}
}
