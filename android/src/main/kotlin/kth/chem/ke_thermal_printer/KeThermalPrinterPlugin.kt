package kth.chem.ke_thermal_printer

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.rt.printerlibrary.utils.ConnectListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kth.chem.ke_thermal_printer.core.PrinterManager
import kth.chem.ke_thermal_printer.core.bluetooth.BluetoothConnectionManager
import kth.chem.ke_thermal_printer.core.enums.EventTypes
import kth.chem.ke_thermal_printer.core.exceptions.ConnectionException


/** KeThermalPrinterPlugin */
class KeThermalPrinterPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null
    private lateinit var printerManager: PrinterManager
    private lateinit var bleManager: BluetoothConnectionManager
    val mainHandler = Handler(Looper.getMainLooper())

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        printerManager = PrinterManager.getInstance()

        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ke_thermal_printer_method")
        channel.setMethodCallHandler(this)
        /// Event sink
        eventChannel =
            EventChannel(flutterPluginBinding.binaryMessenger, "ke_thermal_printer_event")
        eventChannel.setStreamHandler(this)
    }

    //        when (call.method) {
//            call.method -> {
//                result.success("Android ${android.os.Build.VERSION.RELEASE}")
//            }
//
//            else -> {
//                result.notImplemented()
//            }
//        }

    //  ######################## METHOD CHANNEL ########################
    override fun onMethodCall(call: MethodCall, result: Result) {
        printerMethodCallHandler(call, result)
    }
    //  #################################################

    private fun printerMethodCallHandler(call: MethodCall, result: Result) {
//        try {
        when (call.method) {
            "initialize_printer" -> {
                val cmd = call.argument<Int>("cmd") ?: 0
                printerManager.init(cmd)
                response(result, mapOf("message" to "Printer initialized"))
            }

            "self_test" -> {
                printerManager.selfTest()
                response(result, mapOf("message" to "Self test completed"))
            }

            "get_printer_status" -> {
                val status = printerManager.connected()
                response(result, mapOf("message" to status.name))
            }

            "connect_ble" -> {
                val address = call.argument<String>("address") ?: ""
                if (address.trim().isEmpty()) {
                    throw ConnectionException("Invalid BLE address")
                }
                // Initialize BLE manager and connect
                bleManager = BluetoothConnectionManager()
                bleManager.connect(address)
                response(result, mapOf("message" to "Connection to BLE printer, Please wait..."))
                printerConnectionListener()
            }

            "print_text" -> {
                val text = call.argument<String>("text") ?: ""
//                    printerManager.printText(text)
                response(result, mapOf("message" to "Text printed"))
            }

            "disconnect" -> {
                printerManager.disconnect()
                response(result, mapOf("message" to "Printer disconnected"))
            }

            else -> {
                result.notImplemented()
            }
        }
//        } catch (e: Exception) {
//            if (e is ConnectionException) {
//                response(result, mapOf("message" to e.message.toString()), false)
//            }
//
//            print("Error: ${e.message}")
//        }
    }

    fun response(result: Result, data: Map<String, Any>, isSuccess: Boolean? = true) {
        if (isSuccess == false) {
            result.error("ERROR", "An error occurred", data)
            return
        }
        result.success(data)
    }

    fun responseEvent(
        data: Map<String, Any>, isSuccess: Boolean? = true
    ) {
        if (isSuccess == false) {
            eventSink?.error("ERROR", "An error occurred", data)
            return
        }
        eventSink?.success(data)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    //    #################### EVENT SINK ####################
    override fun onListen(
        arguments: Any?, events: EventChannel.EventSink?
    ) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }
//    #########################################################

    //    ################################ PRINTER CONNECTION STATE ################################
    private fun printerConnectionListener() {
        if (printerManager.printer.printerInterface == null) {
            throw ConnectionException("Printer interface is null")
        }
        printerManager.setConnectListener(object : ConnectListener {

            override fun onPrinterConnected(p0: Any?) {
                print("Printer connected listener called")
                mainHandler.post {
                    responseEvent(
                        mapOf(
                            "message" to "Printer connected",
                            "event" to EventTypes.PRINTER_STATUS.value
                        ), true
                    )
                }
            }

            override fun onPrinterDisconnect(p0: Any?) {
                print("Printer disconnected listener called")
                mainHandler.post {
                    responseEvent(
                        mapOf(
                            "message" to "Printer disconnected",
                            "event" to EventTypes.PRINTER_STATUS.value
                        ), true
                    )
                }

            }

            override fun onPrinterWritecompletion(p0: Any?) {
                print("Printer write completed listener called")
                mainHandler.post {
                    responseEvent(
                        mapOf(
                            "message" to "Data write completed",
                            "event" to EventTypes.PRINT_STATUS.value
                        ), true
                    )
                }
            }
        })
    }
    //    #########################################################
}

