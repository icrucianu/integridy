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
@Table(name = "g_parameter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GParameter.findAll", query = "SELECT g FROM GParameter g")
    , @NamedQuery(name = "GParameter.findById", query = "SELECT g FROM GParameter g WHERE g.id = :id")
    , @NamedQuery(name = "GParameter.findByParamName", query = "SELECT g FROM GParameter g WHERE g.paramName = :paramName")
    , @NamedQuery(name = "GParameter.findByParamValue", query = "SELECT g FROM GParameter g WHERE g.paramValue = :paramValue")
    , @NamedQuery(name = "GParameter.findByLastupdatedon", query = "SELECT g FROM GParameter g WHERE g.lastupdatedon = :lastupdatedon")})
public class GParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "param_name")
    private String paramName;
    @Size(max = 2147483647)
    @Column(name = "param_value")
    private String paramValue;
    @Column(name = "Last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdatedon;

    public GParameter() {
    }

    public GParameter(Integer id) {
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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Date getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(Date lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
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
        if (!(object instanceof GParameter)) {
            return false;
        }
        GParameter other = (GParameter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.GParameter[ id=" + id + " ]";
    }
    
}
