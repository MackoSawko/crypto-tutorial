package com.home.cryptomarket.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(val mssg: String): RuntimeException(mssg){

}