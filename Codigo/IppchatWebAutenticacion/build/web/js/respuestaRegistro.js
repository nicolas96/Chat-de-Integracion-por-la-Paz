/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


  var contrasena, contrasena2,ciudades;
$(document).ready(function(){
    desplegarCiudades();
    clave();
    $("#idFormulario").submit(function(){
        document.getElementById("errordiv").style.visibility= 'hidden' ;
        document.getElementById("errortxt").innerHTML = "";
        submitform();
        return false;
    });
    contrasena = document.getElementById('contrasenaUsuario');
    contrasena2 = document.getElementById('contrasenaUsuario2');
    contrasena.onchange = contrasena2.onkeyup =  passwordMatch;  
});
 function passwordMatch() {
        if((contrasena.value !== contrasena2.value))
         contrasena2.setCustomValidity('Las contraseñas no coinciden.');
        else
            contrasena2.setCustomValidity(''); 
    }
function volver(){
    location.href="../index.html";
}
function clave(){
    var input =document.getElementById("correoUsuario");
    input.pattern="[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{1,5}";
}
function desplegarCiudades(){
    var xmlhttp=new  XMLHttpRequest();
    xmlhttp.open("POST","http://localhost:8080/IppchatServiciosAutenticacion/webresources/ciudad/listar",true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
   var parameters="1234";
   xmlhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
        ciudades=JSON.parse(this.responseText);
        llenarCampos();
    }
  };
    xmlhttp.send(parameters);
}
function llenarCampos(){
    var x=document.getElementById("scrollRegistro");
    var tam=Object.keys(ciudades).length-1;
    for (var i=0;i<tam;i++){
        var option=document.createElement("option");
        option.text=ciudades[i].nombre;
        option.id=ciudades[i].id;
        x.add(option);
    }
    document.getElementById("scrollRegistro").value=0;  
}
function submitform() {
  var f = document.getElementsByTagName('form')[0]; 
  if(f.checkValidity()) {
    consumirRegistro(document.getElementById("nombreUsuario").value,
                        document.getElementById("apellidoUsuario").value,
                        ciudades[document.getElementById("scrollRegistro").selectedIndex].id,
                        document.getElementById("correoUsuario").value,
                        document.getElementById("contrasenaUsuario").value,
                        document.getElementById("codigoIngreso").value);
  } 
}
function consumirRegistro(nombre,apellido,ciudad,correo,contrasena,codigo){
    var xmlhttp=new  XMLHttpRequest();
    xmlhttp.open("POST","http://localhost:8080/IppchatServiciosAutenticacion/webresources/usuario/registro?codigo="+codigo+
            "&idCiudad="+ciudad,true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    var parameters = {
    "nombreUsuario": nombre,
    "apellidoUsuario": apellido,
    "emailUsuario": correo,
    "contrasenaUsuario": contrasena};
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