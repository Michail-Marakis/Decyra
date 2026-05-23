package com.example.decyra.extras;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * The type Html file exporter.
 */
public class HTMLFileExporter {

    /**
     * Export to html boolean.
     *
     * @param context the context
     * @param uri     the uri
     * @param content the content
     * @return the boolean
     */
    public static boolean exportToHtml(Context context, Uri uri, String content) {
        try (OutputStream os = context.getContentResolver().openOutputStream(uri)) {
            if (os == null) {
                Toast.makeText(context, "Cannot open file", Toast.LENGTH_SHORT).show();
                return false;
            }

            String safeContent = escapeHtml(content == null ? "" : content.trim());

            String html =
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"el\">\n" +
                            "<head>\n" +
                            "  <meta charset=\"UTF-8\">\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "  <title>DECYRA Results</title>\n" +
                            "  <style>\n" +
                            "    body {\n" +
                            "      font-family: Arial, sans-serif;\n" +
                            "      line-height: 1.6;\n" +
                            "      padding: 16px;\n" +
                            "      color: #222;\n" +
                            "      background: #fff;\n" +
                            "    }\n" +
                            "    h1 {\n" +
                            "      font-size: 22px;\n" +
                            "      margin-bottom: 16px;\n" +
                            "    }\n" +
                            "    .content {\n" +
                            "      white-space: pre-wrap;\n" +
                            "      word-wrap: break-word;\n" +
                            "      font-size: 15px;\n" +
                            "    }\n" +
                            "  </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "  <h1>DECYRA Results</h1>\n" +
                            "  <div class=\"content\">" + safeContent + "</div>\n" +
                            "</body>\n" +
                            "</html>";

            os.write(html.getBytes(StandardCharsets.UTF_8));
            os.flush();

            Toast.makeText(context, "HTML saved on phone", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating HTML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}