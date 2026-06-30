/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import ar.edu.unnoba.model.ElementoInteractivo;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Sandoval
 */
@Stateless
@LocalBean
public class ElementoInteractivoDAO extends AbstractDAO<ElementoInteractivo>{
    
     public ElementoInteractivoDAO(Class<ElementoInteractivo> entityClass) {
        super(entityClass);
    }

 
public ElementoInteractivoDAO(){
    super(ElementoInteractivo.class);
}

public List compuestos(){
      Query query=em.createNamedQuery("compuesto2.all");
      return query.getResultList();
    }
public List findAll(){
      Query query=em.createNamedQuery("elementos.all");
      return query.getResultList();
    }

public List<ElementoInteractivo> findByTematicaId(Integer tematicaId) {
    return em.createNamedQuery("elementos.porTematica", ElementoInteractivo.class)
             .setParameter("tematicaId", tematicaId)
             .getResultList();
}

}
