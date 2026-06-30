/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

/**
 *
 * @author Sandoval
 */
import DAO.AbstractDAO;
import ar.edu.unnoba.model.AbstractEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author jpgm
 */
public abstract class AbstractEntityConverter<T> implements Converter{
  
  public abstract String getDAOName();
    
  private AbstractDAO<T> getEntityDAO(FacesContext context){
      InitialContext ic;  
      try {
          ic = new InitialContext();
          return (AbstractDAO<T>)ic.lookup("java:module/"+getDAOName());  
      } catch (NamingException ex) {}
      return null;
  }
  
  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    try{
        return this.getEntityDAO(context).find(Integer.valueOf(value));
    }catch(Exception e){
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value == null) return "0";
    Integer id = ((AbstractEntity)value).getId();
    return id == null ? "0" : id.toString();
  }
}
