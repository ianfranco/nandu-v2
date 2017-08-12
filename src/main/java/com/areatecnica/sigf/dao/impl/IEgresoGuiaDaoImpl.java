/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.dao.impl;

import com.areatecnica.sigf.dao.IEgresoDao;
import com.areatecnica.sigf.dao.IEgresoGuiaDao;
import com.areatecnica.sigf.entities.CajaRecaudacion;
import com.areatecnica.sigf.entities.Cuenta;
import com.areatecnica.sigf.entities.Egreso;
import com.areatecnica.sigf.entities.EgresoGuia;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author ianfr
 */
public class IEgresoGuiaDaoImpl extends GenericDAOImpl<EgresoGuia> implements IEgresoGuiaDao<EgresoGuia> {

    @Override
    public List<EgresoGuia> findAllByCuenta(Cuenta cuenta) {
        try {
            return this.entityManager.createNamedQuery("EgresoGuia.findAll").setParameter("egresoIdCuenta", cuenta).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

    @Override
    public List<EgresoGuia> findByIdCajaRecaudacionFecha(CajaRecaudacion caja, Date from, Date to) {
        try {
            return this.entityManager.createNamedQuery("EgresoGuia.findByIdCajaRecaudacionFecha").setParameter("egresoGuiaIdCajaRecaudacion", caja).setParameter("from", from).setParameter("to", to).getResultList();
        } catch (NoResultException ne) {
            return null;
        }
    }

}
