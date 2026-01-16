package kth.chem.ke_thermal_printer

import com.rt.printerlibrary.enumerate.BaseEnum
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kth.chem.ke_thermal_printer.core.PrinterManager
import kth.chem.ke_thermal_printer.core.bluetooth.BluetoothConnectionManager
import kth.chem.ke_thermal_printer.core.exceptions.ConnectionException

/** KeThermalPrinterPlugin */
class KeThermalPrinterPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var printerManager: PrinterManager
    private lateinit var bleManager: BluetoothConnectionManager

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ke_thermal_printer")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        printerMethodCallHandler(call, result)
        when (call.method) {
            call.method -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    private fun printerMethodCallHandler(call: MethodCall, result: Result) {
        try {
            when (call.method) {
                "initialize_printer" -> {
                    val cmd = call.argument<String>("cmd") ?: ""
                    printerManager.init(cmd)
                    response(result, mapOf("message" to "Printer initialized"))
                }

                "self_test" -> {
                    printerManager.selfTest()
                    response(result, mapOf("message" to "Self test completed"))
                }

                "connect_ble" -> {
                    val address = call.argument<String>("address") ?: ""
                    if (address.trim().isEmpty()) {
                        throw ConnectionException("Invalid BLE address")
                    }
                    // Initialize BLE manager and connect
                    bleManager = BluetoothConnectionManager()
                    bleManager.connect(address)
                    response(result, mapOf("message" to "Connected to BLE printer"))
                }

                "print_text" -> {

                }

                "disconnectPrinter" -> {
                    printerManager.disconnect()
                    response(result, mapOf("message" to "Printer disconnected"))
                }

                else -> {
                    result.notImplemented()
                }
            }
        } catch (e: Exception) {
            if (e is ConnectionException) {
                response(result, mapOf("message" to e.message.toString()), false)
            }
        }
    }

    fun response(result: Result, data: Map<String, Any>, isSuccess: Boolean? = true) {
        if (isSuccess == false) {
            result.error("ERROR", "An error occurred", Json.encodeToString(data))
            return
        }
        result.success(data)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
