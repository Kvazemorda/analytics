package com.analytics.entity.client;

import javax.persistence.*;

/**
 * Each client has metricsID and oAuthorID.
 * Them need get before start create report
 */
@Entity
@Table
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false) private int id;
    @Basic @Column(nullable = false) private String metricsID;
    @Basic @Column(nullable = false) private String oAuthorIDMetric;
    @Basic @Column(nullable = false) private String oAuthorIDDirect;
    @Basic @Column(nullable = false) private String loginDirect;

    public Client() {
    }

    public Client(String metricsID, String oAuthorIDMetric, String loginDirect) {
        this.metricsID = metricsID;
        this.oAuthorIDMetric = oAuthorIDMetric;
        this.loginDirect = loginDirect;
    }

    public Client(String oAuthorIDDirect, String loginDirect) {
        this.oAuthorIDDirect = oAuthorIDDirect;
        this.loginDirect = loginDirect;
    }

    public Client(String loginDirect) {
        this.loginDirect = loginDirect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetricsID() {
        return metricsID;
    }

    public void setMetricsID(String metricsID) {
        this.metricsID = metricsID;
    }

    public String getoOAuthorIDMetric() {
        return oAuthorIDMetric;
    }

    public void setoOAuthorIDMetric(String oAuthorID) {
        this.oAuthorIDMetric = oAuthorID;
    }

    public String getoAuthorIDDirect() {
        return oAuthorIDDirect;
    }

    public void setoAuthorIDDirect(String oAuthorIDDirect) {
        this.oAuthorIDDirect = oAuthorIDDirect;
    }

    public String getLoginDirect() {
        return loginDirect;
    }

    public void setLoginDirect(String loginDirect) {
        this.loginDirect = loginDirect;
    }

    @Override
    public String toString() {
        return loginDirect;
    }
}
