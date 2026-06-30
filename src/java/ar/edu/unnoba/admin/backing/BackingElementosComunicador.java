package ar.edu.unnoba.admin.backing;

import DAO.ElementoInteractivoDAO;
import DAO.TematicaDAO;
import ar.edu.unnoba.model.ElementoInteractivo;
import ar.edu.unnoba.model.Tematica;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BackingElementosComunicador implements Serializable {

    private Integer tematicaId;
    private Tematica tematica;
    private List<ElementoInteractivo> elementos;

    @EJB
    private TematicaDAO tematicaDAO;
    @EJB
    private ElementoInteractivoDAO elementoDAO;

    public void init() {
        if (tematicaId != null) {
            try {
                tematica = tematicaDAO.find(tematicaId);
                elementos = elementoDAO.findByTematicaId(tematicaId);
            } catch (Exception e) {
                elementos = Collections.emptyList();
            }
        }
    }

    public Integer getTematicaId() { return tematicaId; }
    public void setTematicaId(Integer tematicaId) { this.tematicaId = tematicaId; }

    public Tematica getTematica() { return tematica; }

    public List<ElementoInteractivo> getElementos() {
        return elementos != null ? elementos : Collections.<ElementoInteractivo>emptyList();
    }
}
