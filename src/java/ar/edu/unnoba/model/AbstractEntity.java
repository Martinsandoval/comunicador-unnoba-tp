/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unnoba.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity<T> {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" + '}';
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof AbstractEntity)) return false;
        final AbstractEntity<?> other = (AbstractEntity<?>) obj;
        return this.id != null && this.id.equals(other.getId());
    }

}
