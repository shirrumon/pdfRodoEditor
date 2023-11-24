package pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding
import java.io.File
import java.io.IOException


class TextReplacer(private val filePath: String, replaceToPublishedAt: String, replaceToCompanyName: String) {
    private var replacementText = ""

    private val text: ArrayList<String> = arrayListOf(
        "I hereby declare that I have familiarized",
        "myself with the Privacy Statement",
        "for Applicants published at ",
        "$replaceToPublishedAt and I",
        "hereby give consent for personal data ",
        "included in my application",
        "to be processed for the purposes of ",
        "recruitment in $replaceToCompanyName",
        "(Polska) Sp. z o. o. according to rules described ",
        "in the Privacy Statement for Applicants, as per the ",
        "Regulation (EU) 2016/679 of the European",
        "Parliament and of the Council of 27 April 2016 on ",
        "the protection of natural persons with regard to",
        "the processing of personal data and on the",
        "free movement of such data, ",
        "and repealing Directive 95/46/EC (GDPR)."
    )

    @Throws(IOException::class)
    fun replaceText(pathToSave: String) {
        PDDocument.load(File(filePath)).use { document ->
            val page = document.getPage(0) as PDPage
            replaceTextInPage(page, document)

            document.save(pathToSave.replace("Downloads", "Documents"))
        }
    }

    private fun replaceTextInPage(page: PDPage, document: PDDocument) {
        val contentStream = PDPageContentStream(document, page, AppendMode.APPEND, true, true)
        contentStream.use {
            var yOffset = 830f
            text.forEach {
                val font = PDType1Font.COURIER_BOLD // You can use other PDType1Font constants
                contentStream.setFont(font, 8f)

                contentStream.beginText()
                contentStream.newLineAtOffset(250f, yOffset) // Adjust the position as needed
                contentStream.showText(it)
                contentStream.endText()
                yOffset -= 10
            }
        }
    }

    private fun remove(string: String): String {
        val b = StringBuilder()
        for (i in string.indices) {
            if (WinAnsiEncoding.INSTANCE.contains(string[i].toString())) {
                b.append(string[i].toString())
            }
        }
        return b.toString()
    }
}
