/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.Ciudad;
import java.util.List;
import javax.ejb.Stateless;
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

/**
 *
 * @author maurico
 */
@Stateless
@Path("ciudad")
public class CiudadFacadeREST extends AbstractFacade<Ciudad> {
    @PersistenceContext(unitName = "IppchatServiciosAutenticacionPU")
    private EntityManager em;

    public CiudadFacadeREST() {
        super(Ciudad.class);
    }
 /**
    *Metodo para listar las ciudades de la base de datos 
    *@param clave Se recibe un string para permitir la consulta
    *@return retorna un Arreglo que contine todas las ciudades de la BD
    */
    @POST
    @Path("listar")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public String lista( String clave ){
        String resultado =""; 
        if(clave.equals("1234")){
            resultado ="{";   
            try{
            Query queryCiudad=em.createNamedQuery("Ciudad.findAll");
            List<Ciudad> listC = queryCiudad.getResultList();
            int cont=0;
                while(!listC.isEmpty()){
                    if(listC.size()==1){
                        resultado+="\""+cont+"\":"+"{\"nombre\":"+"\""+listC.get(0).getNombreCiudad()+"\",\"id\":"+"\""+
                                listC.remove(0).getIdCiudad()+"\"}";
                    }else{
                         resultado+="\""+cont+"\":"+"{\"nombre\":"+"\""+listC.get(0).getNombreCiudad()+"\",\"id\":"+"\""+
                                listC.remove(0).getIdCiudad()+"\"},";
                    }
                    cont++;
                }
            resultado+="}";
            }catch (Exception e){
             resultado = "{}]";
            }      
        }else{
            resultado="No tiene acceso para la consulta";
        }
      
      return resultado; 
    }
    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Ciudad entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Ciudad entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Ciudad find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Ciudad> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Ciudad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
