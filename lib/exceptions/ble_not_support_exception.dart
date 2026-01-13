class BLENotSupportException implements Exception {
  final String message;

  BLENotSupportException([
    this.message =
        'Bluetooth Low Energy (BLE) is not supported on this device.',
  ]);

  @override
  String toString() => 'BLENotSupportException: $message';
}
