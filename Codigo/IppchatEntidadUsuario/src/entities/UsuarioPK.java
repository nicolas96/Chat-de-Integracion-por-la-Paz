/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author maurico
 */
@Embeddable
public class UsuarioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private int idUsuario;
    @Basic(optional = false)
    @Column(name = "id_ciudad")
    private int idCiudad;

    public UsuarioPK() {
    }

    public UsuarioPK(int idUsuario, int idCiudad) {
        this.idUsuario = idUsuario;
        this.idCiudad = idCiudad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUsuario;
        hash += (int) idCiudad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPK)) {
            return false;
        }
        UsuarioPK other = (UsuarioPK) object;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.idCiudad != other.idCiudad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UsuarioPK[ idUsuario=" + idUsuario + ", idCiudad=" + idCiudad + " ]";
    }
    
}
