import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/enums/cmd_types.dart';

import 'ke_thermal_printer_platform_interface.dart';

/// An implementation of [KeThermalPrinterPlatform] that uses method channels.
class MethodChannelKeThermalPrinter extends KeThermalPrinterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ke_thermal_printer');

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
  Future<void> initialize(CmdTypes cmdType) {
    return methodChannel.invokeMethod('initialize_printer', {
      'cmd': cmdType.value,
    });
  }

  @override
  Future<Map<String, dynamic>?> connectBluetoothDevice(String printerID) async {
    return methodChannel.invokeMethod<Map<String, dynamic>>('connect_ble', {
      'address': printerID,
    });
  }

  @override
  Future<Map<String, dynamic>?> getPrinterStatus() {
    return methodChannel.invokeMethod<Map<String, dynamic>?>(
      'get_printer_status',
    );
  }

  @override
  Future<Map<String, dynamic>> selfTestPrinter() {
    return methodChannel.invokeMethod<Map<String, dynamic>>('self_test_printer')
        as Future<Map<String, dynamic>>;
  }
}
