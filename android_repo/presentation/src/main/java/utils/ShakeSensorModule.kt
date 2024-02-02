package utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

interface ShakeEventListener {
    fun onShakeSensed()
}

class ShakeSensorModule(context: Context, private val listener: ShakeEventListener) {

    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var sensorEventListener: SensorEventListener? = null

    private var isShakeInitiated = false
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    fun start() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                if (!isShakeInitiated) {
                    lastX = x
                    lastY = y
                    lastZ = z
                    isShakeInitiated = true
                } else {
                    val deltaX: Float = kotlin.math.abs(lastX - x)
                    val deltaY: Float = kotlin.math.abs(lastY - y)
                    val deltaZ: Float = kotlin.math.abs(lastZ - z)
                    if ((deltaX > Shake_THRESHOLD && deltaY > Shake_THRESHOLD) ||
                        (deltaZ > Shake_THRESHOLD && deltaX > Shake_THRESHOLD) ||
                        (deltaY > Shake_THRESHOLD && deltaZ > Shake_THRESHOLD)
                    ) {
                        listener.onShakeSensed()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            }
        }
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
        )
    }

    fun stop() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }

    companion object {
        private const val Shake_THRESHOLD = 12f
    }
}
