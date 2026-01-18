import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/enums/enums.dart';

import 'ke_thermal_printer_platform_interface.dart';

/// An implementation of [KeThermalPrinterPlatform] that uses method channels.
class MethodChannelKeThermalPrinter extends KeThermalPrinterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ke_thermal_printer_method');
  @visibleForTesting
  final eventChannel = const EventChannel('ke_thermal_printer_event');
  late final Stream<Map<String, dynamic>> _printerStatusListener;
  late final Stream<Map<String, dynamic>> _printStatusListener;
  late final Stream<Map<String, dynamic>> _baseStream;

  MethodChannelKeThermalPrinter() {
    /// listen event channel
    _baseStream = eventChannel
        .receiveBroadcastStream()
        .map((e) => Map<String, dynamic>.from(e))
        .asBroadcastStream();

    _printerStatusListener = _baseStream.where(
      (event) => event['event'] == EventTypes.printerStatus.value,
    );

    _printStatusListener = _baseStream.where(
      (event) => event['event'] == EventTypes.printStatus.value,
    );
  }

  @override
  Stream<List<ScanResult>> startScanBluetoothDevices({
    Duration timeout = const Duration(seconds: 5),
  }) {
    FlutterBluePlus.adapterState.listen((state) {
      if (state == BluetoothAdapterState.on) {
        FlutterBluePlus.startScan(timeout: timeout);
      }
      if (state == BluetoothAdapterState.off) {
        FlutterBluePlus.turnOn();
      }
    });
    return FlutterBluePlus.scanResults..listen((results) {
      // for (ScanResult r in results) {
      //   print('${r.device.platformName} found! rssi: ${r.rssi}');
      // if (r.device.platformName == 'Mobile printer-385C') {
      //   FlutterBluePlus.stopScan();
      //   r.device.connect(license: License.free);
      //   bluetoothUUID = r.device.remoteId.str;
      //   log(bluetoothUUID!, name: 'Bluetooth UUID');
      //   // listenerDevice(r.device);
      //   break;
      // }
      // }
    });
  }

  @override
  Future<Map<String, dynamic>?> initialize(CmdTypes cmdType) async {
    final res = await methodChannel.invokeMethod('initialize_printer', {
      'cmd': cmdType.value,
    });
    return Map<String, dynamic>.from(res);
  }

  @override
  Future<Map<String, dynamic>> connectBluetoothDevice(String printerID) async {
    final res = await methodChannel.invokeMethod('connect_ble', {
      'address': printerID,
    });
    log(
      "ke_thermal_printer_method_channel: 66666 connectBluetoothDevice: $res",
    );
    return Map<String, dynamic>.from(res);
  }

  @override
  Future<Map<String, dynamic>?> getPrinterStatus() async {
    final res = await methodChannel.invokeMethod('get_printer_status');
    return Map<String, dynamic>.from(res);
  }

  @override
  Future<Map<String, dynamic>> selfTestPrinter() async {
    final res = await methodChannel.invokeMethod('self_test_printer');
    return Map<String, dynamic>.from(res);
  }

  @override
  Stream<Map<String, dynamic>> listenEventChannel() {
    return _baseStream;
  }

  @override
  Stream<Map<String, dynamic>> listenPrintStatus() {
    return _printStatusListener;
  }

  @override
  Stream<Map<String, dynamic>> listenPrinterStatus() {
    return _printerStatusListener;
  }
}
