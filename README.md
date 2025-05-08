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
  <img alt="Retrofit" src="https://img.shields.io/badge/Retrofit-FF9800?style=for-the-badge&logo=android&logoColor=white">
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

     A classe `MercadoBitcoinServiceFactory` é responsável por configurar o Retrofit e criar uma instância da interface `MercadoBitcoinService`.
     <p> ℹ️ A interface Retrofit transforma a resposta JSON em um objeto.</p>
     
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
```
- ## 🔃 Atualizando a Interface com os Dados da API (`makeRestCall()`)

     A função `makeRestCall()`, localizada em `MainActivity.kt`, é responsável por realizar a chamada à API e atualizar os componentes visuais da aplicação com os dados 
     retornados.

     Essa função utiliza `CoroutineScope(Dispatchers.Main).launch` para realizar a requisição de forma <b>assíncrona</b>.

     ### ✅ Se a resposta for bem-sucedida (`response.isSuccessful`):

          1. Pega o último valor do Bitcoin e converte para Double;
          2. Transforma o valor em reais (R$) e exibe no TextView lbl_value;
          3. Converte a data em formato timestamp Unix para a data legível no formato "dd/MM/yyyy HH:mm:ss" e exibe no TextView lbl_date.

     ### ❌ Se a resposta for negativa ou houver erro na chamada:

          1.  Retorna uma mensagem Toast com o erro correspondente (HTTP 400, 401, 403, 404);
          2.  Mostra um Toast com uma mensagem genérica de erro (em casos de falha na chamada).

```kotlin
private fun makeRestCall() {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val service = MercadoBitcoinServiceFactory().create()
            val response = service.getTicker()

            if (response.isSuccessful) {
                val tickerResponse = response.body()

                val lblValue: TextView = findViewById(R.id.lbl_value)
                val lblDate: TextView = findViewById(R.id.lbl_date)

                val lastValue = tickerResponse?.ticker?.last?.toDoubleOrNull()
                if (lastValue != null) {
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                    lblValue.text = numberFormat.format(lastValue)
                }

                val date = tickerResponse?.ticker?.date?.let { Date(it * 1000L) }
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                lblDate.text = sdf.format(date)

            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Bad Request"
                    401 -> "Unauthorized"
                    403 -> "Forbidden"
                    404 -> "Not Found"
                    else -> "Unknown error"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Falha na chamada: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
```

- ## 🧱 Layout Principal (`activity_main.xml`)
  Para finalizar, a estrutura visual da tela principal é definida no arquivo `activity_main.xml`. Utilizamos o `LinearLayout` vertical para organizar e incluir dois componentes 
  essenciais da interface:
     - Barra superior (Toolbar), incluída utilizando `component_toolbar_main.xml`;
     - Area de cotação, com título, valor e data/hora, definida em `component_quote_information.xml`.

```xml
<!-- res/layout/activity_main.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <include
        android:id="@+id/component_toolbar"
        layout="@layout/component_toolbar_main"
        android:layout_width="match_parent"
        android:layout_height="75dp"
        android:layout_weight="0" />

    <include
        android:id="@+id/component_quote_information"
        layout="@layout/component_quote_information"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
</LinearLayout>
```




