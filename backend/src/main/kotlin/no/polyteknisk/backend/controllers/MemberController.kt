package no.polyteknisk.backend.controllers

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.repositories.MemberRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController(
        val repository: MemberRepository
) {

    @GetMapping
    fun getAllMembers(): List<Member> {
        return repository.findAll()
    }

}