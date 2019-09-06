/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "audit_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuditLog.findAll", query = "SELECT a FROM AuditLog a")
    , @NamedQuery(name = "AuditLog.findById", query = "SELECT a FROM AuditLog a WHERE a.id = :id")
    , @NamedQuery(name = "AuditLog.findByActivityName", query = "SELECT a FROM AuditLog a WHERE a.activityName = :activityName")
    , @NamedQuery(name = "AuditLog.findByUserId", query = "SELECT a FROM AuditLog a WHERE a.userId = :userId")
    , @NamedQuery(name = "AuditLog.findByRegistrationType", query = "SELECT a FROM AuditLog a WHERE a.registrationType = :registrationType")
    , @NamedQuery(name = "AuditLog.findByActivityResult", query = "SELECT a FROM AuditLog a WHERE a.activityResult = :activityResult")
    , @NamedQuery(name = "AuditLog.findByLastUpdatedOn", query = "SELECT a FROM AuditLog a WHERE a.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "AuditLog.findByActivityCategory", query = "SELECT a FROM AuditLog a WHERE a.activityCategory = :activityCategory")})
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "activity_name")
    private String activityName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registration_type")
    private int registrationType;
    @Size(max = 2147483647)
    @Column(name = "activity_result")
    private String activityResult;
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    @Size(max = 2147483647)
    @Column(name = "activity_category")
    private String activityCategory;

    public AuditLog() {
    }

    public AuditLog(Long id) {
        this.id = id;
    }

    public AuditLog(Long id, String activityName, int userId, int registrationType) {
        this.id = id;
        this.activityName = activityName;
        this.userId = userId;
        this.registrationType = registrationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(int registrationType) {
        this.registrationType = registrationType;
    }

    public String getActivityResult() {
        return activityResult;
    }

    public void setActivityResult(String activityResult) {
        this.activityResult = activityResult;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.AuditLog[ id=" + id + " ]";
    }
    
}
