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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "dso_complex_rule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DsoComplexRule.findAll", query = "SELECT d FROM DsoComplexRule d")
    , @NamedQuery(name = "DsoComplexRule.findById", query = "SELECT d FROM DsoComplexRule d WHERE d.id = :id")
    , @NamedQuery(name = "DsoComplexRule.findByRuleName", query = "SELECT d FROM DsoComplexRule d WHERE d.ruleName = :ruleName")
    , @NamedQuery(name = "DsoComplexRule.findByRuleType", query = "SELECT d FROM DsoComplexRule d WHERE d.ruleType = :ruleType")
    , @NamedQuery(name = "DsoComplexRule.findByRule1", query = "SELECT d FROM DsoComplexRule d WHERE d.rule1 = :rule1")
    , @NamedQuery(name = "DsoComplexRule.findByRule2", query = "SELECT d FROM DsoComplexRule d WHERE d.rule2 = :rule2")
    , @NamedQuery(name = "DsoComplexRule.findByOperator", query = "SELECT d FROM DsoComplexRule d WHERE d.operator = :operator")
    , @NamedQuery(name = "DsoComplexRule.findByCreatedOn", query = "SELECT d FROM DsoComplexRule d WHERE d.createdOn = :createdOn")
    , @NamedQuery(name = "DsoComplexRule.findByLastUpdatedOn", query = "SELECT d FROM DsoComplexRule d WHERE d.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "DsoComplexRule.findByValidityStart", query = "SELECT d FROM DsoComplexRule d WHERE d.validityStart = :validityStart")
    , @NamedQuery(name = "DsoComplexRule.findByValidityEnd", query = "SELECT d FROM DsoComplexRule d WHERE d.validityEnd = :validityEnd")})
public class DsoComplexRule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "rule_name")
    private String ruleName;
    @Size(max = 2147483647)
    @Column(name = "rule_type")
    private String ruleType;
    @Size(max = 2147483647)
    @Column(name = "rule1")
    private String rule1;
    @Size(max = 2147483647)
    @Column(name = "rule2")
    private String rule2;
    @Size(max = 2147483647)
    @Column(name = "operator")
    private String operator;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    @Column(name = "validity_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityStart;
    @Column(name = "validity_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityEnd;

    public DsoComplexRule() {
    }

    public DsoComplexRule(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRule1() {
        return rule1;
    }

    public void setRule1(String rule1) {
        this.rule1 = rule1;
    }

    public String getRule2() {
        return rule2;
    }

    public void setRule2(String rule2) {
        this.rule2 = rule2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DsoComplexRule)) {
            return false;
        }
        DsoComplexRule other = (DsoComplexRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.DsoComplexRule[ id=" + id + " ]";
    }
    
}
