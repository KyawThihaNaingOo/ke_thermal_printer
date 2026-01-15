import 'package:flutter_blue_plus/flutter_blue_plus.dart';

class BLEDevice extends BluetoothDevice {
  BLEDevice({required super.remoteId});
}

extension BLEConnectionState on BluetoothConnectionState {
  bool get isConnected => this == BluetoothConnectionState.connected;

  bool get isDisconnected => this == BluetoothConnectionState.disconnected;
}
