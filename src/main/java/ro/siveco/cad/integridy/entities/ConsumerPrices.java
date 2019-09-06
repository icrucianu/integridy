/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roxanam
 */
@Entity
@Table(name = "consumer_prices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsumerPrices.findAll", query = "SELECT c FROM ConsumerPrices c")
    , @NamedQuery(name = "ConsumerPrices.findById", query = "SELECT c FROM ConsumerPrices c WHERE c.id = :id")
    , @NamedQuery(name = "ConsumerPrices.findByPriceDef", query = "SELECT c FROM ConsumerPrices c WHERE c.priceDef = :priceDef")
    , @NamedQuery(name = "ConsumerPrices.findByStartTimePeriod", query = "SELECT c FROM ConsumerPrices c WHERE c.startTimePeriod = :startTimePeriod")
    , @NamedQuery(name = "ConsumerPrices.findByEndTimePeriod", query = "SELECT c FROM ConsumerPrices c WHERE c.endTimePeriod = :endTimePeriod")
    , @NamedQuery(name = "ConsumerPrices.findByPriceVal", query = "SELECT c FROM ConsumerPrices c WHERE c.priceVal = :priceVal")
    , @NamedQuery(name = "ConsumerPrices.findByCreatedOn", query = "SELECT c FROM ConsumerPrices c WHERE c.createdOn = :createdOn")
    , @NamedQuery(name = "ConsumerPrices.findByValidityStart", query = "SELECT c FROM ConsumerPrices c WHERE c.validityStart = :validityStart")
    , @NamedQuery(name = "ConsumerPrices.findByValidityEnd", query = "SELECT c FROM ConsumerPrices c WHERE c.validityEnd = :validityEnd")
    , @NamedQuery(name = "ConsumerPrices.findByPriceType", query = "SELECT c FROM ConsumerPrices c WHERE c.priceType = :priceType")
    , @NamedQuery(name = "ConsumerPrices.findByObservations", query = "SELECT c FROM ConsumerPrices c WHERE c.observations = :observations")
    , @NamedQuery(name = "ConsumerPrices.findByDayOfWeek", query = "SELECT c FROM ConsumerPrices c WHERE c.dayOfWeek = :dayOfWeek")
    , @NamedQuery(name = "ConsumerPrices.findByProvider", query = "SELECT c FROM ConsumerPrices c WHERE c.provider = :provider")
    , @NamedQuery(name = "ConsumerPrices.findByIdProvider", query = "SELECT c FROM ConsumerPrices c WHERE c.idProvider = :idProvider")
    , @NamedQuery(name = "ConsumerPrices.findByIntervalType", query = "SELECT c FROM ConsumerPrices c WHERE c.intervalType = :intervalType")
    , @NamedQuery(name = "ConsumerPrices.findByDefault1", query = "SELECT c FROM ConsumerPrices c WHERE c.defaultPrice = :defaultPrice")})

public class ConsumerPrices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "price_def")
    private String priceDef;
    @Column(name = "start_time_period")
    @Temporal(TemporalType.TIME)
    private Date startTimePeriod;
    @Column(name = "end_time_period")
    @Temporal(TemporalType.TIME)
    private Date endTimePeriod;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price_val")
    private Double priceVal;
    @Column(name = "night_price_val")
    private Double nightPriceVal;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "validity_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityStart;
    @Column(name = "validity_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityEnd;
    @Size(max = 2147483647)
    @Column(name = "price_type")
    private String priceType;
    @Size(max = 2147483647)
    @Column(name = "observations")
    private String observations;
    @Column(name = "day_of_week")
    private Integer dayOfWeek;
    @Size(max = 2147483647)
    @Column(name = "provider")
    private String provider;
    @Column(name = "id_provider")
    private BigInteger idProvider;
    @Size(max = 2147483647)
    @Column(name = "interval_type")
    private String intervalType;
    @Column(name = "default_price")
    private Boolean defaultPrice;
    @Column(name = "price_code")
    private String priceCode;

    public ConsumerPrices() {
    }

    public ConsumerPrices(Integer id) {
        this.id = id;
    }

    public ConsumerPrices(Integer id, String priceDef) {
        this.id = id;
        this.priceDef = priceDef;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPriceDef() {
        return priceDef;
    }

    public void setPriceDef(String priceDef) {
        this.priceDef = priceDef;
    }

    public Date getStartTimePeriod() {
        return startTimePeriod;
    }

    public void setStartTimePeriod(Date startTimePeriod) {
        this.startTimePeriod = startTimePeriod;
    }

    public Date getEndTimePeriod() {
        return endTimePeriod;
    }

    public void setEndTimePeriod(Date endTimePeriod) {
        this.endTimePeriod = endTimePeriod;
    }

    public Double getPriceVal() {
        return priceVal;
    }

    public void setPriceVal(Double priceVal) {
        this.priceVal = priceVal;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public BigInteger getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(BigInteger idProvider) {
        this.idProvider = idProvider;
    }

    public String getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(String intervalType) {
        this.intervalType = intervalType;
    }

    public Boolean getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Boolean defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    

    public Double getNightPriceVal() {
        return nightPriceVal;
    }

    public void setNightPriceVal(Double nightPriceVal) {
        this.nightPriceVal = nightPriceVal;
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
        if (!(object instanceof ConsumerPrices)) {
            return false;
        }
        ConsumerPrices other = (ConsumerPrices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ro.siveco.cad.integridy.ConsumerPrices[ id=" + id + " ]";
    }
    
}
