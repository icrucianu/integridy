/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "optimum_dso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OptimumDso.findAll", query = "SELECT o FROM OptimumDso o")
    , @NamedQuery(name = "OptimumDso.findById", query = "SELECT o FROM OptimumDso o WHERE o.id = :id")
    , @NamedQuery(name = "OptimumDso.findByActivePow", query = "SELECT o FROM OptimumDso o WHERE o.activePow = :activePow")
    , @NamedQuery(name = "OptimumDso.findByCo2", query = "SELECT o FROM OptimumDso o WHERE o.co2 = :co2")
    , @NamedQuery(name = "OptimumDso.findByConsumedActivePow", query = "SELECT o FROM OptimumDso o WHERE o.consumedActivePow = :consumedActivePow")
    , @NamedQuery(name = "OptimumDso.findByConsumedReactivePow", query = "SELECT o FROM OptimumDso o WHERE o.consumedReactivePow = :consumedReactivePow")
    , @NamedQuery(name = "OptimumDso.findByCreatedTime", query = "SELECT o FROM OptimumDso o WHERE o.createdTime = :createdTime")
    , @NamedQuery(name = "OptimumDso.findByDayCost", query = "SELECT o FROM OptimumDso o WHERE o.dayCost = :dayCost")
    , @NamedQuery(name = "OptimumDso.findByDeviceNumber", query = "SELECT o FROM OptimumDso o WHERE o.deviceNumber = :deviceNumber")
    , @NamedQuery(name = "OptimumDso.findByDownTime", query = "SELECT o FROM OptimumDso o WHERE o.downTime = :downTime")
    , @NamedQuery(name = "OptimumDso.findByEnergyMismatch", query = "SELECT o FROM OptimumDso o WHERE o.energyMismatch = :energyMismatch")
    , @NamedQuery(name = "OptimumDso.findByEnergyMismatchRatio", query = "SELECT o FROM OptimumDso o WHERE o.energyMismatchRatio = :energyMismatchRatio")
    , @NamedQuery(name = "OptimumDso.findByNightCost", query = "SELECT o FROM OptimumDso o WHERE o.nightCost = :nightCost")
    , @NamedQuery(name = "OptimumDso.findBySaidi", query = "SELECT o FROM OptimumDso o WHERE o.saidi = :saidi")
    , @NamedQuery(name = "OptimumDso.findByPhaseVoltage", query = "SELECT o FROM OptimumDso o WHERE o.phaseVoltage = :phaseVoltage")
    , @NamedQuery(name = "OptimumDso.findByReactivePow", query = "SELECT o FROM OptimumDso o WHERE o.reactivePow = :reactivePow")
    , @NamedQuery(name = "OptimumDso.findBySendInterval", query = "SELECT o FROM OptimumDso o WHERE o.sendInterval = :sendInterval")
    , @NamedQuery(name = "OptimumDso.findByThdd", query = "SELECT o FROM OptimumDso o WHERE o.thdd = :thdd")
    , @NamedQuery(name = "OptimumDso.findByUpTime", query = "SELECT o FROM OptimumDso o WHERE o.upTime = :upTime")
    , @NamedQuery(name = "OptimumDso.findByVoltageDeviation", query = "SELECT o FROM OptimumDso o WHERE o.voltageDeviation = :voltageDeviation")
    , @NamedQuery(name = "OptimumDso.findByIdx12hours", query = "SELECT o FROM OptimumDso o WHERE o.idx12hours = :idx12hours")
    , @NamedQuery(name = "OptimumDso.findByIdx15minutes", query = "SELECT o FROM OptimumDso o WHERE o.idx15minutes = :idx15minutes")
    , @NamedQuery(name = "OptimumDso.findByIdx1day", query = "SELECT o FROM OptimumDso o WHERE o.idx1day = :idx1day")
    , @NamedQuery(name = "OptimumDso.findByIdx1hour", query = "SELECT o FROM OptimumDso o WHERE o.idx1hour = :idx1hour")
    , @NamedQuery(name = "OptimumDso.findByIdx1month", query = "SELECT o FROM OptimumDso o WHERE o.idx1month = :idx1month")
    , @NamedQuery(name = "OptimumDso.findByIdx1week", query = "SELECT o FROM OptimumDso o WHERE o.idx1week = :idx1week")
    , @NamedQuery(name = "OptimumDso.findByIdx1year", query = "SELECT o FROM OptimumDso o WHERE o.idx1year = :idx1year")
    , @NamedQuery(name = "OptimumDso.findByIdx3month", query = "SELECT o FROM OptimumDso o WHERE o.idx3month = :idx3month")
    , @NamedQuery(name = "OptimumDso.findByIdx6hours", query = "SELECT o FROM OptimumDso o WHERE o.idx6hours = :idx6hours")
    , @NamedQuery(name = "OptimumDso.findByIdx6month", query = "SELECT o FROM OptimumDso o WHERE o.idx6month = :idx6month")})
