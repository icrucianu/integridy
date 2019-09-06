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
@Table(name = "dso_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DsoNotification.findAll", query = "SELECT d FROM DsoNotification d")
    , @NamedQuery(name = "DsoNotification.findById", query = "SELECT d FROM DsoNotification d WHERE d.id = :id")
    , @NamedQuery(name = "DsoNotification.findByNotifCode", query = "SELECT d FROM DsoNotification d WHERE d.notifCode = :notifCode")
    , @NamedQuery(name = "DsoNotification.findBySeverity", query = "SELECT d FROM DsoNotification d WHERE d.severity = :severity")
    , @NamedQuery(name = "DsoNotification.findByShortDescription", query = "SELECT d FROM DsoNotification d WHERE d.shortDescription = :shortDescription")
    , @NamedQuery(name = "DsoNotification.findByLongDescription", query = "SELECT d FROM DsoNotification d WHERE d.longDescription = :longDescription")
    , @NamedQuery(name = "DsoNotification.findByEmitter", query = "SELECT d FROM DsoNotification d WHERE d.emitter = :emitter")
    , @NamedQuery(name = "DsoNotification.findByCreatedOn", query = "SELECT d FROM DsoNotification d WHERE d.createdOn = :createdOn")})
public class DsoNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "notif_code")
    private String notifCode;
    @Column(name = "severity")
    private Integer severity;
    @Size(max = 2147483647)
    @Column(name = "short_description")
    private String shortDescription;
    @Size(max = 2147483647)
    @Column(name = "long_description")
    private String longDescription;
    @Size(max = 2147483647)
    @Column(name = "emitter")
    private String emitter;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "status")
    private Integer status;

    public DsoNotification() {
    }

    public DsoNotification(Long id) {
        this.id = id;
    }

    public DsoNotification(Long id, String notifCode) {
        this.id = id;
        this.notifCode = notifCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotifCode() {
        return notifCode;
    }

    public void setNotifCode(String notifCode) {
        this.notifCode = notifCode;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
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

    public String getEmitter() {
        return emitter;
    }

    public void setEmitter(String emitter) {
        this.emitter = emitter;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof DsoNotification)) {
            return false;
        }
        DsoNotification other = (DsoNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.DsoNotification[ id=" + id + " ]";
    }
    
}
