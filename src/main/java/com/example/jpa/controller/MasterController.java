package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.exception.ResourceException;
import com.example.jpa.model.Master;
import com.example.jpa.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MasterController {

    @Autowired
    MasterRepository masterRepository;

    // @GetMapping("/masters")
    // public Page<Master> getAllMasters(Pageable pageable) {
    //     return masterRepository.findAll(pageable);
    // }

    @PostMapping("/masters")
    public Master createPost(@Valid @RequestBody Master master) {
        return masterRepository.save(master);
    }

    @DeleteMapping("/masters/{mstId}")
    public ResponseEntity<Master> deleteMaster(@PathVariable Long mstId) {
        return masterRepository.findById(mstId).map(master -> {
            masterRepository.delete(master);
            return ResponseEntity.ok(master);
        }).orElseThrow(() -> new ResourceNotFoundException("upload Id " + mstId + " not found"));
    }
    // public ResponseEntity<?> deleteMaster(@PathVariable Long mstId) {
    //     return masterRepository.findById(mstId).map(master -> {
    //         masterRepository.delete(master);
    //         return ResponseEntity.ok().build();
    //     }).orElseThrow(() -> new ResourceNotFoundException("upload Id " + mstId + " not found"));
    // }

    
    // @PutMapping("/masters/{mstId}")
    // public Master updateMaster(@PathVariable Long mstId, @Valid @RequestBody Master mRequest) {
    //     return masterRepository.findById(mstId).map(master -> {
    //         master.setFileName(mRequest.getFileName());
    //         master.setData(mRequest.getData());
    //         master.setUploadBy(mRequest.getUploadBy());
    //         return masterRepository.save(master);
    //     }).orElseThrow(() -> new ResourceNotFoundException("upload Id " + mstId + " not found"));
    // }

  

}
