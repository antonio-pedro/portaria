import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../posto-graduacao.test-samples';

import { PostoGraduacaoFormService } from './posto-graduacao-form.service';

describe('PostoGraduacao Form Service', () => {
  let service: PostoGraduacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostoGraduacaoFormService);
  });

  describe('Service methods', () => {
    describe('createPostoGraduacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPostoGraduacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            sigla: expect.any(Object),
          }),
        );
      });

      it('passing IPostoGraduacao should create a new form with FormGroup', () => {
        const formGroup = service.createPostoGraduacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            sigla: expect.any(Object),
          }),
        );
      });
    });

    describe('getPostoGraduacao', () => {
      it('should return NewPostoGraduacao for default PostoGraduacao initial value', () => {
        const formGroup = service.createPostoGraduacaoFormGroup(sampleWithNewData);

        const postoGraduacao = service.getPostoGraduacao(formGroup) as any;

        expect(postoGraduacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewPostoGraduacao for empty PostoGraduacao initial value', () => {
        const formGroup = service.createPostoGraduacaoFormGroup();

        const postoGraduacao = service.getPostoGraduacao(formGroup) as any;

        expect(postoGraduacao).toMatchObject({});
      });

      it('should return IPostoGraduacao', () => {
        const formGroup = service.createPostoGraduacaoFormGroup(sampleWithRequiredData);

        const postoGraduacao = service.getPostoGraduacao(formGroup) as any;

        expect(postoGraduacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPostoGraduacao should not enable id FormControl', () => {
        const formGroup = service.createPostoGraduacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPostoGraduacao should disable id FormControl', () => {
        const formGroup = service.createPostoGraduacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
