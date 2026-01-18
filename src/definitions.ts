export interface DeepDeviceAccessPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
