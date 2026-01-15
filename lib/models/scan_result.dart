import 'package:flutter_blue_plus/flutter_blue_plus.dart';

import 'ble_device.dart';

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
