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
}

class BLEScanResult extends ScanResult {
  BLEDevice bleDevice;

  BLEScanResult({
    required super.device,
    required super.advertisementData,
    required super.rssi,
    required super.timeStamp,
  }) : bleDevice = BLEDevice(remoteId: device.remoteId);

  @override
  String toString() {
    return 'BLEScanResult{'
        'device: $device, '
        'advertisementData: $advertisementData, '
        'rssi: $rssi, '
        'timeStamp: $timeStamp'
        '}';
  }

  @override
  int get hashCode {
    return device.remoteId.str.hashCode;
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is ScanResult &&
        other.device.remoteId.str == device.remoteId.str;
  }
}

class BLEDevice extends BluetoothDevice {
  BLEDevice({required super.remoteId});
}

extension BLEConnectionState on BluetoothConnectionState {
  bool get isConnected => this == BluetoothConnectionState.connected;

  bool get isDisconnected => this == BluetoothConnectionState.disconnected;
}
