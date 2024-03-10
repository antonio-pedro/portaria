import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOM } from '../om.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../om.test-samples';

import { OMService } from './om.service';

const requireRestSample: IOM = {
  ...sampleWithRequiredData,
};

describe('OM Service', () => {
  let service: OMService;
  let httpMock: HttpTestingController;
  let expectedResult: IOM | IOM[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OMService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a OM', () => {
      const oM = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(oM).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OM', () => {
      const oM = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(oM).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OM', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OM', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OM', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOMToCollectionIfMissing', () => {
      it('should add a OM to an empty array', () => {
        const oM: IOM = sampleWithRequiredData;
        expectedResult = service.addOMToCollectionIfMissing([], oM);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oM);
      });

      it('should not add a OM to an array that contains it', () => {
        const oM: IOM = sampleWithRequiredData;
        const oMCollection: IOM[] = [
          {
            ...oM,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOMToCollectionIfMissing(oMCollection, oM);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OM to an array that doesn't contain it", () => {
        const oM: IOM = sampleWithRequiredData;
        const oMCollection: IOM[] = [sampleWithPartialData];
        expectedResult = service.addOMToCollectionIfMissing(oMCollection, oM);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oM);
      });

      it('should add only unique OM to an array', () => {
        const oMArray: IOM[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const oMCollection: IOM[] = [sampleWithRequiredData];
        expectedResult = service.addOMToCollectionIfMissing(oMCollection, ...oMArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const oM: IOM = sampleWithRequiredData;
        const oM2: IOM = sampleWithPartialData;
        expectedResult = service.addOMToCollectionIfMissing([], oM, oM2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oM);
        expect(expectedResult).toContain(oM2);
      });

      it('should accept null and undefined values', () => {
        const oM: IOM = sampleWithRequiredData;
        expectedResult = service.addOMToCollectionIfMissing([], null, oM, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oM);
      });

      it('should return initial array if no OM is added', () => {
        const oMCollection: IOM[] = [sampleWithRequiredData];
        expectedResult = service.addOMToCollectionIfMissing(oMCollection, undefined, null);
        expect(expectedResult).toEqual(oMCollection);
      });
    });

    describe('compareOM', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOM(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOM(entity1, entity2);
        const compareResult2 = service.compareOM(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOM(entity1, entity2);
        const compareResult2 = service.compareOM(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOM(entity1, entity2);
        const compareResult2 = service.compareOM(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
