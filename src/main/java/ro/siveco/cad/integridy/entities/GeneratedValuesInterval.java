/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "generated_values_interval")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeneratedValuesInterval.findAll", query = "SELECT g FROM GeneratedValuesInterval g")
    , @NamedQuery(name = "GeneratedValuesInterval.findByHourOfDay", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.hourOfDay = :hourOfDay")
    , @NamedQuery(name = "GeneratedValuesInterval.findByActivePowI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.activePowI = :activePowI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByCo2I", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.co2I = :co2I")
    , @NamedQuery(name = "GeneratedValuesInterval.findByConsumedActivePowI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.consumedActivePowI = :consumedActivePowI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByConsumedReactivePowI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.consumedReactivePowI = :consumedReactivePowI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByDayCostI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.dayCostI = :dayCostI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByDownTimeI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.downTimeI = :downTimeI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByEnergyMismatchI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.energyMismatchI = :energyMismatchI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByEnergyMismatchRatioI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.energyMismatchRatioI = :energyMismatchRatioI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByNightCostI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.nightCostI = :nightCostI")
    , @NamedQuery(name = "GeneratedValuesInterval.findBySaidiI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.saidiI = :saidiI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByPhaseVoltageI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.phaseVoltageI = :phaseVoltageI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByReactivePowI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.reactivePowI = :reactivePowI")
    , @NamedQuery(name = "GeneratedValuesInterval.findBySendIntervalI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.sendIntervalI = :sendIntervalI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByThddI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.thddI = :thddI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByUpTimeI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.upTimeI = :upTimeI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByVoltageDeviationI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.voltageDeviationI = :voltageDeviationI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx12hoursI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx12hoursI = :idx12hoursI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx15minutesI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx15minutesI = :idx15minutesI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1dayI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1dayI = :idx1dayI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1hourI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1hourI = :idx1hourI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1monthI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1monthI = :idx1monthI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1weekI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1weekI = :idx1weekI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1yearI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1yearI = :idx1yearI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx3monthI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx3monthI = :idx3monthI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx6hoursI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx6hoursI = :idx6hoursI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx6monthI", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx6monthI = :idx6monthI")
    , @NamedQuery(name = "GeneratedValuesInterval.findByActivePowX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.activePowX = :activePowX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByCo2X", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.co2X = :co2X")
    , @NamedQuery(name = "GeneratedValuesInterval.findByConsumedActivePowX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.consumedActivePowX = :consumedActivePowX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByConsumedReactivePowX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.consumedReactivePowX = :consumedReactivePowX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByDayCostX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.dayCostX = :dayCostX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByDownTimeX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.downTimeX = :downTimeX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByEnergyMismatchX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.energyMismatchX = :energyMismatchX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByEnergyMismatchRatioX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.energyMismatchRatioX = :energyMismatchRatioX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByNightCostX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.nightCostX = :nightCostX")
    , @NamedQuery(name = "GeneratedValuesInterval.findBySaidiX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.saidiX = :saidiX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByPhaseVoltageX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.phaseVoltageX = :phaseVoltageX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByReactivePowX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.reactivePowX = :reactivePowX")
    , @NamedQuery(name = "GeneratedValuesInterval.findBySendIntervalX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.sendIntervalX = :sendIntervalX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByThddX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.thddX = :thddX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByUpTimeX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.upTimeX = :upTimeX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByVoltageDeviationX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.voltageDeviationX = :voltageDeviationX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx12hoursX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx12hoursX = :idx12hoursX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx15minutesX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx15minutesX = :idx15minutesX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1dayX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1dayX = :idx1dayX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1hourX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1hourX = :idx1hourX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1monthX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1monthX = :idx1monthX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1weekX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1weekX = :idx1weekX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx1yearX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx1yearX = :idx1yearX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx3monthX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx3monthX = :idx3monthX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx6hoursX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx6hoursX = :idx6hoursX")
    , @NamedQuery(name = "GeneratedValuesInterval.findByIdx6monthX", query = "SELECT g FROM GeneratedValuesInterval g WHERE g.idx6monthX = :idx6monthX")})
public class GeneratedValuesInterval implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "hour_of_day")
    private Integer hourOfDay;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "active_pow_i")
    private Double activePowI;
    @Column(name = "co2_i")
    private Double co2I;
    @Column(name = "consumed_active_pow_i")
    private Double consumedActivePowI;
    @Column(name = "consumed_reactive_pow_i")
    private Double consumedReactivePowI;
    @Column(name = "day_cost_i")
    private Double dayCostI;
    @Column(name = "down_time_i")
    private BigInteger downTimeI;
    @Column(name = "energy_mismatch_i")
    private Double energyMismatchI;
    @Column(name = "energy_mismatch_ratio_i")
    private Double energyMismatchRatioI;
    @Column(name = "night_cost_i")
    private Double nightCostI;
    @Column(name = "saidi_i")
    private Double saidiI;
    @Column(name = "phase_voltage_i")
    private Double phaseVoltageI;
    @Column(name = "reactive_pow_i")
    private Double reactivePowI;
    @Column(name = "send_interval_i")
    private BigInteger sendIntervalI;
    @Column(name = "thdd_i")
    private Double thddI;
    @Column(name = "up_time_i")
    private BigInteger upTimeI;
    @Column(name = "voltage_deviation_i")
    private Double voltageDeviationI;
    @Column(name = "idx12hours_i")
    private BigInteger idx12hoursI;
    @Column(name = "idx15minutes_i")
    private BigInteger idx15minutesI;
    @Column(name = "idx1day_i")
    private BigInteger idx1dayI;
    @Column(name = "idx1hour_i")
    private BigInteger idx1hourI;
    @Column(name = "idx1month_i")
    private BigInteger idx1monthI;
    @Column(name = "idx1week_i")
    private BigInteger idx1weekI;
    @Column(name = "idx1year_i")
    private BigInteger idx1yearI;
    @Column(name = "idx3month_i")
    private BigInteger idx3monthI;
    @Column(name = "idx6hours_i")
    private BigInteger idx6hoursI;
    @Column(name = "idx6month_i")
    private BigInteger idx6monthI;
    @Column(name = "active_pow_x")
    private Double activePowX;
    @Column(name = "co2_x")
    private Double co2X;
    @Column(name = "consumed_active_pow_x")
    private Double consumedActivePowX;
    @Column(name = "consumed_reactive_pow_x")
    private Double consumedReactivePowX;
    @Column(name = "day_cost_x")
    private Double dayCostX;
    @Column(name = "down_time_x")
    private BigInteger downTimeX;
    @Column(name = "energy_mismatch_x")
    private Double energyMismatchX;
    @Column(name = "energy_mismatch_ratio_x")
    private Double energyMismatchRatioX;
    @Column(name = "night_cost_x")
    private Double nightCostX;
    @Column(name = "saidi_x")
    private Double saidiX;
    @Column(name = "phase_voltage_x")
    private Double phaseVoltageX;
    @Column(name = "reactive_pow_x")
    private Double reactivePowX;
    @Column(name = "send_interval_x")
    private BigInteger sendIntervalX;
    @Column(name = "thdd_x")
    private Double thddX;
    @Column(name = "up_time_x")
    private BigInteger upTimeX;
    @Column(name = "voltage_deviation_x")
    private Double voltageDeviationX;
    @Column(name = "idx12hours_x")
    private BigInteger idx12hoursX;
    @Column(name = "idx15minutes_x")
    private BigInteger idx15minutesX;
    @Column(name = "idx1day_x")
    private BigInteger idx1dayX;
    @Column(name = "idx1hour_x")
    private BigInteger idx1hourX;
    @Column(name = "idx1month_x")
    private BigInteger idx1monthX;
    @Column(name = "idx1week_x")
    private BigInteger idx1weekX;
    @Column(name = "idx1year_x")
    private BigInteger idx1yearX;
    @Column(name = "idx3month_x")
    private BigInteger idx3monthX;
    @Column(name = "idx6hours_x")
    private BigInteger idx6hoursX;
    @Column(name = "idx6month_x")
    private BigInteger idx6monthX;

    public GeneratedValuesInterval() {
    }

    public GeneratedValuesInterval(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Double getActivePowI() {
        return activePowI;
    }

    public void setActivePowI(Double activePowI) {
        this.activePowI = activePowI;
    }

    public Double getCo2I() {
        return co2I;
    }

    public void setCo2I(Double co2I) {
        this.co2I = co2I;
    }

    public Double getConsumedActivePowI() {
        return consumedActivePowI;
    }

    public void setConsumedActivePowI(Double consumedActivePowI) {
        this.consumedActivePowI = consumedActivePowI;
    }

    public Double getConsumedReactivePowI() {
        return consumedReactivePowI;
    }

    public void setConsumedReactivePowI(Double consumedReactivePowI) {
        this.consumedReactivePowI = consumedReactivePowI;
    }

    public Double getDayCostI() {
        return dayCostI;
    }

    public void setDayCostI(Double dayCostI) {
        this.dayCostI = dayCostI;
    }

    public BigInteger getDownTimeI() {
        return downTimeI;
    }

    public void setDownTimeI(BigInteger downTimeI) {
        this.downTimeI = downTimeI;
    }

    public Double getEnergyMismatchI() {
        return energyMismatchI;
    }

    public void setEnergyMismatchI(Double energyMismatchI) {
        this.energyMismatchI = energyMismatchI;
    }

    public Double getEnergyMismatchRatioI() {
        return energyMismatchRatioI;
    }

    public void setEnergyMismatchRatioI(Double energyMismatchRatioI) {
        this.energyMismatchRatioI = energyMismatchRatioI;
    }

    public Double getNightCostI() {
        return nightCostI;
    }

    public void setNightCostI(Double nightCostI) {
        this.nightCostI = nightCostI;
    }

    public Double getSaidiI() {
        return saidiI;
    }

    public void setSaidiI(Double saidiI) {
        this.saidiI = saidiI;
    }

    public Double getPhaseVoltageI() {
        return phaseVoltageI;
    }

    public void setPhaseVoltageI(Double phaseVoltageI) {
        this.phaseVoltageI = phaseVoltageI;
    }

    public Double getReactivePowI() {
        return reactivePowI;
    }

    public void setReactivePowI(Double reactivePowI) {
        this.reactivePowI = reactivePowI;
    }

    public BigInteger getSendIntervalI() {
        return sendIntervalI;
    }

    public void setSendIntervalI(BigInteger sendIntervalI) {
        this.sendIntervalI = sendIntervalI;
    }

    public Double getThddI() {
        return thddI;
    }

    public void setThddI(Double thddI) {
        this.thddI = thddI;
    }

    public BigInteger getUpTimeI() {
        return upTimeI;
    }

    public void setUpTimeI(BigInteger upTimeI) {
        this.upTimeI = upTimeI;
    }

    public Double getVoltageDeviationI() {
        return voltageDeviationI;
    }

    public void setVoltageDeviationI(Double voltageDeviationI) {
        this.voltageDeviationI = voltageDeviationI;
    }

    public BigInteger getIdx12hoursI() {
        return idx12hoursI;
    }

    public void setIdx12hoursI(BigInteger idx12hoursI) {
        this.idx12hoursI = idx12hoursI;
    }

    public BigInteger getIdx15minutesI() {
        return idx15minutesI;
    }

    public void setIdx15minutesI(BigInteger idx15minutesI) {
        this.idx15minutesI = idx15minutesI;
    }

    public BigInteger getIdx1dayI() {
        return idx1dayI;
    }

    public void setIdx1dayI(BigInteger idx1dayI) {
        this.idx1dayI = idx1dayI;
    }

    public BigInteger getIdx1hourI() {
        return idx1hourI;
    }

    public void setIdx1hourI(BigInteger idx1hourI) {
        this.idx1hourI = idx1hourI;
    }

    public BigInteger getIdx1monthI() {
        return idx1monthI;
    }

    public void setIdx1monthI(BigInteger idx1monthI) {
        this.idx1monthI = idx1monthI;
    }

    public BigInteger getIdx1weekI() {
        return idx1weekI;
    }

    public void setIdx1weekI(BigInteger idx1weekI) {
        this.idx1weekI = idx1weekI;
    }

    public BigInteger getIdx1yearI() {
        return idx1yearI;
    }

    public void setIdx1yearI(BigInteger idx1yearI) {
        this.idx1yearI = idx1yearI;
    }

    public BigInteger getIdx3monthI() {
        return idx3monthI;
    }

    public void setIdx3monthI(BigInteger idx3monthI) {
        this.idx3monthI = idx3monthI;
    }

    public BigInteger getIdx6hoursI() {
        return idx6hoursI;
    }

    public void setIdx6hoursI(BigInteger idx6hoursI) {
        this.idx6hoursI = idx6hoursI;
    }

    public BigInteger getIdx6monthI() {
        return idx6monthI;
    }

    public void setIdx6monthI(BigInteger idx6monthI) {
        this.idx6monthI = idx6monthI;
    }

    public Double getActivePowX() {
        return activePowX;
    }

    public void setActivePowX(Double activePowX) {
        this.activePowX = activePowX;
    }

    public Double getCo2X() {
        return co2X;
    }

    public void setCo2X(Double co2X) {
        this.co2X = co2X;
    }

    public Double getConsumedActivePowX() {
        return consumedActivePowX;
    }

    public void setConsumedActivePowX(Double consumedActivePowX) {
        this.consumedActivePowX = consumedActivePowX;
    }

    public Double getConsumedReactivePowX() {
        return consumedReactivePowX;
    }

    public void setConsumedReactivePowX(Double consumedReactivePowX) {
        this.consumedReactivePowX = consumedReactivePowX;
    }

    public Double getDayCostX() {
        return dayCostX;
    }

    public void setDayCostX(Double dayCostX) {
        this.dayCostX = dayCostX;
    }

    public BigInteger getDownTimeX() {
        return downTimeX;
    }

    public void setDownTimeX(BigInteger downTimeX) {
        this.downTimeX = downTimeX;
    }

    public Double getEnergyMismatchX() {
        return energyMismatchX;
    }

    public void setEnergyMismatchX(Double energyMismatchX) {
        this.energyMismatchX = energyMismatchX;
    }

    public Double getEnergyMismatchRatioX() {
        return energyMismatchRatioX;
    }

    public void setEnergyMismatchRatioX(Double energyMismatchRatioX) {
        this.energyMismatchRatioX = energyMismatchRatioX;
    }

    public Double getNightCostX() {
        return nightCostX;
    }

    public void setNightCostX(Double nightCostX) {
        this.nightCostX = nightCostX;
    }

    public Double getSaidiX() {
        return saidiX;
    }

    public void setSaidiX(Double saidiX) {
        this.saidiX = saidiX;
    }

    public Double getPhaseVoltageX() {
        return phaseVoltageX;
    }

    public void setPhaseVoltageX(Double phaseVoltageX) {
        this.phaseVoltageX = phaseVoltageX;
    }

    public Double getReactivePowX() {
        return reactivePowX;
    }

    public void setReactivePowX(Double reactivePowX) {
        this.reactivePowX = reactivePowX;
    }

    public BigInteger getSendIntervalX() {
        return sendIntervalX;
    }

    public void setSendIntervalX(BigInteger sendIntervalX) {
        this.sendIntervalX = sendIntervalX;
    }

    public Double getThddX() {
        return thddX;
    }

    public void setThddX(Double thddX) {
        this.thddX = thddX;
    }

    public BigInteger getUpTimeX() {
        return upTimeX;
    }

    public void setUpTimeX(BigInteger upTimeX) {
        this.upTimeX = upTimeX;
    }

    public Double getVoltageDeviationX() {
        return voltageDeviationX;
    }

    public void setVoltageDeviationX(Double voltageDeviationX) {
        this.voltageDeviationX = voltageDeviationX;
    }

    public BigInteger getIdx12hoursX() {
        return idx12hoursX;
    }

    public void setIdx12hoursX(BigInteger idx12hoursX) {
        this.idx12hoursX = idx12hoursX;
    }

    public BigInteger getIdx15minutesX() {
        return idx15minutesX;
    }

    public void setIdx15minutesX(BigInteger idx15minutesX) {
        this.idx15minutesX = idx15minutesX;
    }

    public BigInteger getIdx1dayX() {
        return idx1dayX;
    }

    public void setIdx1dayX(BigInteger idx1dayX) {
        this.idx1dayX = idx1dayX;
    }

    public BigInteger getIdx1hourX() {
        return idx1hourX;
    }

    public void setIdx1hourX(BigInteger idx1hourX) {
        this.idx1hourX = idx1hourX;
    }

    public BigInteger getIdx1monthX() {
        return idx1monthX;
    }

    public void setIdx1monthX(BigInteger idx1monthX) {
        this.idx1monthX = idx1monthX;
    }

    public BigInteger getIdx1weekX() {
        return idx1weekX;
    }

    public void setIdx1weekX(BigInteger idx1weekX) {
        this.idx1weekX = idx1weekX;
    }

    public BigInteger getIdx1yearX() {
        return idx1yearX;
    }

    public void setIdx1yearX(BigInteger idx1yearX) {
        this.idx1yearX = idx1yearX;
    }

    public BigInteger getIdx3monthX() {
        return idx3monthX;
    }

    public void setIdx3monthX(BigInteger idx3monthX) {
        this.idx3monthX = idx3monthX;
    }

    public BigInteger getIdx6hoursX() {
        return idx6hoursX;
    }

    public void setIdx6hoursX(BigInteger idx6hoursX) {
        this.idx6hoursX = idx6hoursX;
    }

    public BigInteger getIdx6monthX() {
        return idx6monthX;
    }

    public void setIdx6monthX(BigInteger idx6monthX) {
        this.idx6monthX = idx6monthX;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hourOfDay != null ? hourOfDay.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneratedValuesInterval)) {
            return false;
        }
        GeneratedValuesInterval other = (GeneratedValuesInterval) object;
        if ((this.hourOfDay == null && other.hourOfDay != null) || (this.hourOfDay != null && !this.hourOfDay.equals(other.hourOfDay))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.GeneratedValuesInterval[ hourOfDay=" + hourOfDay + " ]";
    }
    
}
