/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;

import java.io.Serializable;
import java.util.Date;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerPrices;

/**
 *
 * @author roxanam
 */



public class InvoiceData implements Serializable{
    private int id;
    private ConsumerClient  consumerClient;
    private ConsumerPrices price;
    private Date dateStart;
    private Date dateStop;
    private double totalConsumedActivePower;
    private double totalCost;

    public InvoiceData(int id, ConsumerClient consumerClient, ConsumerPrices price, Date dateStart, Date dateStop, double totalConsumedActivePower, double totalCost) {
        this.id = id;
        this.consumerClient = consumerClient;
        this.price = price;
        this.dateStart = dateStart;
        this.dateStop = dateStop;
        this.totalConsumedActivePower = totalConsumedActivePower;
        this.totalCost = totalCost;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConsumerClient getConsumerClient() {
        return consumerClient;
    }

    public void setConsumerClient(ConsumerClient consumerClient) {
        this.consumerClient = consumerClient;
    }

    public ConsumerPrices getPrice() {
        return price;
    }

    public void setPrice(ConsumerPrices price) {
        this.price = price;
    }

    

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateStop() {
        return dateStop;
    }

    public void setDateStop(Date dateStop) {
        this.dateStop = dateStop;
    }

    public double getTotalConsumedActivePower() {
        return totalConsumedActivePower;
    }

    public void setTotalConsumedActivePower(double totalConsumedActivePower) {
        this.totalConsumedActivePower = totalConsumedActivePower;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    
    
}
