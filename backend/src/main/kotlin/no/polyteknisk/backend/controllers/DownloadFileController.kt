package no.polyteknisk.backend.controllers

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.entities.MemberError
import no.polyteknisk.backend.repositories.MemberErrorRepository
import no.polyteknisk.backend.repositories.MemberRepository
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/download")
class DownloadFileController(
        val memberRepository: MemberRepository,
        val memberErrorRepository: MemberErrorRepository
) {

    @GetMapping("/tu")
    fun exportTu(response: HttpServletResponse) {

        val members = memberRepository.findAllByTekniskUkeblad(true)
        val filename: String = createFilename("TU Medlemskap.xlsx")

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"$filename\"")
        response.setHeader("filename", filename)
        response.characterEncoding = "UTF-8"

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Report")

        writeTuHeaders(sheet)

        for ((rowCount, member) in members.withIndex()) {
            val row = sheet.createRow(rowCount + 1)
            writeTuMember(member, row)
        }

        workbook.write(response.outputStream)
        response.flushBuffer()
    }

    @GetMapping("/member-error")
    fun exportMemberErrors(response: HttpServletResponse) {

        val errors = memberErrorRepository.findAll()
        val filename = createFilename("CRM Errors.xlsx")

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"$filename\"")
        response.setHeader("filename", filename)
        response.characterEncoding = "UTF-8"

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Report")

        writeMemberErrorHeaders(sheet)

        for ((rowCount, error) in errors.withIndex()) {
            val row = sheet.createRow(rowCount + 1)
            writeMemberError(error, row)
        }

        workbook.write(response.outputStream)
        response.flushBuffer()

    }

    private fun writeMemberError(error: MemberError, row: Row) {
        var cell = row.createCell(0)
        cell.setCellValue(error.memberId.toDouble())

        cell = row.createCell(1)
        cell.setCellValue(error.errorMessage)
    }

    private fun writeMemberErrorHeaders(sheet: XSSFSheet) {
        val headerRow = sheet.createRow(0)
        var cell = headerRow.createCell(0)
        cell.setCellValue("Kundenummer")
        cell = headerRow.createCell(1)
        cell.setCellValue("Error message")
    }

    private fun createFilename(name : String): String {

        val date = LocalDate.now()
        val year = date.year.toString()
        val month = prependZero(date.monthValue)
        val day = prependZero(date.dayOfMonth)

        return year + "_" + month + "_" + day + " - " + name
    }

    private fun prependZero(int: Int): String {
        return if (int < 10) {
            "0$int"
        } else {
            int.toString()
        }
    }

    private fun writeTuHeaders(sheet: XSSFSheet) {
        val headerRow = sheet.createRow(0)
        var cell = headerRow.createCell(0)
        cell.setCellValue("Fornavn")
        cell = headerRow.createCell(1)
        cell.setCellValue("Etternavn")
        cell = headerRow.createCell(2)
        cell.setCellValue("Adresse")
        cell = headerRow.createCell(3)
        cell.setCellValue("Postnummer")
        cell = headerRow.createCell(4)
        cell.setCellValue("Poststed")
    }

    private fun writeTuMember(member: Member, row: Row) {
        var cell = row.createCell(0)
        cell.setCellValue(member.firstName)

        cell = row.createCell(1)
        cell.setCellValue(member.lastName)

        cell = row.createCell(2)
        cell.setCellValue(member.address)

        cell = row.createCell(3)
        cell.setCellValue(member.areaCode)

        cell = row.createCell(4)
        cell.setCellValue(member.postalCode)
    }

}