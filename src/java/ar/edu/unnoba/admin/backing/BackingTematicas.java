package ar.edu.unnoba.admin.backing;

import DAO.TematicaDAO;
import ar.edu.unnoba.model.Tematica;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class BackingTematicas implements Serializable {
   private Tematica tematica;
   @EJB
   private TematicaDAO tematicaDAO;

    public BackingTematicas() {
        this.tematica = new Tematica();
    }

    public Tematica getTematica() {
        return tematica;
    }

    public void setTematica(Tematica tematica) {
        this.tematica = tematica;
    }

    public String agregar() {
        try {
            tematicaDAO.create(tematica);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("La tematica fué creada exitosamente"));
            return "success";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error al crear tematica"));
            return "failure";
        }
    }

    public List<Tematica> getTematicas() {
        return tematicaDAO.findAll();
    }

    public String borrarTematica(int id) {
        try {
            tematica = tematicaDAO.find(id);
            tematicaDAO.remove(tematica);
            return "success";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error al eliminar"));
            return "failure";
        }
    }

    public String activar(int id) {
        try {
            tematica = tematicaDAO.find(id);
            if (tematica.isActiva()) {
                tematica.setActiva(false);
            } else {
                tematica.setActiva(true);
            }
            tematicaDAO.edit(tematica);
            return "success";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No se puedo activar/desactivar"));
            return "failure";
        }
    }
}
