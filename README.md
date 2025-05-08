<h1 align="center">Crypto-Monitor 💵</h1>

<p align="center">
  <b>O aplicativo Crypto-Monitor permite visualizar em tempo real a cotação do Bitcoin.</b>
</p>

<p align="center">
    Ao ser iniciado, o aplicativo exibe o valor zerado, sem data e hora. As informações são atualizadas com a cotação mais recente quando o usuário clica no botão <b>“Atualizar”</b>.
</p>

<p align="center">
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
  <img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white">
</p>

<p align="center">
  <b>Amanda Dantas de Souza- RM552352</b>
</p>

# 📱 Aplicação:
<div align="center"> 
<img src= "https://github.com/user-attachments/assets/6c0df9b3-dc4e-4898-86a6-63531579ab30" width="250"/>
<img src= "https://github.com/user-attachments/assets/2e69db84-66d0-4bc2-92f7-f9caa3af9055" width="250"/>
</div>

# 🛠️ Funcionamento 
<p>Para buscar a cotação mais recente de criptomoedas, utilizamos a <b>API do Mercado Bitcoin</b>:</p> 
<p>🔗 [https://www.mercadobitcoin.com.br/]</p>
<p>Vamos entender melhor a aplicação da API destrinchando os códigos!</p>

- ## 📩 Estrutura da Resposta (`TickerResponse`)
     Quando o usuário clica no botão <b>“Atualizar”</b>, a aplicação realiza uma requisição do tipo <b>GET</b> para a API.
     O retorno esperado é um objeto <b>JSON</b>, que segue a estrutura abaixo:
 ```kotlin
      class TickerResponse(
          val ticker: Ticker
      )
      
      class Ticker(
          val high: String,
          val low: String,
          val vol: String,
          val last: String,
          val buy: String,
          val sell: String,
          val date: Long
      )
```
- ## 🔄 Requisição (`MercadoBitcoinService`)

  A interface `MercadoBitcoinService` é responsável por realizar uma requisição <b>GET</b> para obter a cotação atual do <b>Bitcoin</b> a partir da API do Mercado Bitcoin.

```kotlin
interface MercadoBitcoinService {
    @GET("api/BTC/ticker/")
    suspend fun getTicker(): Response<TickerResponse>
}
```
- ## 🏭 Instanciando o Serviço com Retrofit (`MercadoBitcoinServiceFactory`)

     A classe `MercadoBitcoinServiceFactory` é responsável por configurar o Retrofit e criar uma instância da interface `MercadoBitcoinService`, que permite fazer chamadas HTTP à API.
     <p> <b>OBS: </b>A interface Retrofit é responsável por transformar a resposta JSON em um objeto.</p>
     
```kotlin
class MercadoBitcoinServiceFactory {
    fun create(): MercadoBitcoinService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.mercadobitcoin.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MercadoBitcoinService::class.java)
    }
}



