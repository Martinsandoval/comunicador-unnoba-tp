/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unnoba.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sandoval
 */


@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo", discriminatorType=DiscriminatorType.STRING)
@NamedQueries({
@NamedQuery(name = "elementos.all", query = "Select c from ElementoInteractivo c order by c.nombre"),
@NamedQuery(name = "compuesto2.all", query = "Select c from ElementoInteractivo c where c.tipo='compuesto' order by c.nombre"),
@NamedQuery(name = "elementos.porTematica", query = "Select c from ElementoInteractivo c where c.tematica.id = :tematicaId order by c.nombre")})

@Entity
@Table(name="elementos_iteractivos")
public abstract class ElementoInteractivo extends AbstractEntity implements Serializable{
   @ManyToOne
   @JoinColumn(name = "tematicas_id", referencedColumnName="id")
    private Tematica tematica;
    private String nombre;
    private String tipo;
    private String img_path;
    @ManyToOne
    @JoinColumn(name ="elementos_id", referencedColumnName = "id")
    private InteractivoCompuesto interactivocompuesto;

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    
    
    public ElementoInteractivo(){
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
    
 public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
   
  public InteractivoCompuesto getInteractivocompuesto() {
        return interactivocompuesto;
    }

    public void setInteractivocompuesto(InteractivoCompuesto interactivocompuesto) {
        this.interactivocompuesto = interactivocompuesto;
    }
}
