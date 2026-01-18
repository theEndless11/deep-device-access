import { registerPlugin } from '@capacitor/core';

import type { DeepDeviceAccessPlugin } from './definitions';

const DeepDeviceAccess = registerPlugin<DeepDeviceAccessPlugin>('DeepDeviceAccess', {
  web: () => import('./web').then((m) => new m.DeepDeviceAccessWeb()),
});

export * from './definitions';
export { DeepDeviceAccess };
