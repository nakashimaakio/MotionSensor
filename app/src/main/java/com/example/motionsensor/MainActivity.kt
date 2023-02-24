package com.example.motionsensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.motionsensor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

	private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

	private var sensorManager: SensorManager? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager
	}

	override fun onResume() {
		super.onResume()
		val gyro = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
		if (gyro != null) sensorManager?.registerListener(this, gyro, SensorManager.SENSOR_DELAY_UI)
	}

	override fun onPause() {
		super.onPause()
		sensorManager?.unregisterListener(this)
	}

	override fun onSensorChanged(event: SensorEvent) {
		if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
			val sensorX = event.values[0]
			val sensorY = event.values[1]
			val sensorZ = event.values[2]
			binding.textView.text = "Gyroscope: %.1f, %.1f, %.1f".format(sensorX, sensorY, sensorZ)

			showInfo(event)
		}
	}

	// センサーの各種情報を表示する
	private fun showInfo(event: SensorEvent) {
		// センサー名
		val info = StringBuffer("Name: ")
		info.append(event.sensor.name)
		info.append("\n")

		// ベンダー名
		info.append("Vendor: ")
		info.append(event.sensor.vendor)
		info.append("\n")

		// 型番
		info.append("Type: ")
		info.append(event.sensor.type)
		info.append("\n")

		// 最小遅れ
		var data = event.sensor.minDelay
		info.append("Mindelay: ")
		info.append(data)
		info.append(" usec\n")

		// 最大遅れ
		data = event.sensor.maxDelay
		info.append("Maxdelay: ")
		info.append(data)
		info.append(" usec\n")

		// レポートモード
		data = event.sensor.reportingMode
		var stinfo = "unknown"
		if (data == 0) {
			stinfo = "REPORTING_MODE_CONTINUOUS"
		} else if (data == 1) {
			stinfo = "REPORTING_MODE_ON_CHANGE"
		} else if (data == 2) {
			stinfo = "REPORTING_MODE_ONE_SHOT"
		}
		info.append("ReportingMode: ")
		info.append(stinfo)
		info.append("\n")

		// 最大レンジ
		info.append("MaxRange: ")
		var fData = event.sensor.maximumRange
		info.append(fData)
		info.append("\n")

		// 分解能
		info.append("Resolution: ")
		fData = event.sensor.resolution
		info.append(fData)
		info.append(" m/s^2\n")

		// 消費電流
		info.append("Power: ")
		fData = event.sensor.power
		info.append(fData)
		info.append(" mA\n")

		binding.textInfo.text = info
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}