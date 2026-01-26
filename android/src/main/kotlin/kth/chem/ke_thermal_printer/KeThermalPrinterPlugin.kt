package kth.chem.ke_thermal_printer

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.rt.printerlibrary.enumerate.BaseEnum
import com.rt.printerlibrary.utils.ConnectListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kth.chem.ke_thermal_printer.core.PrinterFacade
import kth.chem.ke_thermal_printer.core.enums.EventTypes
import kth.chem.ke_thermal_printer.core.exceptions.ConnectionException


/** KeThermalPrinterPlugin */
class KeThermalPrinterPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null
    private lateinit var printerManager: PrinterFacade
    val mainHandler = Handler(Looper.getMainLooper())

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        printerManager = PrinterFacade()

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
                printerManager.initialize(cmd)
                response(result, mapOf("message" to "Printer initialized"))
            }

            "get_printer_status" -> {
                val status = printerManager.getStatus()
                response(result, mapOf("message" to status.name))
            }

            "set_printer_cmd_type" -> {
                val cmd = call.argument<Int>("cmd") ?: 0
                printerManager.setCmdType(cmd)
                response(result, mapOf("message" to "Printer cmd initialized"))
            }

//            "set_connection_type" -> {
//                val type = call.argument<Int>("type") ?: 0
//                printerManager.setCurrentConnectType(type)
//                response(result, mapOf("message" to "Connection type set to $type"))
//            }

            "connect_ble" -> {
                val address = call.argument<String>("address") ?: ""
                if (address.trim().isEmpty()) {
                    throw ConnectionException("Invalid BLE address")
                }
                printerManager.connectBle(address)
                val res = printerConnectionListener()
                print("printerConnectionListener set: $res")
                response(result, mapOf("message" to "Connection to BLE printer, Please wait..."))
            }

            "self_test" -> {
                printerManager.selfTest()
                response(result, mapOf("message" to "Self test completed"))
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
    private fun printerConnectionListener(): Boolean {
        print("printerConnectionListener -> setting up listener")
//        if (printerManager.printer.printerInterface == null) {
//            Log.e(TAG, "printerConnectionListener: printer interface is not set")
//            return
//        }
        printerManager.setConnectListener(object : ConnectListener {

            override fun onPrinterConnected(p0: Any?) {
                print("printerConnectionListener: connected")
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
                print("printerConnectionListener: dc")
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
                print("printerConnectionListener: write completed")
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
        return true
    }
    //    #########################################################
}

