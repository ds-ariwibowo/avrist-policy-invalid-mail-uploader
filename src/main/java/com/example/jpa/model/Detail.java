package com.example.jpa.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Created by rajeevkumarsingh on 21/11/17.
 */
@Entity
@Table(name = "t_address_fail_detail")
public class Detail extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(generator="seqGenDetail")
    // @SequenceGenerator(name="seqGenDetail",sequenceName="seq_t_address_fail_detail_id", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "master_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("master_id")
    private Master master;

    @NotNull
    @Column(length = 30, nullable = false)
    private String policyCode;    
 
    @Column(length = 255, nullable = false)
    private String letter;
    
    @Column(nullable = false, columnDefinition="int default 0") // private BigDecimal counter;
    private Long counter;
  
    @Column(length = 255, nullable = true)
    private String reason;
       
    @Column(length = 32, nullable = true)
    private String effectiveDate;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
   
    public Master getMaster() {
        return master;
    }
    public void setMaster(Master master) {
        this.master = master;
    }

    public String getPolicyCode() {
        return policyCode;
    }
    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getLetter() {
        return letter;
    }
    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Long getCounter() {
        return counter;
    }
    public void setCounter(Long counter) {
        this.counter = counter;
    }  

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
