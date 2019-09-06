/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "n_rule_operand")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NRuleOperand.findAll", query = "SELECT n FROM NRuleOperand n")
    , @NamedQuery(name = "NRuleOperand.findByOperandCode", query = "SELECT n FROM NRuleOperand n WHERE n.operandCode = :operandCode")
    , @NamedQuery(name = "NRuleOperand.findByOperandName", query = "SELECT n FROM NRuleOperand n WHERE n.operandName = :operandName")})
public class NRuleOperand implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "operand_code")
    private String operandCode;
    @Size(max = 2147483647)
    @Column(name = "operand_name")
    private String operandName;
    @OneToMany(mappedBy = "operand1")
    private Collection<ConsumerSimpleRule> consumerSimpleRuleCollection;
    @OneToMany(mappedBy = "operand2")
    private Collection<ConsumerSimpleRule> consumerSimpleRuleCollection1;

    public NRuleOperand() {
    }

    public NRuleOperand(String operandCode) {
        this.operandCode = operandCode;
    }

    public String getOperandCode() {
        return operandCode;
    }

    public void setOperandCode(String operandCode) {
        this.operandCode = operandCode;
    }

    public String getOperandName() {
        return operandName;
    }

    public void setOperandName(String operandName) {
        this.operandName = operandName;
    }

    @XmlTransient
    public Collection<ConsumerSimpleRule> getConsumerSimpleRuleCollection() {
        return consumerSimpleRuleCollection;
    }

    public void setConsumerSimpleRuleCollection(Collection<ConsumerSimpleRule> consumerSimpleRuleCollection) {
        this.consumerSimpleRuleCollection = consumerSimpleRuleCollection;
    }

    @XmlTransient
    public Collection<ConsumerSimpleRule> getConsumerSimpleRuleCollection1() {
        return consumerSimpleRuleCollection1;
    }

    public void setConsumerSimpleRuleCollection1(Collection<ConsumerSimpleRule> consumerSimpleRuleCollection1) {
        this.consumerSimpleRuleCollection1 = consumerSimpleRuleCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (operandCode != null ? operandCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NRuleOperand)) {
            return false;
        }
        NRuleOperand other = (NRuleOperand) object;
        if ((this.operandCode == null && other.operandCode != null) || (this.operandCode != null && !this.operandCode.equals(other.operandCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.rules.NRuleOperand[ operandCode=" + operandCode + " ]";
    }
    
}
