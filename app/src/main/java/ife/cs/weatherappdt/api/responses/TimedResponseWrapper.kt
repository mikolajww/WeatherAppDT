package ife.cs.weatherappdt.api.responses

import kotlinx.serialization.Serializable
import java.util.*

class TimedResponseWrapper<T>(response: T?, expirationTimestamp: Date) {

    val myResponse: T? = response
        get() = if(timestamp.after(Calendar.getInstance().time)) field else null

    val timestamp: Date = expirationTimestamp

    constructor(response: T?, expirationInSeconds: Int) : this(response, Calendar.getInstance().apply { add(Calendar.SECOND, expirationInSeconds) }.time)
}