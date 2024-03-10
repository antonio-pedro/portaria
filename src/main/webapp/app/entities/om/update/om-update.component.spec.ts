import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OMService } from '../service/om.service';
import { IOM } from '../om.model';
import { OMFormService } from './om-form.service';

import { OMUpdateComponent } from './om-update.component';

describe('OM Management Update Component', () => {
  let comp: OMUpdateComponent;
  let fixture: ComponentFixture<OMUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let oMFormService: OMFormService;
  let oMService: OMService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OMUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OMUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OMUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    oMFormService = TestBed.inject(OMFormService);
    oMService = TestBed.inject(OMService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const oM: IOM = { id: 456 };

      activatedRoute.data = of({ oM });
      comp.ngOnInit();

      expect(comp.oM).toEqual(oM);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOM>>();
      const oM = { id: 123 };
      jest.spyOn(oMFormService, 'getOM').mockReturnValue(oM);
      jest.spyOn(oMService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oM });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oM }));
      saveSubject.complete();

      // THEN
      expect(oMFormService.getOM).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(oMService.update).toHaveBeenCalledWith(expect.objectContaining(oM));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOM>>();
      const oM = { id: 123 };
      jest.spyOn(oMFormService, 'getOM').mockReturnValue({ id: null });
      jest.spyOn(oMService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oM: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: oM }));
      saveSubject.complete();

      // THEN
      expect(oMFormService.getOM).toHaveBeenCalled();
      expect(oMService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOM>>();
      const oM = { id: 123 };
      jest.spyOn(oMService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ oM });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(oMService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
