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
import javax.persistence.Lob;
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
@Table(name = "observacion_tipo_cuenta", catalog = "sigf_v3", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacionTipoCuenta.findAll", query = "SELECT o FROM ObservacionTipoCuenta o")
    , @NamedQuery(name = "ObservacionTipoCuenta.findByObservacionTipoCuentaId", query = "SELECT o FROM ObservacionTipoCuenta o WHERE o.observacionTipoCuentaId = :observacionTipoCuentaId")
    , @NamedQuery(name = "ObservacionTipoCuenta.findByObservacionTipoCuentaResumen", query = "SELECT o FROM ObservacionTipoCuenta o WHERE o.observacionTipoCuentaResumen = :observacionTipoCuentaResumen")
    , @NamedQuery(name = "ObservacionTipoCuenta.findByObservacionTipoCuentaFechaIngreso", query = "SELECT o FROM ObservacionTipoCuenta o WHERE o.observacionTipoCuentaFechaIngreso = :observacionTipoCuentaFechaIngreso")})
public class ObservacionTipoCuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "observacion_tipo_cuenta_id")
    private Integer observacionTipoCuentaId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "observacion_tipo_cuenta_descripcion")
    private String observacionTipoCuentaDescripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "observacion_tipo_cuenta_resumen")
    private String observacionTipoCuentaResumen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "observacion_tipo_cuenta_fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date observacionTipoCuentaFechaIngreso;
    @JoinColumn(name = "observacion_tipo_cuenta_id_tipo", referencedColumnName = "tipo_cuenta_id")
    @ManyToOne(optional = false)
    private TipoCuenta observacionTipoCuentaIdTipo;

    public ObservacionTipoCuenta() {
    }

    public ObservacionTipoCuenta(Integer observacionTipoCuentaId) {
        this.observacionTipoCuentaId = observacionTipoCuentaId;
    }

    public ObservacionTipoCuenta(Integer observacionTipoCuentaId, String observacionTipoCuentaDescripcion, String observacionTipoCuentaResumen, Date observacionTipoCuentaFechaIngreso) {
        this.observacionTipoCuentaId = observacionTipoCuentaId;
        this.observacionTipoCuentaDescripcion = observacionTipoCuentaDescripcion;
        this.observacionTipoCuentaResumen = observacionTipoCuentaResumen;
        this.observacionTipoCuentaFechaIngreso = observacionTipoCuentaFechaIngreso;
    }

    public Integer getObservacionTipoCuentaId() {
        return observacionTipoCuentaId;
    }

    public void setObservacionTipoCuentaId(Integer observacionTipoCuentaId) {
        this.observacionTipoCuentaId = observacionTipoCuentaId;
    }

    public String getObservacionTipoCuentaDescripcion() {
        return observacionTipoCuentaDescripcion;
    }

    public void setObservacionTipoCuentaDescripcion(String observacionTipoCuentaDescripcion) {
        this.observacionTipoCuentaDescripcion = observacionTipoCuentaDescripcion;
    }

    public String getObservacionTipoCuentaResumen() {
        return observacionTipoCuentaResumen;
    }

    public void setObservacionTipoCuentaResumen(String observacionTipoCuentaResumen) {
        this.observacionTipoCuentaResumen = observacionTipoCuentaResumen;
    }

    public Date getObservacionTipoCuentaFechaIngreso() {
        return observacionTipoCuentaFechaIngreso;
    }

    public void setObservacionTipoCuentaFechaIngreso(Date observacionTipoCuentaFechaIngreso) {
        this.observacionTipoCuentaFechaIngreso = observacionTipoCuentaFechaIngreso;
    }

    public TipoCuenta getObservacionTipoCuentaIdTipo() {
        return observacionTipoCuentaIdTipo;
    }

    public void setObservacionTipoCuentaIdTipo(TipoCuenta observacionTipoCuentaIdTipo) {
        this.observacionTipoCuentaIdTipo = observacionTipoCuentaIdTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (observacionTipoCuentaId != null ? observacionTipoCuentaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObservacionTipoCuenta)) {
            return false;
        }
        ObservacionTipoCuenta other = (ObservacionTipoCuenta) object;
        if ((this.observacionTipoCuentaId == null && other.observacionTipoCuentaId != null) || (this.observacionTipoCuentaId != null && !this.observacionTipoCuentaId.equals(other.observacionTipoCuentaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.areatecnica.sigf.entities.ObservacionTipoCuenta[ observacionTipoCuentaId=" + observacionTipoCuentaId + " ]";
    }
    
}
