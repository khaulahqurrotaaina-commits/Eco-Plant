package com.khaulahqurrota.ecoplant

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object EcoPlantApi {
    suspend fun get(path: String): JSONObject = request("GET", path)
    suspend fun post(path: String, body: JSONObject): JSONObject = request("POST", path, body)
    suspend fun put(path: String, body: JSONObject): JSONObject = request("PUT", path, body)
    suspend fun patch(path: String, body: JSONObject): JSONObject = request("PATCH", path, body)
    suspend fun delete(path: String): JSONObject = request("DELETE", path)

    private suspend fun request(method: String, path: String, body: JSONObject? = null): JSONObject = withContext(Dispatchers.IO) {
        val baseUrl = BuildConfig.API_BASE_URL.trimEnd('/')
        val connection = (URL("$baseUrl$path").openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 8_000
            readTimeout = 12_000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            BuildConfig.API_TOKEN.takeIf { it.isNotBlank() }?.let { setRequestProperty("Authorization", "Bearer $it") }
            if (body != null) {
                doOutput = true
                outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            }
        }

        try {
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val response = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            val json = if (response.isBlank()) JSONObject() else JSONObject(response)
            if (status !in 200..299) throw IOException(json.optString("error", "Permintaan backend gagal ($status)."))
            json
        } finally {
            connection.disconnect()
        }
    }
}

