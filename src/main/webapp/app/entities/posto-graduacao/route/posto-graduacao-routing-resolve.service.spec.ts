import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPostoGraduacao } from '../posto-graduacao.model';
import { PostoGraduacaoService } from '../service/posto-graduacao.service';

import postoGraduacaoResolve from './posto-graduacao-routing-resolve.service';

describe('PostoGraduacao routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PostoGraduacaoService;
  let resultPostoGraduacao: IPostoGraduacao | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(PostoGraduacaoService);
    resultPostoGraduacao = undefined;
  });

  describe('resolve', () => {
    it('should return IPostoGraduacao returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        postoGraduacaoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPostoGraduacao = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPostoGraduacao).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        postoGraduacaoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPostoGraduacao = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPostoGraduacao).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPostoGraduacao>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        postoGraduacaoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPostoGraduacao = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPostoGraduacao).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
