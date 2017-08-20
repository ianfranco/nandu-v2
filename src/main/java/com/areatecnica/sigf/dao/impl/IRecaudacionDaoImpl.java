/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.dao.impl;

import com.areatecnica.sigf.dao.IRecaudacionDao;
import com.areatecnica.sigf.entities.Bus;
import com.areatecnica.sigf.entities.CajaRecaudacion;
import com.areatecnica.sigf.entities.Cuenta;
import com.areatecnica.sigf.entities.GrupoServicio;
import com.areatecnica.sigf.entities.Guia;
import com.areatecnica.sigf.entities.ProcesoRecaudacion;
import com.areatecnica.sigf.entities.Recaudacion;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author ianfr
 */
public class IRecaudacionDaoImpl extends GenericDAOImpl<Guia> implements IRecaudacionDao<Guia> {

    @Override
    public List<Recaudacion> findByProcesoFechaRecaudacion(ProcesoRecaudacion procesoRecaudacion, Date fechaRecaudacion) {
        try {
            return this.entityManager.createNamedQuery("Recaudacion.findByProcesoFechaRecaudacion").setParameter("busIdProcesoRecaudacion", procesoRecaudacion).setParameter("recaudacionFecha", fechaRecaudacion).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }
    
    
    @Override
    public List<Recaudacion> findByProcesoCajaFechaRecaudacion(ProcesoRecaudacion procesoRecaudacion, CajaRecaudacion caja,  Date fechaRecaudacion) {
        try {
            return this.entityManager.createNamedQuery("Recaudacion.findByProcesoFechaRecaudacionCaja").
                    setParameter("busIdProcesoRecaudacion", procesoRecaudacion).
                    setParameter("recaudacionIdCaja", caja).
                    setParameter("recaudacionFecha", fechaRecaudacion).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public List<Recaudacion> findByProcesoFechaGuia(ProcesoRecaudacion procesoRecaudacion, Date fechaGuia) {
        try {
            return this.entityManager.createNamedQuery("Guia.findByProcesoFechaGuia").setParameter("busIdProcesoRecaudacion", procesoRecaudacion).setParameter("guiaFecha", fechaGuia).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public Recaudacion findByCuentaFolio(Cuenta cuenta, int folio) {
        try {
            return (Recaudacion) this.entityManager.createNamedQuery("Guia.findByCuentaFolio").setParameter("guiaIdCuenta", cuenta).setParameter("guiaFolio", folio).getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
    }

    
    @Override
    public List<Recaudacion> findByBusBetweenFechaRecaudacion(Bus bus, Date inicio, Date termino) {
        try {
            return this.entityManager.createNamedQuery("Guia.findByBusBetweenFechaRecaudacion").setParameter("guiaIdBus", bus).setParameter("inicio", inicio).setParameter("termino", inicio).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public List<Recaudacion> findByCuentaFecha(Cuenta cuenta, Date fecha) {
        try {
            return this.entityManager.createNamedQuery("Guia.findByCuentaFecha").setParameter("guiaIdCuenta", cuenta).setParameter("guiaFecha", fecha).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }    

    @Override
    public List<Recaudacion> findByFechaGrupoServicio(GrupoServicio grupoServicio, Date fecha) {
        try {
            return this.entityManager.createNamedQuery("Guia.findByFechaGrupoServicio").setParameter("grupoServicioId", grupoServicio).setParameter("guiaFecha", fecha).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public void delete(Guia guia) {

    }

    @Override
    public List<Recaudacion> findByBusFechaRecaudacion(Bus bus, Date fecha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Recaudacion guia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
