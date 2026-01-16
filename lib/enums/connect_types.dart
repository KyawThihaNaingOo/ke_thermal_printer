enum ConnectTypes {
  bluetooth(1),
  bluetoothBle(2),
  wifi(3),
  usb(4),
  com(5);

  final int value;

  const ConnectTypes(this.value);
}
