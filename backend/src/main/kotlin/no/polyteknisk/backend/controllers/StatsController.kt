package no.polyteknisk.backend.controllers

import no.polyteknisk.backend.entities.MemberStats
import no.polyteknisk.backend.services.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class StatsController(
        val memberService: MemberService
) {

    @GetMapping("/members")
    fun getMemberStats(): MemberStats {
        return memberService.getStats()
    }
}