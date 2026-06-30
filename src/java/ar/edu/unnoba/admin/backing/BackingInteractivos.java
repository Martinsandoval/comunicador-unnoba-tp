/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unnoba.admin.backing;

import DAO.ElementoInteractivoDAO;
import DAO.TematicaDAO;
import ar.edu.unnoba.model.ElementoInteractivo;
import ar.edu.unnoba.model.InteractivoCompuesto;
import ar.edu.unnoba.model.InteractivoFinal;
import ar.edu.unnoba.model.Tematica;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author Sandoval
 */
@ManagedBean
@ViewScoped

public class BackingInteractivos {

    private boolean isCompuesto = true;
    private boolean isPrimerNivel = true;
    private Tematica tematica;
    private Part imagen;
    private InteractivoCompuesto elementosuperior;
    private String nombre;
    private ElementoInteractivo elemento;
    @EJB
    private ElementoInteractivoDAO interactivoDAO;
    @EJB
    private TematicaDAO tematicaDAO;
    
    
    public BackingInteractivos() {
    }
    
    public void establecerTipoElemento(){
        elemento = (isCompuesto)? new InteractivoCompuesto():new InteractivoFinal();
    }
    
     public Part getImagen() {
        return imagen;
    }

    public void setImagen(Part imagen) {
        this.imagen = imagen;
    }
    
     public Tematica getTematica() {
        return tematica;
    }

    public void setTematica(Tematica tematica) {
        this.tematica = tematica;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Tematica>getTematicas(){
        return tematicaDAO.findAll();
    }
    
    public List<ElementoInteractivo>getElementosCompuestos(){
        return interactivoDAO.compuestos();
    }
    
    public List<ElementoInteractivo>getElementos(){
        return interactivoDAO.findAll();
    }
    
      public boolean isIsCompuesto() {
        return isCompuesto;
    }

    public void setIsCompuesto(boolean isCompuesto) {
        this.isCompuesto = isCompuesto;
    }

    public boolean isIsPrimerNivel() {
        return isPrimerNivel;
    }

    public void setIsPrimerNivel(boolean isPrimerNivel) {
        this.isPrimerNivel = isPrimerNivel;
    }

    public InteractivoCompuesto getElementosuperior() {
        return elementosuperior;
    }

    public void setElementosuperior(InteractivoCompuesto elementosuperior) {
        this.elementosuperior = elementosuperior;
    }
    
     public ElementoInteractivo getElemento() {
        return elemento;
    }

    public void setElemento(ElementoInteractivo elemento) {
        this.elemento = elemento;
    }
    
    public String agregar(){
        if(isCompuesto){
                elemento = new InteractivoCompuesto();
                elemento.setTipo("compuesto");
               
        }else{
            elemento = new InteractivoFinal();
            elemento.setTipo("final");
        }
        try{
            elemento.setNombre(nombre);
            elemento.setTematica(tematica);
            elemento.setInteractivocompuesto(elementosuperior);
            interactivoDAO.create(elemento);
              FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("El elemento fué agregado exitosamente"));
            return "success";
            }catch(Exception e){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error al crear elemento"));
            return "failure";
            
        }
       
    }
     public String borrarElemento(int id){
       try{
        elemento = interactivoDAO.find(id);
        interactivoDAO.remove(elemento);
        return "success";
       }catch(Exception e){
           FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error al eliminar"));
           return "failure";
           
       }
      }

}

