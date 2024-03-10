import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PostoGraduacaoService } from '../service/posto-graduacao.service';
import { IPostoGraduacao } from '../posto-graduacao.model';
import { PostoGraduacaoFormService } from './posto-graduacao-form.service';

import { PostoGraduacaoUpdateComponent } from './posto-graduacao-update.component';

describe('PostoGraduacao Management Update Component', () => {
  let comp: PostoGraduacaoUpdateComponent;
  let fixture: ComponentFixture<PostoGraduacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let postoGraduacaoFormService: PostoGraduacaoFormService;
  let postoGraduacaoService: PostoGraduacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PostoGraduacaoUpdateComponent],
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
      .overrideTemplate(PostoGraduacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PostoGraduacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    postoGraduacaoFormService = TestBed.inject(PostoGraduacaoFormService);
    postoGraduacaoService = TestBed.inject(PostoGraduacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const postoGraduacao: IPostoGraduacao = { id: 456 };

      activatedRoute.data = of({ postoGraduacao });
      comp.ngOnInit();

      expect(comp.postoGraduacao).toEqual(postoGraduacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPostoGraduacao>>();
      const postoGraduacao = { id: 123 };
      jest.spyOn(postoGraduacaoFormService, 'getPostoGraduacao').mockReturnValue(postoGraduacao);
      jest.spyOn(postoGraduacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postoGraduacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: postoGraduacao }));
      saveSubject.complete();

      // THEN
      expect(postoGraduacaoFormService.getPostoGraduacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(postoGraduacaoService.update).toHaveBeenCalledWith(expect.objectContaining(postoGraduacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPostoGraduacao>>();
      const postoGraduacao = { id: 123 };
      jest.spyOn(postoGraduacaoFormService, 'getPostoGraduacao').mockReturnValue({ id: null });
      jest.spyOn(postoGraduacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postoGraduacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: postoGraduacao }));
      saveSubject.complete();

      // THEN
      expect(postoGraduacaoFormService.getPostoGraduacao).toHaveBeenCalled();
      expect(postoGraduacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPostoGraduacao>>();
      const postoGraduacao = { id: 123 };
      jest.spyOn(postoGraduacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postoGraduacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(postoGraduacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
