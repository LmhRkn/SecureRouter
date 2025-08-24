package com.tfg.securerouter.data.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set


object QRDecoder {
    private val ANSI_REGEX = Regex("\u001B\\[[;?0-9]*[ -/]*[@-~]")

    fun toPngBase64(ansiRaw: String, scale: Int = 8): String {
        val clean = ANSI_REGEX.replace(ansiRaw, "")
            .replace("\r\n", "\n")
            .replace("\r", "\n")

        val lines = clean.split('\n')
        val rows = lines.size
        val cols = lines.maxOfOrNull { it.length } ?: 0
        if (rows == 0 || cols == 0) return ""

        val outW = cols * scale
        val outH = rows * 2 * scale
        val bmp = createBitmap(outW, outH)

        fun paintCell(cx: Int, cy: Int, black: Boolean) {
            val c = if (black) Color.WHITE else Color.BLACK
            val px = cx * scale
            val py = cy * scale
            for (dy in 0 until scale) for (dx in 0 until scale) {
                bmp[px + dx, py + dy] = c
            }
        }

        for (y in 0 until rows) {
            val row = lines[y]
            for (x in 0 until cols) {
                val ch = if (x < row.length) row[x] else ' '
                when (ch) {
                    '█' -> { paintCell(x, y*2, true);  paintCell(x, y*2+1, true)  }
                    '▀' -> { paintCell(x, y*2, true);  paintCell(x, y*2+1, false) }
                    '▄' -> { paintCell(x, y*2, false); paintCell(x, y*2+1, true)  }
                    else -> { paintCell(x, y*2, false); paintCell(x, y*2+1, false) }
                }
            }
        }

        val bos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bos)
        return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)
    }
}