/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.entities;

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
 * @author ianfr
 */
@Entity
@Table(name = "registro_minuto", catalog = "sigf_v3", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroMinuto.findAll", query = "SELECT r FROM RegistroMinuto r")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoId", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoId = :registroMinutoId")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoDesdeIdBus", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoDesdeIdBus = :registroMinutoDesdeIdBus AND r.registroMinutoFechaMinuto = :registroMinutoFechaMinuto")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoDesdeSinRecaudar", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoDesdeIdBus = :registroMinutoDesdeIdBus AND r.registroMinutoRecaudado = :registroMinutoRecaudado")    
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoHastaIdBus", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoHastaIdBus = :registroMinutoHastaIdBus AND r.registroMinutoFechaMinuto = :registroMinutoFechaMinuto")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoFechaIngreso", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoFechaIngreso = :registroMinutoFechaIngreso")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoFechaMinuto", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoFechaMinuto = :registroMinutoFechaMinuto")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoCantidad", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoCantidad = :registroMinutoCantidad")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoRecaudado", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoRecaudado = :registroMinutoRecaudado")
    , @NamedQuery(name = "RegistroMinuto.findByRegistroMinutoMonto", query = "SELECT r FROM RegistroMinuto r WHERE r.registroMinutoMonto = :registroMinutoMonto")})
public class RegistroMinuto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro_minuto_id")
    private Integer registroMinutoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro_minuto_fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registroMinutoFechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro_minuto_fecha_minuto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registroMinutoFechaMinuto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro_minuto_cantidad")
    private int registroMinutoCantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro_minuto_monto")
    private int registroMinutoMonto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro_minuto_recaudado")
    private boolean registroMinutoRecaudado;
    @JoinColumn(name = "registro_minuto_desde_id_bus", referencedColumnName = "bus_id")
    @ManyToOne(optional = false)
    private Bus registroMinutoDesdeIdBus;
    @JoinColumn(name = "registro_minuto_hasta_id_bus", referencedColumnName = "bus_id")
    @ManyToOne(optional = false)
    private Bus registroMinutoHastaIdBus;

    public RegistroMinuto() {
    }

    public RegistroMinuto(Integer registroMinutoId) {
        this.registroMinutoId = registroMinutoId;
    }

    public RegistroMinuto(Integer registroMinutoId, Date registroMinutoFechaIngreso, Date registroMinutoFechaMinuto, int registroMinutoMonto, Boolean registroMinutoRecaudado) {
        this.registroMinutoId = registroMinutoId;
        this.registroMinutoFechaIngreso = registroMinutoFechaIngreso;
        this.registroMinutoFechaMinuto = registroMinutoFechaMinuto;
        this.registroMinutoMonto = registroMinutoMonto;
        this.registroMinutoRecaudado = registroMinutoRecaudado;
    }

    public Integer getRegistroMinutoId() {
        return registroMinutoId;
    }

    public void setRegistroMinutoId(Integer registroMinutoId) {
        this.registroMinutoId = registroMinutoId;
    }

    public Date getRegistroMinutoFechaIngreso() {
        return registroMinutoFechaIngreso;
    }

    public void setRegistroMinutoFechaIngreso(Date registroMinutoFechaIngreso) {
        this.registroMinutoFechaIngreso = registroMinutoFechaIngreso;
    }

    public Date getRegistroMinutoFechaMinuto() {
        return registroMinutoFechaMinuto;
    }

    public void setRegistroMinutoFechaMinuto(Date registroMinutoFechaMinuto) {
        this.registroMinutoFechaMinuto = registroMinutoFechaMinuto;
    }

    public int getRegistroMinutoCantidad() {
        return registroMinutoCantidad;
    }

    public void setRegistroMinutoCantidad(int registroMinutoCantidad) {
        this.registroMinutoCantidad = registroMinutoCantidad;
    }

    public int getRegistroMinutoMonto() {
        return registroMinutoMonto;
    }

    public void setRegistroMinutoMonto(int registroMinutoMonto) {
        this.registroMinutoMonto = registroMinutoMonto;
    }

    public Bus getRegistroMinutoDesdeIdBus() {
        return registroMinutoDesdeIdBus;
    }

    public void setRegistroMinutoDesdeIdBus(Bus registroMinutoDesdeIdBus) {
        this.registroMinutoDesdeIdBus = registroMinutoDesdeIdBus;
    }

    public Bus getRegistroMinutoHastaIdBus() {
        return registroMinutoHastaIdBus;
    }

    public void setRegistroMinutoHastaIdBus(Bus registroMinutoHastaIdBus) {
        this.registroMinutoHastaIdBus = registroMinutoHastaIdBus;
    }

    public boolean getRegistroMinutoRecaudado() {
        return registroMinutoRecaudado;
    }

    public void setRegistroMinutoRecaudado(boolean registroMinutoRecaudado) {
        this.registroMinutoRecaudado = registroMinutoRecaudado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroMinutoId != null ? registroMinutoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistroMinuto)) {
            return false;
        }
        RegistroMinuto other = (RegistroMinuto) object;
        if ((this.registroMinutoId == null && other.registroMinutoId != null) || (this.registroMinutoId != null && !this.registroMinutoId.equals(other.registroMinutoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.areatecnica.sigf.entities.RegistroMinuto[ registroMinutoId=" + registroMinutoId + " ]";
    }

}
