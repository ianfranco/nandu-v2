package com.areatecnica.sigf.beans;

import com.areatecnica.sigf.entities.VentaCombustible;
import com.areatecnica.sigf.controllers.VentaCombustibleFacade;
import com.areatecnica.sigf.dao.IBusDao;
import com.areatecnica.sigf.dao.IGuiaDao;
import com.areatecnica.sigf.dao.IPrecioCombustibleDao;
import com.areatecnica.sigf.dao.IVentaCombustibleDao;
import com.areatecnica.sigf.dao.impl.IBusDaoImpl;
import com.areatecnica.sigf.dao.impl.IPrecioCombustibleDaoImpl;
import com.areatecnica.sigf.dao.impl.IVentaCombustibleDaoImpl;
import com.areatecnica.sigf.entities.Bus;
import com.areatecnica.sigf.entities.Guia;
import com.areatecnica.sigf.entities.PrecioCombustible;
import com.areatecnica.sigf.entities.UnidadNegocio;
import com.areatecnica.sigf.models.VentaCombustibleModel;
import com.areatecnica.sigf.reports.PdfReportController;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

@Named(value = "ventaCombustibleController")
@ViewScoped
public class VentaCombustibleController extends AbstractController<VentaCombustible> {

    @Inject
    private VentaCombustibleFacade ejbFacade;
    @Inject
    private GuiaController ventaCombustibleIdGuiaController;
    @Inject
    private SurtidorController ventaCombustibleIdSurtidorController;
    @Inject
    private PdfReportController pdfReportController;

    private List<Bus> busItems;
    private List<Guia> guiaItems;
    private List<UnidadNegocio> unidadItems;
    private List<VentaCombustible> ventasItems;
    private VentaCombustibleModel model;
    private Map unidadMap;
    private Guia guiaSelected;
    private Bus busSelected;
    private UnidadNegocio unidadSelected;
    private IGuiaDao guiaDao;
    private IBusDao busDao;
    private IVentaCombustibleDao ventasDao;
    private PrecioCombustible precioCombustible;
    private IPrecioCombustibleDao precioCombustibleDao;
    private String tipoVenta;
    private int total;
    private int ultimaBoleta;
    private String formatTotal;
    private static String pattern = "###,###.###";
    private static DecimalFormat decimalFormat = new DecimalFormat(pattern);
    private Date fecha;
    private String formatFechaVentaBoleto;
    private final static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

