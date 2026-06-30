/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author Sandoval
 */

public abstract class AbstractDAO<T> {
  @PersistenceContext(unitName="comunicadorPU")
  protected EntityManager em;
  private Class<T> entityClass;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) throws Exception{
       em.persist(entity);
    }

    public void edit(T entity) throws Exception {
        em.merge(entity);
    }

    public void remove(T entity) throws Exception {
        em.remove(em.merge(entity));
    }

    public T find(Object id) throws Exception{
        return em.find(entityClass, id);
    }

    
  
    
}

