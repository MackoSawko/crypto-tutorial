package com.home.cryptomarket

import com.home.cryptomarket.exceptions.CantFecthBtcPrice
import com.home.cryptomarket.exceptions.NotEnoughMoneyException
import com.home.cryptomarket.exceptions.UserNotFoundException
import com.home.cryptomarket.model.BtcPrice
import com.home.cryptomarket.model.User
import com.home.cryptomarket.model.Wallet

import com.home.cryptomarket.repository.UserRepository
import com.home.cryptomarket.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

interface CryptoFacade {
    fun getUsers(): List<User>
    fun addUser(firstName: String, lastName: String): User
    fun getUser(id: Long): User
    fun sellBtc(id: Long, amount: Double): Wallet
    fun buyBtc(id: Long, amount: Double): Wallet

}

@Service
class CryptoFacadeImple: CryptoFacade {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var  walletRepository: WalletRepository

    override fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun addUser(firstName: String, lastName: String): User {

        val wallet = Wallet(btc = 5.0, usd = 1250.0)
        walletRepository.save(wallet)

        val user = User(firstName = firstName, lastName = lastName, wallet = wallet)
        userRepository.save(user)

        return user
    }


    override fun getUser(id: Long): User {
        return userRepository.findById(id).orElseThrow{ throw UserNotFoundException("cant find user wich such id")}
    }

    override fun sellBtc(id: Long, amount: Double): Wallet {
        val user = userRepository.getOne(id)
        val btcAmount = user.wallet.btc

        if (btcAmount >= amount){
            user.wallet.btc -= amount
            user.wallet.usd += amount * getBtcPrice().bid
            userRepository.save(user)
        }else{
            throw NotEnoughMoneyException("brak środków na koncie")
        }
        return user.wallet
    }

    override fun buyBtc(id: Long, amount: Double): Wallet {
        val user = userRepository.getOne(id)
        var usdAmount = user.wallet.usd

        if (amount * getBtcPrice().ask <= usdAmount){
            user.wallet.usd -= amount * getBtcPrice().ask
            user.wallet.btc += amount
            userRepository.save(user)
        }else{
            throw NotEnoughMoneyException("you had not enoguh usd")
        }

        return user.wallet
    }

    fun getBtcPrice(): BtcPrice {
        val response = RestTemplate()
                        .getForObject(API_GET_BTC_PRICE, BtcPrice::class.java)

        if (response == null){
            throw CantFecthBtcPrice("Nie moge znalezc ceny bitcoina")
        }

        return response
    }

    companion object {
        const val BTC_PRICE = 5000.0
        const val API_GET_BTC_PRICE= "https://www.bitstamp.net/api/v2/ticker/btcusd/"
    }


}
