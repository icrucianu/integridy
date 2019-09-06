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
@Table(name = "forecast_dso_w")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ForecastDsoW.findAll", query = "SELECT f FROM ForecastDsoW f")
    , @NamedQuery(name = "ForecastDsoW.findById", query = "SELECT f FROM ForecastDsoW f WHERE f.id = :id")
    , @NamedQuery(name = "ForecastDsoW.findByActivePow", query = "SELECT f FROM ForecastDsoW f WHERE f.activePow = :activePow")
    , @NamedQuery(name = "ForecastDsoW.findByCo2", query = "SELECT f FROM ForecastDsoW f WHERE f.co2 = :co2")
    , @NamedQuery(name = "ForecastDsoW.findByConsumedActivePow", query = "SELECT f FROM ForecastDsoW f WHERE f.consumedActivePow = :consumedActivePow")
    , @NamedQuery(name = "ForecastDsoW.findByConsumedReactivePow", query = "SELECT f FROM ForecastDsoW f WHERE f.consumedReactivePow = :consumedReactivePow")
    , @NamedQuery(name = "ForecastDsoW.findByCreatedTime", query = "SELECT f FROM ForecastDsoW f WHERE f.createdTime = :createdTime")
    , @NamedQuery(name = "ForecastDsoW.findByDayCost", query = "SELECT f FROM ForecastDsoW f WHERE f.dayCost = :dayCost")
    , @NamedQuery(name = "ForecastDsoW.findByDownTime", query = "SELECT f FROM ForecastDsoW f WHERE f.downTime = :downTime")
    , @NamedQuery(name = "ForecastDsoW.findByEnergyMismatch", query = "SELECT f FROM ForecastDsoW f WHERE f.energyMismatch = :energyMismatch")
    , @NamedQuery(name = "ForecastDsoW.findByEnergyMismatchRatio", query = "SELECT f FROM ForecastDsoW f WHERE f.energyMismatchRatio = :energyMismatchRatio")
    , @NamedQuery(name = "ForecastDsoW.findByNightCost", query = "SELECT f FROM ForecastDsoW f WHERE f.nightCost = :nightCost")
    , @NamedQuery(name = "ForecastDsoW.findBySaidi", query = "SELECT f FROM ForecastDsoW f WHERE f.saidi = :saidi")
    , @NamedQuery(name = "ForecastDsoW.findByPhaseVoltage", query = "SELECT f FROM ForecastDsoW f WHERE f.phaseVoltage = :phaseVoltage")
    , @NamedQuery(name = "ForecastDsoW.findByReactivePow", query = "SELECT f FROM ForecastDsoW f WHERE f.reactivePow = :reactivePow")
    , @NamedQuery(name = "ForecastDsoW.findBySendInterval", query = "SELECT f FROM ForecastDsoW f WHERE f.sendInterval = :sendInterval")
    , @NamedQuery(name = "ForecastDsoW.findByThdd", query = "SELECT f FROM ForecastDsoW f WHERE f.thdd = :thdd")
    , @NamedQuery(name = "ForecastDsoW.findByUpTime", query = "SELECT f FROM ForecastDsoW f WHERE f.upTime = :upTime")
    , @NamedQuery(name = "ForecastDsoW.findByVoltageDeviation", query = "SELECT f FROM ForecastDsoW f WHERE f.voltageDeviation = :voltageDeviation")
    , @NamedQuery(name = "ForecastDsoW.findByIdx12hours", query = "SELECT f FROM ForecastDsoW f WHERE f.idx12hours = :idx12hours")
    , @NamedQuery(name = "ForecastDsoW.findByIdx15minutes", query = "SELECT f FROM ForecastDsoW f WHERE f.idx15minutes = :idx15minutes")
    , @NamedQuery(name = "ForecastDsoW.findByIdx1day", query = "SELECT f FROM ForecastDsoW f WHERE f.idx1day = :idx1day")
    , @NamedQuery(name = "ForecastDsoW.findByIdx1hour", query = "SELECT f FROM ForecastDsoW f WHERE f.idx1hour = :idx1hour")
    , @NamedQuery(name = "ForecastDsoW.findByIdx1month", query = "SELECT f FROM ForecastDsoW f WHERE f.idx1month = :idx1month")
    , @NamedQuery(name = "ForecastDsoW.findByIdx1week", query = "SELECT f FROM ForecastDsoW f WHERE f.idx1week = :idx1week")
    , @NamedQuery(name = "ForecastDsoW.findByIdx1year", query = "SELECT f FROM ForecastDsoW f WHERE f.idx1year = :idx1year")
    , @NamedQuery(name = "ForecastDsoW.findByIdx3month", query = "SELECT f FROM ForecastDsoW f WHERE f.idx3month = :idx3month")
    , @NamedQuery(name = "ForecastDsoW.findByIdx6hours", query = "SELECT f FROM ForecastDsoW f WHERE f.idx6hours = :idx6hours")
    , @NamedQuery(name = "ForecastDsoW.findByIdx6month", query = "SELECT f FROM ForecastDsoW f WHERE f.idx6month = :idx6month")})
public class ForecastDsoW implements Serializable {

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
    @Column(name = "device_number")
    private Integer deviceNumber;

    public ForecastDsoW() {
    }

    public ForecastDsoW(Long id) {
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

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Integer deviceNumber) {
        this.deviceNumber = deviceNumber;
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
        if (!(object instanceof ForecastDsoW)) {
            return false;
        }
        ForecastDsoW other = (ForecastDsoW) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ForecastDsoW[ id=" + id + " ]";
    }
    
}
