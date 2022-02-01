package com.cloud.plugins

import com.cloud.StorageClient
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlin.streams.toList

fun Application.configureRouting() {
    val client = StorageClient()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/files"){
           val files =  client.getBaseFolders().toList().stream().map {
                File(it, it)
            }.toList()
           val folder =  Folder(files)
//            val folder = Folder(listOf("HASS", "LULU"))

            call.respond(FreeMarkerContent("index.ftl", mapOf("folder" to folder.files)))
        }

        get("/getFiles"){
            val folderName = call.request.queryParameters["path"]
//            var base = call.request.queryParameters["base"] ?: ""
            println(folderName)

            val list = client.getFolders(folderName!!).toList()


            val listWithFullName = list.stream().map {
               File(it, folderName.plus(":").plus(it))
            }.toList()
            val folder =  Folder(listWithFullName)
            println(listWithFullName)
            call.respond(FreeMarkerContent("Files.ftl", mapOf("folder" to  folder.files)))
        }
    }
}

data class Folder(val files : List<File>)
data class File(val name : String, val path : String)