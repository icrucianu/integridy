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
@Table(name = "consumer_simple_rule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumerSimpleRule.findAll", query = "SELECT c FROM ConsumerSimpleRule c")
    , @NamedQuery(name = "ConsumerSimpleRule.findById", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.id = :id")
    , @NamedQuery(name = "ConsumerSimpleRule.findByRuleName", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.ruleName = :ruleName")
    , @NamedQuery(name = "ConsumerSimpleRule.findByOperator", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.operator = :operator")
    , @NamedQuery(name = "ConsumerSimpleRule.findByVald1", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.vald1 = :vald1")
    , @NamedQuery(name = "ConsumerSimpleRule.findByVald2", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.vald2 = :vald2")
    , @NamedQuery(name = "ConsumerSimpleRule.findByCreatedOn", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.createdOn = :createdOn")
    , @NamedQuery(name = "ConsumerSimpleRule.findByLastUpdatedOn", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "ConsumerSimpleRule.findByValidityStart", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.validityStart = :validityStart")
    , @NamedQuery(name = "ConsumerSimpleRule.findByValidityEnd", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.validityEnd = :validityEnd")
    , @NamedQuery(name = "ConsumerSimpleRule.findByNumberOfRegistrations", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.numberOfRegistrations = :numberOfRegistrations")
    , @NamedQuery(name = "ConsumerSimpleRule.findByStartCronFormat", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.startCronFormat = :startCronFormat")
    , @NamedQuery(name = "ConsumerSimpleRule.findByEndCronFormat", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.endCronFormat = :endCronFormat")
    , @NamedQuery(name = "ConsumerSimpleRule.findBySeverity", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.severity = :severity")
    , @NamedQuery(name = "ConsumerSimpleRule.findByMessage", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.message = :message")
    , @NamedQuery(name = "ConsumerSimpleRule.findByConsumerId", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.clientId.id = :consumerId")
    , @NamedQuery(name = "ConsumerSimpleRule.findBGlobalRules", query = "SELECT c FROM ConsumerSimpleRule c WHERE c.clientId is null and c.deviceId is null")})
public class ConsumerSimpleRule implements Serializable {

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
    @Column(name = "operator")
    private String operator;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vald1")
    private Double vald1;
    @Column(name = "vald2")
    private Double vald2;
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
    @Column(name = "number_of_registrations")
    private Integer numberOfRegistrations;
    @Size(max = 2147483647)
    @Column(name = "start_cron_format")
    private String startCronFormat;
    @Size(max = 2147483647)
    @Column(name = "end_cron_format")
    private String endCronFormat;
    @Column(name = "severity")
    private Integer severity;
    @Size(max = 2147483647)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private ConsumerClient clientId;
    @JoinColumn(name = "rule_type", referencedColumnName = "domain_code")
    @ManyToOne
    private NRuleDomain ruleType;
    @JoinColumn(name = "operand1", referencedColumnName = "operand_code")
    @ManyToOne
    private NRuleOperand operand1;
    @JoinColumn(name = "operand2", referencedColumnName = "operand_code")
    @ManyToOne
    private NRuleOperand operand2;
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    @ManyToOne
    private SmartMeter deviceId;

    public ConsumerSimpleRule() {
    }

    public ConsumerSimpleRule(Integer id) {
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getVald1() {
        return vald1;
    }

    public void setVald1(Double vald1) {
        this.vald1 = vald1;
    }

    public Double getVald2() {
        return vald2;
    }

    public void setVald2(Double vald2) {
        this.vald2 = vald2;
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

    public Integer getNumberOfRegistrations() {
        return numberOfRegistrations;
    }

    public void setNumberOfRegistrations(Integer numberOfRegistrations) {
        this.numberOfRegistrations = numberOfRegistrations;
    }

    public String getStartCronFormat() {
        return startCronFormat;
    }

    public void setStartCronFormat(String startCronFormat) {
        this.startCronFormat = startCronFormat;
    }

    public String getEndCronFormat() {
        return endCronFormat;
    }

    public void setEndCronFormat(String endCronFormat) {
        this.endCronFormat = endCronFormat;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConsumerClient getClientId() {
        return clientId;
    }

    public void setClientId(ConsumerClient clientId) {
        this.clientId = clientId;
    }

    public NRuleDomain getRuleType() {
        return ruleType;
    }

    public void setRuleType(NRuleDomain ruleType) {
        this.ruleType = ruleType;
    }

    public NRuleOperand getOperand1() {
        return operand1;
    }

    public void setOperand1(NRuleOperand operand1) {
        this.operand1 = operand1;
    }

    public NRuleOperand getOperand2() {
        return operand2;
    }

    public void setOperand2(NRuleOperand operand2) {
        this.operand2 = operand2;
    }

    public SmartMeter getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(SmartMeter deviceId) {
        this.deviceId = deviceId;
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
        if (!(object instanceof ConsumerSimpleRule)) {
            return false;
        }
        ConsumerSimpleRule other = (ConsumerSimpleRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.reee.ConsumerSimpleRule[ id=" + id + " ]";
    }
    
}
