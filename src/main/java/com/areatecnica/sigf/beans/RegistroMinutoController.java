package com.areatecnica.sigf.beans;

import com.areatecnica.sigf.entities.RegistroMinuto;
import com.areatecnica.sigf.controllers.RegistroMinutoFacade;
import com.areatecnica.sigf.dao.IBusDao;
import com.areatecnica.sigf.dao.IRegistroMinutoDao;
import com.areatecnica.sigf.dao.impl.IBusDaoImpl;
import com.areatecnica.sigf.dao.impl.IRegistroMinutoDaoImpl;
import com.areatecnica.sigf.entities.Bus;
import com.areatecnica.sigf.models.RegistroMinutoDataModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "registroMinutoController")
@ViewScoped
public class RegistroMinutoController extends AbstractController<RegistroMinuto> {

    @Inject
    private RegistroMinutoFacade ejbFacade;
    @Inject
    private ValorMinutoController registroMinutoIdValorMinutoController;
    @Inject
    private BusController registroMinutoDesdeIdBusController;
    @Inject
    private BusController registroMinutoHastaIdBusController;
    private RegistroMinutoDataModel model;
    private Date fecha;
    private Date fechaMax;
    private List<RegistroMinuto> minutosItems;
    private List<Bus> recibeBusItems;
    private List<Bus> pagaBusItems;
    private IBusDao busDao;
    private IRegistroMinutoDao registroDao;

    /**
     * Initialize the concrete RegistroMinuto controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public RegistroMinutoController() {
        // Inform the Abstract parent controller of the concrete RegistroMinuto Entity
        super(RegistroMinuto.class);
        this.fechaMax = new Date();
        this.busDao = new IBusDaoImpl();
        this.pagaBusItems = this.busDao.findAllByTerminal(this.getCurrentUser().getUsuarioIdTerminal());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        registroMinutoIdValorMinutoController.setSelected(null);
        registroMinutoDesdeIdBusController.setSelected(null);
        registroMinutoHastaIdBusController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Bus controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRegistroMinutoDesdeIdBus(ActionEvent event) {
        if (this.getSelected() != null && registroMinutoDesdeIdBusController.getSelected() == null) {
            registroMinutoDesdeIdBusController.setSelected(this.getSelected().getRegistroMinutoDesdeIdBus());
        }
    }

    /**
     * Sets the "selected" attribute of the Bus controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRegistroMinutoHastaIdBus(ActionEvent event) {
        if (this.getSelected() != null && registroMinutoHastaIdBusController.getSelected() == null) {
            registroMinutoHastaIdBusController.setSelected(this.getSelected().getRegistroMinutoHastaIdBus());
        }
    }

    public void setPagaBusItems(List<Bus> pagaBusItems) {
        this.pagaBusItems = pagaBusItems;
    }

    public List<Bus> getPagaBusItems() {
        return pagaBusItems;
    }

    public void setRecibeBusItems(List<Bus> recibeBusItems) {
        this.recibeBusItems = recibeBusItems;
    }

    public List<Bus> getRecibeBusItems() {
        return recibeBusItems;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<RegistroMinuto> getMinutosItems() {
        return minutosItems;
    }

    public void setMinutosItems(List<RegistroMinuto> minutosItems) {
        this.minutosItems = minutosItems;
    }

    public void setModel(RegistroMinutoDataModel model) {
        this.model = model;
    }

    public RegistroMinutoDataModel getModel() {
        return model;
    }

    public void setFechaMax(Date fechaMax) {
        this.fechaMax = fechaMax;
    }

    public Date getFechaMax() {
        return fechaMax;
    }

    public void load() {
        this.registroDao = new IRegistroMinutoDaoImpl();
        this.minutosItems = this.registroDao.findByDate(fecha);
        this.model = new RegistroMinutoDataModel(minutosItems);
        this.setSelected(prepareCreate(null));
    }

    @Override
    public RegistroMinuto prepareCreate(ActionEvent event) {
        super.prepareCreate(event); //To change body of generated methods, choose Tools | Templates.
        this.getSelected().setRegistroMinutoFechaIngreso(new Date());
        this.getSelected().setRegistroMinutoFechaMinuto(fecha);
        this.getSelected().setRegistroMinutoRecaudado(Boolean.FALSE);
        return this.getSelected();
    }

    @Override
    public void saveNew(ActionEvent event) {
        super.saveNew(event); //To change body of generated methods, choose Tools | Templates.
        this.minutosItems.add(this.getSelected());
        this.setSelected(prepareCreate(event));
    }

    @Override
    public void save(ActionEvent event) {
        super.save(event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(ActionEvent event) {
        this.minutosItems.remove(this.getSelected());
    }

    public void removeBus() {
        this.recibeBusItems = new ArrayList<>(this.pagaBusItems);
        this.recibeBusItems.remove(this.getSelected().getRegistroMinutoDesdeIdBus());
    }
}
