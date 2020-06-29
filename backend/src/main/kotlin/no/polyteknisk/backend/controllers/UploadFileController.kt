package no.polyteknisk.backend.controllers

import no.polyteknisk.backend.services.CrmReportService
import no.polyteknisk.backend.services.MemberService
import no.polyteknisk.backend.services.RepeatingInvoiceService
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/upload")
class UploadFileController(
        private val memberService: MemberService,
        private val crmReportService: CrmReportService,
        private val repeatingInvoiceService: RepeatingInvoiceService
) {

    @Transactional
    @PostMapping("/crm")
    fun uploadCrmFile(@RequestParam file: MultipartFile) {
        println("Starting uploading")
        val inputStream = file.inputStream
        memberService.deleteAllMembers()
        val members = crmReportService.parse(inputStream)
        println("Done uploading, starting to persist")
        memberService.saveAll(members)
        println("Done persisting")
    }

    @Transactional
    @PostMapping("/repeterende-faktura")
    fun uploadRepeterendeFakturaFile(@RequestParam file: MultipartFile) {
        println("Starting uploading")
        val inputStream = file.inputStream
        repeatingInvoiceService.parse(inputStream)
        println("Done uploading, starting to persist")
        println("Done persisting")
    }

}