    /**
     * Initialize the concrete VentaCombustible controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        this.fecha = new Date();
        this.formatFechaVentaBoleto = this.format.format(fecha);
        this.busDao = new IBusDaoImpl();
        this.busItems = this.busDao.findAllByTerminal(this.getCurrentUser().getUsuarioIdTerminal());

        unidadMap = new HashMap();

        for (Bus b : this.busItems) {
            unidadMap.put(b.getBusIdUnidadNegocio(), b.getBusIdUnidadNegocio());
        }

        this.unidadItems = new ArrayList(this.unidadMap.values());

        this.precioCombustibleDao = new IPrecioCombustibleDaoImpl();
        this.precioCombustible = this.precioCombustibleDao.findLastPrecio(this.getUserCount());

        this.ventasDao = new IVentaCombustibleDaoImpl();
        this.ventasItems = this.ventasDao.findByTerminalDate(this.getCurrentUser().getUsuarioIdTerminal(), fecha);

        this.model = new VentaCombustibleModel(ventasItems);

        this.total = 0;
        for (VentaCombustible v : this.ventasItems) {
            this.total += v.getVentaCombustibleTotal();
        }
        this.formatTotal = this.decimalFormat.format(total);
        this.ultimaBoleta = this.ventasDao.findLastNumeroBoleta(this.getCurrentUser().getUsuarioIdTerminal());
    }

    public VentaCombustibleController() {
        // Inform the Abstract parent controller of the concrete VentaCombustible Entity
        super(VentaCombustible.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        ventaCombustibleIdGuiaController.setSelected(null);
        ventaCombustibleIdSurtidorController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Surtidor controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareVentaCombustibleIdSurtidor(ActionEvent event) {
        if (this.getSelected() != null && ventaCombustibleIdSurtidorController.getSelected() == null) {
            ventaCombustibleIdSurtidorController.setSelected(this.getSelected().getVentaCombustibleIdSurtidor());
        }
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Guia> getGuiaItems() {
        return guiaItems;
    }

    public void setGuiaItems(List<Guia> guiaItems) {
        this.guiaItems = guiaItems;
    }

    public Guia getGuiaSelected() {
        return guiaSelected;
    }

    public void setGuiaSelected(Guia guiaSelected) {
        this.guiaSelected = guiaSelected;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public List<UnidadNegocio> getUnidadItems() {
        return unidadItems;
    }

    public void setUnidadItems(List<UnidadNegocio> unidadItems) {
        this.unidadItems = unidadItems;
    }

    public void setUnidadMap(Map unidadMap) {
        this.unidadMap = unidadMap;
    }

    public Map getUnidadMap() {
        return unidadMap;
    }

    public UnidadNegocio getUnidadSelected() {
        return unidadSelected;
    }

    public void setUnidadSelected(UnidadNegocio unidadSelected) {
        this.unidadSelected = unidadSelected;
    }

    public void setBusItems(List<Bus> busItems) {
        this.busItems = busItems;
    }

    public List<Bus> getBusItems() {
        return busItems;
    }

    public void setBusSelected(Bus busSelected) {
        this.busSelected = busSelected;
    }

    public Bus getBusSelected() {
        return busSelected;
    }

    public void setVentasItems(List<VentaCombustible> ventasItems) {
        this.ventasItems = ventasItems;
    }

    public List<VentaCombustible> getVentasItems() {
        return ventasItems;
    }

    public void setModel(VentaCombustibleModel model) {
        this.model = model;
    }

    public VentaCombustibleModel getModel() {
        return model;
    }

    public void setFormatFechaVentaBoleto(String formatFechaVentaBoleto) {
        this.formatFechaVentaBoleto = formatFechaVentaBoleto;
    }

    public String getFormatFechaVentaBoleto() {
        return formatFechaVentaBoleto;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFormatTotal() {
        return formatTotal;
    }

    public void setFormatTotal(String formatTotal) {
        this.formatTotal = formatTotal;
    }

    public void setUltimaBoleta(int ultimaBoleta) {
        this.ultimaBoleta = ultimaBoleta;
    }

    public int getUltimaBoleta() {
        return ultimaBoleta;
    }

    @Override
    public VentaCombustible prepareCreate(ActionEvent event) {
        super.prepareCreate(event); //To change body of generated methods, choose Tools | Templates.
        this.getSelected().setVentaCombustibleFecha(new Date());
        this.getSelected().setVentaCombustiblePrecio(precioCombustible.getPrecioCombustibleValor());

        this.getSelected().setVentaCombustibleNumeroBoleta(this.ventasDao.findLastNumeroBoleta(this.getCurrentUser().getUsuarioIdTerminal()));
        return this.getSelected();
    }

    @Override
    public void saveNew(ActionEvent event) {
        this.getSelected().setVentaCombustibleHora(new Date());
        this.total += this.getSelected().getVentaCombustibleTotal();
        this.formatTotal = decimalFormat.format(total);
        super.saveNew(event); //To change body of generated methods, choose Tools | Templates.
        this.ventasItems.add(this.getSelected());
        this.ultimaBoleta = this.getSelected().getVentaCombustibleNumeroBoleta();
    }

    public void handleOperadorChange() {

        if (this.unidadSelected != null) {
            this.busDao = new IBusDaoImpl();
            this.busItems = this.busDao.findByUnidad(unidadSelected);
        }
    }

    public void handleBusChange() {
        /*this.guiaDao = new IGuiaDaoImpl();
        this.guiaSelected = this.guiaDao.findLastGuiaByBusEqualsFecha(busSelected, fecha);*/

    }

    public void handleTotalChange() {
        this.getSelected().setVentaCombustibleCantidad((float) this.getSelected().getVentaCombustibleTotal() / precioCombustible.getPrecioCombustibleValor());
    }

    public void handleNumeroLitrosChange() {
        this.getSelected().setVentaCombustiblePrecio((int) (precioCombustible.getPrecioCombustibleValor() * this.getSelected().getVentaCombustibleCantidad()));
    }

    public void exportPdf(ActionEvent event) {
        /*this.pdfReportController.setProcesoRecaudacion(this.procesoRecaudacion);
        this.pdfReportController.setRecaudacion(this.fechaRecaudacion);*/

        try {
            this.pdfReportController.PDF(event);
        } catch (JRException ex) {
            Logger.getLogger(RecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
