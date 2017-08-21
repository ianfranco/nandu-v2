package com.areatecnica.sigf.beans;

import com.areatecnica.sigf.beans.util.JsfUtil;
import com.areatecnica.sigf.controllers.RecaudacionFacade;
import com.areatecnica.sigf.controllers.ResumenRecaudacionFacade;
import com.areatecnica.sigf.dao.IBusDao;
import com.areatecnica.sigf.dao.ICajaRecaudacionDao;
import com.areatecnica.sigf.dao.IProcesoRecaudacionDao;
import com.areatecnica.sigf.dao.IRecaudacionDao;
import com.areatecnica.sigf.dao.IRegistroMinutoDao;
import com.areatecnica.sigf.dao.IResumenRecaudacionDao;
import com.areatecnica.sigf.dao.IVentaCombustibleDao;
import com.areatecnica.sigf.dao.impl.IBusDaoImpl;
import com.areatecnica.sigf.dao.impl.ICajaRecaudacionDaoImpl;
import com.areatecnica.sigf.dao.impl.IRecaudacionDaoImpl;
import com.areatecnica.sigf.dao.impl.IRegistroMinutoDaoImpl;
import com.areatecnica.sigf.dao.impl.IResumenRecaudacionDaoImpl;
import com.areatecnica.sigf.dao.impl.IVentaCombustibleDaoImpl;
import com.areatecnica.sigf.entities.Bus;
import com.areatecnica.sigf.entities.CajaProceso;
import com.areatecnica.sigf.entities.CajaRecaudacion;
import com.areatecnica.sigf.entities.Egreso;
import com.areatecnica.sigf.entities.EgresoCajaRecaudacion;
import com.areatecnica.sigf.entities.EgresoProcesoRecaudacion;
import com.areatecnica.sigf.entities.EgresoResumenRecaudacion;
import com.areatecnica.sigf.entities.ProcesoRecaudacion;
import com.areatecnica.sigf.entities.Recaudacion;
import com.areatecnica.sigf.entities.RecaudacionEgreso;
import com.areatecnica.sigf.entities.RegistroMinuto;
import com.areatecnica.sigf.entities.ResumenRecaudacion;
import com.areatecnica.sigf.entities.VentaCombustible;
import com.areatecnica.sigf.models.RecaudacionDataModel;
import com.areatecnica.sigf.reports.PdfReportController;
import com.areatecnica.sigf.sockets.NotificationController;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

@Named(value = "recaudacionController")
@ViewScoped
public class RecaudacionController extends AbstractController<Recaudacion> {

    @Inject
    private RecaudacionFacade ejbFacade;

    @Inject
    private BusController guiaIdBusController;
    @Inject
    private CajaRecaudacionController guiaIdCajaTerminalController;
    @Inject
    private TrabajadorController guiaIdTrabajadorController;
    @Inject
    private ResumenRecaudacionFacade resumenRecaudacionFacade;
    @Inject
    private NotificationController notificationController;
    @Inject
    private PdfReportController pdfReportController;

    private List<Recaudacion> recaudacionItems;
    private List<RecaudacionEgreso> recaudacionEgresoList;
    private List<Egreso> egresosList;
    private List<ProcesoRecaudacion> procesoRecaudacionList;
    private List<CajaRecaudacion> cajaRecaudacionList;
    private List<EgresoResumenRecaudacion> egresosResumenList;
    private List<Bus> busesList;

    private List<PetroleoHelper> ventaCombustibleList;
    private List<MinutosHelper> registroMinutoList;
    private ArrayList<LinkedHashMap> listOfMaps;
    private LinkedHashMap guiaLink;
    private LinkedHashMap totals;
    private List<String> resultsHeader;
    private List<String> resultsTotals;
    private Map folios;
    private ProcesoRecaudacion procesoRecaudacion;
    private ResumenRecaudacion resumenRecaudacion;
    private CajaRecaudacion cajaRecaudacion;
    private RecaudacionDataModel model;
    private MinutosHelper registroMinuto;
    private PetroleoHelper ventaCombustible;
    private String minutos;
    private String petroleo;
    private Date fechaRecaudacion;
    private Boolean permitirEgresoFlota;
    private Boolean permitirEgresoBus;
    private Boolean permitirEgresoProceso;
    private Boolean estadoProceso;
    private int resumenTotal;
    private String resumenTotalFormat;
    private IRecaudacionDao recaudacionDao;
    private IProcesoRecaudacionDao procesoDao;
    private ICajaRecaudacionDao cajaRecaudacionDao;
    private IBusDao busDao;
    private IResumenRecaudacionDao resumenRecaudacionDao;
    private IRegistroMinutoDao registroMinutoDao;
    private IVentaCombustibleDao ventaCombustibleDao;
    private static String pattern = "###,###.###";
    private static DecimalFormat decimalFormat = new DecimalFormat(pattern);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

