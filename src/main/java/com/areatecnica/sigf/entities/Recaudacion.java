/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ianfr
 */
@Entity
@Table(name = "recaudacion", catalog = "sigf_v3", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recaudacion.findAll", query = "SELECT r FROM Recaudacion r")
    , @NamedQuery(name = "Recaudacion.findByRecaudacionId", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionId = :recaudacionId")
    , @NamedQuery(name = "Recaudacion.findByRecaudacionIdentificador", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionIdentificador = :recaudacionIdentificador")
    , @NamedQuery(name = "Recaudacion.findByRecaudacionTotal", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionTotal = :recaudacionTotal")
    , @NamedQuery(name = "Recaudacion.findByRecaudacionFecha", query = "SELECT r FROM Recaudacion r WHERE r.recaudacionFecha = :recaudacionFecha")})
public class Recaudacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "recaudacion_id")
    private Integer recaudacionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recaudacion_identificador")
    private int recaudacionIdentificador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recaudacion_total")
    private int recaudacionTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recaudacion_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recaudacionFecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recaudacionEgresoIdRecaudacion")
    private List<RecaudacionEgreso> recaudacionEgresoList;
    @JoinColumn(name = "recaudacion_id_bus", referencedColumnName = "bus_id")
    @ManyToOne(optional = false)
    private Bus recaudacionIdBus;
    @JoinColumn(name = "recaudacion_id_caja", referencedColumnName = "caja_recaudacion_id")
    @ManyToOne(optional = false)
    private CajaRecaudacion recaudacionIdCaja;
    @JoinColumn(name = "recaudacion_id_trabajador", referencedColumnName = "trabajador_id")
    @ManyToOne(optional = false)
    private Trabajador recaudacionIdTrabajador;

    public Recaudacion() {
    }

    public Recaudacion(Integer recaudacionId) {
        this.recaudacionId = recaudacionId;
    }

    public Recaudacion(Integer recaudacionId, int recaudacionIdentificador, int recaudacionTotal, Date recaudacionFecha) {
        this.recaudacionId = recaudacionId;
        this.recaudacionIdentificador = recaudacionIdentificador;
        this.recaudacionTotal = recaudacionTotal;
        this.recaudacionFecha = recaudacionFecha;
    }

    public Integer getRecaudacionId() {
        return recaudacionId;
    }

    public void setRecaudacionId(Integer recaudacionId) {
        this.recaudacionId = recaudacionId;
    }

    public int getRecaudacionIdentificador() {
        return recaudacionIdentificador;
    }

    public void setRecaudacionIdentificador(int recaudacionIdentificador) {
        this.recaudacionIdentificador = recaudacionIdentificador;
    }

    public int getRecaudacionTotal() {
        return recaudacionTotal;
    }

    public void setRecaudacionTotal(int recaudacionTotal) {
        this.recaudacionTotal = recaudacionTotal;
    }

    public Date getRecaudacionFecha() {
        return recaudacionFecha;
    }

    public void setRecaudacionFecha(Date recaudacionFecha) {
        this.recaudacionFecha = recaudacionFecha;
    }

    @XmlTransient
    public List<RecaudacionEgreso> getRecaudacionEgresoList() {
        return recaudacionEgresoList;
    }

    public void setRecaudacionEgresoList(List<RecaudacionEgreso> recaudacionEgresoList) {
        this.recaudacionEgresoList = recaudacionEgresoList;
    }

    public Bus getRecaudacionIdBus() {
        return recaudacionIdBus;
    }

    public void setRecaudacionIdBus(Bus recaudacionIdBus) {
        this.recaudacionIdBus = recaudacionIdBus;
    }

    public CajaRecaudacion getRecaudacionIdCaja() {
        return recaudacionIdCaja;
    }

    public void setRecaudacionIdCaja(CajaRecaudacion recaudacionIdCaja) {
        this.recaudacionIdCaja = recaudacionIdCaja;
    }

    public Trabajador getRecaudacionIdTrabajador() {
        return recaudacionIdTrabajador;
    }

    public void setRecaudacionIdTrabajador(Trabajador recaudacionIdTrabajador) {
        this.recaudacionIdTrabajador = recaudacionIdTrabajador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recaudacionId != null ? recaudacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recaudacion)) {
            return false;
        }
        Recaudacion other = (Recaudacion) object;
        if ((this.recaudacionId == null && other.recaudacionId != null) || (this.recaudacionId != null && !this.recaudacionId.equals(other.recaudacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.areatecnica.sigf.entities.Recaudacion[ recaudacionId=" + recaudacionId + " ]";
    }
    
}
