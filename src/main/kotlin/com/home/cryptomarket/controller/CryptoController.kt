package com.home.cryptomarket.controller

import com.home.cryptomarket.CryptoFacade
import com.home.cryptomarket.exceptions.UserNotFoundException
import com.home.cryptomarket.model.User
import com.home.cryptomarket.model.Wallet
import com.home.cryptomarket.repository.UserRepository
import com.home.cryptomarket.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*

@RestController
class CryptoController {

    @Autowired
    lateinit var cryptoFacade: CryptoFacade

    @GetMapping("/users")
    fun getUser(): List<User>{
        return cryptoFacade.getUsers()
    }

    @PostMapping("/user")
    fun addUser(@RequestParam("firstName") firstName: String,
                @RequestParam("lastName") lastName: String): User{

        return cryptoFacade.addUser(firstName, lastName)
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): User{
        return cryptoFacade.getUser(id)
    }

    @PostMapping("/user/{id}/sell/btc")
    fun sellBtc(@PathVariable id: Long, @RequestParam("amount") amount: Double): Wallet {
        return cryptoFacade.sellBtc(id, amount)
    }


}