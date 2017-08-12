/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areatecnica.sigf.dao;

import com.areatecnica.sigf.entities.CajaRecaudacion;
import com.areatecnica.sigf.entities.Cuenta;
import com.areatecnica.sigf.entities.EgresoGuia;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ianfr
 */
public interface IEgresoGuiaDao<T> extends IGenericDAO<T> {
    
    public List<EgresoGuia> findAllByCuenta(Cuenta cuenta);
    
    public List<EgresoGuia> findByIdCajaRecaudacionFecha(CajaRecaudacion caja, Date from, Date to);
    
}
