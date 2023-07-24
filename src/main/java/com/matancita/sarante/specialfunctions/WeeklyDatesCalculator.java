package com.matancita.sarante.specialfunctions;

import com.matancita.sarante.domain.Pagare;
import com.matancita.sarante.domain.Prestamo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author manue
 */
public class WeeklyDatesCalculator {
        //this method is to generate the pagares
    public List<Pagare> weeklyIterator(LocalDateTime startDate,Long idPrestamo, int cuotas, double capital, double interes){
        //List of pagare
        List<Pagare> pagares = new ArrayList<>();
        //Here I calculate capitalPerweek
        double capitalPerWeek = capital/cuotas;
        //Here I calculate interesPerweek
        double interesPerWeek = interes/cuotas;
        //Here I calculate totalPagare
        double totalPagare = capitalPerWeek + interesPerWeek;
        //create an prestamo object
        Prestamo prestamo = new Prestamo();
        prestamo.setIdPrestamo(idPrestamo);

        for (int i = 1; i <= cuotas; i++) {
            LocalDateTime expireDate = startDate.plusWeeks(i);
            Pagare pagare = new Pagare();
            pagare.setNoPagare(i);
            pagare.setCapital(capitalPerWeek);
            pagare.setInteres(interesPerWeek);
            pagare.setTotal(totalPagare);
            pagare.setVencimiento(expireDate);
            pagare.setPrestamo(prestamo);
            //Add the pagare to the list
            pagares.add(pagare);
        }

        return pagares;

    }
}
