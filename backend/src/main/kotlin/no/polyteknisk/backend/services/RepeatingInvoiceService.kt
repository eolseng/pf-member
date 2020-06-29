package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.RepeatingInvoice
import no.polyteknisk.backend.entities.RepeatingInvoiceDTO
import no.polyteknisk.backend.repositories.MemberRepository
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDate

@Service
class RepeatingInvoiceService(
        val memberRepository: MemberRepository
) {

    fun parse(inputStream: InputStream) {
        val csvString = parseToString(inputStream)
        val dtos = convertToDTOs(csvString)
        val invoices = convertToInvoices(dtos)
        println("BREAK")
    }

    private fun convertToInvoices(dtos: List<RepeatingInvoiceDTO>): List<RepeatingInvoice> {
        return dtos.map {
            val member = memberRepository.findById(it.customerId).get()
            val repeatingInvoice = RepeatingInvoice(
                    id = it.invoiceId,
                    member = member,
                    productId = it.productId,
                    productName = it.productName,
                    price = it.price,
                    dynamicPrice = it.dynamicPrice,
                    frequency = it.frequency,
                    nextDate = it.nextDate
            )
            repeatingInvoice
        }
    }

    private fun parseToString(inputStream: InputStream): String {
        val parser = Tika()
        parser.maxStringLength = 10 * 1024 * 1024
        return parser.parseToString(inputStream)
    }

    private fun convertToDTOs(csvString: String): List<RepeatingInvoiceDTO> {

        val filteredString = csvString.replace("\"", "")
        val csvRows = filteredString.split('\n').map { line -> line.split(';') }
        val headerIndexes = csvRows[0].mapIndexed { index, headerField -> headerField to index }.toMap()

        return csvRows.subList(1, csvRows.size)
                .filter { it[0].isNotBlank() }
                .map {
                    RepeatingInvoiceDTO(
                            invoiceId = it[headerIndexes["Id"] ?: error("0")],
                            customerId = it[headerIndexes["Customer number"] ?: error("0")].toInt(),
                            productId = it[headerIndexes["Product number"] ?: error("0")].toInt(),
                            productName = it[headerIndexes["Text"] ?: error("0")],
                            price = it[headerIndexes["Price"] ?: error("0")].toInt(),
                            dynamicPrice = it[headerIndexes["Is price dynamic"] ?: error("0")].toBoolean(),
                            frequency = it[headerIndexes["Frequency"] ?: error("0")],
                            nextDate = it[headerIndexes["Next date"] ?: error("0")].toLocalDate()
                    )
                }
    }

    private fun String.toLocalDate(): LocalDate {
        val splitString = this.split("-")
        val year: Int = splitString[0].toInt()
        val month: Int = splitString[1].toInt()
        val day: Int = splitString[2].substring(0, 2).toInt()
        return LocalDate.of(year, month, day)
    }

}