    /**
     * Initialize the concrete Region controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        this.cajaRecaudacionDao = new ICajaRecaudacionDaoImpl();
        this.setCajaRecaudacionList((List<CajaRecaudacion>) this.cajaRecaudacionDao.findAllByUser(this.getCurrentUser()));

        if (this.getCajaRecaudacionList().size() == 1) {
            this.setCajaRecaudacion(this.getCajaRecaudacionList().get(0));

            handleCajaRecaudacionChange(null);
        }

        this.setPermitirEgresoBus(Boolean.TRUE);
        this.setPermitirEgresoFlota(Boolean.TRUE);
        this.setPermitirEgresoProceso(Boolean.TRUE);
        this.setResumenRecaudacion(new ResumenRecaudacion());
        this.getResumenRecaudacion().setResumenRecaudacionCerrado(Boolean.FALSE);
        this.setEstadoProceso(Boolean.FALSE);
        this.ventaCombustibleDao = new IVentaCombustibleDaoImpl();
        this.registroMinutoDao = new IRegistroMinutoDaoImpl();
    }

    public RecaudacionController() {
        // Inform the Abstract parent controller of the concrete Region Entity
        super(Recaudacion.class);
        this.fechaRecaudacion = new Date();
    }

    /**
     * @return the recaudacionItems
     */
    public List<Recaudacion> getRecaudacionItems() {
        return recaudacionItems;
    }

    /**
     * @param recaudacionItems the recaudacionItems to set
     */
    public void setRecaudacionItems(List<Recaudacion> recaudacionItems) {
        this.recaudacionItems = recaudacionItems;
    }

    /**
     * @return the egresosList
     */
    public List<Egreso> getEgresosList() {
        return egresosList;
    }

    /**
     * @param egresosList the egresosList to set
     */
    public void setEgresosList(List<Egreso> egresosList) {
        this.egresosList = egresosList;
    }

    /**
     * @return the procesoRecaudacionList
     */
    public List<ProcesoRecaudacion> getProcesoRecaudacionList() {
        return procesoRecaudacionList;
    }

    /**
     * @param procesoRecaudacionList the procesoRecaudacionList to set
     */
    public void setProcesoRecaudacionList(List<ProcesoRecaudacion> procesoRecaudacionList) {
        this.procesoRecaudacionList = procesoRecaudacionList;
    }

    /**
     * @return the cajaRecaudacionList
     */
    public List<CajaRecaudacion> getCajaRecaudacionList() {
        return cajaRecaudacionList;
    }

    /**
     * @param cajaRecaudacionList the cajaRecaudacionList to set
     */
    public void setCajaRecaudacionList(List<CajaRecaudacion> cajaRecaudacionList) {
        this.cajaRecaudacionList = cajaRecaudacionList;
    }

    /**
     * @return the egresosResumenList
     */
    public List<EgresoResumenRecaudacion> getEgresosResumenList() {
        return egresosResumenList;
    }

    /**
     * @param egresosResumenList the egresosResumenList to set
     */
    public void setEgresosResumenList(List<EgresoResumenRecaudacion> egresosResumenList) {
        this.egresosResumenList = egresosResumenList;
    }

    /**
     * @return the busesList
     */
    public List<Bus> getBusesList() {
        return busesList;
    }

    /**
     * @param busesList the busesList to set
     */
    public void setBusesList(List<Bus> busesList) {
        this.busesList = busesList;
    }

    /**
     * @return the listOfMaps
     */
    public ArrayList<LinkedHashMap> getListOfMaps() {
        return listOfMaps;
    }

    /**
     * @param listOfMaps the listOfMaps to set
     */
    public void setListOfMaps(ArrayList<LinkedHashMap> listOfMaps) {
        this.listOfMaps = listOfMaps;
    }

    /**
     * @return the guiaLink
     */
    public LinkedHashMap getGuiaLink() {
        return guiaLink;
    }