public class OptimumDso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "active_pow")
    private Double activePow;
    @Column(name = "co2")
    private Double co2;
    @Column(name = "consumed_active_pow")
    private Double consumedActivePow;
    @Column(name = "consumed_reactive_pow")
    private Double consumedReactivePow;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "day_cost")
    private Double dayCost;
    @Column(name = "device_number")
    private Integer deviceNumber;
    @Column(name = "down_time")
    private BigInteger downTime;
    @Column(name = "energy_mismatch")
    private Double energyMismatch;
    @Column(name = "energy_mismatch_ratio")
    private Double energyMismatchRatio;
    @Column(name = "night_cost")
    private Double nightCost;
    @Column(name = "saidi")
    private Double saidi;
    @Column(name = "phase_voltage")
    private Double phaseVoltage;
    @Column(name = "reactive_pow")
    private Double reactivePow;
    @Column(name = "send_interval")
    private BigInteger sendInterval;
    @Column(name = "thdd")
    private Double thdd;
    @Column(name = "up_time")
    private BigInteger upTime;
    @Column(name = "voltage_deviation")
    private Double voltageDeviation;
    @Column(name = "idx12hours")
    private BigInteger idx12hours;
    @Column(name = "idx15minutes")
    private BigInteger idx15minutes;
    @Column(name = "idx1day")
    private BigInteger idx1day;
    @Column(name = "idx1hour")
    private BigInteger idx1hour;
    @Column(name = "idx1month")
    private BigInteger idx1month;
    @Column(name = "idx1week")
    private BigInteger idx1week;
    @Column(name = "idx1year")
    private BigInteger idx1year;
    @Column(name = "idx3month")
    private BigInteger idx3month;
    @Column(name = "idx6hours")
    private BigInteger idx6hours;
    @Column(name = "idx6month")
    private BigInteger idx6month;

    public OptimumDso() {
    }

    public OptimumDso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getActivePow() {
        return activePow;
    }

    public void setActivePow(Double activePow) {
        this.activePow = activePow;
    }

    public Double getCo2() {
        return co2;
    }

    public void setCo2(Double co2) {
        this.co2 = co2;
    }

    public Double getConsumedActivePow() {
        return consumedActivePow;
    }

    public void setConsumedActivePow(Double consumedActivePow) {
        this.consumedActivePow = consumedActivePow;
    }

    public Double getConsumedReactivePow() {
        return consumedReactivePow;
    }

    public void setConsumedReactivePow(Double consumedReactivePow) {
        this.consumedReactivePow = consumedReactivePow;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Double getDayCost() {
        return dayCost;
    }

    public void setDayCost(Double dayCost) {
        this.dayCost = dayCost;
    }

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Integer deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public BigInteger getDownTime() {
        return downTime;
    }

    public void setDownTime(BigInteger downTime) {
        this.downTime = downTime;
    }

    public Double getEnergyMismatch() {
        return energyMismatch;
    }

    public void setEnergyMismatch(Double energyMismatch) {
        this.energyMismatch = energyMismatch;
    }

    public Double getEnergyMismatchRatio() {
        return energyMismatchRatio;
    }

    public void setEnergyMismatchRatio(Double energyMismatchRatio) {
        this.energyMismatchRatio = energyMismatchRatio;
    }

    public Double getNightCost() {
        return nightCost;
    }

    public void setNightCost(Double nightCost) {
        this.nightCost = nightCost;
    }

    public Double getSaidi() {
        return saidi;
    }

    public void setSaidi(Double saidi) {
        this.saidi = saidi;
    }

    public Double getPhaseVoltage() {
        return phaseVoltage;
    }

    public void setPhaseVoltage(Double phaseVoltage) {
        this.phaseVoltage = phaseVoltage;
    }

    public Double getReactivePow() {
        return reactivePow;
    }

    public void setReactivePow(Double reactivePow) {
        this.reactivePow = reactivePow;
    }

    public BigInteger getSendInterval() {
        return sendInterval;
    }

    public void setSendInterval(BigInteger sendInterval) {
        this.sendInterval = sendInterval;
    }

    public Double getThdd() {
        return thdd;
    }

    public void setThdd(Double thdd) {
        this.thdd = thdd;
    }

    public BigInteger getUpTime() {
        return upTime;
    }

    public void setUpTime(BigInteger upTime) {
        this.upTime = upTime;
    }

    public Double getVoltageDeviation() {
        return voltageDeviation;
    }

    public void setVoltageDeviation(Double voltageDeviation) {
        this.voltageDeviation = voltageDeviation;
    }

    public BigInteger getIdx12hours() {
        return idx12hours;
    }

    public void setIdx12hours(BigInteger idx12hours) {
        this.idx12hours = idx12hours;
    }

    public BigInteger getIdx15minutes() {
        return idx15minutes;
    }

    public void setIdx15minutes(BigInteger idx15minutes) {
        this.idx15minutes = idx15minutes;
    }

    public BigInteger getIdx1day() {
        return idx1day;
    }

    public void setIdx1day(BigInteger idx1day) {
        this.idx1day = idx1day;
    }

    public BigInteger getIdx1hour() {
        return idx1hour;
    }

    public void setIdx1hour(BigInteger idx1hour) {
        this.idx1hour = idx1hour;
    }

    public BigInteger getIdx1month() {
        return idx1month;
    }

    public void setIdx1month(BigInteger idx1month) {
        this.idx1month = idx1month;
    }

    public BigInteger getIdx1week() {
        return idx1week;
    }

    public void setIdx1week(BigInteger idx1week) {
        this.idx1week = idx1week;
    }

    public BigInteger getIdx1year() {
        return idx1year;
    }

    public void setIdx1year(BigInteger idx1year) {
        this.idx1year = idx1year;
    }

    public BigInteger getIdx3month() {
        return idx3month;
    }

    public void setIdx3month(BigInteger idx3month) {
        this.idx3month = idx3month;
    }

    public BigInteger getIdx6hours() {
        return idx6hours;
    }

    public void setIdx6hours(BigInteger idx6hours) {
        this.idx6hours = idx6hours;
    }

    public BigInteger getIdx6month() {
        return idx6month;
    }

    public void setIdx6month(BigInteger idx6month) {
        this.idx6month = idx6month;
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
        if (!(object instanceof OptimumDso)) {
            return false;
        }
        OptimumDso other = (OptimumDso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.OptimumDso[ id=" + id + " ]";
    }
    
}
