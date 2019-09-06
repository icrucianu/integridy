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
@Table(name = "error_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ErrorLog.findAll", query = "SELECT e FROM ErrorLog e")
    , @NamedQuery(name = "ErrorLog.findById", query = "SELECT e FROM ErrorLog e WHERE e.id = :id")
    , @NamedQuery(name = "ErrorLog.findByErrCode", query = "SELECT e FROM ErrorLog e WHERE e.errCode = :errCode")
    , @NamedQuery(name = "ErrorLog.findBySeverity", query = "SELECT e FROM ErrorLog e WHERE e.severity = :severity")
    , @NamedQuery(name = "ErrorLog.findByShortDescription", query = "SELECT e FROM ErrorLog e WHERE e.shortDescription = :shortDescription")
    , @NamedQuery(name = "ErrorLog.findByLongDescription", query = "SELECT e FROM ErrorLog e WHERE e.longDescription = :longDescription")
    , @NamedQuery(name = "ErrorLog.findByLastUpdatedOn", query = "SELECT e FROM ErrorLog e WHERE e.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "ErrorLog.findByUserId", query = "SELECT e FROM ErrorLog e WHERE e.userId = :userId")})
public class ErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "err_code")
    private int errCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "severity")
    private int severity;
    @Size(max = 2147483647)
    @Column(name = "short_description")
    private String shortDescription;
    @Size(max = 2147483647)
    @Column(name = "long_description")
    private String longDescription;
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    @Column(name = "user_id")
    private Integer userId;

    public ErrorLog() {
    }

    public ErrorLog(Long id) {
        this.id = id;
    }

    public ErrorLog(Long id, int errCode, int severity) {
        this.id = id;
        this.errCode = errCode;
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        if (!(object instanceof ErrorLog)) {
            return false;
        }
        ErrorLog other = (ErrorLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ErrorLog[ id=" + id + " ]";
    }
    
}