    /**
     * @param guiaLink the guiaLink to set
     */
    public void setGuiaLink(LinkedHashMap guiaLink) {
        this.guiaLink = guiaLink;
    }

    /**
     * @return the totals
     */
    public LinkedHashMap getTotals() {
        return totals;
    }

    /**
     * @param totals the totals to set
     */
    public void setTotals(LinkedHashMap totals) {
        this.totals = totals;
    }

    /**
     * @return the resultsHeader
     */
    public List<String> getResultsHeader() {
        return resultsHeader;
    }

    /**
     * @param resultsHeader the resultsHeader to set
     */
    public void setResultsHeader(List<String> resultsHeader) {
        this.resultsHeader = resultsHeader;
    }

    /**
     * @return the resultsTotals
     */
    public List<String> getResultsTotals() {
        return resultsTotals;
    }

    /**
     * @param resultsTotals the resultsTotals to set
     */
    public void setResultsTotals(List<String> resultsTotals) {
        this.resultsTotals = resultsTotals;
    }

    /**
     * @return the folios
     */
    public Map getFolios() {
        return folios;
    }

    /**
     * @param folios the folios to set
     */
    public void setFolios(Map folios) {
        this.folios = folios;
    }

    /**
     * @return the procesoRecaudacion
     */
    public ProcesoRecaudacion getProcesoRecaudacion() {
        return procesoRecaudacion;
    }

    /**
     * @param procesoRecaudacion the procesoRecaudacion to set
     */
    public void setProcesoRecaudacion(ProcesoRecaudacion procesoRecaudacion) {
        this.procesoRecaudacion = procesoRecaudacion;
    }

    /**
     * @return the resumenRecaudacion
     */
    public ResumenRecaudacion getResumenRecaudacion() {
        return resumenRecaudacion;
    }

    /**
     * @param resumenRecaudacion the resumenRecaudacion to set
     */
    public void setResumenRecaudacion(ResumenRecaudacion resumenRecaudacion) {
        this.resumenRecaudacion = resumenRecaudacion;
    }

    /**
     * @return the cajaRecaudacion
     */
    public CajaRecaudacion getCajaRecaudacion() {
        return cajaRecaudacion;
    }

    /**
     * @param cajaRecaudacion the cajaRecaudacion to set
     */
    public void setCajaRecaudacion(CajaRecaudacion cajaRecaudacion) {
        this.cajaRecaudacion = cajaRecaudacion;
    }

    /**
     * @return the model
     */
    public RecaudacionDataModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(RecaudacionDataModel model) {
        this.model = model;
    }

    /**
     * @return the fechaRecaudacion
     */
    public Date getFechaRecaudacion() {
        return fechaRecaudacion;
    }

    /**
     * @param fechaRecaudacion the fechaRecaudacion to set
     */
    public void setFechaRecaudacion(Date fechaRecaudacion) {
        this.fechaRecaudacion = fechaRecaudacion;
    }

    /**
     * @return the permitirEgresoFlota
     */
    public Boolean getPermitirEgresoFlota() {
        return permitirEgresoFlota;
    }

    /**
     * @param permitirEgresoFlota the permitirEgresoFlota to set
     */
    public void setPermitirEgresoFlota(Boolean permitirEgresoFlota) {
        this.permitirEgresoFlota = permitirEgresoFlota;
    }

    /**
     * @return the permitirEgresoBus
     */
    public Boolean getPermitirEgresoBus() {
        return permitirEgresoBus;
    }

    /**
     * @param permitirEgresoBus the permitirEgresoBus to set
     */
    public void setPermitirEgresoBus(Boolean permitirEgresoBus) {
        this.permitirEgresoBus = permitirEgresoBus;
    }

    /**
     * @return the permitirEgresoProceso
     */
    public Boolean getPermitirEgresoProceso() {
        return permitirEgresoProceso;
    }

    /**
     * @param permitirEgresoProceso the permitirEgresoProceso to set
     */
    public void setPermitirEgresoProceso(Boolean permitirEgresoProceso) {
        this.permitirEgresoProceso = permitirEgresoProceso;
    }

    /**
     * @return the estadoProceso
     */
    public Boolean getEstadoProceso() {
        return estadoProceso;
    }

