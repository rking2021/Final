import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.io.BufferedReader
import java.io.InputStreamReader

class Classifier(private val context: Context) {

    private var interpreter: Interpreter
    private var labels: List<String>
    private val inputImageSize = 224

    init {
        interpreter = Interpreter(loadModelFile("flower_model.tflite"))
        labels = loadLabels("class_labels.txt")
    }

    fun classify(bitmap: Bitmap): String {
        val scaled = createScaledBitmap(bitmap, inputImageSize, inputImageSize, true)
        val input = preprocessImage(scaled)
        val output = Array(1) { FloatArray(labels.size) }
        interpreter.run(input, output)
        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        return labels[maxIdx]
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputImageSize * inputImageSize * 3)
        buffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageSize * inputImageSize)
        bitmap.getPixels(pixels, 0, inputImageSize, 0, 0, inputImageSize, inputImageSize)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f
            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }

        return buffer
    }

    private fun loadModelFile(modelName: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabels(fileName: String): List<String> {
        val reader = BufferedReader(InputStreamReader(context.assets.open(fileName)))
        return reader.readLines()
    }
}
