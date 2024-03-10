import { IMilitar, NewMilitar } from './militar.model';

export const sampleWithRequiredData: IMilitar = {
  id: 13964,
  identidade: 7736,
  cpf: 5370,
  postoGraduacao: 'meh now whether',
  nome: 'why acclaimed unto',
  nomeGuerra: 'before',
  email: 'Caua_Xavier70@gmail.com',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithPartialData: IMilitar = {
  id: 2336,
  identidade: 9737,
  cpf: 19666,
  postoGraduacao: 'scout',
  nome: 'especially perfectly narrow',
  nomeGuerra: 'towards thick stew',
  email: 'PedroHenrique.Moreira86@yahoo.com',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithFullData: IMilitar = {
  id: 32378,
  identidade: 17238,
  cpf: 18075,
  postoGraduacao: 'wait say',
  nome: 'modulo',
  nomeGuerra: 'along component',
  email: 'Heitor.Oliveira@gmail.com',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithNewData: NewMilitar = {
  identidade: 6098,
  cpf: 4530,
  postoGraduacao: 'trampoline blindly',
  nome: 'bee unless',
  nomeGuerra: 'pillage obnoxiously famously',
  email: 'Victor_Carvalho19@bol.com.br',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
