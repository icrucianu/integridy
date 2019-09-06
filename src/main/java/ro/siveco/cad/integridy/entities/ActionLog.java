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
@Table(name = "action_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionLog.findAll", query = "SELECT a FROM ActionLog a")
    , @NamedQuery(name = "ActionLog.findById", query = "SELECT a FROM ActionLog a WHERE a.id = :id")
    , @NamedQuery(name = "ActionLog.findByActionName", query = "SELECT a FROM ActionLog a WHERE a.actionName = :actionName")
    , @NamedQuery(name = "ActionLog.findByActionDate", query = "SELECT a FROM ActionLog a WHERE a.actionDate = :actionDate")
    , @NamedQuery(name = "ActionLog.findByPage", query = "SELECT a FROM ActionLog a WHERE a.page = :page")})
public class ActionLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "action_name")
    private String actionName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Size(max = 2147483647)
    @Column(name = "page")
    private String page;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public ActionLog() {
    }

    public ActionLog(Long id) {
        this.id = id;
    }

    public ActionLog(Long id, String actionName, Date actionDate) {
        this.id = id;
        this.actionName = actionName;
        this.actionDate = actionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof ActionLog)) {
            return false;
        }
        ActionLog other = (ActionLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.entities.ActionLog[ id=" + id + " ]";
    }
    
}
