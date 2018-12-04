package com.datagrid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datagrid.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer>{

	List<Player> findByName(String name);
}
