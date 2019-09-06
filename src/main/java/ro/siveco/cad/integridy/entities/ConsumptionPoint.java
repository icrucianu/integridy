/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "consumption_point")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumptionPoint.findAll", query = "SELECT c FROM ConsumptionPoint c")
    , @NamedQuery(name = "ConsumptionPoint.findById", query = "SELECT c FROM ConsumptionPoint c WHERE c.id = :id")
    , @NamedQuery(name = "ConsumptionPoint.findByPointName", query = "SELECT c FROM ConsumptionPoint c WHERE c.pointName = :pointName")
    , @NamedQuery(name = "ConsumptionPoint.findByPointType", query = "SELECT c FROM ConsumptionPoint c WHERE c.pointType = :pointType")
    , @NamedQuery(name = "ConsumptionPoint.findByAddress", query = "SELECT c FROM ConsumptionPoint c WHERE c.address = :address")
    , @NamedQuery(name = "ConsumptionPoint.findByValidityStart", query = "SELECT c FROM ConsumptionPoint c WHERE c.validityStart = :validityStart")
    , @NamedQuery(name = "ConsumptionPoint.findByValidityEnd", query = "SELECT c FROM ConsumptionPoint c WHERE c.validityEnd = :validityEnd")
    , @NamedQuery(name = "ConsumptionPoint.findByClientId", query = "SELECT c FROM ConsumptionPoint c WHERE c.clientId.id = :clientId")})
public class ConsumptionPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "point_name")
    private String pointName;
    @Size(max = 2147483647)
    @Column(name = "point_type")
    private String pointType;
    @Size(max = 2147483647)
    @Column(name = "address")
    private String address;
    @Column(name = "validity_start")
    @Temporal(TemporalType.DATE)
    private Date validityStart;
    @Column(name = "validity_end")
    @Temporal(TemporalType.DATE)
    private Date validityEnd;
    @OneToMany(mappedBy = "consumptionPointId")
    private Collection<SmartMeter> smartMeterCollection;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private ConsumerClient clientId;

    public ConsumptionPoint() {
    }

    public ConsumptionPoint(Integer id) {
        this.id = id;
    }

    public ConsumptionPoint(Integer id, String pointName) {
        this.id = id;
        this.pointName = pointName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }

    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }

    @XmlTransient
    public Collection<SmartMeter> getSmartMeterCollection() {
        return smartMeterCollection;
    }

    public void setSmartMeterCollection(Collection<SmartMeter> smartMeterCollection) {
        this.smartMeterCollection = smartMeterCollection;
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
        if (!(object instanceof ConsumptionPoint)) {
            return false;
        }
        ConsumptionPoint other = (ConsumptionPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ConsumptionPoint[ id=" + id + " ]";
    }
    
}
