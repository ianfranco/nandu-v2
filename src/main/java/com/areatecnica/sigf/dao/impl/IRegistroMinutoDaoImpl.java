/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.dao.impl;

import com.areatecnica.sigf.dao.IRegistroMinutoDao;
import com.areatecnica.sigf.entities.Bus;
import com.areatecnica.sigf.entities.RegistroMinuto;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ianfr
 */
public class IRegistroMinutoDaoImpl extends GenericDAOImpl<RegistroMinuto> implements IRegistroMinutoDao<RegistroMinuto> {

    @Override
    public List<RegistroMinuto> findByDate(Date fecha) {
        return this.entityManager.createNamedQuery("RegistroMinuto.findByRegistroMinutoFechaMinuto").setParameter("registroMinutoFechaMinuto", fecha).getResultList();
    }

    @Override
    public List<RegistroMinuto> findByBusPagaDate(Bus bus, Date fecha) {
        return this.entityManager.createNamedQuery("RegistroMinuto.findByRegistroMinutoDesdeIdBus").setParameter("registroMinutoDesdeIdBus", bus).setParameter("registroMinutoFechaMinuto", fecha).getResultList();
    }

    @Override
    public List<RegistroMinuto> findByBusRecibeDate(Bus bus, Date fecha) {
        return this.entityManager.createNamedQuery("RegistroMinuto.findByRegistroMinutoHastaIdBus").setParameter("registroMinutoHastaIdBus", bus).setParameter("registroMinutoFechaMinuto", fecha).getResultList();
    }

    @Override
    public List<RegistroMinuto> findByBusSinRecaudar(Bus bus) {
        return this.entityManager.createNamedQuery("RegistroMinuto.findByRegistroMinutoDesdeSinRecaudar").setParameter("registroMinutoDesdeIdBus", bus).setParameter("registroMinutoRecaudado", Boolean.FALSE).getResultList();
    }
    
}
