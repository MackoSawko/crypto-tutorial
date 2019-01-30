package com.home.cryptomarket.controller

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
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var  walletRepository: WalletRepository

    @GetMapping("/users")
    fun getUser(): List<User>{
        return userRepository.findAll()
    }

    @PostMapping("/user")
    fun addUser(@RequestParam("firstName") firstName: String,
                @RequestParam("lastName") lastName: String): User{

        val wallet = Wallet(btc = 5.0, usd = 1250.0)
        walletRepository.save(wallet)

        val user = User(firstName = firstName, lastName = lastName, wallet = wallet)
        userRepository.save(user)

        return user
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): User{
        return userRepository
                .findById(id)
                .orElseThrow{ throw UserNotFoundException("cant find user witch such id")}
    }

}