import { IPostoGraduacao, NewPostoGraduacao } from './posto-graduacao.model';

export const sampleWithRequiredData: IPostoGraduacao = {
  id: 21602,
  descricao: 'cheesecake',
  sigla: 'makeup',
};

export const sampleWithPartialData: IPostoGraduacao = {
  id: 26868,
  descricao: 'marmalade official pawn',
  sigla: 'unto reluctantly',
};

export const sampleWithFullData: IPostoGraduacao = {
  id: 2086,
  descricao: 'circa psst',
  sigla: 'spatter theater',
};

export const sampleWithNewData: NewPostoGraduacao = {
  descricao: 'house at outshine',
  sigla: 'pfft speedily',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
