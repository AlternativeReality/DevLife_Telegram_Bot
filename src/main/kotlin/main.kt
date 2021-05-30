package com.github.kotlintelegrambot.echo

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManager
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


private const val CHARACTER_DATA_API =
    "https://developerslife.ru/random?json=true"
private const val USER_MORE_MEM = "Хочу мем про прогу"
var ID: Long = 0
fun main() {
    val bot = bot {
        token = "1711897570:AAG7icp8FcKo17ImSJPQI6DYgHvP2vXjRhQ"
        dispatch {
            command("start") {
//                val  list1: List<String> = listOf(USER_MORE_MEM)
//                val keyBoardList: List<List<String>> = listOf(list1)
//                val a = KeyboardReplyMarkup.createSimpleKeyboard(keyBoardList)
//                val  list2: List<String> = listOf(USER_MORE_MEM)
//                val keyBoardList2: List<List<String>> = listOf(list1)
                val yes = InlineKeyboardButton.CallbackData("давай", "yes")
                val no = InlineKeyboardButton.CallbackData("не надо", "no")
                val b: List<InlineKeyboardButton> = listOf(yes, no)
                val a2 = InlineKeyboardMarkup.createSingleRowKeyboard(b)
             //  a.createSimpleKeyboard(keyBoardList)

               bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Привет, ${message.from?.username ?: "странник"}! Я могу " +
                        "присылать тебе мемы про программирование", replyMarkup = a2)
                println(message.from?.username)
               // ID =  message.messageId
              //  println(ID)
                }


          callbackQuery{
              if (callbackQuery.data == "yes" || callbackQuery.data == "more" ){
//                  val keyBoardList: List<List<String>> = listOf(listOf(USER_MORE_MEM))
//                  val a = KeyboardReplyMarkup.createSimpleKeyboard(keyBoardList)
//                  bot.sendMessage(chatId = ChatId.fromId(callbackQuery.from.id),text = "хорошо, просто нажми на кнопку",
//                      replyMarkup = a)
                  GlobalScope.launch {
                      try {
                          val ctx: SSLContext = SSLContext.getInstance("TLS")
                          ctx.init(arrayOfNulls<KeyManager>(0), arrayOf<TrustManager>(DefaultTrustManager()), SecureRandom())
                          SSLContext.setDefault(ctx)
                          val apiData = URL(CHARACTER_DATA_API).readText()
                        //  println(apiData)
                          val builder = Gson()
                          val simpleData: Generator.DataApi =
                              builder.fromJson<Generator.DataApi>(apiData, Generator.DataApi::class.java)
                          Data.URL = simpleData.gifURL
                          Data.Description = simpleData.description
                      //    println(Data.Description)
                       //   println(Data.URL)
                          val more = InlineKeyboardButton.CallbackData("хочу еще мем", "more")
                          val b: List<InlineKeyboardButton> = listOf(more)
                          val a2 = InlineKeyboardMarkup.createSingleRowKeyboard(b)
                        //  bot.deleteMessage(ChatId.fromId(callbackQuery.from.id), message())

                        bot.sendAnimation(chatId = ChatId.fromId(callbackQuery.from.id), fileId = Data.URL ,caption = Data.Description,
                              replyMarkup = a2)
                          println(callbackQuery.from.username)
                         if (callbackQuery.data == "yes")
                             bot.deleteMessage(ChatId.fromId(callbackQuery.from.id), callbackQuery.message!!.messageId)

                      } catch (e: Exception){
                          bot.sendMessage(chatId = ChatId.fromId(callbackQuery.from.id), text = "Какая-то ошибка с апихой, сорян")
                      }
                  }
              }
              if (callbackQuery.data == "no"){
                  //bot.deleteMessage(ChatId.fromId(callbackQuery.from.id), ID-1)
                  bot.sendMessage(ChatId.fromId(callbackQuery.from.id), "ну и не надо")
              }

          }
            text(USER_MORE_MEM){
                GlobalScope.launch {
                    try {
                        val ctx: SSLContext = SSLContext.getInstance("TLS")
                        ctx.init(arrayOfNulls<KeyManager>(0), arrayOf<TrustManager>(DefaultTrustManager()), SecureRandom())
                        SSLContext.setDefault(ctx)
                        val apiData = URL(CHARACTER_DATA_API).readText()
                     //   println(apiData)
                        val builder = Gson()
                        val simpleData: Generator.DataApi =
                            builder.fromJson<Generator.DataApi>(apiData, Generator.DataApi::class.java)
                        Data.URL = simpleData.gifURL
                        Data.Description = simpleData.description
                      //  println(Data.Description)
                      //  println(Data.URL)
                        bot.sendAnimation(chatId = ChatId.fromId(message.chat.id), fileId = Data.URL ,caption = Data.Description)
                       // bot.deleteMessage(ChatId.fromId(message.chat.id), message.messageId)
                    } catch (e: Exception){
                        bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Какая-то ошибка с апихой, сорян")
                    }

                }
            }
        }
    }
    bot.startPolling()

}


private class DefaultTrustManager : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return null
    }
}

object Data{
    var URL = ""
    var Description = ""
}












