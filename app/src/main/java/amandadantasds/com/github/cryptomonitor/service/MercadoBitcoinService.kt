package amandadantasds.com.github.cryptomonitor.service

import amandadantasds.com.github.cryptomonitor.model.TickerResponse
import retrofit2.Response
import retrofit2.http.GET

interface MercadoBitcoinService {

    @GET("api/BTC/ticker/")
    suspend fun getTicker(): Response<TickerResponse>
}