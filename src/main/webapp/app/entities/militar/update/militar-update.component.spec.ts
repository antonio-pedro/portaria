import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPostoGraduacao } from 'app/entities/posto-graduacao/posto-graduacao.model';
import { PostoGraduacaoService } from 'app/entities/posto-graduacao/service/posto-graduacao.service';
import { IOM } from 'app/entities/om/om.model';
import { OMService } from 'app/entities/om/service/om.service';
import { IMilitar } from '../militar.model';
import { MilitarService } from '../service/militar.service';
import { MilitarFormService } from './militar-form.service';

import { MilitarUpdateComponent } from './militar-update.component';

describe('Militar Management Update Component', () => {
  let comp: MilitarUpdateComponent;
  let fixture: ComponentFixture<MilitarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let militarFormService: MilitarFormService;
  let militarService: MilitarService;
  let postoGraduacaoService: PostoGraduacaoService;
  let oMService: OMService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MilitarUpdateComponent],
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
      .overrideTemplate(MilitarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MilitarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    militarFormService = TestBed.inject(MilitarFormService);
    militarService = TestBed.inject(MilitarService);
    postoGraduacaoService = TestBed.inject(PostoGraduacaoService);
    oMService = TestBed.inject(OMService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PostoGraduacao query and add missing value', () => {
      const militar: IMilitar = { id: 456 };
      const posto: IPostoGraduacao = { id: 945 };
      militar.posto = posto;

      const postoGraduacaoCollection: IPostoGraduacao[] = [{ id: 3878 }];
      jest.spyOn(postoGraduacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: postoGraduacaoCollection })));
      const additionalPostoGraduacaos = [posto];
      const expectedCollection: IPostoGraduacao[] = [...additionalPostoGraduacaos, ...postoGraduacaoCollection];
      jest.spyOn(postoGraduacaoService, 'addPostoGraduacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ militar });
      comp.ngOnInit();

      expect(postoGraduacaoService.query).toHaveBeenCalled();
      expect(postoGraduacaoService.addPostoGraduacaoToCollectionIfMissing).toHaveBeenCalledWith(
        postoGraduacaoCollection,
        ...additionalPostoGraduacaos.map(expect.objectContaining),
      );
      expect(comp.postoGraduacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OM query and add missing value', () => {
      const militar: IMilitar = { id: 456 };
      const om: IOM = { id: 27508 };
      militar.om = om;

      const oMCollection: IOM[] = [{ id: 7829 }];
      jest.spyOn(oMService, 'query').mockReturnValue(of(new HttpResponse({ body: oMCollection })));
      const additionalOMS = [om];
      const expectedCollection: IOM[] = [...additionalOMS, ...oMCollection];
      jest.spyOn(oMService, 'addOMToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ militar });
      comp.ngOnInit();

      expect(oMService.query).toHaveBeenCalled();
      expect(oMService.addOMToCollectionIfMissing).toHaveBeenCalledWith(oMCollection, ...additionalOMS.map(expect.objectContaining));
      expect(comp.oMSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const militar: IMilitar = { id: 456 };
      const posto: IPostoGraduacao = { id: 7620 };
      militar.posto = posto;
      const om: IOM = { id: 19573 };
      militar.om = om;

      activatedRoute.data = of({ militar });
      comp.ngOnInit();

      expect(comp.postoGraduacaosSharedCollection).toContain(posto);
      expect(comp.oMSSharedCollection).toContain(om);
      expect(comp.militar).toEqual(militar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMilitar>>();
      const militar = { id: 123 };
      jest.spyOn(militarFormService, 'getMilitar').mockReturnValue(militar);
      jest.spyOn(militarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ militar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: militar }));
      saveSubject.complete();

      // THEN
      expect(militarFormService.getMilitar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(militarService.update).toHaveBeenCalledWith(expect.objectContaining(militar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMilitar>>();
      const militar = { id: 123 };
      jest.spyOn(militarFormService, 'getMilitar').mockReturnValue({ id: null });
      jest.spyOn(militarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ militar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: militar }));
      saveSubject.complete();

      // THEN
      expect(militarFormService.getMilitar).toHaveBeenCalled();
      expect(militarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMilitar>>();
      const militar = { id: 123 };
      jest.spyOn(militarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ militar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(militarService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePostoGraduacao', () => {
      it('Should forward to postoGraduacaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(postoGraduacaoService, 'comparePostoGraduacao');
        comp.comparePostoGraduacao(entity, entity2);
        expect(postoGraduacaoService.comparePostoGraduacao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOM', () => {
      it('Should forward to oMService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(oMService, 'compareOM');
        comp.compareOM(entity, entity2);
        expect(oMService.compareOM).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
