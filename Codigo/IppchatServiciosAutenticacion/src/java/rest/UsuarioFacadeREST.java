/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.Codigo;
import entities.Sesion;
import entities.Usuario;
import entities.UsuarioPK;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.PathSegment;
import mundo.FechaActual;

/**
 *
 * @author maurico
 */
@Stateless
@Path("usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "IppchatServiciosAutenticacionPU")
    private EntityManager em;

    private UsuarioPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;idUsuario=idUsuarioValue;idCiudad=idCiudadValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        entities.UsuarioPK key = new entities.UsuarioPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> idUsuario = map.get("idUsuario");
        if (idUsuario != null && !idUsuario.isEmpty()) {
            key.setIdUsuario(new java.lang.Integer(idUsuario.get(0)));
        }
        java.util.List<String> idCiudad = map.get("idCiudad");
        if (idCiudad != null && !idCiudad.isEmpty()) {
            key.setIdCiudad(new java.lang.Integer(idCiudad.get(0)));
        }
        return key;
    }

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }
    /**
     * Método que genera el token para el consumo de los servicios.
     * @return 
     */
    private String generateToken(){
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "").concat(FechaActual.timeToken());
    }
    /**
     * Metodo para registar a un usuario
     * @param usuario entidad de un usuario con las credenciales 
     * @param idCiudad Codigo con la referencia de la ciudad
     * @param codigo String con el codigo de verificación
     * @return String estructura JSON con la respuesta de la consulta
     */
    @POST
    @Path("registro")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public String registro(@QueryParam("codigo") String codigo,@QueryParam("idCiudad") int idCiudad,
            Usuario usuario){
            String resultado ="";
            try{
                UsuarioPK llavesF= new UsuarioPK();
                llavesF.setIdCiudad(idCiudad);
                Query queryCodigo=em.createNamedQuery("Codigo.findByCodigo");
                queryCodigo.setParameter("codigo", codigo);        
                Codigo cod = (Codigo) queryCodigo.getSingleResult();
                if(cod.getDisponibilidad()==0){                  
                    resultado = "{\"response\":\"KO\", \"cause\":\"El codigo ya ha sido utilizado\",\"valor\":\"false\"}";
                }else{
                    try{                        
                        Query queryEmail=em.createNamedQuery("Usuario.findByEmailUsuario");
                        queryEmail.setParameter("emailUsuario", usuario.getEmailUsuario());
                        Usuario user =(Usuario)queryEmail.getSingleResult();
                        resultado = "{\"response\":\"KO\", \"cause\":\"El correo ya esta registrado\",\"valor\":\"false\"}";
                    }catch (Exception e){
                        usuario.setFechaRegistro(FechaActual.timestamp());                                              
                        cod.setDisponibilidad(0);
                        cod.setEmailUsuario(usuario.getEmailUsuario());
                        usuario.setUsuarioPK(llavesF);
                        usuario.setCodigo(cod);
                        em.merge(cod);
                        em.merge(usuario);
                        resultado = "{\"response\":\"OK\", \"cause\":\"Se ha realizado el registro exitosamente\",\"valor\":\"true\"}";
                    }                  
                }          
            }catch (Exception e){
             resultado = "{\"response\":\"KO\", \"cause\":\"El codigo es invalido\",\"valor\":\"false\"}";
            }
        return resultado;
    }
     /**
     * Metodo para comprobar si las credenciales son validas
     * @param usuario entidad de un usuario con las credenciales 
     * @return String estructura JSON con la respuesta de la consulta
     */
    @POST
    @Path("login")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public String login( Usuario usuario){
        String resultado ="";   
        if(usuario.getEmailUsuario()==null){
            resultado = "{\"response\":\"KO\", \"cause\":'No se envio el correo\"}";
        }else{
            try{
                Query queryEmail=em.createNamedQuery("Usuario.findByEmailUsuario");
                queryEmail.setParameter("emailUsuario", usuario.getEmailUsuario());
                Usuario user =(Usuario)queryEmail.getSingleResult();
                if(user==null){
                    resultado = "{\"response\":\"KO\",\"cause\":\"No existe usuario\",\"valor\":\"false\"}";
                }else{
                    if(user.getContrasenaUsuario().equals(usuario.getContrasenaUsuario())){               
                        try{
                            Query querySesion= em.createNamedQuery("Sesion.findByEmailUsuario");
                            querySesion.setParameter("emailUsuario", usuario.getEmailUsuario());
                            Sesion sesion=(Sesion)querySesion.getSingleResult();
                            em.remove(sesion);
                        }catch(Exception e){ }
                        Sesion se= new Sesion(generateToken(),usuario.getEmailUsuario());
                        user.setFechaToken(FechaActual.timestamp());
                        user.setToken(se);
                        em.merge(se);
                        em.merge(user);
                        resultado = "{\"response\":\"OK\", \"token\":\""+user.getToken().getToken()+"\",\"valor\":\"true\"}";
                    }else{
                        resultado = "{\"response\":\"KO\",\"cause\":\"User not found\",\"valor\":\"false\"}";
                    }
                }
            }catch (Exception e){
             resultado = "{\"response\":\"KO\", \"cause\":\"El correo no esta registrado\",\"valor\":\"false\"}";
            }
        }
        return resultado;
    }
    /**
     * Metodo para comprobar si las credenciales son validas
     * @param usuario entidad de un usuario con las credenciales 
     * @return String estructura JSON con la respuesta de la consulta
     */
    @POST
    @Path("recuperar")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public String recuperarContrasena( Usuario usuario){
        String resultado ="";   
        String correo = "ippchat@gmail.com";
        String contrasena = "politecnico123";
        String asunto = "RECORDAR  CONTRASEÑA IPPCHAT";
        if(usuario.getEmailUsuario()==null){
            resultado = "{\"response\":\"KO\", \"cause\":'No se envio el correo\"}";
        }else{
            try{
                Query queryEmail=em.createNamedQuery("Usuario.findByEmailUsuario");
                queryEmail.setParameter("emailUsuario", usuario.getEmailUsuario());
                Usuario user =(Usuario)queryEmail.getSingleResult();
                if(user==null){
                    resultado = "{\"response\":\"KO\",\"cause\":\"No existe usuario\",\"valor\":\"false\"}";
                }else{
                    String mensaje = "\n\nHola "+user.getNombreUsuario()+",\n\nTu contraseña es: " + user.getContrasenaUsuario()+"\n\n\nYa puedes Ingresar!\n\n\nSaludos."
                            + "\n\nEl equipo de IPPCHAT.";
                    try {
                    Properties p = new Properties();
                    p.put("mail.smtp.host", "smtp.gmail.com");
                    p.setProperty("mail.smtp.starttls.enable", "true");
                    p.setProperty("mail.smtp.port", "587");
                    p.setProperty("mail.smtp.user", correo);
                    p.setProperty("mail.smtp.auth", "true");
                    Session s = Session.getInstance(p, null);                 
                    BodyPart texto = new MimeBodyPart();
                    texto.setText(mensaje); 
                    MimeMultipart m = new MimeMultipart();
                    m.addBodyPart(texto);
                    MimeMessage mensajeC = new MimeMessage(s);
                    mensajeC.setFrom(new InternetAddress(correo));
                    mensajeC.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmailUsuario()));
                    mensajeC.setSubject(asunto);
                    mensajeC.setContent(m);

                    Transport t = s.getTransport("smtp");
                    t.connect(correo, contrasena);
                    t.sendMessage(mensajeC, mensajeC.getAllRecipients());
                    t.close();
                     resultado = "{\"response\":\"OK\", \"cause\":\"Se ha enviado la contraseña al correo\",\"valor\":\"true\"}";
                } catch (MessagingException e) {
                  resultado="{\"response\":\"KO\", \"cause\":\"No se pudo enviar el correo, por favor vuelve a intentar.\",\"valor\":\"false\"}";
                   
                }
                }
            }catch (Exception e){
             resultado = "{\"response\":\"KO\", \"cause\":\"El correo no esta registrado\",\"valor\":\"false\"}";
            }
        }
        return resultado;
    }
    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Usuario entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") PathSegment id, Usuario entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        entities.UsuarioPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Usuario find(@PathParam("id") PathSegment id) {
        entities.UsuarioPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Usuario> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
