import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../om.test-samples';

import { OMFormService } from './om-form.service';

describe('OM Form Service', () => {
  let service: OMFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OMFormService);
  });

  describe('Service methods', () => {
    describe('createOMFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOMFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            codom: expect.any(Object),
          }),
        );
      });

      it('passing IOM should create a new form with FormGroup', () => {
        const formGroup = service.createOMFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            codom: expect.any(Object),
          }),
        );
      });
    });

    describe('getOM', () => {
      it('should return NewOM for default OM initial value', () => {
        const formGroup = service.createOMFormGroup(sampleWithNewData);

        const oM = service.getOM(formGroup) as any;

        expect(oM).toMatchObject(sampleWithNewData);
      });

      it('should return NewOM for empty OM initial value', () => {
        const formGroup = service.createOMFormGroup();

        const oM = service.getOM(formGroup) as any;

        expect(oM).toMatchObject({});
      });

      it('should return IOM', () => {
        const formGroup = service.createOMFormGroup(sampleWithRequiredData);

        const oM = service.getOM(formGroup) as any;

        expect(oM).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOM should not enable id FormControl', () => {
        const formGroup = service.createOMFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOM should disable id FormControl', () => {
        const formGroup = service.createOMFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
