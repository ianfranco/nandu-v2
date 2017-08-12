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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

/**
 *
 * @author ianfr
 */
@Entity
@Table(name = "tipo_demanda", catalog = "sigf_v3", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDemanda.findAll", query = "SELECT t FROM TipoDemanda t")
    , @NamedQuery(name = "TipoDemanda.findByTipoDemandaId", query = "SELECT t FROM TipoDemanda t WHERE t.tipoDemandaId = :tipoDemandaId")
    , @NamedQuery(name = "TipoDemanda.findByTipoDemandaNombre", query = "SELECT t FROM TipoDemanda t WHERE t.tipoDemandaNombre = :tipoDemandaNombre")
    , @NamedQuery(name = "TipoDemanda.findByTipoDemandaFechaIngreso", query = "SELECT t FROM TipoDemanda t WHERE t.tipoDemandaFechaIngreso = :tipoDemandaFechaIngreso")})
public class TipoDemanda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo_demanda_id")
    private Integer tipoDemandaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo_demanda_nombre")
    private String tipoDemandaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_demanda_fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tipoDemandaFechaIngreso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "frecuenciaServicioIdTipoDemanda")
    private List<FrecuenciaServicio> frecuenciaServicioList;

    public TipoDemanda() {
    }

    public TipoDemanda(Integer tipoDemandaId) {
        this.tipoDemandaId = tipoDemandaId;
    }

    public TipoDemanda(Integer tipoDemandaId, String tipoDemandaNombre, Date tipoDemandaFechaIngreso) {
        this.tipoDemandaId = tipoDemandaId;
        this.tipoDemandaNombre = tipoDemandaNombre;
        this.tipoDemandaFechaIngreso = tipoDemandaFechaIngreso;
    }

    public Integer getTipoDemandaId() {
        return tipoDemandaId;
    }

    public void setTipoDemandaId(Integer tipoDemandaId) {
        this.tipoDemandaId = tipoDemandaId;
    }

    public String getTipoDemandaNombre() {
        return tipoDemandaNombre;
    }

    public void setTipoDemandaNombre(String tipoDemandaNombre) {
        this.tipoDemandaNombre = tipoDemandaNombre;
    }

    public Date getTipoDemandaFechaIngreso() {
        return tipoDemandaFechaIngreso;
    }

    public void setTipoDemandaFechaIngreso(Date tipoDemandaFechaIngreso) {
        this.tipoDemandaFechaIngreso = tipoDemandaFechaIngreso;
    }

    @XmlTransient
    public List<FrecuenciaServicio> getFrecuenciaServicioList() {
        return frecuenciaServicioList;
    }

    public void setFrecuenciaServicioList(List<FrecuenciaServicio> frecuenciaServicioList) {
        this.frecuenciaServicioList = frecuenciaServicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoDemandaId != null ? tipoDemandaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDemanda)) {
            return false;
        }
        TipoDemanda other = (TipoDemanda) object;
        if ((this.tipoDemandaId == null && other.tipoDemandaId != null) || (this.tipoDemandaId != null && !this.tipoDemandaId.equals(other.tipoDemandaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.areatecnica.sigf.entities.TipoDemanda[ tipoDemandaId=" + tipoDemandaId + " ]";
    }
    
}
