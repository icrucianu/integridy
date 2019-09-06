/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "whatif_scenario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WhatifScenario.findAll", query = "SELECT w FROM WhatifScenario w")
    , @NamedQuery(name = "WhatifScenario.findById", query = "SELECT w FROM WhatifScenario w WHERE w.id = :id")
    , @NamedQuery(name = "WhatifScenario.findByScenarioName", query = "SELECT w FROM WhatifScenario w WHERE w.scenarioName = :scenarioName")
    , @NamedQuery(name = "WhatifScenario.findByScenarioType", query = "SELECT w FROM WhatifScenario w WHERE w.scenarioType = :scenarioType")
    , @NamedQuery(name = "WhatifScenario.findByStartPeriod", query = "SELECT w FROM WhatifScenario w WHERE w.startPeriod = :startPeriod")
    , @NamedQuery(name = "WhatifScenario.findByEndPeriod", query = "SELECT w FROM WhatifScenario w WHERE w.endPeriod = :endPeriod")
    , @NamedQuery(name = "WhatifScenario.findByCreatedOn", query = "SELECT w FROM WhatifScenario w WHERE w.createdOn = :createdOn")
    , @NamedQuery(name = "WhatifScenario.findByReferenceStartPeriod", query = "SELECT w FROM WhatifScenario w WHERE w.referenceStartPeriod = :referenceStartPeriod")
    , @NamedQuery(name = "WhatifScenario.findByReferenceEndPeriod", query = "SELECT w FROM WhatifScenario w WHERE w.referenceEndPeriod = :referenceEndPeriod")
    , @NamedQuery(name = "WhatifScenario.findAllOrederedbyID", query = "SELECT w FROM WhatifScenario w order by w.id")})
public class WhatifScenario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "scenario_name")
    private String scenarioName;
    @Column(name = "scenario_type")
    private Integer scenarioType;
    @Column(name = "start_period")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startPeriod;
    @Column(name = "end_period")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endPeriod;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "reference_start_period")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referenceStartPeriod;
    @Column(name = "reference_end_period")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referenceEndPeriod;
    @OneToMany(mappedBy = "scenarioId")
    private List<WhatIfParameters> whatIfParametersCollection;

    public WhatifScenario() {
    }

    public WhatifScenario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public Integer getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(Integer scenarioType) {
        this.scenarioType = scenarioType;
    }

    public Date getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Date startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Date getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Date endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getReferenceStartPeriod() {
        return referenceStartPeriod;
    }

    public void setReferenceStartPeriod(Date referenceStartPeriod) {
        this.referenceStartPeriod = referenceStartPeriod;
    }

    public Date getReferenceEndPeriod() {
        return referenceEndPeriod;
    }

    public void setReferenceEndPeriod(Date referenceEndPeriod) {
        this.referenceEndPeriod = referenceEndPeriod;
    }

    @XmlTransient
    public Collection<WhatIfParameters> getWhatIfParametersCollection() {
        return whatIfParametersCollection;
    }

    public void setWhatIfParametersCollection(List<WhatIfParameters> whatIfParametersCollection) {
        this.whatIfParametersCollection = whatIfParametersCollection;
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
        if (!(object instanceof WhatifScenario)) {
            return false;
        }
        WhatifScenario other = (WhatifScenario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.WhatifScenario[ id=" + id + " ]";
    }
    
}
