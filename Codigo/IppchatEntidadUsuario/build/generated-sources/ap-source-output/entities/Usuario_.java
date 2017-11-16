package entities;

import entities.Ciudad;
import entities.Codigo;
import entities.Sesion;
import entities.UsuarioPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-13T23:12:27")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Codigo> codigo;
    public static volatile SingularAttribute<Usuario, String> apellidoUsuario;
    public static volatile SingularAttribute<Usuario, String> contrasenaUsuario;
    public static volatile SingularAttribute<Usuario, String> fechaToken;
    public static volatile SingularAttribute<Usuario, String> fechaRegistro;
    public static volatile SingularAttribute<Usuario, Ciudad> ciudad;
    public static volatile SingularAttribute<Usuario, String> emailUsuario;
    public static volatile SingularAttribute<Usuario, String> nombreUsuario;
    public static volatile SingularAttribute<Usuario, UsuarioPK> usuarioPK;
    public static volatile SingularAttribute<Usuario, Sesion> token;

}