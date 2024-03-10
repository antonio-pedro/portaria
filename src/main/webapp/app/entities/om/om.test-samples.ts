import { IOM, NewOM } from './om.model';

export const sampleWithRequiredData: IOM = {
  id: 23469,
  nome: 'lest',
  sigla: 'now boo infuriate',
};

export const sampleWithPartialData: IOM = {
  id: 9694,
  nome: 'cucumber',
  sigla: 'tan bad',
};

export const sampleWithFullData: IOM = {
  id: 20966,
  nome: 'cradle',
  sigla: 'grove commonly',
  codom: 5411,
};

export const sampleWithNewData: NewOM = {
  nome: 'suddenly',
  sigla: 'following likewise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