    /**
     * @param estadoProceso the estadoProceso to set
     */
    public void setEstadoProceso(Boolean estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    /**
     * @return the resumenTotal
     */
    public int getResumenTotal() {
        return resumenTotal;
    }

    /**
     * @param resumenTotal the resumenTotal to set
     */
    public void setResumenTotal(int resumenTotal) {
        this.resumenTotal = resumenTotal;
    }

    /**
     * @return the resumenTotalFormat
     */
    public String getResumenTotalFormat() {
        return resumenTotalFormat;
    }

    /**
     * @param resumenTotalFormat the resumenTotalFormat to set
     */
    public void setResumenTotalFormat(String resumenTotalFormat) {
        this.resumenTotalFormat = resumenTotalFormat;
    }

    /**
     * @return the recaudacionEgresoList
     */
    public List<RecaudacionEgreso> getRecaudacionEgresoList() {
        return recaudacionEgresoList;
    }

    /**
     * @param recaudacionEgresoList the recaudacionEgresoList to set
     */
    public void setRecaudacionEgresoList(List<RecaudacionEgreso> recaudacionEgresoList) {
        this.recaudacionEgresoList = recaudacionEgresoList;
    }

    /**
     * @return the pattern
     */
    public static String getPattern() {
        return pattern;
    }

    /**
     * @param aPattern the pattern to set
     */
    public static void setPattern(String aPattern) {
        pattern = aPattern;
    }

    /**
     * @return the decimalFormat
     */
    public static DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    /**
     * @param aDecimalFormat the decimalFormat to set
     */
    public static void setDecimalFormat(DecimalFormat aDecimalFormat) {
        decimalFormat = aDecimalFormat;
    }

    /**
     * @return the ventaCombustibleList
     */
    public List<PetroleoHelper> getVentaCombustibleList() {
        return ventaCombustibleList;
    }

    /**
     * @param ventaCombustibleList the ventaCombustibleList to set
     */
    public void setVentaCombustibleList(List<PetroleoHelper> ventaCombustibleList) {
        this.ventaCombustibleList = ventaCombustibleList;
    }

    /**
     * @return the registroMinutoList
     */
    public List<MinutosHelper> getRegistroMinutoList() {
        return registroMinutoList;
    }

    /**
     * @param registroMinutoList the registroMinutoList to set
     */
    public void setRegistroMinutoList(List<MinutosHelper> registroMinutoList) {
        this.registroMinutoList = registroMinutoList;
    }

    /**
     * @return the registroMinuto
     */
    public MinutosHelper getRegistroMinuto() {
        return registroMinuto;
    }

    /**
     * @param registroMinuto the registroMinuto to set
     */
    public void setRegistroMinuto(MinutosHelper registroMinuto) {
        this.registroMinuto = registroMinuto;
    }

    /**
     * @return the ventaCombustible
     */
    public PetroleoHelper getVentaCombustible() {
        return ventaCombustible;
    }

    /**
     * @param ventaCombustible the ventaCombustible to set
     */
    public void setVentaCombustible(PetroleoHelper ventaCombustible) {
        this.ventaCombustible = ventaCombustible;
    }

    public void setPetroleo(String petroleo) {
        this.petroleo = petroleo;
    }

    public String getPetroleo() {
        return petroleo;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }

    public String getMinutos() {
        return minutos;
    }

    @Override
    public Recaudacion prepareCreate(ActionEvent event) {
        super.prepareCreate(event); //To change body of generated methods, choose Tools | Templates.
        this.resumenTotalFormat = "$ 0";
        this.setRecaudacionEgresoList(egresosRecaudacion(this.getSelected()));
        this.getSelected().setRecaudacionFecha(fechaRecaudacion);
        this.getSelected().setRecaudacionIdCaja(cajaRecaudacion);
        return this.getSelected();
    }

    private List<RecaudacionEgreso> egresosRecaudacion(Recaudacion recaudacion) {
        List<RecaudacionEgreso> egresoGuiaAuxList = new ArrayList<>();

        int i = 0;

        for (Egreso e : this.egresosList) {

            if (e.getEgresoActivo()) {
                RecaudacionEgreso egresoGuia = new RecaudacionEgreso();
                egresoGuia.setRecaudacionEgresoIdRecaudacion(recaudacion);
                egresoGuia.setRecaudacionEgresoIdEgreso(e);
                egresoGuia.setRecaudacionEgresoMonto(e.getEgresoValorDefecto());

                egresoGuiaAuxList.add(egresoGuia);
            }

            i++;
        }

        return egresoGuiaAuxList;
    }

    @Override
    public void save(ActionEvent event) {

        super.save(event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveNew(ActionEvent event) {
        this.getSelected().setRecaudacionHora(new Date());
        this.getSelected().setRecaudacionEgresoList(recaudacionEgresoList);
        this.ejbFacade.create(this.getSelected());

        if (this.resumenRecaudacion.getResumenRecaudacionCerrado()) {
            this.resumenRecaudacion.setResumenRecaudacionCerrado(Boolean.FALSE);
            this.resumenRecaudacionFacade.edit(resumenRecaudacion);
        }

        /*
         * Agrega nueva fila a la tabla
         *
         */
        LinkedHashMap auxLink = new LinkedHashMap();
        for (RecaudacionEgreso eg : this.getSelected().getRecaudacionEgresoList()) {
            if (eg.getRecaudacionEgresoIdEgreso().getEgresoObligatorio()) {
                String key = eg.getRecaudacionEgresoIdEgreso().getEgresoNombre();

                if (totals.containsKey(key)) {
                    int aux = (int) totals.get(key);
                    aux += eg.getRecaudacionEgresoMonto();
                    totals.put(key, aux);
                } else {
                    totals.put(key, eg.getRecaudacionEgresoMonto());
                }
                auxLink.put(eg.getRecaudacionEgresoIdEgreso().getEgresoNombre(), eg.getRecaudacionEgresoMonto());
            }
        }

        this.getSelected().setLink(auxLink);
        this.getListOfMaps().add(this.getSelected().getLink());

        this.resultsTotals = new ArrayList<>();

        totals.values().stream().forEach((i) -> {
            this.resultsTotals.add(decimalFormat.format((int) i));
        });

        //dudas acá
        this.egresosResumenList.stream().forEach((eg) -> {
            if (totals.containsKey(eg.getEgresoResumenRecaudacionIdEgreso().getEgresoNombre())) {
                eg.setEgresoResumenRecaudacionTotal((int) totals.get(eg.getEgresoResumenRecaudacionIdEgreso().getEgresoNombre()));
            }
        });

        /*
        this.resumenRecaudacion.setResumenRecaudacionTotal(this.resumenTotal);
        this.resumenRecaudacion.setResumenRecaudacionFechaActualizacion(new Date());

        this.resumenRecaudacionFacade.edit(resumenRecaudacion);*/
        this.recaudacionItems.add(this.getSelected());

        notificationController.pushNotify("/notify", "Se ha ingresado la Guia N°" + this.getSelected().getRecaudacionIdentificador());

        this.setSelected(prepareCreate(event));
        this.setResumenTotalFormat(decimalFormat.format(getResumenTotal()));

        /*this.resumenRecaudacion.setEgresoRecaudacionList(egresosResumenList);
        this.resumenRecaudacionFacade.edit(this.resumenRecaudacion);*/
 /**/
    }

    public void load() {
        this.egresosList = new ArrayList<>();
        this.setListOfMaps(new ArrayList<>());
        this.setFolios(new HashMap<Integer, Integer>());
        this.setResultsHeader(new ArrayList<>());
        this.resultsTotals = new ArrayList<>();
        this.resumenRecaudacion = new ResumenRecaudacion();
        this.totals = new LinkedHashMap();

        /*NOMBRE DE LAS COLUMNAS*/
        if (this.permitirEgresoProceso) {
            for (EgresoProcesoRecaudacion epr : this.procesoRecaudacion.getEgresoProcesoRecaudacionList()) {

                if (epr.getEgresoProcesoRecaudacionIdEgreso().getEgresoObligatorio()) {
                    Egreso egreso = epr.getEgresoProcesoRecaudacionIdEgreso();
                    egreso.setEgresoValorDefecto(epr.getEgresoProcesoRecaudacionValorDefecto());
                    egreso.setEgresoPorcentaje(epr.getEgresoProcesoRecaudacionPorcentaje());
                    this.egresosList.add(egreso);
                }

            }
        } else {
            for (EgresoCajaRecaudacion ecr : this.cajaRecaudacion.getEgresoCajaRecaudacionList()) {
                Egreso egreso = ecr.getEgresoCajaRecaudacionIdEgreso();
                egreso.setEgresoValorDefecto(ecr.getEgresoCajaRecaudacionValorDefecto());
                egreso.setEgresoPorcentaje(ecr.getEgresoCajaRecaudacionPorcentaje());
                this.egresosList.add(egreso);
            }
        }

        this.recaudacionDao = new IRecaudacionDaoImpl();
        this.recaudacionItems = this.recaudacionDao.findByProcesoCajaFechaRecaudacion(procesoRecaudacion, cajaRecaudacion, fechaRecaudacion);
        JsfUtil.addSuccessMessage("Cantidad de Guías Registradas: " + this.recaudacionItems.size());

        /*Si la lista no está vacía*/
        if (!this.recaudacionItems.isEmpty()) {
            /*Proceso de carga de egresos a partir de cada guía*/
            for (Recaudacion g : this.recaudacionItems) {
                List<RecaudacionEgreso> auxEgresosGuia = g.getRecaudacionEgresoList();
                LinkedHashMap auxLink = new LinkedHashMap();

                for (RecaudacionEgreso eg : auxEgresosGuia) {
                    if (eg.getRecaudacionEgresoIdEgreso().getEgresoObligatorio()) {
                        String key = eg.getRecaudacionEgresoIdEgreso().getEgresoNombre();
                        this.getResultsHeader().add(key);
                        if (totals.containsKey(key)) {
                            int aux = (int) totals.get(key);
                            aux += eg.getRecaudacionEgresoMonto();
                            totals.put(key, aux);
                        } else {
                            totals.put(key, eg.getRecaudacionEgresoMonto());
                        }
                        auxLink.put(eg.getRecaudacionEgresoIdEgreso().getEgresoNombre(), eg.getRecaudacionEgresoMonto());
                    }
                }
                g.setLink(auxLink);
                this.getListOfMaps().add(g.getLink());
            }

            for (Object i : totals.values()) {
                resultsTotals.add(decimalFormat.format((int) i));
            }

        } else {
            LinkedHashMap auxLink = new LinkedHashMap();

            for (Egreso eg : this.egresosList) {
                if (eg.getEgresoObligatorio()) {
                    this.totals.put(eg.getEgresoNombre(), 0);
                    this.resultsTotals.add("0");
                    this.resultsHeader.add(eg.getEgresoNombre());
                    auxLink.put(eg.getEgresoNombre(), 0);
                }
            }

            this.listOfMaps.add(auxLink);
        }

        this.setModel(new RecaudacionDataModel(recaudacionItems));

        this.setBusesList(busesProceso(this.procesoRecaudacion));
        JsfUtil.addSuccessMessage("N° de Buses en el Proceso: " + this.getBusesList().size());

        this.resumenRecaudacionDao = new IResumenRecaudacionDaoImpl();
        this.resumenRecaudacion = this.resumenRecaudacionDao.findByCajaProcesoDate(cajaRecaudacion, procesoRecaudacion, fechaRecaudacion);

        if (this.resumenRecaudacion == null) {

            this.resumenRecaudacion = new ResumenRecaudacion();
            this.resumenRecaudacion.setResumenRecaudacionFechaIngreso(new Date());
            this.resumenRecaudacion.setResumenRecaudacionFecha(fechaRecaudacion);
            this.resumenRecaudacion.setResumenRecaudacionIdCaja(cajaRecaudacion);
            this.resumenRecaudacion.setResumenRecaudacionCerrado(Boolean.FALSE);
            this.resumenRecaudacion.setResumenRecaudacionIdProceso(procesoRecaudacion);
            this.resumenRecaudacion.setResumenRecaudacionTotal(0);

            this.egresosResumenList = new ArrayList<>();
            this.egresosList.stream().map((eg) -> {
                EgresoResumenRecaudacion egresoRecaudacion = new EgresoResumenRecaudacion();
                egresoRecaudacion.setEgresoResumenRecaudacionIdResumen(resumenRecaudacion);
                egresoRecaudacion.setEgresoResumenRecaudacionIdEgreso(eg);
                return egresoRecaudacion;
            }).map((egresoRecaudacion) -> {
                egresoRecaudacion.setEgresoResumenRecaudacionTotal(0);
                return egresoRecaudacion;
            }).forEach((egresoRecaudacion) -> {
                this.egresosResumenList.add(egresoRecaudacion);
            });
            //prueba
            this.listOfMaps.add(new LinkedHashMap());
            this.resumenRecaudacion.setEgresoResumenRecaudacionList(this.egresosResumenList);
            this.resumenRecaudacionFacade.create(this.resumenRecaudacion);

        } else {
            this.egresosResumenList = this.resumenRecaudacion.getEgresoResumenRecaudacionList();

            this.egresosResumenList.stream().forEach((EgresoResumenRecaudacion eg) -> {
                int val = (int) totals.get(eg.getEgresoResumenRecaudacionIdEgreso().getEgresoNombre());
                System.err.println(":" + val);
                eg.setEgresoResumenRecaudacionTotal(val);
            });
            this.resumenRecaudacion.setEgresoResumenRecaudacionList(this.egresosResumenList);

        }

        this.setResumenTotalFormat(decimalFormat.format(getResumenTotal()));
    }

    private List<Bus> busesProceso(ProcesoRecaudacion procesoRecaudacion) {
        this.busDao = new IBusDaoImpl();
        return this.busDao.findByProceso(procesoRecaudacion);
    }

    public void loadGuia() {
        if (this.getSelected() != null) {
            this.guiaLink = this.getSelected().getLink();
            this.recaudacionEgresoList = this.getSelected().getRecaudacionEgresoList();
            //this.porcentajesList = new ArrayList<PorcentajeHelper>();

            for (RecaudacionEgreso eg : this.recaudacionEgresoList) {
                if (this.totals.containsKey(eg.getRecaudacionEgresoIdEgreso().getEgresoNombre())) {
                    int val = (int) this.totals.get(eg.getRecaudacionEgresoIdEgreso().getEgresoNombre());
                    val -= eg.getRecaudacionEgresoMonto();
                    this.totals.put(eg.getRecaudacionEgresoIdEgreso().getEgresoNombre(), val);
                }
            }
        }
    }

    public int calculaTotal() {
        int total = 0;

        for (RecaudacionEgreso r : recaudacionEgresoList) {
            total = total + r.getRecaudacionEgresoMonto();
        }
        if (this.ventaCombustible != null) {
            total = total + ventaCombustible.getVentaCombustible().getVentaCombustibleTotal();
            if (this.ventaCombustible.getVentaCombustible().getVentaCombustibleIdBus() == null) {

            }
        }
        if (this.registroMinuto != null) {
            total = total + registroMinuto.getRegistro().getRegistroMinutoMonto();
            if (this.registroMinuto.getRegistro().getRegistroMinutoDesdeIdBus() == null) {

            }
        }
        this.getSelected().setRecaudacionTotal(total);
        this.resumenTotalFormat = decimalFormat.format(total);
        return total;
    }

    public int calculaTotalPetroleo() {
        int total = calculaTotal();

        if (this.ventaCombustible != null) {
            if (this.ventaCombustible.getVentaCombustible().getVentaCombustibleIdBus() == null) {
                this.petroleo = "Sin deuda";
            } else {
                total = total + ventaCombustible.getVentaCombustible().getVentaCombustibleTotal();
            }
        }

        this.getSelected().setRecaudacionTotal(total);

        return total;
    }

    public int calculaTotalMinutos() {
        int total = calculaTotal();

        if (this.registroMinuto != null) {
            if (this.registroMinuto.getRegistro().getRegistroMinutoDesdeIdBus() == null) {
                this.minutos = "Sin deuda";
            } else {
                total = total + registroMinuto.getRegistro().getRegistroMinutoMonto();
            }
        }

        this.getSelected().setRecaudacionTotal(total);

        return total;
    }

    public void handleCajaRecaudacionChange(ActionEvent event) {
        if (this.getCajaRecaudacion() != null) {
            this.setProcesoRecaudacionList(new ArrayList<ProcesoRecaudacion>());

            List<CajaProceso> cajaProcesoList = this.getCajaRecaudacion().getCajaProcesoList();

            for (CajaProceso cp : cajaProcesoList) {
                if (cp.getCajaProcesoActivo()) {
                    this.getProcesoRecaudacionList().add(cp.getCajaProcesoIdProceso());
                }
            }
        }
    }

    public void handleBusChange(ActionEvent event) {
        if (this.getSelected().getRecaudacionIdBus() != null) {
            this.registroMinutoList = null;
            this.ventaCombustibleList = null;
            this.minutos = "";
            this.petroleo = "";
            this.ventaCombustible = null;
            this.registroMinuto = null;

            List<RegistroMinuto> minutosList = this.registroMinutoDao.findByBusSinRecaudar(this.getSelected().getRecaudacionIdBus());

            if (minutosList.isEmpty()) {
                this.registroMinuto = null;
                /*RegistroMinuto minuto = new RegistroMinuto();
                minuto.setRegistroMinutoMonto(0);
                this.registroMinutoList.add(minuto);*/
            } else {
                this.registroMinutoList = new ArrayList<>();
                if (minutosList.size() > 1) {

                    int total = 0;
                    for (RegistroMinuto m : minutosList) {
                        total = total + m.getRegistroMinutoMonto();
                        MinutosHelper minuto = new MinutosHelper();
                        minuto.setRegistro(m);
                        minuto.setObservacion("$ " + decimalFormat.format(m.getRegistroMinutoMonto()) + "   N° Bus: " + m.getRegistroMinutoHastaIdBus().getBusNumero() + " - " + sdf.format(m.getRegistroMinutoFechaMinuto()));
                        this.registroMinutoList.add(minuto);
                    }
                    RegistroMinuto r = new RegistroMinuto();
                    r.setRegistroMinutoMonto(total);
                    MinutosHelper totalMinutos = new MinutosHelper();
                    totalMinutos.setObservacion("$ " + decimalFormat.format(total) + " Todos");
                    totalMinutos.setRegistro(r);
                    this.registroMinutoList.add(0, totalMinutos);
                    calculaTotal();

                } else {
                    RegistroMinuto r = minutosList.get(0);
                    MinutosHelper minuto = new MinutosHelper();
                    minuto.setRegistro(r);
                    minuto.setObservacion("$ " + decimalFormat.format(r.getRegistroMinutoMonto()) + "   N° Bus: " + r.getRegistroMinutoHastaIdBus().getBusNumero() + " - " + sdf.format(r.getRegistroMinutoFechaMinuto()));
                    this.registroMinutoList.add(minuto);
                }

            }

            List<VentaCombustible> combustibleList = this.ventaCombustibleDao.findByBusSinRecaudar(this.getSelected().getRecaudacionIdBus());

            if (combustibleList.isEmpty()) {
                this.ventaCombustible = null;
                /*VentaCombustible venta = new VentaCombustible();
                venta.setVentaCombustibleTotal(0);
                this.ventaCombustibleList.add(venta);*/
            } else {
                this.ventaCombustibleList = new ArrayList<>();

                for (VentaCombustible v : combustibleList) {
                    PetroleoHelper vPetroleo = new PetroleoHelper();
                    vPetroleo.setObservacion("$ " + decimalFormat.format(v.getVentaCombustibleTotal()) + " -  " + sdf.format(v.getVentaCombustibleFecha()));
                    vPetroleo.setVentaCombustible(v);
                    this.ventaCombustibleList.add(vPetroleo);
                }
            }

        }
    }

    public void handleFechaChange(ActionEvent event) {

    }

    public void exportPdf(ActionEvent event) {
        this.pdfReportController.setProcesoRecaudacion(this.procesoRecaudacion);
        this.pdfReportController.setRecaudacion(this.fechaRecaudacion);

        try {
            this.pdfReportController.PDF(event);
        } catch (JRException ex) {
            Logger.getLogger(RecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class MinutosHelper {

        private String observacion;
        private RegistroMinuto registro;

        public MinutosHelper(String observacion, RegistroMinuto registro) {
            this.observacion = observacion;
            this.registro = registro;
        }

        public MinutosHelper() {
        }

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

        public RegistroMinuto getRegistro() {
            return registro;
        }

        public void setRegistro(RegistroMinuto registro) {
            this.registro = registro;
        }

    }

    public class PetroleoHelper {

        private String observacion;
        private VentaCombustible ventaCombustible;

        public PetroleoHelper() {
        }

        public PetroleoHelper(String observacion, VentaCombustible ventaCombustible) {
            this.observacion = observacion;
            this.ventaCombustible = ventaCombustible;
        }

        public VentaCombustible getVentaCombustible() {
            return ventaCombustible;
        }

        public void setVentaCombustible(VentaCombustible ventaCombustible) {
            this.ventaCombustible = ventaCombustible;
        }

        public String getObservacion() {
            return observacion;
        }

        public void setObservacion(String observacion) {
            this.observacion = observacion;
        }

    }

}
