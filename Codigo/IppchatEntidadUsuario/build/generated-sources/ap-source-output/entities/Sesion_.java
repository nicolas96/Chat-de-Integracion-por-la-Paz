package entities;

import entities.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-13T23:12:27")
@StaticMetamodel(Sesion.class)
public class Sesion_ { 

    public static volatile SingularAttribute<Sesion, String> emailUsuario;
    public static volatile CollectionAttribute<Sesion, Usuario> usuarioCollection;
    public static volatile SingularAttribute<Sesion, String> token;

}