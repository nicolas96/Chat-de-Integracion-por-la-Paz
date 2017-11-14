package entities;

import entities.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-13T23:12:27")
@StaticMetamodel(Codigo.class)
public class Codigo_ { 

    public static volatile SingularAttribute<Codigo, String> codigo;
    public static volatile SingularAttribute<Codigo, Integer> disponibilidad;
    public static volatile SingularAttribute<Codigo, String> emailUsuario;
    public static volatile CollectionAttribute<Codigo, Usuario> usuarioCollection;

}