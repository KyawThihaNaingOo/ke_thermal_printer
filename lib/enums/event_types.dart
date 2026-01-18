enum EventTypes {
  printerStatus('printer_status'),
  printStatus('print_status');

  final String value;

  const EventTypes(this.value);
}
