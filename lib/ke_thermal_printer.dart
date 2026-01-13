import 'package:flutter_blue_plus/flutter_blue_plus.dart';

import 'ke_thermal_printer_platform_interface.dart';

class KEThermalPrinter {
  Future<String?> getPlatformVersion() {
    return KeThermalPrinterPlatform.instance.getPlatformVersion();
  }

  void stopScanBluetoothDevices() {
    FlutterBluePlus.stopScan();
  }

  Stream<List<BLEScanResult>> startScanBluetoothDevices() {
    return KeThermalPrinterPlatform.instance.startScanBluetoothDevices().map((
        event,) {
      return event
          .map(
            (e) =>
            BLEScanResult(
              device: e.device,
              advertisementData: e.advertisementData,
              rssi: e.rssi,
              timeStamp: e.timeStamp,
            ),
      )
          .toList();
    });
  }
}

class BLEScanResult extends ScanResult {
  BLEScanResult({
    required super.device,
    required super.advertisementData,
    required super.rssi,
    required super.timeStamp,
  });
}
