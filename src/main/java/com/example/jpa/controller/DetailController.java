package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Detail;
import com.example.jpa.repository.DetailRepository;
import com.example.jpa.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DetailController {

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private MasterRepository masterRepository;

    @GetMapping("/masters/{mstId}/details")
    public Page<Detail> getAllDetailsByMasterId(@PathVariable (value = "mstId") Long mstId,
                                                Pageable pageable) {
        return detailRepository.findByMasterId(mstId, pageable);
    }


    @PostMapping("/masters/{mstId}/details/{policyCode}")
    public Detail createDetail(@PathVariable (value = "mstId") Long mstId,
                                @PathVariable (value = "policyCode") String policyCode,
                                 @Valid @RequestBody Detail detail) {
        return masterRepository.findById(mstId).map(master -> {
            detail.setMaster(master);
            Long counter = detailRepository.countByPolicy(policyCode) + detail.getCounter();
            detail.setCounter(counter);
            return detailRepository.save(detail);
        }).orElseThrow(() -> new ResourceNotFoundException("Upload id " + mstId + " not found"));
    }

    @DeleteMapping("/updateAddress/{mstId}")
    public ResponseEntity<?> updateAddress(@PathVariable (value = "mstId") Long mstId) {
        if(!masterRepository.existsById(mstId)) {
            throw new ResourceNotFoundException("Upload Id " + mstId + " not found");
        }
        
        // List<Detail> details = detailRepository.findAllMaxCounterByMasterId(mstId);
        // details.forEach((detail)->{
        //     detailRepository.updateMailFailedTimes(detail.getCounter(), detail.getPolicyCode());
        //     detailRepository.updateInvalidAddr(detail.getPolicyCode()); 
        //   });
              
        detailRepository.updateMailFailedTimes(Long.valueOf(7), "102030");
        detailRepository.updateInvalidAddr("102030"); 
        return ResponseEntity.ok().build();   
    }


    // @PutMapping("/masters/{mstId}/details/{dtlId}")
    // public PDetail updateDetail(@PathVariable (value = "mstId") Long mstId,
    //                              @PathVariable (value = "dtlId") Long dtlId,
    //                              @Valid @RequestBody Detail dRequest) {
    //     if(!pmasterRepository.existsById(mstId)) {
    //         throw new ResourceNotFoundException("Upload Id " + mstId + " not found");
    //     }
    //     return detailRepository.findById(dtlId).map(detail -> {
    //         detail.setPolicyCode(dRequest.getPolicyCode());
    //         detail.setLetter(dRequest.getLetter());
    //         detail.setReason(dRequest.getReason());    
    //         detail.setEffDate(dRequest.getEffDate()); 
    //         detail.setCounter(dRequest.getCounter());  
    //         return detailRepository.save(detail);
    //     }).orElseThrow(() -> new ResourceNotFoundException("Detail Id " + dtlId + "not found"));
    // }

    // @DeleteMapping("/masters/{mstId}/details/{dtlId}")
    // public ResponseEntity<?> deleteComment(@PathVariable (value = "mstId") Long mstId,
    //                           @PathVariable (value = "dtlId") Long dtlId) {
    //     if(!masterRepository.existsById(mstId)) {
    //         throw new ResourceNotFoundException("Upload Id " + mstId + " not found");
    //     }
    //     return detailRepository.findById(dtlId).map(detail -> {
    //         detailRepository.delete(detail);
    //          return ResponseEntity.ok().build();
    //     }).orElseThrow(() -> new ResourceNotFoundException("Detail Id " + dtlId + " not found"));
    // }
}
