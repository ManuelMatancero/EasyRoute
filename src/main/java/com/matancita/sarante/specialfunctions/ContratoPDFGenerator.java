package com.matancita.sarante.specialfunctions;

import com.matancita.sarante.domain.Prestamo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Data;

@Data
public class ContratoPDFGenerator {

    private Prestamo prestamo;

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
        Paragraph title = new Paragraph("Contrato de prestamo (EASY ROUTE)", fontTitle);
        Paragraph company = new Paragraph("Invernandez", fontCompany);
        Paragraph date = new Paragraph(LocalDate.now().toString(), fontDate);
        String bodyContent = "El presente contrato establece que el cliente de nombre "+ prestamo.getCliente().getNombre() +" "
                + prestamo.getCliente().getApellido() + ", con el numero de cedula siguiente: " + prestamo.getCliente().getCedula() +", "
                + "tomo prestado la suma de: $" + prestamo.getMonto() + " pesos dominicanos a la compañia mencionada al inicio de este documento. Los cuales seran pagados en cuotas semanales de " +
                " un total de: $"+ prestamo.getPagares().get(0).getTotal() + " pesos dominicanos, durante "+ prestamo.getCuotas()+ " semanas.";
        String space = " ";
        Paragraph body = new Paragraph(bodyContent, fontBody);
        String firma = "Firma del cliente:";
        String line = "_____________________________________________";
        Paragraph footer = new Paragraph(firma, fontBody);
        String totalPrestado = "$" + prestamo.getMonto();
        Paragraph totalPrestadoLt = new Paragraph("Monto del prestamo: ", fontBody);
        Paragraph total = new Paragraph(totalPrestado, fontTotal);
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
        document.add(blankSpace);
        document.add(body);
        document.add(blankSpace);
        document.add(blankSpace);
        document.add(totalPrestadoLt);
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
