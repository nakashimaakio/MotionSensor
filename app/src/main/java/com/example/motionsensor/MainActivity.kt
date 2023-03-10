package com.example.motionsensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionsensor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

	/** ViewBinding */
	private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

	/** センサーマネージャー */
	private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

	private val accelerometerReading = FloatArray(3)
	private val magnetometerReading = FloatArray(3)

	private val rotationMatrix = FloatArray(9)
	private val orientationAngles = FloatArray(3)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}

	override fun onResume() {
		super.onResume()

		//ジャイロ
		val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
		if (gyroscope != null) sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)

		//向き
		val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
		if (accelerometer != null) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

		//向き
		val magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
		if (magneticField != null) sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL)
	}

	override fun onPause() {
		super.onPause()
		sensorManager.unregisterListener(this)
	}

	override fun onSensorChanged(event: SensorEvent) {
		//ジャイロ
		if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
			val sensorX = event.values[0]
			val sensorY = event.values[1]
			val sensorZ = event.values[2]
			binding.textGyro.text = "Gyroscope: %.1f, %.1f, %.1f".format(sensorX, sensorY, sensorZ)
		}

		//向き
		if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
			System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
		}

		//向き
		if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
		}

		SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
		SensorManager.getOrientation(rotationMatrix, orientationAngles)

		val dx = Math.toDegrees(orientationAngles[0].toDouble())
		val dy = Math.toDegrees(orientationAngles[1].toDouble())
		val dz = Math.toDegrees(orientationAngles[2].toDouble())
		binding.textDirection.text = "Direction: %.1f, %.1f, %.1f".format(dx, dy, dz)
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}