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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "consumer_recommendation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumerRecommendation.findAll", query = "SELECT c FROM ConsumerRecommendation c")
    , @NamedQuery(name = "ConsumerRecommendation.findById", query = "SELECT c FROM ConsumerRecommendation c WHERE c.id = :id")
    , @NamedQuery(name = "ConsumerRecommendation.findByNotifCode", query = "SELECT c FROM ConsumerRecommendation c WHERE c.notifCode = :notifCode")
    , @NamedQuery(name = "ConsumerRecommendation.findBySeverity", query = "SELECT c FROM ConsumerRecommendation c WHERE c.severity = :severity")
    , @NamedQuery(name = "ConsumerRecommendation.findByShortDescription", query = "SELECT c FROM ConsumerRecommendation c WHERE c.shortDescription = :shortDescription")
    , @NamedQuery(name = "ConsumerRecommendation.findByLongDescription", query = "SELECT c FROM ConsumerRecommendation c WHERE c.longDescription = :longDescription")
    , @NamedQuery(name = "ConsumerRecommendation.findByEmitter", query = "SELECT c FROM ConsumerRecommendation c WHERE c.emitter = :emitter")
    , @NamedQuery(name = "ConsumerRecommendation.findByCreatedOn", query = "SELECT c FROM ConsumerRecommendation c WHERE c.createdOn = :createdOn")
    , @NamedQuery(name = "ConsumerRecommendation.findByStatus", query = "SELECT c FROM ConsumerRecommendation c WHERE c.status = :status")
    , @NamedQuery(name = "ConsumerRecommendation.findByReduceIntervalStart", query = "SELECT c FROM ConsumerRecommendation c WHERE c.reduceIntervalStart = :reduceIntervalStart")
    , @NamedQuery(name = "ConsumerRecommendation.findByReduceIntervalStop", query = "SELECT c FROM ConsumerRecommendation c WHERE c.reduceIntervalStop = :reduceIntervalStop")
    , @NamedQuery(name = "ConsumerRecommendation.findByQuantity", query = "SELECT c FROM ConsumerRecommendation c WHERE c.quantity = :quantity")
    , @NamedQuery(name = "ConsumerRecommendation.findByConfirmedDate", query = "SELECT c FROM ConsumerRecommendation c WHERE c.confirmedDate = :confirmedDate")
    , @NamedQuery(name = "ConsumerRecommendation.findByConfirmationStatus", query = "SELECT c FROM ConsumerRecommendation c WHERE c.confirmationStatus = :confirmationStatus")
    , @NamedQuery(name = "ConsumerRecommendation.findByClientId", query = "SELECT c FROM ConsumerRecommendation c WHERE c.clientId.id = :clientId")})
public class ConsumerRecommendation implements Serializable {

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
    @Size(max = 2147483647)
    @Column(name = "reduce_interval_start")
    private String reduceIntervalStart;
    @Size(max = 2147483647)
    @Column(name = "reduce_interval_stop")
    private String reduceIntervalStop;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private Double quantity;
    @Column(name = "confirmed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmedDate;
    @Size(max = 2147483647)
    @Column(name = "confirmation_status")
    private String confirmationStatus;
    @Column(name = "reduce_day_of_week")
    private Integer reduceDayOfWeek;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private ConsumerClient clientId;
    
    public ConsumerRecommendation() {
    }

    public ConsumerRecommendation(Long id) {
        this.id = id;
    }

    public ConsumerRecommendation(Long id, String notifCode) {
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

    public String getReduceIntervalStart() {
        return reduceIntervalStart;
    }

    public void setReduceIntervalStart(String reduceIntervalStart) {
        this.reduceIntervalStart = reduceIntervalStart;
    }

    public String getReduceIntervalStop() {
        return reduceIntervalStop;
    }

    public void setReduceIntervalStop(String reduceIntervalStop) {
        this.reduceIntervalStop = reduceIntervalStop;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public Integer getReduceDayOfWeek() {
        return reduceDayOfWeek;
    }

    public void setReduceDayOfWeek(Integer reduceDayOfWeek) {
        this.reduceDayOfWeek = reduceDayOfWeek;
    }
    
    

    public ConsumerClient getClientId() {
        return clientId;
    }

    public void setClientId(ConsumerClient clientId) {
        this.clientId = clientId;
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
        if (!(object instanceof ConsumerRecommendation)) {
            return false;
        }
        ConsumerRecommendation other = (ConsumerRecommendation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ConsumerRecommendation[ id=" + id + " ]";
    }
    
}
