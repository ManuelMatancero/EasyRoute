package com.matancita.sarante.specialfunctions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.matancita.sarante.domain.Pagare;

import lombok.Data;

@Data
public class ReciboPDFGenerator {
     private Pagare pagare;

    public void generate(HttpServletResponse response) throws DocumentException, IOException{
        //Creating the object of document
        Document document = new Document(PageSize.B5);
        //Getting instance PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());

        //Openning the document
        document.open();

        //creattig font
        //Setting font style and size
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font fontCompany = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        Font fontTotal = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTotal.setSize(25);
        fontCompany.setSize(18);
        fontDate.setSize(14);
        fontTitle.setSize(20);
        Font fontBody = FontFactory.getFont(FontFactory.HELVETICA);
        fontBody.setSize(15);

        //Creatting new paragraph
        Paragraph title = new Paragraph("Recibo de prestamo", fontTitle);
        Paragraph company = new Paragraph("INVERCREDY", fontCompany);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        String cabeceroF = "Fecha: " + formatter.format(pagare.getVencimiento());
        String cabeceroN = "Numero de pagare: " + String.valueOf(pagare.getNoPagare());
        Paragraph date = new Paragraph(cabeceroF, fontDate);
        Paragraph no = new Paragraph(cabeceroN, fontDate);
        String bodyContent = "Recibi de " + pagare.getPrestamo().getCliente().getNombre() + " "+pagare.getPrestamo().getCliente().getApellido() + 
        ", la cantidad de: " + pagare.getTotal() + " por concepto de pago de cuotas del prestamo con codigo: " + pagare.getPrestamo().getIdPrestamo() + 
        ", el cual se cobra de manera semanal los dias, " + pagare.getPrestamo().getCliente().getRuta().getDia()+ ".";
        String space = " ";
        Paragraph body = new Paragraph(bodyContent, fontBody);
        String firma = "Firma del cliente:";
        String line = "_____________________________________________";
        Paragraph footer = new Paragraph(firma, fontBody);
        String totalPagare = "$" + pagare.getTotal();
        Paragraph totalPagareLt = new Paragraph("Monto del pagare: ", fontBody);
        Paragraph total = new Paragraph(totalPagare, fontTotal);
        Paragraph lineToFirma = new Paragraph(line);
        Paragraph blankSpace =new Paragraph(space);

        //Aligning the paragrhap in document
        title.setAlignment(Paragraph.ALIGN_CENTER);
        company.setAlignment(Paragraph.ALIGN_CENTER);
        body.setAlignment(Paragraph.ALIGN_JUSTIFIED);

        //Adding created paragraph to the document
        document.add(title);
        document.add(company);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(date);
        document.add(blankSpace);
        document.add(no);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(body);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(totalPagareLt);
        document.add(total);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(footer);
        document.add(blankSpace);
        document.add(lineToFirma);
        document.close();

    }
}
