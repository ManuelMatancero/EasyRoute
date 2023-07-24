package com.matancita.sarante.util;

import com.matancita.sarante.domain.Cobrador;
import com.matancita.sarante.domain.Ruta;
import com.matancita.sarante.domain.Zona;
import com.matancita.sarante.servicio.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncriptarPassword {
      @Autowired
       RutaService rutaService;
        
    public static void main(String[] args) {
        
        var password = "123456";
        System.out.println("password: " + password);
        System.out.println("password encriptado:" + encriptarPassword(password));
        
        Ruta ruta = new Ruta();
        ruta.setIdRuta(5L);
        ruta.setNombre("MATANCITA");
        ruta.setDia("JUEVES");
        Zona zona = new Zona();
        zona.setIdZona(2L);
        ruta.setZona(zona);
        Cobrador cobrador = new Cobrador();
        cobrador.setIdCobrador(6L);
    
        
      
        
        
    }
    
    public static String encriptarPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    
}
