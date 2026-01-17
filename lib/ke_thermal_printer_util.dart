import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:ke_thermal_printer/enums/cmd_types.dart';
import 'package:ke_thermal_printer/exceptions/ble_not_support_exception.dart';

import 'ke_thermal_printer_platform_interface.dart';
import 'models/scan_result.dart';

class KEThermalPrinterUtils {
  final KeThermalPrinterPlatform _printerPlatform =
      KeThermalPrinterPlatform.instance;

  Future<void> initialize(CmdTypes cmdType) async {
    return KeThermalPrinterPlatform.instance.initialize(cmdType);
  }

  // ################### BLE Methods ###################
  Future<bool> get isSupportBLE async {
    return FlutterBluePlus.isSupported;
  }

  Future<void> turnOnBluetooth() async {
    final isSupport = await isSupportBLE;
    if (!isSupport) {
      throw BLENotSupportException();
    }
    return FlutterBluePlus.turnOn();
  }

  void stopScanBluetoothDevices() {
    FlutterBluePlus.stopScan();
  }

  Stream<List<BLEScanResult>> startScanBluetoothDevices() {
    return KeThermalPrinterPlatform.instance.startScanBluetoothDevices().map((
      event,
    ) {
      return event
          .map(
            (e) => BLEScanResult(
              device: e.device,
              advertisementData: e.advertisementData,
              rssi: e.rssi,
              timeStamp: e.timeStamp,
            ),
          )
          .toSet()
          .toList();
    });
  }

  Future<Map<String, dynamic>?> connectBluetoothDevice(String printerID) {
    return _printerPlatform.connectBluetoothDevice(printerID);
  }
}

// ################### End of BLE Methods ###################
