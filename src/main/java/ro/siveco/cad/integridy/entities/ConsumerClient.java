/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import ro.siveco.cad.integridy.entities.ConsumerSimpleRule;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "consumer_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumerClient.findAll", query = "SELECT c FROM ConsumerClient c")
    , @NamedQuery(name = "ConsumerClient.findById", query = "SELECT c FROM ConsumerClient c WHERE c.id = :id")
    , @NamedQuery(name = "ConsumerClient.findByFullName", query = "SELECT c FROM ConsumerClient c WHERE c.fullName = :fullName")
    , @NamedQuery(name = "ConsumerClient.findByConsumerType", query = "SELECT c FROM ConsumerClient c WHERE c.consumerType = :consumerType")
    , @NamedQuery(name = "ConsumerClient.findByBillingAddress", query = "SELECT c FROM ConsumerClient c WHERE c.billingAddress = :billingAddress")
    , @NamedQuery(name = "ConsumerClient.findByEmail", query = "SELECT c FROM ConsumerClient c WHERE c.email = :email")
    , @NamedQuery(name = "ConsumerClient.findByMainPhone", query = "SELECT c FROM ConsumerClient c WHERE c.mainPhone = :mainPhone")
    , @NamedQuery(name = "ConsumerClient.findByCreatedOn", query = "SELECT c FROM ConsumerClient c WHERE c.createdOn = :createdOn")
    , @NamedQuery(name = "ConsumerClient.findByLastUpdatedOn", query = "SELECT c FROM ConsumerClient c WHERE c.lastUpdatedOn = :lastUpdatedOn")
    , @NamedQuery(name = "ConsumerClient.findByReceiveRecommendations", query = "SELECT c FROM ConsumerClient c WHERE c.receiveRecommendations = :receiveRecommendations")})
public class ConsumerClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "full_name")
    private String fullName;
    @Size(max = 2147483647)
    @Column(name = "consumer_type")
    private String consumerType;
    @Size(max = 2147483647)
    @Column(name = "billing_address")
    private String billingAddress;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Size(max = 2147483647)
    @Column(name = "main_phone")
    private String mainPhone;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    @Column(name = "receive_recommendations")
    private Boolean receiveRecommendations;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(mappedBy = "clientId")
    private Collection<ConsumptionPoint> consumptionPointCollection;
    @OneToMany(mappedBy = "clientId")
    private Collection<ConsumerSimpleRule> consumerSimpleRuleCollection;

    public ConsumerClient() {
    }

    public ConsumerClient(Integer id) {
        this.id = id;
    }

    public ConsumerClient(Integer id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
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

    public Boolean getReceiveRecommendations() {
        return receiveRecommendations;
    }

    public void setReceiveRecommendations(Boolean receiveRecommendations) {
        this.receiveRecommendations = receiveRecommendations;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<ConsumptionPoint> getConsumptionPointCollection() {
        return consumptionPointCollection;
    }

    public void setConsumptionPointCollection(Collection<ConsumptionPoint> consumptionPointCollection) {
        this.consumptionPointCollection = consumptionPointCollection;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsumerClient)) {
            return false;
        }
        ConsumerClient other = (ConsumerClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.rules.ConsumerClient[ id=" + id + " ]";
    }
    
}
