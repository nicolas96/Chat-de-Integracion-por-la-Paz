/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    $("#idFormulario").submit(function(){
        document.getElementById("errordiv").style.visibility= 'hidden' ;
        document.getElementById("errortxt").innerHTML = "";
        submitform(document.getElementById("correoUsuario"));
        return false;
    });
});
function submitform(x) {
  var f = document.getElementsByTagName('form')[0];
  if(f.checkValidity()) {
    consumirRecuperar(x.value);
  } 
}
function consumirRecuperar(x){
    var xmlhttp=new  XMLHttpRequest();
    xmlhttp.open("POST","http://imac-de-maurico:8080/IppchatServiciosAutenticacion/webresources/usuario/recuperar",true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    var parameters = {
    "emailUsuario": x};
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
        document.getElementById("errortxt").style.color='#D32F2F';
        document.getElementById("errordiv").style.visibility= 'visible' ;
        document.getElementById("errortxt").innerHTML = json.cause;
    }else{
        document.getElementById("errordiv").style.visibility= 'hidden' ;
        document.getElementById("errortxt").style.color='#444B41';       
        document.getElementById("errortxt").innerHTML = json.cause;
        document.getElementById("errordiv").style.visibility= 'visible' ;
    }     
}
function volver(){
    location.href="../index.html";
}
