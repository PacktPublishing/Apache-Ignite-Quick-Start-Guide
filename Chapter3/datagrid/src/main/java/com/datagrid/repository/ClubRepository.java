package com.datagrid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datagrid.entity.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer>{

}
