enum CmdTypes {
  none(-1),
  esc(1),
  tsc(2),
  cpcl(3),
  zpl(4),
  pin(5);

  final int value;

  const CmdTypes(this.value);
}

extension CmdTypesExtension on CmdTypes {
  static CmdTypes fromValue(int value) {
    return CmdTypes.values.firstWhere(
      (e) => e.value == value,
      orElse: () => CmdTypes.none,
    );
  }

  String upper() {
    return name.toUpperCase();
  }
}
