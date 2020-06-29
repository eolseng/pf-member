package no.polyteknisk.backend.controllers

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.services.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(
        val service: MemberService
) {

    @GetMapping
    fun getAllMembers(): List<Member> {
        val members = service.findAll()
        return members
    }

}