package tools

import com.github.kittinunf.fuel.core.Response
import com.google.common.collect.EvictingQueue
import kotlinx.coroutines.*
import java.io.*
import kotlin.system.measureNanoTime

object Downloader {
    suspend fun download(response: Response, size: Long, output: File, progressCallback: suspend CoroutineScope.(current: Long, max: Long, bps: Long) -> Unit) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val offset = if (output.exists()) output.length() else 0
                val chunkSize = 0x300000
                val input = BufferedInputStream(response.body().toStream())

                val read = ByteArray(chunkSize)

                FileOutputStream(output, true).use { writer ->
                    var len: Int
                    var totalLen = 0L

                    val chunk = ArrayList<Triple<Long, Long, Long>>(1000)

                    while (isActive) {
                        val nano = measureNanoTime {
                            len = input.readNBytes(read, 0, chunkSize)
                            totalLen += len
                            writer.write(read, 0, len)
                        }

                        if (len <= 0) break

                        chunk.add(Triple(nano, len.toLong(), System.nanoTime()))

                        val current = System.nanoTime()
                        chunk.removeIf { current - it.third > 1000 * 1000 * 1000 }

                        val timeAvg = chunk.map { it.first }.sum()
                        val lenAvg = chunk.map { it.second }.sum()

                        async {
                            progressCallback(totalLen + offset, size, (lenAvg / (timeAvg.toDouble() / 1000.0 / 1000.0 / 1000.0)).toLong())
                        }
                    }
                }

                input.close()
            }
        }
    }
}