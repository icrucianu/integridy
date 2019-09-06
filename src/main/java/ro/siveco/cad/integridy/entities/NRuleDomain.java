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
@Table(name = "n_rule_domain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NRuleDomain.findAll", query = "SELECT n FROM NRuleDomain n")
    , @NamedQuery(name = "NRuleDomain.findByDomainCode", query = "SELECT n FROM NRuleDomain n WHERE n.domainCode = :domainCode")
    , @NamedQuery(name = "NRuleDomain.findByDomainName", query = "SELECT n FROM NRuleDomain n WHERE n.domainName = :domainName")})
public class NRuleDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "domain_code")
    private String domainCode;
    @Size(max = 2147483647)
    @Column(name = "domain_name")
    private String domainName;
    @OneToMany(mappedBy = "ruleType")
    private Collection<ConsumerSimpleRule> consumerSimpleRuleCollection;

    public NRuleDomain() {
    }

    public NRuleDomain(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @XmlTransient
    public Collection<ConsumerSimpleRule> getConsumerSimpleRuleCollection() {
        return consumerSimpleRuleCollection;
    }

    public void setConsumerSimpleRuleCollection(Collection<ConsumerSimpleRule> consumerSimpleRuleCollection) {
        this.consumerSimpleRuleCollection = consumerSimpleRuleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (domainCode != null ? domainCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NRuleDomain)) {
            return false;
        }
        NRuleDomain other = (NRuleDomain) object;
        if ((this.domainCode == null && other.domainCode != null) || (this.domainCode != null && !this.domainCode.equals(other.domainCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.rules.NRuleDomain[ domainCode=" + domainCode + " ]";
    }
    
}
