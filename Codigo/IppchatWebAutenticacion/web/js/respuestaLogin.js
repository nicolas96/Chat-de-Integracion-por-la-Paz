/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    $("#idFormulario").submit(function(){
        document.getElementById("errordiv").style.visibility= 'hidden' ;
        document.getElementById("errortxt").innerHTML = "";
        submitform(document.getElementById("correoUsuario"),document.getElementById("contrasenaUsuario"));
        return false;
    });
});
function consumirLogin(x,y){
    var em=x.value;
    var con=y.value;
    var xmlhttp=new  XMLHttpRequest();
    xmlhttp.open("POST","http://imac-de-maurico:8080/IppchatServiciosAutenticacion/webresources/usuario/login",true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    var parameters = {
    "emailUsuario": em,
    "contrasenaUsuario": con};
   xmlhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
        var json=this.responseText;
        respuesta(JSON.parse(json));
    }
  };
    xmlhttp.send(JSON.stringify(parameters)); 
}

function respuesta(json){
    if(json.valor==="false"){
        document.getElementById("errordiv").style.visibility= 'visible' ;
        document.getElementById("errortxt").innerHTML = "Los datos ingresados no son válidos, inténtalo de nuevo por favor";
    }else{
        document.getElementById("errordiv").style.visibility= 'hidden' ;
        document.getElementById("errortxt").innerHTML = "";
    }     
}
function submitform(x,y) {
  var f = document.getElementsByTagName('form')[0];
  if(f.checkValidity()) {
    consumirLogin(x,y);
  } 
}