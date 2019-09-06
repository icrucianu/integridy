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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "customer_add_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerAddInfo.findAll", query = "SELECT c FROM CustomerAddInfo c")
    , @NamedQuery(name = "CustomerAddInfo.findById", query = "SELECT c FROM CustomerAddInfo c WHERE c.id = :id")
    , @NamedQuery(name = "CustomerAddInfo.findByAddInfoName", query = "SELECT c FROM CustomerAddInfo c WHERE c.addInfoName = :addInfoName")
    , @NamedQuery(name = "CustomerAddInfo.findByAddInfoVal", query = "SELECT c FROM CustomerAddInfo c WHERE c.addInfoVal = :addInfoVal")
    , @NamedQuery(name = "CustomerAddInfo.findByCreatedOn", query = "SELECT c FROM CustomerAddInfo c WHERE c.createdOn = :createdOn")})
public class CustomerAddInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "add_info_name")
    private String addInfoName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "add_info_val")
    private String addInfoVal;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ConsumerClient clientId;

    public CustomerAddInfo() {
    }

    public CustomerAddInfo(Integer id) {
        this.id = id;
    }

    public CustomerAddInfo(Integer id, String addInfoName, String addInfoVal) {
        this.id = id;
        this.addInfoName = addInfoName;
        this.addInfoVal = addInfoVal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddInfoName() {
        return addInfoName;
    }

    public void setAddInfoName(String addInfoName) {
        this.addInfoName = addInfoName;
    }

    public String getAddInfoVal() {
        return addInfoVal;
    }

    public void setAddInfoVal(String addInfoVal) {
        this.addInfoVal = addInfoVal;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ConsumerClient getClientId() {
        return clientId;
    }

    public void setClientId(ConsumerClient clientId) {
        this.clientId = clientId;
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
        if (!(object instanceof CustomerAddInfo)) {
            return false;
        }
        CustomerAddInfo other = (CustomerAddInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ClientAddInfo[ id=" + id + " ]";
    }
    
}
