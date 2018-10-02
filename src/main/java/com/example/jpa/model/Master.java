package com.example.jpa.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by rajeevkumarsingh on 21/11/17.
 */
@Entity
@Table(name = "t_address_fail_master")
public class Master extends AuditModel {

    public Master(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue(generator="seqGen")
    // @SequenceGenerator(name="seqGen",sequenceName="seq_t_address_fail_master_id", allocationSize=1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String fileName;

    @Column(name="data", columnDefinition="BLOB NOT NULL", 
    table="t_address_fail_master") 
    @Lob
    private byte[] data;

    @Column(length = 255, nullable = true)
    private String uploadBy;

    @Column(length = 1, nullable = true)
    private String recovery;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUploadBy() {
        return uploadBy;
    }
    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public String getRecovery() {
        return recovery;
    }
    public void setRecovery(String recovery) {
        this.recovery = recovery;
    }

    public Master (String fileName, String uploadBy, byte[] data, String recovery)
    {
        this.fileName = fileName;
        this.uploadBy = uploadBy;
        this.data = data;
        this.recovery = recovery;
    }
}
