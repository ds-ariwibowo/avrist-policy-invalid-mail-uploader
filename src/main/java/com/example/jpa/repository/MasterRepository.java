package com.example.jpa.repository;

import com.example.jpa.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {
    @Query(value = "SELECT * FROM t_address_fail_master t WHERE t.file_name=:filename ",
    nativeQuery = true)    
    Optional<Master> findByFile(@Param("filename") String filename);  

}
