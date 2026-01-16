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
