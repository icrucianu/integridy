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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "smart_meter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SmartMeter.findAll", query = "SELECT s FROM SmartMeter s")
    , @NamedQuery(name = "SmartMeter.findById", query = "SELECT s FROM SmartMeter s WHERE s.id = :id")
    , @NamedQuery(name = "SmartMeter.findByDeviceName", query = "SELECT s FROM SmartMeter s WHERE s.deviceName = :deviceName")
    , @NamedQuery(name = "SmartMeter.findBySerialNo", query = "SELECT s FROM SmartMeter s WHERE s.serialNo = :serialNo")
    , @NamedQuery(name = "SmartMeter.findByDeviceType", query = "SELECT s FROM SmartMeter s WHERE s.deviceType = :deviceType")
    , @NamedQuery(name = "SmartMeter.findByValidityFrom", query = "SELECT s FROM SmartMeter s WHERE s.validityFrom = :validityFrom")
    , @NamedQuery(name = "SmartMeter.findByValidityTo", query = "SELECT s FROM SmartMeter s WHERE s.validityTo = :validityTo")
    , @NamedQuery(name = "SmartMeter.findByConsumptionPointId", query = "SELECT s FROM SmartMeter s WHERE s.consumptionPointId.id = :consumptionPointId")
    , @NamedQuery(name = "SmartMeter.findByConsumerId", query = "SELECT s FROM SmartMeter s WHERE s.consumptionPointId.clientId.id = :consumerId")})
public class SmartMeter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "device_name")
    private String deviceName;
    @Size(max = 2147483647)
    @Column(name = "serial_no")
    private String serialNo;
    @Size(max = 2147483647)
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "validity_from")
    @Temporal(TemporalType.DATE)
    private Date validityFrom;
    @Column(name = "validity_to")
    @Temporal(TemporalType.DATE)
    private Date validityTo;
    @JoinColumn(name = "consumption_point_id", referencedColumnName = "id")
    @ManyToOne
    private ConsumptionPoint consumptionPointId;

    public SmartMeter() {
    }

    public SmartMeter(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getValidityFrom() {
        return validityFrom;
    }

    public void setValidityFrom(Date validityFrom) {
        this.validityFrom = validityFrom;
    }

    public Date getValidityTo() {
        return validityTo;
    }

    public void setValidityTo(Date validityTo) {
        this.validityTo = validityTo;
    }

    public ConsumptionPoint getConsumptionPointId() {
        return consumptionPointId;
    }

    public void setConsumptionPointId(ConsumptionPoint consumptionPointId) {
        this.consumptionPointId = consumptionPointId;
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
        if (!(object instanceof SmartMeter)) {
            return false;
        }
        SmartMeter other = (SmartMeter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.SmartMeter[ id=" + id + " ]";
    }
    
}
