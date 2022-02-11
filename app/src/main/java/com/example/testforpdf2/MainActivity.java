package com.example.testforpdf2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PdfHtmlHeaderAndFooter shet = new PdfHtmlHeaderAndFooter();
        shet.manipulatePdf();
    }


    public static class PdfHtmlHeaderAndFooter {
        public static final String SRC = "./src/main/resources/pdfhtml/";
        public static final String DEST = "./target/sandbox/pdfhtml/ipsum.pdf";

        public static void main(String[] args) throws IOException {
            File file = new File(DEST);
            file.getParentFile().mkdirs();
            String htmlSource = SRC + "ipsum.html";

            new PdfHtmlHeaderAndFooter().manipulatePdf(DEST);
        }

        public void manipulatePdf(String pdfDest) throws IOException {
            PdfWriter writer = new PdfWriter(pdfDest);
            PdfDocument pdfDocument = new PdfDocument(writer);
            String header = "pdfHtml Header and footer example using page-events";
            Document document = new Document(pdfDocument);
            Header headerHandler = new Header(header);
            Footer footerHandler = new Footer();

            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

            Paragraph farmId = new Paragraph("Farm ID: ").setMarginTop(0).setMarginBottom(0);
            Paragraph farmName = new Paragraph("Farm Name: ").setMarginTop(0).setMarginBottom(0);
            Paragraph ownerName = new Paragraph("Owner’s Name: ").setMarginTop(0).setMarginBottom(0);
            Paragraph farmLocation = new Paragraph("Location: ").setMarginTop(0).setMarginBottom(0);
            Paragraph filler = new Paragraph("");
            Paragraph reportLabels = new Paragraph("Month - Year\nHarvest Report\nRequested MM/DD/YYYY").setTextAlignment(TextAlignment.CENTER);

            float colWidth[]={225f, 300f, 225f};
            Table table = new Table(colWidth);
            table.setTextAlignment(TextAlignment.CENTER);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            Text greenSakahanda = new Text("Sakahanda");
            Color myColor = new DeviceRgb(37, 163, 88);
            greenSakahanda.setFontColor(myColor);
            table.addCell(new Cell().add(new Paragraph("Municipality of San Ildefonso\n" +
                    "Municipal Agriculture Office of San Ildefonso\n").add(greenSakahanda)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setFontSize(14f));

            float columnWidth[] = {100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f};
            Table table2 = new Table(columnWidth);
            table2.setMarginTop(0);
            table2.addCell(new Cell().add(new Paragraph("Harvest ID").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Plot ID").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Plot Name").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Crop").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Sowing Date").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Harvest Date").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Weight (tons)").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Market Price (per ton)").setBold()));
            table2.addCell(new Cell().add(new Paragraph("Gross Income").setBold()));

            for (int x=0; x<15; x++){
                for (int y=0; y<9; y++){
                    table2.addCell("content");
                }
            }


            document.add(table);
            document.add(reportLabels);
            document.add(filler).add(filler);
            document.add(farmId).add(farmName).add(ownerName).add(farmLocation);
            document.add(filler).add(filler);
            document.add(table2);
            document.close();
            Toast.makeText(view.getContext(), "oks na", Toast.LENGTH_SHORT).show();

            // Write the total number of pages to the placeholder
            footerHandler.writeTotal(pdfDocument);
            pdfDocument.close();
        }

        // Header event handler
        protected class Header implements IEventHandler {
            private String header;

            public Header(String header) {
                this.header = header;
            }

            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdf = docEvent.getDocument();

                PdfPage page = docEvent.getPage();
                Rectangle pageSize = page.getPageSize();

                Canvas canvas = new Canvas(new PdfCanvas(page), pageSize);
                canvas.setFontSize(18);

                // Write text at position
                canvas.showTextAligned(header,
                        pageSize.getWidth() / 2,
                        pageSize.getTop() - 30, TextAlignment.CENTER);
                canvas.close();
            }
        }

        // Footer event handler
        protected class Footer implements IEventHandler {
            protected PdfFormXObject placeholder;
            protected float side = 20;
            protected float x = 300;
            protected float y = 25;
            protected float space = 4.5f;
            protected float descent = 3;

            public Footer() {
                placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
            }

            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdf = docEvent.getDocument();
                PdfPage page = docEvent.getPage();
                int pageNumber = pdf.getPageNumber(page);
                Rectangle pageSize = page.getPageSize();

                // Creates drawing canvas
                PdfCanvas pdfCanvas = new PdfCanvas(page);
                Canvas canvas = new Canvas(pdfCanvas, pageSize);

                Paragraph p = new Paragraph()
                        .add("Page ")
                        .add(String.valueOf(pageNumber))
                        .add(" of");

                canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
                canvas.close();

                // Create placeholder object to write number of pages
                pdfCanvas.addXObjectAt(placeholder, x + space, y - descent);
                pdfCanvas.release();
            }

            public void writeTotal(PdfDocument pdf) {
                Canvas canvas = new Canvas(placeholder, pdf);
                canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),
                        0, descent, TextAlignment.LEFT);
                canvas.close();
            }
        }
    }
}
