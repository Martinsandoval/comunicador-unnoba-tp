package ar.edu.unnoba.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("final")
@Entity
public class InteractivoFinal extends ElementoInteractivo {

}
