package com.example.phasmatic.extras;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class PDF {

    public static String exportToPdf(Context context, String fileName, String content) {
        try {
            //Φάκελος: /Android/data/yourapp/files/Documents/
            File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir == null) {
                Toast.makeText(context, "Storage error", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName + ".pdf");

            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //Title
            document.add(new Paragraph("AI Response")
                    .setBold()
                    .setFontSize(18));

            document.add(new Paragraph("\n"));

            //Content (LLM output)
            document.add(new Paragraph(content)
                    .setFontSize(12));

            document.close();

            Toast.makeText(context, "PDF saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}