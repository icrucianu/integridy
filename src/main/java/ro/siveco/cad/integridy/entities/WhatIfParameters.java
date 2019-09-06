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
@Table(name = "what_if_parameters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WhatIfParameters.findAll", query = "SELECT w FROM WhatIfParameters w")
    , @NamedQuery(name = "WhatIfParameters.findById", query = "SELECT w FROM WhatIfParameters w WHERE w.id = :id")
    , @NamedQuery(name = "WhatIfParameters.findByParamName", query = "SELECT w FROM WhatIfParameters w WHERE w.paramName = :paramName")
    , @NamedQuery(name = "WhatIfParameters.findByParamDval", query = "SELECT w FROM WhatIfParameters w WHERE w.paramDval = :paramDval")
    , @NamedQuery(name = "WhatIfParameters.findByLastUpdatedOn", query = "SELECT w FROM WhatIfParameters w WHERE w.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "WhatIfParameters.findByStartTime", query = "SELECT w FROM WhatIfParameters w WHERE w.startTime = :startTime")
    , @NamedQuery(name = "WhatIfParameters.findByEndTime", query = "SELECT w FROM WhatIfParameters w WHERE w.endTime = :endTime")
    , @NamedQuery(name = "WhatIfParameters.findByDayOfWeek", query = "SELECT w FROM WhatIfParameters w WHERE w.dayOfWeek = :dayOfWeek")
    , @NamedQuery(name = "WhatIfParameters.findByScenario", query = "SELECT w FROM WhatIfParameters w WHERE w.scenarioId = :scenarioId order by w.id")})
public class WhatIfParameters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "param_name")
    private String paramName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "param_dval")
    private Double paramDval;
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    @Column(name = "day_of_week")
    private Integer dayOfWeek;
    @JoinColumn(name = "scenario_id", referencedColumnName = "id")
    @ManyToOne
    private WhatifScenario scenarioId;

    public WhatIfParameters() {
    }

    public WhatIfParameters(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Double getParamDval() {
        return paramDval;
    }

    public void setParamDval(Double paramDval) {
        this.paramDval = paramDval;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

   

    

    public void setScenarioId(WhatifScenario scenarioId) {
        this.scenarioId = scenarioId;
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
        if (!(object instanceof WhatIfParameters)) {
            return false;
        }
        WhatIfParameters other = (WhatIfParameters) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.WhatIfParameters[ id=" + id + " ]";
    }
    
}
