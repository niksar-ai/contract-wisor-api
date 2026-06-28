package ai.niksar.contract_wisor_api.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class DocumentParserService {

    private final Tika tika = new Tika();
    public String determineFileType(byte[] documentData) throws IOException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(documentData)) {
            return tika.detect(is);
        }
    }

    public  int determinePageCount(byte[] documentData, String fileType) throws Exception {
        if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType)) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(documentData);
            XWPFDocument document = new XWPFDocument(inputStream);
            int characterCount = 0;
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                characterCount += paragraph.getText().length();
            }
            // Calculate the approximate page count (e.g. every 2000 characters can be treated as one page)
            int approximatePageCount = (characterCount / 2000) + 1;
            return approximatePageCount;

        }  else if ("application/pdf".equals(fileType)) {
            PDDocument document = PDDocument.load(new ByteArrayInputStream(documentData));
            return document.getNumberOfPages();
        } else {
            return 0;
        }
    }
}
